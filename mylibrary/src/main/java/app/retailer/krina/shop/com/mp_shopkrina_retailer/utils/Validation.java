package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.regex.Pattern;

public class Validation {
    public ProgressDialog proBuilder;
    Context vContext;

    public Validation(Context c) {
        vContext = c;
    }

    public static boolean chkmobile(String mobileno) {
        String str = "";
        try {
            str = mobileno.substring(0, 2);
        } catch (StringIndexOutOfBoundsException e) {
            Log.e("mob", "" + str);
        }

        /*str.equals("04") */
        return mobileno.length() >= 4 && mobileno.length() <= 12;
    }


    public static boolean chkmobileNo(String mobileno) {
        if (mobileno == null) {
            return false;
        } else {
            return mobileno.length() < 10;
        }
    }

    public static boolean checkEmail(String email) {
        final Pattern EMAIL_ADDRESS_PATTERN = Pattern
                .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                        + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                        + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static boolean checkPassword(String password) {
        String PASSWORD_PATTERN1 =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*\\(\\)\\_\\-\\+\\>\\<\\¥\\£\\€]).{8,1000})";
        final Pattern PASSWORD_PATTERN = Pattern
                .compile(PASSWORD_PATTERN1);
        //&&strongPassword(password)
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean checkPasswordStats(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean numOfSpecial = false;
        boolean numOfUpperLetters = false;
        boolean numOfLowerLetters = false;
        boolean numOfDigits = false;

        byte[] bytesOfPasswordCharacter = password.getBytes();
        for (byte tempByte : bytesOfPasswordCharacter) {
            char tempChar = (char) tempByte;

            if (Character.isDigit(tempChar)) {
                numOfDigits = true;
            } else if (Character.isUpperCase(tempChar)) {
                numOfUpperLetters = true;
            } else if (Character.isLowerCase(tempChar)) {
                numOfLowerLetters = true;
            } else {
                numOfSpecial = true;
            }
            if (numOfSpecial && numOfDigits && numOfUpperLetters && numOfLowerLetters) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkPasswordLogin(String password) {
        String PASSWORD_PATTERN1 =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*\\(\\)\\_\\-\\+\\>\\<\\¥\\£\\€]).{4,1000})";
        final Pattern PASSWORD_PATTERN = Pattern
                .compile(PASSWORD_PATTERN1);
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean chkPersonName(String name) {
        return true;
    }

    public static boolean haveNetworkConnection(Context ctx) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public boolean checkName(String name) {
        final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]+$");
        return NAME_PATTERN.matcher(name).matches();
    }

    public static boolean checkNetworkConnectivity(Context context) {
        Boolean bNetwork = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        for (NetworkInfo networkInfo : connectivityManager.getAllNetworkInfo()) {
            int netType = networkInfo.getType();
            int netSubType = networkInfo.getSubtype();

            if (netType == ConnectivityManager.TYPE_WIFI) {
                bNetwork = networkInfo.isConnected();
                if (bNetwork == true)
                    break;
            } else if (netType == ConnectivityManager.TYPE_MOBILE
                    && netSubType != TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                bNetwork = networkInfo.isConnected();
                if (bNetwork == true)
                    break;
            } else {
                bNetwork = false;
            }
        }
        if (!bNetwork) {
            // Toast.makeText(vContext.getApplicationContext(),
            // "You are not in network", Toast.LENGTH_SHORT).show();
        }
        return bNetwork;
    }

}
