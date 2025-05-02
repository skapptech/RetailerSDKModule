package app.retailer.krina.shop.com.mp_shopkrina_retailer.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.PrefManager;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import io.reactivex.observers.DisposableObserver;

public class ReceiveApiService extends Service {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    private int notificationId = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        mBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.app_name))
                .setSmallIcon(R.drawable.logo_sk)
                .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() != null && intent.hasExtra("notificationId")) {
            notificationId = intent.getIntExtra("notificationId", 0);
        }

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                    getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mChannel.setShowBadge(false);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        Intent notificationIntent = new Intent(getApplicationContext(), HomeActivity.class);
        if (!new PrefManager(this).isLoggedIn()) {
            notificationIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        }
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), PendingIntent.FLAG_UPDATE_CURRENT, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        startForeground((int) System.currentTimeMillis(), mBuilder.getNotification());

        CommonClassForAPI.getInstance(null).notificationReceived(observer, notificationId,
                SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CUSTOMER_ID));

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private final DisposableObserver<JsonElement> observer = new DisposableObserver<JsonElement>() {
        @Override
        public void onNext(@NotNull JsonElement response) {
            stopSelf();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };
}