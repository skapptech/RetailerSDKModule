package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Utility {


    public static Context appContext;

    private static String DIALCOUNT = "dialcount";


    public static long hoursSinceEpoch() {
        final Date now = new Date();
        final long nowMillis = now.getTime();
        return nowMillis / 1000 * 60 * 60;
    }

    public static int hourOfTheDay() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static void s(Context context, String name, int value) {

        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(DIALCOUNT, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putInt(name, value);
        editor.commit();

    }


    public static String getTime(String dateTime) {


        String inputPattern = "yyyy-MM-dd HH:mm:ss";

//        String outputPattern = "dd/MM/yyyy h:mm a";

//		String outputPattern = "dd/MM/yyyy";

        String outputPattern = "HH:mm:ss";


        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return str;

    }


}// final class ends here

