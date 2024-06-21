package ru.levkopo.plugins.mccisco.utils;

import java.security.SecureRandom;
import java.util.List;

public class RandomUtil {
    public static final SecureRandom random = new SecureRandom();
    private static final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuwxy";
    private static final String[] deviceNameVariants = new String[] { "my", "key", "moodle", "auteam", "cisco", "switch", "hub", "br", "net" };
    private static final String[] username = new String[] { "admin", "stepan", "lev", "max", "voda" };

    public static String domainName() {
        StringBuilder randStr = new StringBuilder();
        randStr.append(deviceName())
                .append(".")
                .append(deviceName())
                .append(".");

        String[] domains = {"de", "com", "in", "en", "us", "ru", "su"};

        int idx = random.nextInt(domains.length);
        String random = (domains[idx]);

        randStr.append(random);
        return randStr.toString();
    }

    public static String ipAddress() {
        return random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256);
    }

    public static int mask() {
        return 1 + random.nextInt(30);
    }

    public static String deviceName() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            builder.append(deviceNameVariants[random.nextInt(deviceNameVariants.length)]);
        }

        return builder.toString();
    }

    public static String username() {
        return username[random.nextInt(username.length)];
    }

    public static String password() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8 + 4 * Math.random(); i++) {
            builder.append(lexicon.charAt(random.nextInt(lexicon.length())));
        }

        return builder.toString();
    }

    public static int privilege() {
        return random.nextInt(15);
    }

    public static <T> T fromList(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    public static <T> T fromArray(T[] array) {
        return array[random.nextInt(array.length)];
    }
}
