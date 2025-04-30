package app.retailer.krina.shop.com.mp_shopkrina_retailer.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.PrefManager;

public class NotificationTimerService extends Service implements PropertyChangeListener {

    private static final String ACTION_CHANGESTATE = "com.notificationstopwatch.action_changestate";
    private static final String ACTION_RESET = "com.notificationstopwatch.action_reset";
    private static final String ACTION_EXIT = "com.notificationstopwatch.action_exit";

    private static final int NOTIFICATION_ID = 590123562;

    private NotificationManager mNotificationManager;
    private boolean isNotificationShowing;
    private BroadcastReceiver recieverStateChange;
    private BroadcastReceiver recieverReset;
    private BroadcastReceiver recieverExit;
    private Timer t;
    private NotificationCompat.Builder mBuilder;

    private String intentTitle, intentTime;


    @Override
    public void onCreate() {
        super.onCreate();
        mBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.app_name))
                .setSmallIcon(R.drawable.logo_sk)
                .setOnlyAlertOnce(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.hasExtra("Title") && intent.hasExtra("Time")) {
            intentTitle = intent.getStringExtra("Title");
            intentTime = intent.getStringExtra("Time");
        }

        isNotificationShowing = false;
        this.mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        IntentFilter filterNext = new IntentFilter(ACTION_CHANGESTATE);
        recieverStateChange = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (TimeContainer.getInstance().getCurrentState() == TimeContainer.STATE_RUNNING) {
                    TimeContainer.getInstance().pause();
                } else {
                    TimeContainer.getInstance().start();
                }
                updateNotification();
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(recieverStateChange, filterNext, Context.RECEIVER_NOT_EXPORTED);
        } else
            registerReceiver(recieverStateChange, filterNext);
        recieverReset = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TimeContainer.getInstance().reset();
                updateNotification();
            }
        };
        IntentFilter filterPause = new IntentFilter(ACTION_RESET);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(recieverReset, filterPause, Context.RECEIVER_NOT_EXPORTED);
        } else
            registerReceiver(recieverReset, filterPause);

        IntentFilter filterPrev = new IntentFilter(ACTION_EXIT);
        recieverExit = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TimeContainer.getInstance().reset();
                stopForeground(true);
                isNotificationShowing = false;
                stopSelf();
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(recieverExit, filterPrev, Context.RECEIVER_NOT_EXPORTED);
        } else
            registerReceiver(recieverExit, filterPrev);
        startUpdateTimer();
        TimeContainer.getInstance().isServiceRunning.set(true);
        TimeContainer.getInstance().start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (t != null) {
            t.cancel();
            t = null;
        }
        unregisterReceiver(recieverExit);
        unregisterReceiver(recieverReset);
        unregisterReceiver(recieverStateChange);
        TimeContainer.getInstance().isServiceRunning.set(false);
        super.onDestroy();
    }


    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName() == TimeContainer.STATE_CHANGED) {
            startUpdateTimer();
        }
    }


    public void startUpdateTimer() {
        if (t != null) {
            t.cancel();
            t = null;
        }
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                updateNotification();
            }
        }, 0, 1000);
    }

    private synchronized void updateNotification() {
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_timer);
        /*if (TimeContainer.getInstance().getCurrentState() == TimeContainer.STATE_RUNNING) {
            contentView.setImageViewResource(R.id.btn_notification_changestate, android.R.drawable.ic_media_pause);
        } else {
            contentView.setImageViewResource(R.id.btn_notification_changestate, android.R.drawable.ic_media_play);
        }*/

        contentView.setTextViewText(R.id.tv_notificationTitle, intentTitle);

        contentView.setTextViewText(R.id.btn_notficationTimer, msToHourMinSec(TimeContainer.getInstance().getElapsedTime()));

        Intent changeStateIntent = new Intent(ACTION_CHANGESTATE, null);
        PendingIntent changeStatePendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, changeStateIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent resetIntent = new Intent(ACTION_RESET, null);
        PendingIntent resetPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, resetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent exitIntent = new Intent(ACTION_EXIT, null);
        PendingIntent exitPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //contentView.setOnClickPendingIntent(R.id.btn_notficationTimer, resetPendingIntent);
        //contentView.setOnClickPendingIntent(R.id.btn_notification_changestate, changeStatePendingIntent);
        contentView.setOnClickPendingIntent(R.id.btn_notification_exit, exitPendingIntent);

        mBuilder.setContent(contentView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                    getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableLights(true);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        Intent notificationIntent = new Intent(getApplicationContext(), HomeActivity.class);
        if (!new PrefManager(this).isLoggedIn()) {
            notificationIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        }
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), PendingIntent.FLAG_UPDATE_CURRENT, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(intent);
        if (isNotificationShowing) {
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.getNotification());
        } else {
            isNotificationShowing = true;
            startForeground(NOTIFICATION_ID, mBuilder.getNotification());
        }
    }

    /**
     * Singleton design class used to communicate between {@link android.app.Activity} and {@link Service}
     * @author KodarKoooperativet
     */
    public static class TimeContainer {

        public static final int STATE_STOPPED = 0;
        public static final int STATE_PAUSED = 1;
        public static final int STATE_RUNNING = 2;

        private static TimeContainer instance;
        public AtomicBoolean isServiceRunning;
        private final PropertyChangeSupport observers;

        public static final String STATE_CHANGED = "state_changed";

        private int currentState;
        private long startTime;
        private long elapsedTime;

        private final Object mSynchronizedObject = new Object();

        private TimeContainer() {
            isServiceRunning = new AtomicBoolean(false);
            observers = new PropertyChangeSupport(this);
        }

        public void addObserver(PropertyChangeListener listener) {
            observers.addPropertyChangeListener(listener);
        }

        public void removeObserver(PropertyChangeListener listener) {
            observers.removePropertyChangeListener(listener);
        }

        public static TimeContainer getInstance() {
            if (instance == null) {
                instance = new TimeContainer();
            }
            return instance;
        }

        public void notifyStateChanged() {
            observers.firePropertyChange(STATE_CHANGED, null, currentState);
        }

        public int getCurrentState() {
            return currentState;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getElapsedTime() {
            if (startTime == 0) {
                return elapsedTime;
            } else {
                return elapsedTime + (System.currentTimeMillis() - startTime);
            }
        }

        public void start() {
            synchronized (mSynchronizedObject) {
                startTime = System.currentTimeMillis();
                currentState = STATE_RUNNING;
                notifyStateChanged();
            }
        }

        public void reset() {
            synchronized (mSynchronizedObject) {
                if (currentState == STATE_RUNNING) {
                    startTime = System.currentTimeMillis();
                    elapsedTime = 0;
                    currentState = STATE_RUNNING;
                    notifyStateChanged();
                } else {
                    startTime = 0;
                    elapsedTime = 0;
                    currentState = STATE_STOPPED;
                    notifyStateChanged();
                }
            }
        }

        public void stopAndReset() {
            synchronized (mSynchronizedObject) {
                startTime = 0;
                elapsedTime = 0;
                currentState = STATE_STOPPED;
                notifyStateChanged();
            }
        }

        public void pause() {
            synchronized (mSynchronizedObject) {
                elapsedTime = elapsedTime + (System.currentTimeMillis() - startTime);
                startTime = 0;
                currentState = STATE_PAUSED;
                notifyStateChanged();
            }
        }
    }

    private String msToHourMinSec(long ms) {
        if (ms == 0) {
            return "00:00";
        } else {
            long seconds = (ms / 1000) % 60;
            long minutes = (ms / 1000) / 60;
            long hours = minutes / 60;

            StringBuilder sb = new StringBuilder();
            if (hours > 0) {
                sb.append(hours);
                sb.append(':');
            }
            if (minutes > 0) {
                minutes = minutes % 60;
                if (minutes >= 10) {
                    sb.append(minutes);
                } else {
                    sb.append(0);
                    sb.append(minutes);
                }
            } else {
                sb.append('0');
                sb.append('0');
            }
            sb.append(':');
            if (seconds > 0) {
                if (seconds >= 10) {
                    sb.append(seconds);
                } else {
                    sb.append(0);
                    sb.append(seconds);
                }
            } else {
                sb.append('0');
                sb.append('0');
            }
            return sb.toString();
        }
    }
}