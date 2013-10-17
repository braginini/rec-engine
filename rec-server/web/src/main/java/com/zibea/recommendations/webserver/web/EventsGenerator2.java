package com.zibea.recommendations.webserver.web;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.util.HashSet;

/**
 * @author Mikhail Bragin
 */
public class EventsGenerator2 {

    private static HashSet<String> ruIds = new HashSet<>();

    public static void main(String args[]) throws Exception {

        createUsers(100000);

    }

    private static void createUsers(int userCount) throws IOException {
        HttpClient httpclient = new HttpClient();
        for (int i = 0; i < userCount; i++) {
            System.out.println("next user " + i);
            httpclient.getState().clearCookies();
            HttpMethod method = new GetMethod("localhost:8080/auth/assign/rkTYxrZUlqS2Z2U6uondZh9Pk/");
            if (httpclient.executeMethod(method) == HttpStatus.SC_OK) {
                Cookie[] cookies = httpclient.getState().getCookies();
                for (int j = 0; j < cookies.length; j++) {
                    Cookie cookie = cookies[j];
                    if (cookie.getName().toLowerCase().equals("ruid")) {
                        ruIds.add(cookie.getValue());
                    }
                }
            }
        }
    }
}
