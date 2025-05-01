package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.murli.MurliActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.CommonResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import io.reactivex.observers.DisposableObserver;

public class Utils {
    private static final String MOBILE_NO_PATTERN = "^[+]?[0-9]{10,13}$";
    private static final String EMAIL_NO_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static Dialog customDialog;
    public static String myFormat = "yyyy-MM-dd'T'HH:mm:ss";
    private static Context context;
    public static String pattern = "##.##";


    public Utils(Context _context) {
        context = _context;
    }

    public static void setToast(Context _mContext, String str) {
        Toast.makeText(_mContext, str, Toast.LENGTH_SHORT).show();
    }

    public static void setLongToast(Context _mContext, String str) {
        Toast.makeText(_mContext, str, Toast.LENGTH_LONG).show();
    }

    public static boolean isValidMobile(String mobile) {
        Pattern pattern = Pattern.compile(MOBILE_NO_PATTERN);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_NO_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidPanCardNo(String panCardNo) {
        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        Pattern pattern = Pattern.compile(regex);
        if (panCardNo == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(panCardNo);
        return matcher.matches();
    }
    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*\\d)(?=.*[#$^+=!*()@%&]).{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean getConnectivityStatusString(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else return activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            return false;
        }
    }

    public static void showProgressDialog(Activity activity) {
        if (customDialog != null) {
            customDialog.dismiss();
            customDialog = null;
        }
        customDialog = new Dialog(activity, R.style.CustomDialog);
        View mView = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null);
        customDialog.setCancelable(false);
        customDialog.setContentView(mView);
        if (!customDialog.isShowing() && !activity.isFinishing()) {
            // Bad token exception handled by devendra
            try {
                customDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showProgressDialogContinue(Activity activity) {
        if (customDialog != null) {
            customDialog.dismiss();
            customDialog = null;
        }
        customDialog = new Dialog(activity, R.style.CustomDialog);
        View mView = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null);
        customDialog.setContentView(mView);
        customDialog.setCancelable(false);
        if (!customDialog.isShowing() && !activity.isFinishing()) {
            // Bad token exception handled by devendra
            try {
                customDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void hideProgressDialog() {
        if (customDialog != null && customDialog.isShowing()) {
            // Bad token exception handled by devendra
            try {
                customDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static String getDateFormat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "";
        }
    }

    public static String getDateFormat(String ServerDate,String requiredFormat) {
        // 2018-12-24T15:48:15.707+05:30
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat(requiredFormat, Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "";
        }
    }

    public static String getDateMonthFormat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "";
        }
    }

    public static String getDateDashFormat(String ServerDate) {
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("MM-dd", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);
                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "null";
        }
    }

    public static String getSimpleDateFormat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);
                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "null";
        }
    }

