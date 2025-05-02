package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct;

import static app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActvityTradeBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.Listener;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.EditProfileActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order.MyOrderActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.notification.NotificationActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.EasyWayLocation;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.GPSTracker;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;

public class TradeActivity extends AppCompatActivity implements Listener {
    private final int MY_PERMISSION_REQUEST_CODE = 123, INPUT_FILE_REQUEST_CODE = 1, FILECHOOSER_RESULTCODE = 1;
    private ActvityTradeBinding mBinding;

    private TradeActivity activity;

    private ValueCallback<Uri> mUploadMessage;
    private final Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private boolean isRegistered = false;
    private String mCameraPhotoPath;
    private Dialog dialogRazor;
    boolean permissionResult = false;
    String razorRedirectUrl = "";
    private EasyWayLocation easyWayLocation;
    private LocationRequest locationRequest;
    private ProgressDialog progressDialog;
    private LatLng locationLatLong = new LatLng(0, 0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.actvity_trade);
        activity = this;
        if (RetailerSDKApp.getInstance().prefManager.isLoggedIn()) {
            initialization();
        } else {
            startActivity(new Intent(getApplicationContext(), MobileSignUpActivity.class));
            finish();
            Utils.leftTransaction(this);
        }

        initLocation();

        mBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            mBinding.webview.reload();
            mBinding.swipeRefreshLayout.setRefreshing(false);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBinding.webview.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> mBinding.swipeRefreshLayout.setEnabled(scrollY == 0));
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getIntent().getExtras() != null && getIntent().hasExtra("notificationId")) {
            int notificationId = getIntent().getExtras().getInt("notificationId");
            RetailerSDKApp.getInstance().notificationView(notificationId);
            getIntent().getExtras().clear();
        }
    }


    @Override
    public void onBackPressed() {
        mBinding.swipeRefreshLayout.setEnabled(true);
        if (mBinding.webview.canGoBack()) {
//            mBinding.webview.goBack();
            mBinding.webview.loadUrl(SharePrefs.getInstance(getApplicationContext())
                    .getString(SharePrefs.TRADE_WEB_URL) + SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID));
        } else {
            if (!SharePrefs.getInstance(getApplicationContext()).getBoolean(SharePrefs.IS_WAREHOUSE_AVAIL)) {
                finish();
                finishAffinity();
                return;
            }
            super.onBackPressed();
            Utils.fadeTransaction(TradeActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (onComplete != null && isRegistered) {
            unregisterReceiver(onComplete);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCATION_SETTING_REQUEST_CODE && easyWayLocation != null) {
            easyWayLocation.onActivityResult(resultCode);
            if (resultCode == 0) {
                permissionDeniedDialog("Permission Denied", "You have to turn on GPS for location.", 2);
            }
        } else if (requestCode == 1002) {
            String jsonDataIntent = data.getStringExtra("jsonData");
            mBinding.webview.loadUrl(razorRedirectUrl + "?" + jsonDataIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }


    @Override
    public void locationOn() {
        doLocationWork();
    }

    @Override
    public void currentLocation(Location location) {
        easyWayLocation.endUpdates();
        locationLatLong = new LatLng(location.getLatitude(), location.getLongitude());
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        initialization();
    }

    @Override
    public void locationCancelled() {
        Toast.makeText(activity, "Location Cancelled", Toast.LENGTH_SHORT).show();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    private void initLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        easyWayLocation = new EasyWayLocation(activity, locationRequest, false, true, activity);
    }

    public void callLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        Permissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                checkLocationSetting();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                permissionDeniedDialog("Permission Denied", "You have to allow location permission.", 1);
            }
        });
    }

    private void checkLocationSetting() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.setAlwaysShow(true);
        builder.addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            doLocationWork();
        });

        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(activity,
                            LOCATION_SETTING_REQUEST_CODE);
                } catch (IntentSender.SendIntentException sendEx) {
                    sendEx.printStackTrace();
                }
            }
        });
    }

    private void permissionDeniedDialog(String title, String msg, int type) {
        Dialog dialog = new Dialog(activity, R.style.BottomTheme);
        dialog.setContentView(R.layout.dialog_permission_denied);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvDetail = dialog.findViewById(R.id.tvDetail);
        TextView tvOk = dialog.findViewById(R.id.tvOk);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        tvTitle.setText(title);
        tvDetail.setText(msg);
        dialog.show();
        tvOk.setOnClickListener(view -> {
            if (type == 1) {
                callLocationPermission();
            } else {
                checkLocationSetting();
            }
            dialog.dismiss();
        });

        tvCancel.setOnClickListener(view -> finish());
    }

    private void doLocationWork() {
        showLoader();
        easyWayLocation.startLocation();
    }

    public void showLoader() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Getting Location, Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    private void initialization() {
        WebSettings webSettings = mBinding.webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
//        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mBinding.webview.addJavascriptInterface(new JavaScriptInterface(this), "Android");

        mBinding.webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(".png") || url.contains(".jpg")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "application/file");
                    try {
                        view.getContext().startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mBinding.webview.loadUrl(url);
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mBinding.pBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mBinding.pBar.setVisibility(View.GONE);
            }

            @Override
            public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP)
                    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        mBinding.webview.loadUrl("javascript:onEnter()");
                    }
            }
        });
        mBinding.webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                dialogRazor = new Dialog(activity, R.style.BottomTheme);
                dialogRazor.setContentView(R.layout.dialog_webview);
                dialogRazor.setCanceledOnTouchOutside(false);
                WebView dialogWebview = dialogRazor.findViewById(R.id.dialogWebview);
                WebSettings webSettings = dialogWebview.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);
