package pk.com.Taj.app.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    public static Date parseDate(String date, String format) {
        Date dt = null;
        try {
            dt = new SimpleDateFormat(format).parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }


    public static String extractDate(Date date, String format) {
        String dt = null;
        try {
            dt = new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static Date getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date dt = null;
        try {
            dt = sdf.parse(sdf.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }


    public static String left(String text, int length) {
        if (text.length() > length)
            return text.substring(0, length);

        return text;
    }

    public static String padRight(String text, int totalWidth) {
        return String.format("%-" + totalWidth + "s", text);
    }


    public static String padLeft(String text, int totalWidth) {
        return String.format("%" + totalWidth + "s", text);
    }

    public static boolean isName(String text) {
        if (text.matches("^[a-zA-Z\\s\\.]+$")) {
            return true;
        } else {
            return false;
        }
    }

    public static String repeat(String text, int length) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(text);
        }
        return sb.toString();
    }

    public static float parseFloat(String value) {
        if (value == null) {
            value = "0";
        }
        return Float.valueOf("0" + value);
    }

    public static int parseInt(String value) {
        if (value == null) {
            value = "0";
        }
        return Integer.valueOf("0" + value);
    }


    public static String parseTwoDecimal(String value) {
        return String.format("%.2f", Float.valueOf("0" + value)).replaceFirst("\\.?0*$", "");
    }

    public static String parseTwoDecimal(float value) {
        return String.format("%.2f", value).replaceFirst("\\.?0*$", "");
    }

    public static String formatTwoDecimal(String value) {
        return String.format("%,.2f", Float.valueOf("0" + value)).replaceFirst("\\.?0*$", "");
    }

    public static String formatTwoDecimal(float value) {
        return String.format("%,.2f", value).replaceFirst("\\.?0*$", "");
    }

    public static boolean isNumeric(String text) {
        if (text.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
            return true;
        } else {
            return false;
        }
    }

    public static String toProperCase(String text) {
        String ACTIONABLE_DELIMITERS = " '-/"; // Next of these character to be capitalized

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : text.toCharArray()) {
            if (capNext) {
                c = Character.toUpperCase(c);
            } else {
                c = Character.toLowerCase(c);
            }
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0);
        }
        return sb.toString();
    }

    public static String getMD5Hash(String text) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getSHA1Hash(String text) {
        try {
            java.security.MessageDigest sha1 = java.security.MessageDigest.getInstance("SHA-1");
            byte[] array = sha1.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