    public static String getChangeDateFormatInProfile(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "null";
        }
    }

    public static String getOrderDateFormat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "";
        }
    }

    public static String formatToYesterdayOrToday(String date) throws ParseException {
        Date dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("hh:mma", Locale.ENGLISH);

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today ";
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday ";
        } else {
            return getChangeDateFormatInProfile(date);
        }
    }

    public static String getTimeForChat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!ServerDate.equalsIgnoreCase(null) && !ServerDate.equalsIgnoreCase("")) {

            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            DateFormat targetFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "null";
        }
    }

    public static String getDateTimeFormate(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        }
        return "";
    }

    public static String getDateTimeForTarget(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("EEE,MMM dd yyyy", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "";
        }
    }

    public static String getCurrentDate(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("EEE,MMM dd yyyy", Locale.ENGLISH);
        return df.format(d);
    }

    public static String getDate(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("dd", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "";
        }
    }

    public static long getRemainingDays(String date) {
        try {
            System.out.println(date);
            Date time2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(date);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time2);
            Log.e("calendar", calendar1.getTime() + "");

            long currentTimeMillis = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date resultDate = new Date(currentTimeMillis);

            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(sdf.format(resultDate));
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(d);
            Log.e("calendar2", calendar2.getTime() + "");

            Date startDate = calendar1.getTime();
            Date endDate = calendar2.getTime();

            // checks whether the current time is between.
            System.out.println(endDate.after(calendar1.getTime()));

            long different = startDate.getTime() - endDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            return different / daysInMilli;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getTimeDiffMin(String date) {
        try {
            Date time2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(date);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time2);

            long currentTimeMillis = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date resultDate = new Date(currentTimeMillis);

            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(sdf.format(resultDate));
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(d);

            Date startDate = calendar1.getTime();
            Date endDate = calendar2.getTime();
            // checks whether the current time is between.
            System.out.println(endDate.after(calendar1.getTime()));

            long different = startDate.getTime() - endDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;

            return different / minutesInMilli;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String currentDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        return date;
    }

    public static String formatToDateMonth(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);
                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "";
        }
    }


    public static void hideKeyboard(Activity context, View view) {
        // Then just use the following:
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static String capitalize(String word) {
        StringBuilder sb = null;
        try {
            if (word != null) {
                String[] words = word.split(" ");
                sb = new StringBuilder();
                if (words[0].length() > 0) {
                    sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
                    for (int i = 1; i < words.length; i++) {
                        sb.append(" ");
                        sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
                    }
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void buttonEffect(View button) {
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                    v.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.getBackground().clearColorFilter();
                    v.invalidate();
                    break;
                }
            }
            return false;
        });
    }

    public static void leftTransaction(Activity activity) {
        activity.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }

    public static void rightTransaction(Activity activity) {
        activity.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    public static void fadeTransaction(Activity activity) {
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    public static String getDeviceUniqueID(Activity activity) {
        if (BuildConfig.DEBUG) {
            return Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID) + "shinoo";
        } else {
            return Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

    public static void showSnackBar(View view, String text, boolean isShowAction, String actionText) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        if (isShowAction)
            snackbar.setAction(actionText, view1 -> snackbar.dismiss());
        snackbar.show();
    }

    public static String getToken(Context context) {
        return SharePrefs.getInstance(context).getString(SharePrefs.TOKEN);
    }

    public static String getCustMobile(Context context) {
        return SharePrefs.getInstance(context).getString(SharePrefs.MOBILE_NUMBER).concat("_") + SharePrefs.getInstance(context).getInt(SharePrefs.CUSTOMER_ID);
    }

    public static String getCustomerType(Context context) {
        return SharePrefs.getInstance(context).getString(SharePrefs.CUSTOMER_TYPE);
    }

    public static String getHKCustomerID(Context context) {
        return SharePrefs.getInstance(context).getString(SharePrefs.HISAB_KITAB_ID) + "";
    }


    public static void addFav(int itemId, Boolean isLike, Activity activity) {
        try {
            CommonClassForAPI commonClassForAPI = CommonClassForAPI.getInstance(activity);
            commonClassForAPI.addItemFav(new DisposableObserver<CommonResponse>() {
                @Override
                public void onNext(@NotNull CommonResponse response) {
                    Logger.logD("Constant", "isLike::" + response);
                }

                @Override
                public void onError(@NotNull Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            }, SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID), itemId, isLike);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean gpsPermission(Activity activity, String callTime) {
        boolean[] isGPS = new boolean[1];
        try {
            new GpsUtils(activity).turnGPSOn(isGPSEnable -> {
                isGPS[0] = isGPSEnable;
                if (!isGPS[0] && !callTime.equalsIgnoreCase("runtime")) {
                    new GpsUtils(activity).turnGPSOn(isGPSEnable1 -> isGPS[0] = isGPSEnable1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
        return isGPS[0];
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNetworkAvailable(Context context1) {
        // context updated by devendra
        context = context1;
        ConnectivityManager connectivityManager = (ConnectivityManager) context1.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean getDay(boolean firstLogin) {
        // Saturday
        boolean b = false;
        if (firstLogin) {
            b = true;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);
            switch (dayOfTheWeek) {
                case "Monday":
                    b = true;
                    break;
                case "Thursday":
                    b = true;
                    break;
            }
        }
        return b;
    }

    public static long getAvailableSpaceInMB() {
        final long SIZE_KB = 1024L;
        final long SIZE_MB = SIZE_KB * SIZE_KB;
        long availableSpace;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        return availableSpace / SIZE_MB;
    }

    public void addShortcut(Activity activity) {
        try {
            if (!SharePrefs.getInstance(activity).getString("LAST_LAUNCH_DATE").equals(new SimpleDateFormat("yyyy/MM/dd", Locale.US).format(new Date()))) {
                // Adding shortcut for Activity on Home screen
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
                    ShortcutManager shortcutManager = activity.getSystemService(ShortcutManager.class);
                    List<ShortcutInfo> currPinned = shortcutManager.getPinnedShortcuts();
                    if (currPinned.size() == 0) {
                        if (ShortcutManagerCompat.isRequestPinShortcutSupported(activity)) {
                            ShortcutInfoCompat shortcutInfo = new ShortcutInfoCompat.Builder(activity, "#1")
                                    .setIntent(new Intent(activity, MurliActivity.class)
                                            .setAction(Intent.ACTION_MAIN) // !!! intent's action must be set on oreo
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK))
                                    .setShortLabel("Murli")
                                    .setIcon(IconCompat.createWithResource(activity, R.drawable.murli_icon))
                                    .build();
                            ShortcutManagerCompat.requestPinShortcut(activity, shortcutInfo, null);
                        }
                    }
                } else {
                    if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_ICON_CREATED)) {
                        if (ShortcutManagerCompat.isRequestPinShortcutSupported(activity)) {
                            ShortcutInfoCompat shortcutInfo = new ShortcutInfoCompat.Builder(activity, "#1")
                                    .setIntent(new Intent(activity, MurliActivity.class)
                                            .setAction(Intent.ACTION_MAIN) // !!! intent's action must be set on oreo
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK))
                                    .setShortLabel("Murli")
                                    .setIcon(IconCompat.createWithResource(activity, R.drawable.murli_icon))
                                    .build();
                            ShortcutManagerCompat.requestPinShortcut(activity, shortcutInfo, null);
                        } else {
                            // Shortcut is not supported by your launcher
                        }
                        SharePrefs.getInstance(activity).putBoolean(SharePrefs.IS_ICON_CREATED, true);
                    }
                }
                SharePrefs.getInstance(activity).putString("LAST_LAUNCH_DATE", new SimpleDateFormat("yyyy/MM/dd", Locale.US).format(new Date()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String wordFirstCap(String str) {
        String[] words = str.trim().split(" ");
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (words[i].trim().length() > 0) {
                ret.append(Character.toUpperCase(words[i].trim().charAt(0)));
                ret.append(words[i].trim().substring(1));
                if (i < words.length - 1) {
                    ret.append(' ');
                }
            }
        }
        return ret.toString();
    }

    public static void setLocale(Context context, String lang) {
//        if (lang == null) {
//            lang = "hi";
//        }
//        Locale myLocale = new Locale(lang);
//        Resources res = context.getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
    }

    public static void rateApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }

    public static void shareApp(Context context, boolean isWhatsApp, String skCode) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            if (isWhatsApp) {
                shareIntent.setPackage("com.whatsapp");
            }
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
            String shareMessage = "\n" + MyApplication.getInstance().dbHelper.getString(R.string.referral_share_message) + "\n\n";
            shareMessage = shareMessage + "play.google.com/store/apps/details?id=" + context.getPackageName() + "&referrer=" + skCode;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "Choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyApplication.getInstance().updateAnalyticShare("Referral", "App Share");
    }

    public void showOfferView(View v, @Nullable View anchorView, String s) {
        Snackbar snackbar = Snackbar.make(v, "Offer" + s, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(v.getResources().getColor(R.color.colorAccent));
//        View snackBarView = snackbar.getView();
//        snackBarView.setBackgroundColor(android.R.color.black);
//        TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
//        textView.setTextColor(v.getResources().getColor(android.R.color.white));
        if (anchorView != null)
            snackbar.setAnchorView(anchorView);

//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackBarView.getLayoutParams();
//        params.setMargins(params.leftMargin,
//                params.topMargin,
//                params.rightMargin,
//                params.bottomMargin + 60);
//        snackBarView.setLayoutParams(params);

        snackbar.setAction("view", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }




}