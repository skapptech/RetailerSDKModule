package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import java.util.ArrayList;
import java.util.Map;

public class TextUtils {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String MOBILE_NO_PATTERN = "^[+]?[0-9]{10,13}$";

    public static boolean isValidEmail(final String hex) {
        return hex.matches(EMAIL_PATTERN);
    }

    public static boolean isValidMobileNo(final String hex) {
        return hex.matches(MOBILE_NO_PATTERN);
    }

    public static int parseNullSafeInteger(String numberString) {
        int number = 0;
        try {
            Integer.parseInt(numberString);
        } catch (NumberFormatException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
        return number;
    }

    public static boolean isIntegerValue(String numberString) {
        boolean result = false;
        try {
            Integer.parseInt(numberString);
            result = true;
        } catch (NumberFormatException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public static boolean isInteger_GreaterThen0Value(String numberString) {
        boolean result = false;
        try {
            if (Integer.parseInt(numberString) > 0)
                result = true;
        } catch (NumberFormatException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public static <T> boolean isNull(T t) {
        return t == null;
    }

    public static boolean isNullOrEmpty(String s) {
        return (s == null) || (s.length() == 0) || (s.equalsIgnoreCase("null")) || (s.equalsIgnoreCase("0"));
    }

    public static <T> boolean isNullOrEmpty(ArrayList<T> a) {
        return (a == null) || (a.size() == 0);
    }

    public static <T, Y> boolean isNullOrEmpty(Map<T, Y> m) {
        return (m == null) || (m.size() == 0);
    }


}
