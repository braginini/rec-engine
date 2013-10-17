import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Mikhail Bragin
 */
public class RetailRocketStats {

    private static AtomicBoolean buy = new AtomicBoolean(true);

    public static void main(String args[]) {

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

        pool.scheduleWithFixedDelay((new Runnable() {
            @Override
            public void run() {
                assign();
            }
        }), 0, 1, TimeUnit.MINUTES);

        pool.scheduleWithFixedDelay((new Runnable() {
            @Override
            public void run() {
                buy.set(true);
            }
        }), 0, 35, TimeUnit.MINUTES);


    }

    public static void assign() {
        HttpClient httpclient = new HttpClient();
        httpclient.getState().clearCookies();
        HttpMethod method = new GetMethod("http://retailrocket.ru/api/PartnerApiParams/515055a887900a05d444bd0e/jsonp?callback=rrApi._initialize&rcuidnull" + "auth/assign/rkTYxrZUlqS2Z2U6uondZh9Pk/");
        try {
            if (httpclient.executeMethod(method) == HttpStatus.SC_OK) {
                Cookie[] cookies = httpclient.getState().getCookies();
                for (int j = 0; j < cookies.length; j++) {
                    Cookie cookie = cookies[j];
                    if (cookie.getName().toLowerCase().equals("rcuid")) {

                        String rcuid = cookie.getValue();
                        System.out.println(method.getResponseBodyAsString());
                        System.out.println(rcuid);
                        for (int k = 0; k < 3; k++) {

                            int item = getRandom(29, 49);
                            System.out.println("Item=" + item);
                            HttpMethod vieMethod = new GetMethod("http://retailrocket.ru/api/Event/PageView/515055a887900a05d444bd0e/0/?rcuid=" + rcuid + "&referrer=http%3A%2F%2Ftechno5.p.ht%2Findex.php%3Froute%3Dproduct%2Fcategory%26path%3D18&" + System.currentTimeMillis());
                            try {
                                httpclient.executeMethod(vieMethod);
                                System.out.println("PageView=" + vieMethod.getResponseBodyAsString());
                            } finally {
                                vieMethod.releaseConnection();
                            }
                            vieMethod = new GetMethod("http://retailrocket.ru/api/Event/View/515055a887900a05d444bd0e/" + item + "/?rcuid=" + rcuid + "&" + System.currentTimeMillis());
                            try {
                                httpclient.executeMethod(vieMethod);
                                System.out.println("ItemView=" + vieMethod.getResponseBodyAsString());
                            } finally {
                                vieMethod.releaseConnection();
                            }

                            if (buy.get()) {
                                vieMethod = new GetMethod("http://retailrocket.ru/api/Event/AddToBasket/515055a887900a05d444bd0e/" + item + "/?rcuid=" + rcuid + "&" + System.currentTimeMillis());
                                try {
                                    httpclient.executeMethod(vieMethod);
                                    System.out.println("Basket=" + vieMethod.getResponseBodyAsString());
                                } finally {
                                    vieMethod.releaseConnection();
                                }

                                vieMethod = new GetMethod("http://retailrocket.ru/api/Event/Buy/515055a887900a05d444bd0e/" + item + "/?rcuid=" + rcuid + "&" + System.currentTimeMillis());
                                try {
                                    httpclient.executeMethod(vieMethod);
                                    System.out.println("Buy=" + vieMethod.getResponseBodyAsString());
                                } finally {
                                    vieMethod.releaseConnection();
                                }

                                buy.set(false);
                            }

                        }

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            method.releaseConnection();
        }

        System.out.println();
    }

    public static int getRandom(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));

    }
}
