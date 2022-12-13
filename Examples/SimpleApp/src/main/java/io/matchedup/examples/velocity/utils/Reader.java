package io.matchedup.examples.velocity.utils;

import io.matchedup.api.types.PlayerAttributeType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reader {

    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String readString(String message) {
        return readString(message, true);
    }

    public static String readString(String message, boolean indent) {
        String val = null;
        if (indent) message = "  > " + message;

        while (val == null) {
            try {
                System.out.println(message);
                val = reader.readLine();
            } catch (IOException ignored) {
            }
        }
        return val;
    }

    public static int readInt(String message) {
        return readInt(message, true);
    }

    public static int readInt(String message, boolean indent) {
        Integer val = null;
        if (indent) message = "  > " + message;

        while (val == null) {
            try {
                System.out.println(message);
                val = Integer.parseInt(reader.readLine());
            } catch (Exception ignored) {
            }
        }
        return val;
    }

    public static PlayerAttributeType readPlayerAttributeType(String message) {
        return readPlayerAttributeType(message, true);
    }

    public static PlayerAttributeType readPlayerAttributeType(String message, boolean indent) {
        PlayerAttributeType attribType = null;
        if (indent) message = "  > " + message;

        while (attribType == null) {
            try {
                System.out.println(message);
                String val = reader.readLine();
                if (isNumber(val)) {
                    attribType = new PlayerAttributeType(Integer.parseInt(val));
                } else {
                    attribType = new PlayerAttributeType(val);
                }
            } catch (IOException ignored) {
            }
        }
        return attribType;
    }
    
    private static boolean isNumber(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

}
