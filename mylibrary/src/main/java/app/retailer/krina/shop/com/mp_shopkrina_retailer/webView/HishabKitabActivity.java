package app.retailer.krina.shop.com.mp_shopkrina_retailer.webView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityHisabKitabBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.HisabKistbListAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerExistModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomersContactModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HKContactModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.UpdateContactModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class HishabKitabActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityHisabKitabBinding mBinding;
    private HisabKistbListAdapter hisabKistbListAdapter;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;
    private ArrayList<CustomerRes> customerResArrayList;
    ArrayList<CustomersContactModel> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(HishabKitabActivity.this, R.layout.activity_hisab_kitab);
        // init view
        initialization();

        mBinding.toolbarHisabKitab.back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        customerExistCall();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hisab_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_collection:
                startActivity(new Intent(HishabKitabActivity.this, HisabCollectionActivity.class));
                Utils.leftTransaction(HishabKitabActivity.this);
                break;
            case R.id.menu_customer_balnce:
                startActivity(new Intent(HishabKitabActivity.this, CustomerBalanceActivity.class));
                Utils.leftTransaction(HishabKitabActivity.this);
                break;
          /*  case R.id.menu_share_wudu:
               // shareWudu();
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_customer_bt:
            case R.id.bt_customer:
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", customerResArrayList);
                startActivity(new Intent(getApplicationContext(), CustomerListActivity.class).putExtras(bundle));
                Utils.leftTransaction(this);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.fadeTransaction(this);
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
        contactList = new ArrayList<>();

        setSupportActionBar(mBinding.toolbarHisabKitab.arrowToolbar);

        mBinding.toolbarHisabKitab.title.setText(getString(R.string.hishab_kitab));
        mBinding.rvCutomerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mBinding.llAddCustomerBt.setOnClickListener(this);
        mBinding.btCustomer.setOnClickListener(this);

        mBinding.swiperefresh.setOnRefreshListener(() -> {
            callAPI();
            mBinding.swiperefresh.setRefreshing(false);
        });

        mBinding.etSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchContactList(s.toString());
            }
        });
    }

    private void callAPI() {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this);
                commonClassForAPI.GetCustomerContact(getCustomer,
                        SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID));
            }
        } else {
            Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
        }
    }

    private void callCheckMatchedContact(UpdateContactModel updateContactModel) {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this);
                commonClassForAPI.matchedContactApi(matchedContactObserver, updateContactModel);
            }
        } else {
            Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
        }
    }

    private void searchContactList(String s) {

        if (hisabKistbListAdapter != null) {
            hisabKistbListAdapter.filter(s);
        }
        if (hisabKistbListAdapter != null && hisabKistbListAdapter.getItemCount() == 0) {
            mBinding.tvNoResult.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvNoResult.setVisibility(View.GONE);
        }
    }

    private void customerExistCall() {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                String fcmToken = EndPointPref.getInstance(getApplicationContext()).getFcmToken(EndPointPref.FCM_TOKEN);
                Utils.showProgressDialog(this);
                commonClassForAPI.customerExists(customerExist,
                        SharePrefs.getInstance(this).getString(SharePrefs.MOBILE_NUMBER), fcmToken);
            }
        } else {
            Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
        }
    }


    DisposableObserver<JsonArray> getCustomer = new DisposableObserver<JsonArray>() {

        @Override
        public void onNext(JsonArray response) {
            Utils.hideProgressDialog();
            try {
                if (response != null && response.size() > 0) {
                    Type listType = new TypeToken<ArrayList<CustomerRes>>() {
                    }.getType();
                    customerResArrayList = new Gson().fromJson(response.toString(), listType);

                    Gson gson = new GsonBuilder().create();
                    JsonArray customerBalance = gson.toJsonTree(customerResArrayList).getAsJsonArray();
                    SharePrefs.getInstance(HishabKitabActivity.this).putString(SharePrefs.CUSTOMER_BALANCE, String.valueOf(customerBalance));

                    hisabKistbListAdapter = new HisabKistbListAdapter(HishabKitabActivity.this, customerResArrayList);
                    mBinding.rvCutomerList.setAdapter(hisabKistbListAdapter);

                    //SharedPref
                    String savedContactList = SharePrefs.getInstance(HishabKitabActivity.this).getString(SharePrefs.CONTACT_LIST);
                    Type listTypeq = new TypeToken<ArrayList<CustomersContactModel>>() {
                    }.getType();
                    contactList = new Gson().fromJson(savedContactList, listTypeq);
                    HKContactModel hkContactModel = new HKContactModel();
                    ArrayList<HKContactModel> arrayList = new ArrayList<>();
                    for (int i = 0; i < contactList.size(); i++) {
                        String Number = contactList.get(i).getCustomerNumber();
                        String Name = contactList.get(i).getCustomerName();
                        for (int j = 0; j < customerResArrayList.size(); j++) {
                            String CustomerNumber = customerResArrayList.get(j).getMobile();
                            String CustomeName = customerResArrayList.get(j).getName();
                            if (Number.equals(CustomerNumber)) {
                                if (!Name.equals(CustomeName)) {
                                    hkContactModel.setId(customerResArrayList.get(j).getId());
                                    hkContactModel.setMobile(customerResArrayList.get(j).getMobile());
                                    hkContactModel.setName(Name);
                                    arrayList.add(hkContactModel);
                                }
                            }
                        }
                    }

                    if (arrayList.size() > 0) {
                        UpdateContactModel updateContactModel = new UpdateContactModel(SharePrefs.getInstance(HishabKitabActivity.this).getString(SharePrefs.HISAB_KITAB_ID), arrayList);
                        callCheckMatchedContact(updateContactModel);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };

    DisposableObserver<JsonArray> matchedContactObserver = new DisposableObserver<JsonArray>() {

        @Override
        public void onNext(JsonArray response) {
            Utils.hideProgressDialog();
            try {
                Type listType = new TypeToken<ArrayList<CustomerRes>>() {
                }.getType();
                customerResArrayList = new Gson().fromJson(response.toString(), listType);

                Gson gson = new GsonBuilder().create();
                JsonArray customerBalance = gson.toJsonTree(customerResArrayList).getAsJsonArray();
                SharePrefs.getInstance(HishabKitabActivity.this).putString(SharePrefs.CUSTOMER_BALANCE, String.valueOf(customerBalance));

                hisabKistbListAdapter = new HisabKistbListAdapter(HishabKitabActivity.this, customerResArrayList);
                mBinding.rvCutomerList.setAdapter(hisabKistbListAdapter);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };

    DisposableObserver<JsonObject> customerExist = new DisposableObserver<JsonObject>() {

        @Override
        public void onNext(JsonObject response) {
            Utils.hideProgressDialog();
            try {
                JSONObject obj = new JSONObject(response.toString());
                CustomerExistModel customerExistModel = new Gson().fromJson(obj.toString(), CustomerExistModel.class);
                SharePrefs.getInstance(HishabKitabActivity.this).putString(SharePrefs.HISAB_KITAB_ID, customerExistModel.getId());
                callAPI();
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