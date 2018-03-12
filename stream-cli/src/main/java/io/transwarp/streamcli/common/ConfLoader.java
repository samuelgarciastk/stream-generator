package io.transwarp.streamcli.common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Author: stk
 * Date: 2018/3/5
 * <p>
 * This is a tool class for loading config files.
 */
public class ConfLoader {
    /**
     * Get configuration file from resource directory or the path where the artifact is.
     *
     * @param name file name
     * @return InputStreamReader
     */
    private static InputStreamReader getConf(String name) {
        File conf = new File(System.getProperty("user.dir") + File.separator + name);
        try {
            return new InputStreamReader(new FileInputStream(conf), "UTF-8");
        } catch (FileNotFoundException e) {
            try {
                return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(name), "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("File not found: " + name);
        throw new NullPointerException();
    }

    /**
     * Load property file.
     *
     * @param name file name
     * @return Loaded properties
     */
    public static Properties loadProps(String name) {
        Properties props = new Properties();
        try (InputStreamReader in = getConf(name)) {
            props.load(in);
        } catch (IOException e) {
            System.out.println("Properties load error: " + name);
            e.printStackTrace();
        }
        return props;
    }

    /**
     * Load configuration file.
     *
     * @param name   file name
     * @param length group length. each group is separated by space.
     * @param index  index of weight in each group. Format: String:weight
     * @return List with weight
     * E.g., each line is in the following format: 1011110 123456 China:99 1034211 654321 USA:88
     * When invoking locdConf(file, 3, 2), each three Strings will be one group.
     * And the String with index '2' in each group will be added into the result List by times defined after colon.
     */
    public static List<String> loadConf(String name, int length, int index) {
        List<String> list = new ArrayList<>();
        try (InputStreamReader in = getConf(name); BufferedReader bf = new BufferedReader(in)) {
            String line;
            while ((line = bf.readLine()) != null) {
                String[] strArr = line.split(" ");
                for (int i = 0; i < strArr.length; i += length) {
                    String[] weight = strArr[i + index].split(":");
                    if (weight.length == 2) {
                        int times = Integer.parseInt(weight[1]);
                        for (int j = 0; j < times; j++) list.add(weight[0]);
                    } else list.add(weight[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * The default method of loading configuration file.
     *
     * @param name file name
     * @return List with weight
     */
    public static List<String> loadConf(String name) {
        return loadConf(name, 1, 0);
    }

    /**
     * Convert file into a String.
     *
     * @param name file name
     * @return result String
     */
    public static String loadString(String name) {
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader in = getConf(name); BufferedReader bf = new BufferedReader(in)) {
            String line;
            while ((line = bf.readLine()) != null) sb.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Convert file into a List.
     *
     * @param name file name
     * @return String List
     */
    public static List<String> loadData(String name) {
        List<String> list = new ArrayList<>();
        try (InputStreamReader in = getConf(name); BufferedReader bf = new BufferedReader(in)) {
            String line;
            while ((line = bf.readLine()) != null) list.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
