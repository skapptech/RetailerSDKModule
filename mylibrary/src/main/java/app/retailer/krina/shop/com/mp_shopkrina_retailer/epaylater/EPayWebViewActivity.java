package app.retailer.krina.shop.com.mp_shopkrina_retailer.epaylater;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;

public class EPayWebViewActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // not to take screenshot - devendra
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_epay_web_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progress_bar);
        webView = findViewById(R.id.webview_epaylater);
        setUpWebView(webView);

        String var3 = this.getIntent().getStringExtra("extra_mcode");
        String var4 = this.getIntent().getStringExtra("extra_checksum");
        String var5 = this.getIntent().getStringExtra("extra_encdata");
        String var6 = this.getIntent().getStringExtra("extra_payment_url");
        /*System.out.println("mcode:"+var3);
        System.out.println("checksum:"+var4);
        System.out.println("encdata:"+var5);
        System.out.println("payment_url:"+var6);*/
        if (var4 != null && !var4.isEmpty()) {
            if (var3 != null && !var3.isEmpty()) {
                if (var5 != null && !var5.isEmpty() && var5.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
                    if (var6 != null && !var6.isEmpty() && URLUtil.isValidUrl(var6)) {
                        try {
                            byte[] var7 = a(var3, var4, var5);
                            webView.postUrl(var6, var7);
                        } catch (UnsupportedEncodingException var8) {
                            a(800, var8.getMessage(), 0);
                        }
                    } else {
                        a(800, "paymenturl is invalid", 0);
                    }
                } else {
                    a(800, "encdata is invalid", 0);
                }
            } else {
                a(800, "mcode is invalid", 0);
            }
        } else {
            a(800, "checksum is invalid", 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void setUpWebView(WebView var1) {
        var1.setWebChromeClient(new EplChromeClient());
        var1.getSettings().setJavaScriptEnabled(true);
        var1.getSettings().setSupportMultipleWindows(true);
        var1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        var1.getSettings().setAllowContentAccess(true);
        var1.getSettings().setAllowFileAccess(true);
        var1.getSettings().setDatabaseEnabled(true);
        var1.getSettings().setDomStorageEnabled(true);
//        var1.getSettings().setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            var1.getSettings().setMixedContentMode(0);
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        var1.addJavascriptInterface(new PaymentCallBack(this), "EpayLaterFormActivity");
        var1.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView webView, String url, Bitmap var3) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(webView, url, var3);
            }
            public void onPageFinished(WebView webView, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                super.onPageFinished(webView, url);
                Log.e("Listener", "Finish" + url);
                if (url.contains("Transaction/paymentcallback")) {
                    webView.evaluateJavascript("(function(){return window.document.body.outerHTML})();",
                            html -> {
                                Log.w("HTML", html);
                                Intent intent = new Intent();
                                intent.putExtra("extra_payment_response", "" + convertStandardJSONString(html));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                                //a(200, "" + data, Activity.RESULT_OK);
                            });
                }
            }
        });
    }

    private final class EplChromeClient extends WebChromeClient {
        private EplChromeClient() {
        }

        public void onProgressChanged(WebView var1, int var2) {
            super.onProgressChanged(var1, var2);
            progressBar.setProgress(var2);
        }

        public boolean onCreateWindow(WebView var1, boolean var2, boolean var3, Message var4) {
            WebView.HitTestResult var5 = var1.getHitTestResult();
            String var6 = var5.getExtra();
            Context var7 = var1.getContext();
            Intent var8 = new Intent("android.intent.action.VIEW", Uri.parse(var6));
            var7.startActivity(var8);
            return false;
        }
    }

    private void a(int var1, String var2, int var3) {
        Intent var4 = new Intent();
       // Activity.RESULT_CANCELED,
        PaymentResponse var5 = new PaymentResponse(var1, var2);
        var4.putExtra("extra_payment_response", var5);
        this.setResult(var3, var4);
        this.finish();
    }

    private byte[] a(String var1, String var2, String var3) throws UnsupportedEncodingException {
        String var4 = "mcode=" + URLEncoder.encode(var1, "UTF-8") + "&checksum=" + URLEncoder.encode(var2, "UTF-8") + "&encdata=" + URLEncoder.encode(var3, "UTF-8");
        return var4.getBytes();
    }

    private void showDialog() {
        AlertDialog.Builder var1 = new AlertDialog.Builder(this);
        var1.setTitle("Exit Payment");
        var1.setMessage("Do you want to exit payment?");
        var1.setPositiveButton("Exit", (var11, var2) -> a(600, "Payment cancelled!", 0));
        var1.setNegativeButton("Cancel", null);
        var1.create().show();
    }

    public String convertStandardJSONString(String data_json) {
        data_json = data_json.replace("\\", "");
        data_json = data_json.replace("u003Cbody>", "");
        data_json = data_json.replace("u003C/body>", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        Log.i("data_json", data_json);
        return data_json;
    }
}