//                webSettings.setAppCacheEnabled(true);
                webSettings.setDatabaseEnabled(true);
                webSettings.setDomStorageEnabled(true);
                webSettings.setAllowContentAccess(true);
                webSettings.setSupportMultipleWindows(true);
                webSettings.setAllowFileAccessFromFileURLs(true);
                webSettings.setAllowUniversalAccessFromFileURLs(true);
                webSettings.setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
                webSettings.setSupportZoom(true);
                webSettings.setBuiltInZoomControls(true);
                webSettings.setPluginState(WebSettings.PluginState.ON);
                dialogWebview.addJavascriptInterface(new PaymentInterface(), "PaymentInterface");
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(dialogWebview);
                resultMsg.sendToTarget();

                dialogWebview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (url.contains("status=failed")) {
                            if (dialogRazor != null && dialogRazor.isShowing()) {
                                dialogRazor.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        //System.out.println("ErrorUrl - "+ failingUrl+" - "+view.getUrl());
                    }
                });
                dialogRazor.show();
                return true;
            }

            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]>
                    filePath, FileChooserParams fileChooserParams) {
                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePath;

                /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        Log.e("Common.TAG", "Unable to create Image File", ex);
                    }

                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
*/
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                /*Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }*/

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                // chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

                return true;
            }
        });

        int customerID = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID);
//        try {
//            mobileNo = Aes256.encryptMobile(SharePrefs.getInstance(this).getString(SharePrefs.MOBILE_NUMBER),
//                    new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(new Date()) + "1201");
//        } catch (Exception e) {
//            e.printStackTrace();
//            mobileNo = SharePrefs.getInstance(this).getString(SharePrefs.MOBILE_NUMBER);
//        }

        if (getIntent().getExtras() != null) {
            String url;
            if (getIntent().getIntExtra("screen", 0) == 1) {
                url = SharePrefs.getInstance(this).getString(SharePrefs.TRADE_WEB_URL) +
                        customerID + "?redirectUrl=" +
                        getIntent().getStringExtra("url");
            } else if (getIntent().getIntExtra("screen", 0) == 2) {
                url = getIntent().getStringExtra("url");
            } else if (getIntent().getIntExtra("screen", 0) == 3) {
                url = SharePrefs.getInstance(this).getString(SharePrefs.TRADE_WEB_URL)
                        + customerID;
            } else {
                if (getIntent().getData() != null) {
                    url = getIntent().getData().toString();
                    url = url.replace("http://dl.trade.er15.xyz/",
                            SharePrefs.getInstance(getApplicationContext())
                                    .getString(SharePrefs.TRADE_WEB_URL)
                                    + customerID
                                    + "-");
                } else {
                    url = SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.TRADE_WEB_URL)
                            + customerID;
                }
            }
