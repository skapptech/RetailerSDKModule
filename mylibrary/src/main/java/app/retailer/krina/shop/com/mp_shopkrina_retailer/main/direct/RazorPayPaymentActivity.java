package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;


import org.json.JSONObject;

import java.util.Map;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityRazorPayPaymentBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;

public class RazorPayPaymentActivity extends AppCompatActivity {
    ActivityRazorPayPaymentBinding mBinding;
    RazorPayPaymentActivity activity;
  //  Razorpay razorpay = null;
    String intentKey;
    String intentData;
    String intentOrderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRazorPayPaymentBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        activity = this;
        initViews();
    }

    private void initViews() {
        intentKey = getIntent().getStringExtra("key");
        intentData = getIntent().getStringExtra("data");
        try {
            System.out.println(intentData);
            JSONObject job = new JSONObject(intentData);
            intentOrderId = job.getString("order_id");
           /* razorpay = new Razorpay(activity, intentKey);
            razorpay.setWebView(mBinding.razorWebview);
            razorpay.validateFields(job, new ValidationListener() {
                @Override
                public void onValidationSuccess() {
                    try {
                        razorpay.submit(job, new PaymentResultWithDataListener() {
                            @Override
                            public void onPaymentSuccess(String razorpayPaymentId, PaymentData paymentData) {
                                String orderData = "Status=Success&OrderId="+intentOrderId+"&paymentId="+paymentData.getPaymentId()
                                        +"&signature="+paymentData.getSignature();
                                Intent intent=new Intent();
                                intent.putExtra("jsonData",orderData);
                                setResult(1002,intent);
                                finish();
                            }

                            @Override
                            public void onPaymentError(int i, String s, PaymentData paymentData) {
                                String orderData = "Status=Failed&OrderId="+intentOrderId;
                                Intent intent=new Intent();
                                intent.putExtra("jsonData",orderData);
                                setResult(1002,intent);
                                finish();
                            }
                        });
                    } catch (Exception e) {
                        Log.e("com.example", "Exception: ", e);
                    }
                }

                @Override
                public void onValidationError(Map<String, String> error) {
                    String orderData = "Status=Failed&OrderId="+intentOrderId;
                    Intent intent=new Intent();
                    intent.putExtra("jsonData",orderData);
                    setResult(1002,intent);
                    finish();
                    Log.d("com.example", "Validation failed: " + error.get("field") + " " + error.get("description"));
                    Toast.makeText(activity, "Validation: " + error.get("field") + " " + error.get("description"), Toast.LENGTH_SHORT).show();
                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        String orderData = "Status=Failed&OrderId="+intentOrderId;
        Intent intent=new Intent();
        intent.putExtra("jsonData",orderData);
        setResult(1002,intent);
        finish();
        super.onBackPressed();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (razorpay != null) {
            razorpay.onActivityResult(requestCode, resultCode, data);
        }*/
    }

}