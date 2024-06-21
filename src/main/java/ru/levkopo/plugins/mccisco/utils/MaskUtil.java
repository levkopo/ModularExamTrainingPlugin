package ru.levkopo.plugins.mccisco.utils;

public class MaskUtil {
    public static String fromShortToFull(int shortMask) {
        String[] fullMask = new String[4];
        int tmpMask = shortMask;
        for (int i = 0; i < 4; i++) {
            tmpMask -= 8;
            if(tmpMask >= 0) {
                fullMask[i] = "255";
            }else if(tmpMask > -8) {
                fullMask[i] = String.valueOf(256 - (int) Math.pow(2, -tmpMask));
            }else fullMask[i] = "0";
        }

        return String.join(".", fullMask);
    }
}
