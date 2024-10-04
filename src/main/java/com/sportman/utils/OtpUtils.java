package com.sportman.utils;

import java.util.HashMap;
import java.util.Map;

public class OtpUtils {

    public static String create() {

        Map<Integer, String> pairChar = new HashMap<>();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < 26; i++) {
            pairChar.put(i, String.valueOf((char) (i + 'A')));
        }

        //create otp
        for (int turn = 0; turn < 6; turn++) {
            double numeric = Math.random() * 26;
            if (numeric % 2 == 0) otp.append(pairChar.get(numeric).toUpperCase());
            else otp.append(numeric);
        }

        return otp.toString();
    }

}
