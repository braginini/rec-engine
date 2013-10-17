package com.zibea.recommendations.webserver.web;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author MIkhail Bragin
 */
public class EventsGenerator {

    private static final int usersCount = 1;

    public static void main(String args[]) throws Exception {

        final String host = args[0];

        int[] users = new int[usersCount];
        for (int i = 0; i < usersCount; i++) {
            users[i] = i;
        }

        ExecutorService service = Executors.newFixedThreadPool(2);

        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    connect(host, 0, 4);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static void connect(String host, int min, int max) throws IOException {
        HttpClient httpclient = new HttpClient();
        for (int i = 0; i < usersCount; i++) {
            System.out.println("next user " + i);
            httpclient.getState().clearCookies();
            HttpMethod method = new GetMethod(host + "auth/assign/rkTYxrZUlqS2Z2U6uondZh9Pk/");
            try {
                if (httpclient.executeMethod(method) == HttpStatus.SC_OK) {
                    Cookie[] cookies = httpclient.getState().getCookies();
                    for (int j = 0; j < cookies.length; j++) {
                        Cookie cookie = cookies[j];
                        if (cookie.getName().toLowerCase().equals("ruid")) {

                            for (int k = 0; k < 3; k++) {

                                int item = getRandom(min, max);
                                HttpMethod vieMethod = new GetMethod(host + "event/itemview/rkTYxrZUlqS2Z2U6uondZh9Pk/" +
                                        item + "?ts=" + System.currentTimeMillis());
                                try {
                                    httpclient.executeMethod(vieMethod);
                                } finally {
                                    vieMethod.releaseConnection();
                                }
                            }

                        }
                    }
                }

                System.out.println("finished " + i);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } finally {
                method.releaseConnection();
            }

        }
        System.exit(0);

    }

    public static int getRandom(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));

    }
}
