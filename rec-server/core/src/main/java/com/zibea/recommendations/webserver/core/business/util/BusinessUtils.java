package com.zibea.recommendations.webserver.core.business.util;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author Mikhail Bragin
 */
public class BusinessUtils {

    private static final int RANDOM_STRING_LENGTH = 32;

    public static String generateUserIdString() {
        return RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
    }
}
