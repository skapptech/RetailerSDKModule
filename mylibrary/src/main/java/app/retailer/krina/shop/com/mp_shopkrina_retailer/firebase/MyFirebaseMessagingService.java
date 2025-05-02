package app.retailer.krina.shop.com.mp_shopkrina_retailer.firebase;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.notification.ActionBleNotificationActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.FeedbackActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.FullNotificationActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.GamesListActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order.MyOrderActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyWalletActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.notification.NotificationActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.ProductShareActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.VideoNotificationActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.WebViewActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.auth.LoginActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.mygullak.MyGullakActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.TokenResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.PrefManager;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.services.NotificationTimerService;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CreateContact;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import io.reactivex.observers.DisposableObserver;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private int notificationId = 0;
    private boolean isEnabledDismissBtn = false;


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        EndPointPref.getInstance(getApplicationContext()).putString(EndPointPref.FCM_TOKEN, s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("From data payload: " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {
            System.out.println("Message data payload: " + remoteMessage.getData());
        }

        isEnabledDismissBtn = false;
        JSONObject object = new JSONObject(remoteMessage.getData());
        try {
            if (object.has("notificationId")) {
                notificationId = object.getInt("notificationId");
            }
            if (object.has("IsEnabledDismissNotification")) {
                isEnabledDismissBtn = object.getBoolean("IsEnabledDismissNotification");
            }
            RetailerSDKApp.getInstance().mixpanel.getPeople().increment("notificationReceived", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String icon = object.has("icon") ? object.getString("icon") : "";
            if (object.has("apptype") && object.getString("apptype").equalsIgnoreCase("sellerapp")) {
                Intent intent = null;
                if (EndPointPref.getInstance(getApplicationContext()).getBoolean(EndPointPref.showNewSocial))
                    intent = new Intent(getApplicationContext(), FeedActivity.class);
                else
                    intent = new Intent(getApplicationContext(), TradeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("screen", 1);
                intent.putExtra("url", object.getString("url"));
                // send notification
                createNotification(intent, object.getString("body"),
                        object.getString("title"), icon);
            } else if (object.has("notify_type") && !object.getString("notify_type").equals("null") && !TextUtils.isEmpty(object.getString("notify_type"))) {
                switch (object.getString("notify_type")) {
                    case "home":
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class)
                                .putExtra("notificationId", notificationId)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "section":
                        intent = new Intent(getApplicationContext(), HomeActivity.class)
                                .putExtra("notificationCategory", "notificationCategory")
                                .putExtra("sectionName", object.getString("sectionName"))
                                .putExtra("notificationId", notificationId)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "offer":
                        createNotification(new Intent(getApplicationContext(), HomeActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .putExtra("audioFileName", icon)
                                        .putExtra("notificationCategory", "offer")
                                        .putExtra("notificationId", notificationId),
                                object.getString("body"), object.getString("title"), icon);
                        break;
                    case "FinBox":
                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("notificationCategory", "FinBox");
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "CreditLine":
                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("notificationCategory", "CreditLine");
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "category":
                        createNotification(new Intent(getApplicationContext(), HomeActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("audioFileName", icon)
                                        .putExtra("notificationCategory", "category")
                                        .putExtra("notificationId", notificationId),
                                object.getString("body"), object.getString("title"), icon);
                        break;
                    case "search":
                        createNotification(new Intent(getApplicationContext(), HomeActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("audioFileName", icon)
                                        .putExtra("notificationCategory", "search")
                                        .putExtra("notificationId", notificationId),
                                object.getString("body"), object.getString("title"), icon);
                        break;
                    case "brand":
                        createNotification(new Intent(getApplicationContext(), HomeActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("audioFileName", icon)
                                        .putExtra("notificationCategory", "brand")
                                        .putExtra("notificationId", notificationId),
                                object.getString("body"), object.getString("title"), icon);
                    case "brandItem":
                        createNotification(new Intent(getApplicationContext(), HomeActivity.class)
                                        .putExtra("subCategory", object.has("subCategory") ? object.getString("subCategory") : "1")
                                        .putExtra("categoryId", object.has("categoryId") ? object.getString("categoryId") : "1")
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("audioFileName", icon)
                                        .putExtra("notificationCategory", "brandItem")
                                        .putExtra("notificationId", notificationId),
                                object.getString("body"), object.getString("title"), icon);
                        break;
                    case "image":
                        showNotification(icon,
                                object.getString("body"),
                                object.getString("title"), 0);
                        break;
                    case "murli":
                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("audioFileName", icon);
                        intent.putExtra("notificationCategory", "");
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "product":
                        intent = new Intent(getApplicationContext(), ProductShareActivity.class);
                        intent.putExtra("number", object.getString("number"));
                        intent.putExtra("multiMRPId", object.getString("multiMRPId"));
                        intent.putExtra("notificationId", notificationId);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                            createNotification(intent, object.getString("body"),
                                    object.getString("title"),
                                    icon);
                        } else {
                            startActivity(intent);
                        }
                        break;
                    case "order":
                    case "orderShipped":
                    case "OrderShippedAlert":
                        intent = new Intent(getApplicationContext(), MyOrderActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "orderRating":
                        showRatingNotification(object.getString("body"),
                                object.getString("title"),
                                object.getString("ObjectId"));
                        break;
                    case "fullScreen":
                        intent = new Intent(getApplicationContext(), FullNotificationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("image", icon);
                        intent.putExtra("notificationId", notificationId);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                            createNotification(intent, object.getString("body"),
                                    object.getString("title"), icon);
                        } else {
                            startActivity(intent);
                        }
                        break;
                    case "video":
                        intent = new Intent(getApplicationContext(), VideoNotificationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("url", object.getString("url"));
                        intent.putExtra("notificationId", notificationId);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                            createNotification(intent, object.getString("body"),
                                    object.getString("title"), icon);
                        } else {
                            startActivity(intent);
                        }
                        break;
                    case "basket":
                    case "shoppingCart":
                        intent = new Intent(getApplicationContext(), ShoppingCartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"),
                                icon);
                        break;
                    case "Claim":
                    case "target":
                        intent = new Intent(getApplicationContext(), CustomerSubCategoryTargetActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"),
                                icon);
                        break;
                    case "item":
                        intent = new Intent(getApplicationContext(), ProductShareActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("number", object.getString("number"));
                        intent.putExtra("multiMRPId", object.getString("multiMRPId"));
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "tradeWebView":
                    case "trade":
                        showTradeNotification(object.getString("title"),
                                object.getString("body"), object.getString("link"));
                        break;
                    case "tradeOrder":
                        showTradeNotification("Order status changed",
                                object.getString("body"), object.getString("link"));
                        break;
                    case "game":
                        intent = new Intent(getApplicationContext(), GamesListActivity.class);
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"),
                                icon);
                        break;
                    case "url":
                        intent = new Intent(getApplicationContext(), WebViewActivity.class)
                                .putExtra("url", object.getString("url"))
                                .putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"),
                                icon);
                        break;
                    case "feedback":
                        intent = new Intent(getApplicationContext(), FeedbackActivity.class)
                                .putExtra("url", object.getString("url"))
                                .putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "plan":
                        intent = new Intent(getApplicationContext(), MembershipPlanActivity.class)
                                .putExtra("page", 1)
                                .putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "prime":
                        intent = new Intent(getApplicationContext(), MembershipActivity.class);
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "logout":
                        silentLogOut(getApplicationContext(), 0);
                        break;
                    case "logoutAll":
                        silentLogOut(getApplicationContext(), 1);
                        break;
                    case "updateLanguage":
                        SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.IS_FETCH_LANGUAGE, true);
                        break;
                    case "saveContact":
                        new CreateContact(getApplicationContext(), object.getString("name"), object.getString("number"));
                        break;
                    case "direct":
                        if (EndPointPref.getInstance(getApplicationContext()).getBoolean(EndPointPref.showNewSocial))
                            intent = new Intent(getApplicationContext(), FeedActivity.class);
                        else
                            intent = new Intent(getApplicationContext(), TradeActivity.class);
                        intent.putExtra("screen", 3);
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "gullak":
                        intent = new Intent(getApplicationContext(), MyGullakActivity.class);
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "wallet":
                        intent = new Intent(getApplicationContext(), MyWalletActivity.class);
                        intent.putExtra("notificationId", notificationId);
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "baseUrl":
                        new EndPointPref(getApplicationContext()).putString(EndPointPref.API_ENDPOINT,
                                object.getString("baseUrl"));
                        break;
                    case "ApphomeBottomCall":
                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("notificationId", notificationId);
                        intent.putExtra("notificationCategory", object.getString("notificationCategory"));
                        intent.putExtra("typeId", object.getString("typeId"));
                        intent.putExtra("url", object.getString("url"));
                        createNotification(intent, object.getString("body"),
                                object.getString("title"), icon);
                        break;
                    case "flashDealStart":
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(new Intent(getApplicationContext(), NotificationTimerService.class)
                                    .putExtra("Title", object.getString("title"))
                                    .putExtra("Time", object.getString("body")));
                        } else
                            startService(new Intent(getApplicationContext(), NotificationTimerService.class)
                                    .putExtra("Title", object.getString("title"))
                                    .putExtra("Time", object.getString("body")));
                        break;
                    case "flashDealEnd":
                        stopService(new Intent(getApplicationContext(), NotificationTimerService.class));
                        break;
                    case "UdharUPINotification":
                        createNotification(new Intent(), object.getString("body"),
                                object.getString("title"), icon);
                        intent = new Intent("UdharUPINotification");
                        intent.putExtra("url", object.getString("url"));
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        break;
                    default:
                        showNotification(icon,
                                object.getString("body"),
                                object.getString("title"), 0);
                        break;
                }
                if (!object.getString("notify_type").equalsIgnoreCase("logout") && !object.getString("notify_type")
                        .equalsIgnoreCase("prime")) {
                    genToken();
                }
            } else {
                String body = "";
                String title = "";
                int typeId = 0;
                int notificationId = 0;
                String notificationType = "";
                String notificationCategory = "";
                String image = "";

                if (object.has("body")) {
                    body = object.getString("body");
                }
                if (object.has("icon")) {
                    icon = object.getString("icon");
                    image = icon.replaceAll(" ", "%20");
                }
                if (object.has("title")) {
                    title = object.getString("title");
                }
                if (object.has("notificationCategory")) {
                    notificationCategory = object.getString("notificationCategory");
                }
                if (object.has("notificationType")) {
                    notificationType = object.getString("notificationType");
                }
                if (object.has("typeId")) {
                    typeId = object.getInt("typeId");
                }
                if (object.has("notificationId")) {
                    notificationId = object.getInt("notificationId");
                }

                if (notificationType.equalsIgnoreCase("Actionable")) {
                    setNotificationItems(image, body, title, notificationCategory, typeId, notificationId);
                } else {
                    showNotification(image, body, title, notificationId);
                }
                genToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.NOTIFICATION_COUNT,
                SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.NOTIFICATION_COUNT) + 1);

        if (notificationId != 0) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(new Intent(getApplicationContext(), ReceiveApiService.class)
//                        .putExtra("notificationId", notificationId));
//            } else
//                startService(new Intent(getApplicationContext(), ReceiveApiService.class)
//                        .putExtra("notificationId", notificationId));
            CommonClassForAPI.getInstance(null).notificationReceived(observer, notificationId,
                    SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CUSTOMER_ID));
        }
    }


    private void genToken() {
        if (TextUtils.isEmpty(SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.TOKEN_DATE))) {
            SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.TOKEN_DATE, new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(new Date()));
        }
        if (!new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(new Date()).equals(SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.TOKEN_DATE))) {
            CommonClassForAPI.getInstance(null).getToken(callTokenDes, "password",
                    SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.TOKEN_NAME),
                    SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.TOKEN_PASSWORD));
        }
    }


    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.logo_sk : R.drawable.logo_sk;
    }

    private void showNotification(String imageUrl, String messageBody, String title, int actionId) {
        int id = (int) System.currentTimeMillis();
        Bitmap myBitmap = null;
        try {
            if (!TextUtils.isEmpty(imageUrl)) {
                if (imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                    myBitmap = getBitmapFromURL(imageUrl);
                }
            }
            Intent intent;
            if (!new PrefManager(getApplicationContext()).isLoggedIn())
                intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            else
                intent = new Intent(getApplicationContext(), NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", true);
            intent.putExtra("NotificationId", actionId);

            PendingIntent pendingIntent = null;
            int flag = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, intent,
                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT);
                flag = PendingIntent.FLAG_IMMUTABLE;
            } else {
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, intent,
                        PendingIntent.FLAG_ONE_SHOT);
                flag = PendingIntent.FLAG_UPDATE_CURRENT;
            }
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // create channel
            String channelId = getChannel(getResources().getString(R.string.app_name));

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
            // exit button
            if (isEnabledDismissBtn) {
                // close Intent
                Intent closeIntent = new Intent(getApplicationContext(), NotificationDismissActivity.class);
                closeIntent.putExtra("id", id);
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), id, closeIntent, flag);
                builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "Dismiss", contentIntent);
                builder.setOngoing(true);
            } else {
                builder.setAutoCancel(true);
            }
            if (myBitmap != null) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(myBitmap));
            } else {
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
            }
            builder.setSmallIcon(getNotificationIcon());
            builder.setContentTitle(title);
            builder.setContentText(messageBody);
            builder.setContentInfo(messageBody);
            builder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setSound(defaultSoundUri);
            builder.setChannelId(channelId);
            builder.setSubText("New Message");
            builder.setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManagerCompat =
                    NotificationManagerCompat.from(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManagerCompat.notify(id, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showRatingNotification(String messageBody, String title, String OrderId) {
        int id = (int) System.currentTimeMillis();
        try {
            // convert image bitmap
            Intent intent;
            if (new PrefManager(getApplicationContext()).isLoggedIn()) {
                intent = new Intent(getApplicationContext(), HomeActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("ORDER_ID", OrderId);
            intent.putExtra("notificationId", notificationId);
            PendingIntent pendingIntent = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, intent,
                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT);
            } else {
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            }
            // create channel
            String channelId = getChannel("Rating");
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                    .setSmallIcon(getNotificationIcon())
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setContentInfo(messageBody)
                    .setTicker("Rate your Order")
                    .setSubText("Rate your Order")
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                    .setChannelId(channelId)
                    .setContentIntent(pendingIntent);
            NotificationManagerCompat notificationManagerCompat =
                    NotificationManagerCompat.from(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManagerCompat.notify(id, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNotification(Intent intent, String body, String title, String imageUrl) {
        int id = (int) System.currentTimeMillis();
        Bitmap myBitmap = null;
        try {
            if (!TextUtils.isEmpty(imageUrl)) {
                if (imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                    myBitmap = getBitmapFromURL(imageUrl);
                }
            }
            if (!new PrefManager(getApplicationContext()).isLoggedIn()) {
                intent = new Intent(getApplicationContext(), MobileSignUpActivity.class);
            }
            PendingIntent pendingIntent = null;
            int flag = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, intent,
                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT);
                flag = PendingIntent.FLAG_IMMUTABLE;
            } else {
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, intent,
                        PendingIntent.FLAG_ONE_SHOT);
                flag = PendingIntent.FLAG_UPDATE_CURRENT;
            }
            // create channel
            String channelId = getChannel(getResources().getString(R.string.app_name));

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
            // exit button
            if (isEnabledDismissBtn) {
                // close Intent
                Intent closeIntent = new Intent(getApplicationContext(), NotificationDismissActivity.class);
                closeIntent.putExtra("id", id);
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), id, closeIntent, flag);
                builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "Dismiss", contentIntent);
                builder.setOngoing(true);
            } else {
                builder.setAutoCancel(true);
            }

            if (myBitmap != null) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(myBitmap));
            } else {
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
            }
            builder.setSmallIcon(getNotificationIcon());
            builder.setContentTitle(title);
            builder.setContentText(body);
            builder.setContentInfo(body);
            builder.setAutoCancel(true);
            builder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setChannelId(channelId);
            builder.setSubText("New Message");
            builder.setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManagerCompat =
                    NotificationManagerCompat.from(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManagerCompat.notify(id, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setNotificationItems(String imageUrl, String messageBody, String title, String notificationCategory, int typeId, int notificationId) {
        int id = (int) System.currentTimeMillis();
        Bitmap myBitmap = null;
        Intent intent;
        try {
            // convert image bitmap
            if (!TextUtils.isEmpty(imageUrl)) {
                if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                    myBitmap = getBitmapFromURL(imageUrl);
                }
            }
            // convert
            if (notificationCategory.equalsIgnoreCase("Flash Deal") || notificationCategory.equalsIgnoreCase("offer")) {
                intent = new Intent(getApplicationContext(), HomeActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), ActionBleNotificationActivity.class);
            }
            if (!new PrefManager(getApplicationContext()).isLoggedIn()) {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            }
            intent.putExtra("notificationCategory", notificationCategory);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("typeId", typeId);
            intent.putExtra("Notification", true);
            intent.putExtra("NotificationId", notificationId);

            PendingIntent pendingIntent = null;
            int flag = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, intent,
                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT);
                flag = PendingIntent.FLAG_IMMUTABLE;
            } else {
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, intent,
                        PendingIntent.FLAG_ONE_SHOT);
                flag = PendingIntent.FLAG_UPDATE_CURRENT;
            }
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // create channel
            String channelId = getChannel("Items");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
            // exit button
            if (isEnabledDismissBtn) {
                // close Intent
                Intent closeIntent = new Intent(getApplicationContext(), NotificationDismissActivity.class);
                closeIntent.putExtra("id", id);
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), id, closeIntent, flag);
                builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "Dismiss", contentIntent);
                builder.setOngoing(true);
            } else {
                builder.setAutoCancel(true);
            }

            if (myBitmap != null) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(myBitmap));
            } else {
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
            }
            builder.setSmallIcon(getNotificationIcon());
            builder.setContentTitle(title);
            builder.setContentText(messageBody);
            builder.setContentInfo(messageBody);
            builder.setAutoCancel(true);
            builder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setSound(defaultSoundUri);
            builder.setChannelId(channelId);
            builder.setSubText("New Message");
            builder.setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManagerCompat =
                    NotificationManagerCompat.from(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManagerCompat.notify(id, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showTradeNotification(String title, String body, String link) {
        int id = (int) System.currentTimeMillis();
        // convert image bitmap
        Intent intent;
        if (new PrefManager(getApplicationContext()).isLoggedIn()) {
            if (EndPointPref.getInstance(getApplicationContext()).getBoolean(EndPointPref.showNewSocial))
                intent = new Intent(getApplicationContext(), FeedActivity.class);
            else
                intent = new Intent(getApplicationContext(), TradeActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("url", link);
        intent.putExtra("screen", 1);
        intent.putExtra("notificationId", notificationId);
        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT);
        } else {
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        // create channel
        String channelId = getChannel("Trade");

        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.direct_sign)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo(body)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_grey_blue))
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setChannelId(channelId)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(id, notificationBuilder.build());
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getChannel(String channelId) {
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            channel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setLightColor(Color.MAGENTA);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
        }
        return channelId;
    }


    // update notification count
    private final DisposableObserver<JsonElement> observer = new DisposableObserver<JsonElement>() {
        @Override
        public void onNext(@NotNull JsonElement response) {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };

    // getting token response
    private final DisposableObserver<TokenResponse> callTokenDes = new DisposableObserver<TokenResponse>() {
        @Override
        public void onNext(@NotNull TokenResponse response) {
            try {
                if (response != null) {
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.TOKEN, response.access_token);
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.TOKEN_DATE, new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(new Date()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };


    public void silentLogOut(Context context, int i) {
        if (!BuildConfig.DEBUG || i == 1) {
            RetailerSDKApp.getInstance().clearLocalData();
            RetailerSDKApp.getInstance().clearCartData();
            PrefManager prefManager = new PrefManager(context);
            prefManager.setLoggedIn(false);
            Intent intent = new Intent(context, MobileSignUpActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            LocaleHelper.setLocale(context, "hi");
        }
    }


    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
}