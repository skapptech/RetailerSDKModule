package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentWebViewBinding;

public class RazorPayWebviewActivity extends AppCompatActivity {
    private FragmentWebViewBinding mBinding;
    String intentUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = FragmentWebViewBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        intentUrl = getIntent().getStringExtra("Url");

        WebSettings webSettings = mBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
//        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mBinding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                return true;
            }
        });
        mBinding.webView.loadUrl(intentUrl);
    }


    @Override
    public void onBackPressed() {
        if (mBinding.webView.canGoBack()) {
            mBinding.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}