package app.retailer.krina.shop.com.mp_shopkrina_retailer.webView;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.text.DecimalFormat;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityCustomerBalnceBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerBalance;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class CustomerBalanceActivity extends AppCompatActivity {
    private ActivityCustomerBalnceBinding mBinding;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_customer_balnce);
        // init view
        initialization();
        mBinding.toolbarHisabKitab.back.setOnClickListener(v -> onBackPressed());

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
        mBinding.toolbarHisabKitab.title.setText(CustomerBalanceActivity.this.getResources().getString(R.string.customer_balance));
        callMyBalance();
    }


    private void callMyBalance() {
        if (utils.isNetworkAvailable()) {
                Utils.showProgressDialog(this);
                commonClassForAPI.GetMyBalanceHisab(Getcustomer);
        } else {
            Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
        }
    }


    DisposableObserver<CustomerBalance> Getcustomer = new DisposableObserver<CustomerBalance>() {

        @Override
        public void onNext(CustomerBalance customerBalance) {
            Utils.hideProgressDialog();
            try {
                if (customerBalance != null) {
                    if (customerBalance.Balance < 0) {
                        String StringAmt = String.valueOf(customerBalance.Balance).replace("-", "");
                        mBinding.tvTotalBalnce.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(StringAmt)) +
                                " " + CustomerBalanceActivity.this.getResources().getString(R.string.advance));
                        mBinding.tvTotalBalnce.setTextColor(CustomerBalanceActivity.this.getResources().getColor(R.color.green_50));
                    } else {
                        mBinding.tvTotalBalnce.setText("₹ " + new DecimalFormat("##.##").format(customerBalance.Balance) + " " + CustomerBalanceActivity.this.getResources().getString(R.string.due));
                        mBinding.tvTotalBalnce.setTextColor(CustomerBalanceActivity.this.getResources().getColor(R.color.red));
                    }

                    mBinding.tvAccpetPay.setText("₹ " + new DecimalFormat("##.##").format(customerBalance.DrAmount));
                    mBinding.tvGivePay.setText("₹ " + new DecimalFormat("##.##").format(customerBalance.CrAmount));
                    mBinding.tvPayment.setText(customerBalance.DrCount + " " + CustomerBalanceActivity.this.getResources().getString(R.string.payment));
                    mBinding.tvCredit.setText(customerBalance.CrCount + " " + CustomerBalanceActivity.this.getResources().getString(R.string.credit));
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
