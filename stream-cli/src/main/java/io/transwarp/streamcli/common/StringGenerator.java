package io.transwarp.streamcli.common;

import com.mifmif.common.regex.Generex;

import java.util.Random;

/**
 * Author: stk
 * Date: 2018/3/1
 * <p>
 * This is a tool class for generating String randomly.
 */
public class StringGenerator {
    /**
     * Number: 0-10; Lowercase: 10-36; Uppercase: 36-62
     */
    private static final String BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Generate random String.
     *
     * @param length length of String
     * @param start  start index of BASE
     * @param end    end index of BASE
     * @return random String
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

    /**
     * Generate random String using the whole BASE.
     *
     * @param length length of String
     * @return random String
     */
    public static String randomString(int length) {
        return randomString(length, 0, 62);
    }

    /**
     * Generate random String using a custom BASE.
     *
     * @param length length of String
     * @param base   custom base
     * @return random String
     */
    public static String randomString(int length, String base) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(base.length());
            sb.append(base.charAt(index));
        }
        return sb.toString();
    }

    /**
     * Generate random String only containing number.
     *
     * @param length length of String
     * @return random String
     */
    public static String randomNumber(int length) {
        return randomString(length, 0, 10);
    }

    /**
     * Generate random String only containing uppercase characters.
     *
     * @param length length of String
     * @return random String
     */
    public static String randomUpper(int length) {
        return randomString(length, 36, 62);
    }

    /**
     * Generate random String only containing lowercase characters.
     *
     * @param length length of String
     * @return random String
     */
    public static String randomLower(int length) {
        return randomString(length, 10, 36);
    }

    /**
     * Generate random String using a given regular expression.
     *
     * @param regex regular expression
     * @return random String
     */
    public static String randomRegexString(String regex) {
        return new Generex(regex).random();
    }
}
