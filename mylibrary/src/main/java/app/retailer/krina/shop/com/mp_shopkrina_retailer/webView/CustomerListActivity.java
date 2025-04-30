package app.retailer.krina.shop.com.mp_shopkrina_retailer.webView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityCutomerListBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.CustomerListClicked;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.CustomerListAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddCustomerModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomersContactModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class CustomerListActivity extends AppCompatActivity implements View.OnClickListener, CustomerListClicked {
    private ActivityCutomerListBinding mBinding;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public ArrayList<CustomerRes> contactModelArrayList;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;
    private CustomerListAdapter customerListAdapter;
    String CustomerType = "";
    private ArrayList<CustomersContactModel> contactList;
    ArrayList<CustomersContactModel> contactListNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(CustomerListActivity.this, R.layout.activity_cutomer_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // init view

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();

            contactModelArrayList = (ArrayList<CustomerRes>) bundle.getSerializable("list");
        }
        initialization();
        permission();


        mBinding.toolbarHisabKitab.back.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_import_contect:
                askForContactPermission();
                break;
        }

    }


    private void permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                mBinding.rlImportContect.setVisibility(View.VISIBLE);
                mBinding.rvCutomerList.setVisibility(View.GONE);

            } else {
                checkConnect();
                mBinding.rlImportContect.setVisibility(View.GONE);
                mBinding.rvCutomerList.setVisibility(View.VISIBLE);
            }
        } else {
            checkConnect();
        }
    }

    private void initialization() {

        contactList = new ArrayList<>();
        contactListNew = new ArrayList<>();
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        utils = new Utils(this);
        mBinding.toolbarHisabKitab.title.setText(getString(R.string.customer_list));
        mBinding.rlImportContect.setOnClickListener(this);
        mBinding.rvCutomerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mBinding.etConSearchView.addTextChangedListener(new TextWatcher() {
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

    private void searchContactList(String toString) {

        if (customerListAdapter != null) {
            customerListAdapter.filter(toString);
        }
        if (customerListAdapter != null && customerListAdapter.getItemCount() == 0) {
            mBinding.tvNoResult.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvNoResult.setVisibility(View.GONE);
        }
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                checkConnect();
                mBinding.rlImportContect.setVisibility(View.GONE);
                mBinding.rvCutomerList.setVisibility(View.VISIBLE);
            }
        } else {
            checkConnect();
        }
    }

    private void checkConnect() {
        // String savedContactList = SharePrefs.getInstance(CustomerListActivity.this).getString(SharePrefs.CONTACT_LIST);
       /* if (!savedContactList.equals("")&& savedContactList!=null){
            getContactFromLocal();
        }else {*/
        new FetchContact().execute();
        //}

    }

    public void getContactFromLocal() {
        ArrayList<CustomersContactModel> contactListNew;

        String savedContactList = SharePrefs.getInstance(CustomerListActivity.this).getString(SharePrefs.CONTACT_LIST);
        Type listTypeq = new TypeToken<ArrayList<CustomersContactModel>>() {
        }.getType();
        contactList = new Gson().fromJson(savedContactList, listTypeq);

        try {
            contactListNew = contactList;
            for (int i = 0; i < contactModelArrayList.size(); i++) {
                String TempNumber = contactModelArrayList.get(i).getMobile();
                for (int j = 0; j < contactListNew.size(); j++) {
                    if (TempNumber.equalsIgnoreCase(contactListNew.get(j).getCustomerNumber())) {
                        contactList.remove(j);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        customerListAdapter = new CustomerListAdapter(CustomerListActivity.this, contactList, CustomerListActivity.this);
        mBinding.rvCutomerList.setAdapter(customerListAdapter);
    }


    @Override
    public void getPosition(int position, CustomersContactModel customersContactModel) {
        Dialog(customersContactModel);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                mBinding.rlImportContect.setVisibility(View.GONE);
                mBinding.rvCutomerList.setVisibility(View.VISIBLE);
                checkConnect();

            }
        }
    }


    public void Dialog(CustomersContactModel customersContactModel) {
        String[] Type = {"Customer", "Supplier", "Other"};
        CustomerType = "";
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_add_contact);
        TextView tv_DName = dialog.findViewById(R.id.tv_DName);
        TextView tv_DNumber = dialog.findViewById(R.id.tv_DNumber);
        Spinner spinner = dialog.findViewById(R.id.spinner);
        TextView tv_DAddContact = dialog.findViewById(R.id.tv_DAddContact);
        dialog.setCancelable(true);
        spinner.setAdapter(aa);

        tv_DName.setText(customersContactModel.getCustomerName());
        tv_DNumber.setText(customersContactModel.getCustomerNumber());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomerType = Type[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_DAddContact.setOnClickListener(v -> {
            AddCustomerModel addCustomerModel = new AddCustomerModel(SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID),
                    customersContactModel.getCustomerNumber(), customersContactModel.getCustomerName(), CustomerType);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    commonClassForAPI.AddContact(addCustomer, addCustomerModel);
                }
            } else {
                Utils.setToast(this, MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
            }

            dialog.dismiss();
        });
        dialog.show();
    }


    @SuppressLint("StaticFieldLeak")
    private class FetchContact extends AsyncTask<String, Void, String> {
        private Cursor cursor;
        private ContentResolver contentResolver;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBinding.progressBarContact.setVisibility(View.VISIBLE);
            contentResolver = CustomerListActivity.this.getContentResolver();

            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER;
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
            cursor = contentResolver.query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.Contacts._ID}, selection, null, sortOrder);
        }

        @Override
        protected String doInBackground(String... strings) {
            String contactName, phoneNumber;
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    String number = phoneNumber.trim();
                    number = number.replaceAll("[^0-9]", "");
                    if (number == null || number.trim().length() == 0 || number.trim().length() < 10) {
                    } else {
                        number = number.substring(number.length() - 10);
                        contactList.add(new CustomersContactModel(contactName, number));
                    }

                }
            }
            return null;
        }

        private ArrayList<CustomersContactModel> clearListFromDuplicateFirstName(List<CustomersContactModel> list1) {
            Map<String, CustomersContactModel> cleanMap = new LinkedHashMap<>();
            for (int i = 0; i < list1.size(); i++) {
                cleanMap.put(list1.get(i).getCustomerNumber(), list1.get(i));
            }
            return new ArrayList<>(cleanMap.values());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mBinding.progressBarContact.setVisibility(View.GONE);
            try {

                contactList = clearListFromDuplicateFirstName(contactList);
                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(contactList).getAsJsonArray();
                SharePrefs.getInstance(CustomerListActivity.this).putString(SharePrefs.CONTACT_LIST, String.valueOf(myCustomArray));

                contactListNew = contactList;
                if (contactModelArrayList != null) {
                    for (int i = 0; i < contactModelArrayList.size(); i++) {
                        String TempNumber = contactModelArrayList.get(i).getMobile();
                        for (int j = 0; j < contactListNew.size(); j++) {
                            if (TempNumber.equalsIgnoreCase(contactListNew.get(j).getCustomerNumber())) {
                                contactList.remove(j);
                            }
                        }
                    }
                }

                customerListAdapter = new CustomerListAdapter(CustomerListActivity.this, contactList, CustomerListActivity.this);
                mBinding.rvCutomerList.setAdapter(customerListAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    DisposableObserver<JsonObject> addCustomer = new DisposableObserver<JsonObject>() {

        @Override
        public void onNext(JsonObject response) {
            Utils.hideProgressDialog();
            if (response != null) {
                startActivity(new Intent(getApplicationContext(), HishabKitabActivity.class));
                CustomerRes customerRes = new GsonBuilder().create().fromJson(response.toString(), CustomerRes.class);
                //startActivity(new Intent(CustomerListActivity.this, HisabDetailActivity.class).putExtra("list", customerRes).putExtra("id", customerRes.getId()));
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