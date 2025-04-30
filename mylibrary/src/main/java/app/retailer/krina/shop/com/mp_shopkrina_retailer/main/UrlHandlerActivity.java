package app.retailer.krina.shop.com.mp_shopkrina_retailer.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CreateContact;

public class UrlHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_url_handle);
        try {
            Uri uri = getIntent().getData();
            String url = String.valueOf(uri);
            String name = url.substring(url.lastIndexOf("/") + 1, url.length() - 10);
            String number = url.substring(url.length() - 10);
            System.out.println(name + number);

            new CreateContact(getApplicationContext(), name, number);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), SplashScreenActivity.class));
                finish();
            }, 900);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }
}