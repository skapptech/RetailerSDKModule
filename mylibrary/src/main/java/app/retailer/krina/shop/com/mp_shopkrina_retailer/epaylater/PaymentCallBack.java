package app.retailer.krina.shop.com.mp_shopkrina_retailer.epaylater;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

final class PaymentCallBack {
    private Activity a;
    private Intent b;

    PaymentCallBack(Activity var1) {
        this.a = var1;
        this.b = new Intent();
    }

    @JavascriptInterface
    public void success() {
        PaymentResponse var1 = new PaymentResponse(200, "");
        this.b.putExtra("extra_payment_response", var1);
        this.a.setResult(-1, this.b);
        this.a.finish();
    }

    @JavascriptInterface
    public void failure() {
        PaymentResponse var1 = new PaymentResponse(400, "");
        this.b.putExtra("extra_payment_response", var1);
        this.a.setResult(-1, this.b);
        this.a.finish();
    }

    @JavascriptInterface
    public void success(String var1) {
        PaymentResponse var2 = new PaymentResponse(200, var1);
        this.b.putExtra("extra_payment_response", var2);
        this.a.setResult(-1, this.b);
        this.a.finish();
    }

    @JavascriptInterface
    public void failure(String var1) {
        PaymentResponse var2 = new PaymentResponse(400, var1);
        this.b.putExtra("extra_payment_response", var2);
        this.a.setResult(-1, this.b);
        this.a.finish();
    }
}