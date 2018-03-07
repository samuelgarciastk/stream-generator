package io.transwarp.streamgenerator.common;

import java.util.Random;

/**
 * Author: stk
 * Date: 2018/3/1
 */
public class StringGenerator {
    private static final String BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Number: 0-10; Lowercase: 10-36; Uppercase: 36-62
     */
    public static String randomString(int length, int start, int end) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = start + random.nextInt(end - start);
            sb.append(BASE.charAt(index));
        }
        return sb.toString();
    }

    public static String randomString(int length) {
        return randomString(length, 0, 62);
    }

    public static String randomString(int length, String base) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(base.length());
            sb.append(base.charAt(index));
        }
        return sb.toString();
    }

    public static String randomNumber(int length) {
        return randomString(length, 0, 10);
    }

    public static String randomUpper(int length) {
        return randomString(length, 36, 62);
    }

    public static String randomLower(int length) {
        return randomString(length, 10, 36);
    }
}
