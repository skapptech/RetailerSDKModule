package app.retailer.krina.shop.com.mp_shopkrina_retailer.webView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.ByteArrayOutputStream;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityTransactionDetailBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.HKImageAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HisabDetailModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;

public class TransactionDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityTransactionDetailBinding mBinding;

    private CustomerRes customersContactModel;
    private HisabDetailModel hisabDetailModel;
    private HKImageAdapter hkImageAdapter;
    private String IntentgiveAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityTransactionDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            customersContactModel = (CustomerRes) getIntent().getSerializableExtra("list");
            hisabDetailModel = (HisabDetailModel) getIntent().getSerializableExtra("msgList");
            IntentgiveAccept = getIntent().getStringExtra("giveAccept");
        }
        initialization();

        mBinding.btWhatsapp.setOnClickListener(this);
    }

    private void initialization() {
        mBinding.toolbarDream.tvUserName.setText(customersContactModel.getName());
        mBinding.toolbarDream.tvUserNumber.setText(customersContactModel.getMobile());
        mBinding.toolbarDream.back.setOnClickListener(v -> {
            onBackPressed();
        });

        if (!customersContactModel.getName().equals("") && customersContactModel.getName() != null) {
            mBinding.initialsTextView.setText(Utils.wordFirstCap(customersContactModel.getName()).substring(0, 1));
        }

        mBinding.tvUserName.setText(customersContactModel.getName());
        mBinding.tvUserNumber.setText(customersContactModel.getMobile());

        if (IntentgiveAccept.equalsIgnoreCase("Accept")) {
            mBinding.tvLastTrsAmount.setText(TransactionDetailActivity.this.getResources().getString(R.string.payment_amount) + "\n₹" + hisabDetailModel.getAmount());
            mBinding.tvLastTrsAmount.setTextColor(getResources().getColor(R.color.green_50));
        } else {
            mBinding.tvLastTrsAmount.setText(TransactionDetailActivity.this.getResources().getString(R.string.credit_amount) + "\n₹" + hisabDetailModel.getAmount());
            mBinding.tvLastTrsAmount.setTextColor(getResources().getColor(R.color.red));
        }

        mBinding.tvDate.setText(Utils.getChangeDateFormatInProfile(hisabDetailModel.getCreatedDate()));
        if (hisabDetailModel.getHisabNotesList().get(0).getNotes().equals("")) {
            mBinding.tvNote.setVisibility(View.GONE);
        } else {
            mBinding.tvNote.setVisibility(View.VISIBLE);
            mBinding.tvNote.setText(hisabDetailModel.getHisabNotesList().get(0).getNotes());
        }

        mBinding.rvImageList.setLayoutManager(new LinearLayoutManager(TransactionDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mBinding.rvImageList.setHasFixedSize(true);

        hkImageAdapter = new HKImageAdapter(TransactionDetailActivity.this, hisabDetailModel.HisabKitabImagesList);
        mBinding.rvImageList.setAdapter(hkImageAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_whatsapp) {
                shareWhatsapp();
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


    private void shareWhatsapp() {
        try {
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
            RetailerSDKApp.getInstance().updateAnalyticShare(getClass().getSimpleName(), "Transaction Share On WhatsApp");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Whatsapp not installed.", Toast.LENGTH_SHORT).show();
        }

    }

    public Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(650, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);
        return returnedBitmap;
    }
}
