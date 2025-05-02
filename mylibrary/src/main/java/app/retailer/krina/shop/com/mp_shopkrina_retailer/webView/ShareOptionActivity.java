package app.retailer.krina.shop.com.mp_shopkrina_retailer.webView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityShareOptionBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class ShareOptionActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityShareOptionBinding mBinding;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;
    private String filePath;
    private double totalBalanceDue;
    private CustomerRes customersContactModel;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityShareOptionBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        try {
            if (getIntent().getExtras() != null) {
                totalBalanceDue = getIntent().getExtras().getDouble("balance");
                customersContactModel = (CustomerRes) getIntent().getSerializableExtra("customerContectID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // init view
        initialization();

        mBinding.toolbarDream.back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.ll_mini_state_ment) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(ShareOptionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ShareOptionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else {
                        ActivityCompat.requestPermissions(ShareOptionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                } else {
                    callMiniStateMeant("Top10");
                }
            } else {
                callMiniStateMeant("Top10");
            }
        } else if (id == R.id.LL_ReminderonWhatsapp) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(ShareOptionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ShareOptionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else {
                        ActivityCompat.requestPermissions(ShareOptionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                } else {
                    paymentReminder();
                }
            } else {
                paymentReminder();
            }
        } else if (id == R.id.ll_month_statememt) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(ShareOptionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ShareOptionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else {
                        ActivityCompat.requestPermissions(ShareOptionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                } else {
                    callMiniStateMeant("Month");
                }
            } else {
                callMiniStateMeant("Month");
            }
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }


    private void initialization() {
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        utils = new Utils(this);
        mBinding.toolbarDream.title.setText(ShareOptionActivity.this.getResources().getString(R.string.share_option));
        mBinding.llMiniStateMent.setOnClickListener(this);
        mBinding.LLReminderonWhatsapp.setOnClickListener(this);
        mBinding.llMonthStatememt.setOnClickListener(this);

        if (totalBalanceDue < 0) {
            mBinding.LLReminderonWhatsapp.setVisibility(View.VISIBLE);
        } else {
            mBinding.LLReminderonWhatsapp.setVisibility(View.GONE);
        }
    }

    private void callMiniStateMeant(String Type) {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this);
                commonClassForAPI.GetHisabKitabInvoice(Getinvoice, SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID), customersContactModel.getId(), Type);
            }
        } else {
            Utils.setToast(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection));
        }
    }

    private void downloadFileFromUrl(String url, String FilePath) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED);
        }
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FilePath);

        downloadManager.enqueue(request);
    }

    private void shareToOneWhatsAppUser(Uri uri) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("application/pdf");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, uri);

        if (whatsappIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(getApplicationContext(), "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(whatsappIntent);
    }

    private void paymentReminder() {
        try {
            if (!customersContactModel.getName().equals("") && customersContactModel.getName() != null) {
                mBinding.shareLayout.initialsTextView.setText(Utils.wordFirstCap(customersContactModel.getName()).substring(0, 1));
            }
            mBinding.shareLayout.tvUserName.setText(customersContactModel.getName());
            mBinding.shareLayout.tvUserNumber.setText(customersContactModel.getMobile());
            if (totalBalanceDue < 0) {
                String StringAmt = String.valueOf(totalBalanceDue).replace("-", "");
                mBinding.shareLayout.tvLastTrsAmount.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(StringAmt)) +
                        " " + ShareOptionActivity.this.getResources().getString(R.string.due));
            } else {
                mBinding.shareLayout.tvLastTrsAmount.setText("₹ " + totalBalanceDue + " " + ShareOptionActivity.this.getResources().getString(R.string.advance));
            }

            Date d = new Date();
            CharSequence currentDate = DateFormat.format("MMM d, yyyy ", d.getTime());
            mBinding.shareLayout.tvDate.setText(currentDate);
            Bitmap bitmap = loadBitmapFromView(findViewById(R.id.ll_main_chat_view));
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "");
            sendIntent.setPackage("com.whatsapp");
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
            sendIntent.setType("image/url");
            startActivity(sendIntent);
            RetailerSDKApp.getInstance().updateAnalyticShare(getClass().getSimpleName(), "App Share On WhatsApp");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(600, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);
        return returnedBitmap;
    }


    private final BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {

            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filePath);
            Uri uri;
            if (Build.VERSION.SDK_INT < 24) {
                uri = Uri.fromFile(outputFile);
            } else {
                uri = Uri.parse(outputFile.getPath());
            }
            shareToOneWhatsAppUser(uri);
        }


    };

    private final DisposableObserver<String> Getinvoice = new DisposableObserver<String>() {

        @Override
        public void onNext(@NotNull String response) {
            Utils.hideProgressDialog();
            try {
                if (response != null) {
                    filePath = response;
                    String invoiceURl = EndPointPref.getInstance(RetailerSDKApp.getInstance()).getTradeEndpoint() + response;
                    downloadFileFromUrl(invoiceURl, response);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };
}