//            url = "https://directb2bbuyer.shopkirana.in/#/feed/list/" +
//                    SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CUSTOMER_ID);
            mBinding.webview.loadUrl(url);
            System.out.println("postUrl " + url);
        } else {
            String url = SharePrefs.getInstance(getApplicationContext())
                    .getString(SharePrefs.TRADE_WEB_URL) + customerID;
//            url = "https://directb2bbuyer.shopkirana.in/#/feed/list/" +
//                    SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CUSTOMER_ID);
            mBinding.webview.loadUrl(url);
            System.out.println("postUrl " + mBinding.webview.getUrl());
        }
    }

    private void closeDialogRazor() {
        if (dialogRazor != null && dialogRazor.isShowing()) {
            dialogRazor.dismiss();
        }
    }

    private void downloadFileFromUrl(String url) {
        isRegistered = true;
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED);

        String fileName = url.substring(url.lastIndexOf("/") + 1);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDescription("Download " + fileName + " from " + url);
        request.setTitle("Document Downloading");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Trade");
        downloadManager.enqueue(request);
        Toast.makeText(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper
                .getString(R.string.document_downloaded_in), Toast.LENGTH_SHORT).show();
    }


    private void setNotification(String messageBody, String title) {
        try {
            final String CHANNEL_ID = "chat";
            final String CHANNEL_NAME = "chat";

            Intent intent = new Intent(this, TradeActivity.class);
            //intent.putExtra("list", poModel);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Main PendingIntent that restarts
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            // create notification
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.direct_sign)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setContentInfo(title)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            notificationManager.notify(1, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareProduct(String imagePath, String body, String returnPath) {
        Utils.hideProgressDialog();

        runOnUiThread(() -> {
            try {
                Picasso.get().load(imagePath).into(target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                getApplicationContext().getPackageName()  + ".provider", new File(Environment.getExternalStorageDirectory()
                        + Constant.PRODUCT_IMAGE_FOLDER + "image.png")));
        share.putExtra(Intent.EXTRA_TEXT, body + "\n http://dl.trade.er15.xyz/" + returnPath);
        startActivity(Intent.createChooser(share, "Share Product"));
    }


    public void ringtone() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShareText(String text) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(share, "Share"));
        RetailerSDKApp.getInstance().updateAnalyticShare("TradeActivity", text);
    }

    public void Call(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission(text);
        } else {
            callContact(text);
        }
    }

    public void checkPermission(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    ActivityCompat.requestPermissions(
                            TradeActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSION_REQUEST_CODE);
                } else { // Request permission
                    ActivityCompat.requestPermissions(
                            TradeActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSION_REQUEST_CODE
                    );
                }
            } else {
                callContact(text);
            }
        }
    }

    private boolean accessCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_REQUEST_CODE);
                }
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private void callContact(String text) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel: " + text));
        if (ActivityCompat.checkSelfPermission
                (TradeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(TradeActivity.this, Manifest.permission.CALL_PHONE)) {

            } else {
                ActivityCompat.requestPermissions(TradeActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSION_REQUEST_CODE);
            }
        }
        startActivity(callIntent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    private String getCurrentLatLong() {
        JSONObject jsonObject = new JSONObject();
        GPSTracker gpsTracker = new GPSTracker(TradeActivity.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            try {
                jsonObject.put("lat", latitude);
                jsonObject.put("long", longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
        return jsonObject.toString();
    }

    private void Open(String PackageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(PackageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + PackageName));
            startActivity(intent);
        }
    }

    private void Logout() {
        Intent i = new Intent(activity, HomeActivity.class);
        startActivity(i);
    }

    private boolean appInstalledOrNot(String packageManager) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageManager, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showShareWhatsappDialog(String textMsg, String number) {
        BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.BottomTheme);
        dialog.setContentView(R.layout.dialog_whatsapp_share);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout llWhatsapp = dialog.findViewById(R.id.llWhatsapp);
        LinearLayout llWhatsappBusiness = dialog.findViewById(R.id.llWhatsappBusiness);
        if (appInstalledOrNot("com.whatsapp") && appInstalledOrNot("com.whatsapp.w4b")) {
            dialog.show();
        } else shareOnWhatsapp(textMsg, number, !appInstalledOrNot("com.whatsapp"));
        llWhatsapp.setOnClickListener(view -> {
            shareOnWhatsapp(textMsg, number, false);
            dialog.dismiss();
        });
        llWhatsappBusiness.setOnClickListener(view -> {
            shareOnWhatsapp(textMsg, number, true);
            dialog.dismiss();
        });
    }

    private void shareOnWhatsapp(String textMsg, String number, boolean isWB) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        if (isWB) {
            whatsappIntent.setPackage("com.whatsapp.w4b");
        } else {
            whatsappIntent.setPackage("com.whatsapp");
        }
        if (number != null && !number.equals("")) {
            whatsappIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("91" + number) + "@s.whatsapp.net");
        }
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, textMsg);
        try {
            activity.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Utils.setToast(activity, "Whatsapp not installed.");
        }
    }

    private void closeApp() {
        finish();
        finishAffinity();
    }

    private void reloadPageview() {
        mBinding.webview.reload();
    }

    private void redirectPageview(String url) {
        mBinding.webview.loadUrl(url);
    }

    private void urlOpenInBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void clearWebviewCache() {
        mBinding.webview.clearCache(true);
        mBinding.webview.reload();
    }


    private void showToastMessage(String msg) {
        Utils.setToast(activity, msg);
    }

    private void initRazorPayCustom(String key, String data) {
       /* Intent i = new Intent(activity, RazorPayPaymentActivity.class);
        i.putExtra("key", key);
        i.putExtra("data", data);
        startActivityForResult(i, 1002);*/
    }


    private class JavaScriptInterface {
        private final Context context;

        JavaScriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void setHeader(String title) {
            setTitle(title);
        }

        @JavascriptInterface
        public void openActivity(String data) {
            startActivity(new Intent(context, TradeActivity.class));
        }

        @JavascriptInterface
        public void shareItem(String imagePath, String body, String returnPath) {
            shareProduct(imagePath, body, returnPath);
        }

        @JavascriptInterface
        public void openHome() {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        @JavascriptInterface
        public void closeTrade() {
            if (!TextUtils.isNullOrEmpty(SharePrefs.getInstance(TradeActivity.this).getString(SharePrefs.CLUSTER_ID))) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                Utils.fadeTransaction(TradeActivity.this);
            } else {
//                finish();
            }
        }

        @JavascriptInterface
        public void editcustomer(boolean b) {
            if (b) {
                Utils.setToast(context, RetailerSDKApp.getInstance().dbHelper.getData("please_update_your_profile_to_access_trade"));
                startActivity(new Intent(context, EditProfileActivity.class));
            } else {
                startActivity(new Intent(context, EditProfileActivity.class));
            }
            finish();
        }

        @JavascriptInterface
        public void showToast(String toast) {
            showToastMessage(toast);
        }

        @JavascriptInterface
        public void downloadPath(String url) {
            downloadFileFromUrl(url);
        }

        @JavascriptInterface
        public void toneSoundPath() {
            ringtone();
        }

        @JavascriptInterface
        public void openSellerBuyerSelectActivity(String data) {
            startActivity(new Intent(context, HomeActivity.class));
        }

        @JavascriptInterface
        public void sendNotification(String data) {
            setNotification(data, "SKDirect");
        }

        @JavascriptInterface
        public void shareWhatsapp(String message, String number) {
            showShareWhatsappDialog(message, number);
        }

        @JavascriptInterface
        public void exitApp() {
            closeApp();
        }

        @JavascriptInterface
        public void clearCache() {
            clearWebviewCache();
        }

        @JavascriptInterface
        public void openApp(String AppName, String PackageName) {
            Open(PackageName);
        }

        @JavascriptInterface
        public void shareText(String text) {
            ShareText(text);
        }

        @JavascriptInterface
        public void callNumber(String text) {
            Call(text);
        }

        @JavascriptInterface
        public void callLogout() {
            Logout();
        }

        @JavascriptInterface
        public void updateToken(String token) {

        }

        @JavascriptInterface
        public boolean isOpenInApp() {
            return true;
        }

        @JavascriptInterface
        public void reloadPage() {
            reloadPageview();
        }

        @JavascriptInterface
        public void redirectPage(String url) {
            redirectPageview(url);
        }

        @JavascriptInterface
        public void openUrlInBrowser(String url) {
            urlOpenInBrowser(url);
        }

        @JavascriptInterface
        public void askPermission() {

        }

        @JavascriptInterface
        public String getCurrentLocation() {
            return getCurrentLatLong();
        }

        @JavascriptInterface
        public void closeRazorDialog() {
            closeDialogRazor();
        }

        @JavascriptInterface
        public boolean getCameraPermission() {
            return callRunTimePermissions();
        }

        @JavascriptInterface
        public void openRazorPayment(String key, String jsonData, String redirectUrl) {
            razorRedirectUrl = redirectUrl;
            initRazorPayCustom(key, jsonData);
        }

        @JavascriptInterface
        public void clearAllDataLogout() {
            RetailerSDKApp.getInstance().logout(TradeActivity.this);
        }

        @JavascriptInterface
        public void initiateLocationSearch() {
            callLocationPermission();
        }

        @JavascriptInterface
        public String returnLatLongtoServer() {
            return getLatLong();
        }

        @JavascriptInterface
        public void openNotification() {
            startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
        }

        @JavascriptInterface
        public void openMyOrder() {
            startActivity(new Intent(getApplicationContext(), MyOrderActivity.class));
        }

        @JavascriptInterface
        public void openOffer() {
            finish();
        }

        @JavascriptInterface
        public void setRefreshing(boolean refreshing) {
            mBinding.swipeRefreshLayout.setEnabled(refreshing);
        }
    }

    private class PaymentInterface {
        @JavascriptInterface
        public void success(String data) {
        }

        @JavascriptInterface
        public void error(String data) {
        }

    }

    public String getLatLong() {
        String location = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Latitude", "" + locationLatLong.latitude);
            jsonObject.put("Longitude", "" + locationLatLong.longitude);
            location = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public boolean callRunTimePermissions() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                permissionResult = true;
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                permissionResult = false;
            }
        });
        return permissionResult;
    }

    private final Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Utils.hideProgressDialog();
            try {
                String filename = Constant.PRODUCT_IMAGE_FOLDER;
                File sd = Environment.getExternalStorageDirectory();
                File dest = new File(sd, filename);
                if (!dest.exists()) {
                    dest.mkdirs();
                }
                FileOutputStream out = new FileOutputStream(dest + "/image.png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };


    private final BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(TradeActivity.this, "trade")
                    .setSmallIcon(R.drawable.direct_sign)
                    .setContentTitle("File Downloaded")
                    .setContentText("All Download completed");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(455, mBuilder.build());
        }
    };
}