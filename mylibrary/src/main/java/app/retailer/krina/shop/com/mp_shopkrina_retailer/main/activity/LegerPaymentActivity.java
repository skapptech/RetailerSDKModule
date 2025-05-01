package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivitySupplierPaymentBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.SupplierPaymentModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.LadgerEntryListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.SupplierDocModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.SupplierPaymentResponce;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class LegerPaymentActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ActivitySupplierPaymentBinding mBinding;
    private String selectedYear = "";
    private final SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private final String myFormat = "yyyy-MM-dd HH:mm:ss.sss"; //2017-01-04 21:29:32.000
    private TextView StartDate, EndDate;
    private Long StartEpochTime, EndEpochTime;
    private String details;
    private String startDate, BaseStartDate = "", BaseEndDate = "";
    private String endDate, endDatestr;
    private final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
    private int customerId;
    private long differenceDates = 0;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;
    private LinearLayout llLedgerData;
    private LinearLayout llCommingSoon;
    private final ArrayList<Long> list = new ArrayList<>();
    private long referenceId;
    private DownloadManager downloadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_supplier_payment);

        intView();
        if (!SharePrefs.getInstance(getApplicationContext()).getBoolean(SharePrefs.IS_SHOW_LEDGER)) {
            llCommingSoon.setVisibility(View.VISIBLE);
            llLedgerData.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.menuButton) {
            onBackPressed();
        } else if (id == R.id.startImage) {
            StartDate();
        }else if (id == R.id.endImage) {
            EndDate();
        }else if (id == R.id.get_data) {
            if (details.equalsIgnoreCase("Pending Payment")) {
                if (utils.isNetworkAvailable()) {
                    if (commonClassForAPI != null) {
                        Utils.showProgressDialog(this);
                        commonClassForAPI.CustomerPendingPayment(pedingobj, customerId);
                    }
                } else {
                    Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
                }
            } else {
                if (CheckDates(startDate, endDate)) {
                    if (details.equalsIgnoreCase("Please select Option")) {
                        Toast.makeText(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.please_select_ledger_type), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (utils.isNetworkAvailable()) {
                            SupplierPaymentModel supplierPaymentModel = new SupplierPaymentModel(customerId, BaseStartDate, BaseEndDate, 1, true, details);
                            if (commonClassForAPI != null) {
                                Utils.showProgressDialog(this);
                                commonClassForAPI.CustomerLedgerForRetailerApp(customerLedger, supplierPaymentModel);
                            }
                        } else {
                            Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
                        }
                    }
                } else {
                    Utils.setToast(getApplicationContext(), "please select valid date");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.fadeTransaction(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            details = "Please select Option";
        } else if (i == 1) {
            details = "DR";
        } else if (i == 2) {
            details = "SR";
        } else if (i == 3) {
            details = "Pending Payment";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    private void intView() {
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        utils = new Utils(this);

        mBinding.toolbarMyLedger.title.setText(MyApplication.getInstance().dbHelper.getString(R.string.my_ledger));
        mBinding.tvComingSoonL.setText(MyApplication.getInstance().dbHelper.getString(R.string.coming_soon));
        mBinding.tvCheckLater.setText(MyApplication.getInstance().dbHelper.getString(R.string.please_check_back_later));
        mBinding.tvLedgerNote.setText(MyApplication.getInstance().dbHelper.getString(R.string.ledger_note));
        mBinding.tvFinYr.setText(MyApplication.getInstance().dbHelper.getString(R.string.financial_year));
        mBinding.tvFromL.setText(MyApplication.getInstance().dbHelper.getString(R.string.From));
        mBinding.tvToL.setText(MyApplication.getInstance().dbHelper.getString(R.string.To));
        mBinding.tvLedgerType.setText(MyApplication.getInstance().dbHelper.getString(R.string.ledger_type));
        mBinding.getData.setText(MyApplication.getInstance().dbHelper.getString(R.string.hint_search));

        mBinding.toolbarMyLedger.back.setOnClickListener(v -> onBackPressed());

        String[] detailsList = new String[]{getString(R.string.select_option), getString(R.string.details), getString(R.string.ledger_summary), getString(R.string.pending_payment)};
        StartDate = mBinding.startDate;
        EndDate = mBinding.endDate;
        llCommingSoon = mBinding.llComingSoon;
        llLedgerData = mBinding.llLedgerLayer;

        ImageView end = mBinding.endImage;
        mBinding.getData.setOnClickListener(this);
        mBinding.startImage.setOnClickListener(this);
        end.setOnClickListener(this);
        Date date = new Date();
        endDate = sdf.format(date);
        EndEpochTime = date.getTime();
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        startDate = sdf.format(now.getTime());
        StartEpochTime = now.getTimeInMillis();
        customerId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID);

        mBinding.spinner.setOnItemSelectedListener(this);

        ArrayList<String> years = new ArrayList<>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add(getString(R.string.Please_Select_year));
        for (int i = 2018; i <= thisYear; i++) {
            years.add((i) + " - " + (i + 1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerYear.setAdapter(adapter);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, detailsList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinner.setAdapter(aa);
        mBinding.spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedYear = mBinding.spinnerYear.getSelectedItem().toString();
                StartDate.setText("");
                EndDate.setText("");
                startDate = "";
                endDate = "";
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        isStoragePermissionGranted();
    }

    private void StartDate() {
        EndDate.setText("");
        startDate = "";
        endDate = "";
        if (selectedYear.equalsIgnoreCase(getString(R.string.Please_Select_year))) {
            Toast.makeText(this, MyApplication.getInstance().dbHelper.getString(R.string.please_select_year),
                    Toast.LENGTH_SHORT).show();
        } else {
            String[] separated = selectedYear.split("-");
            String strYear = separated[0].trim();
            String endYear = separated[1].trim();
            try {
                createDialogforstartDate(Integer.parseInt(strYear), 3, 1, Integer.parseInt(endYear), true).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void EndDate() {
        if (selectedYear.equals(getString(R.string.Please_Select_year))) {
            Toast.makeText(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.please_select_year), Toast.LENGTH_SHORT).show();
        } else if (StartDate.getText().toString().trim().length() == 0) {

            Toast.makeText(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.please_select_start_date), Toast.LENGTH_SHORT).show();
        } else {
            String[] separated = selectedYear.split("-");
            String strYear = separated[0].trim();
            String endYear = separated[1].trim();
            String[] separatedDate = StartDate.getText().toString().trim().split("/");
            String startMonth = separatedDate[0].trim();
            String startDay = separatedDate[1].trim();
            String startYear = separatedDate[2].trim();

            createDialogforstartDate(Integer.parseInt(startYear),
                    Integer.parseInt(startMonth) - 1, Integer.parseInt(startDay),
                    Integer.parseInt(endYear), false).show();
        }
    }

    public boolean CheckDates(String d1, String d2) {
        boolean b = false;
        try {
            //If start date is after the end date
            if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                b = true;//If start date is before end date
            } else b = dfDate.parse(d1).equals(dfDate.parse(d2));//If two dates are equal
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private void callLimitpopup() {
        final View mView = getLayoutInflater().inflate(R.layout.for_pdf_down, null);
        Dialog customDialog = new Dialog(this, R.style.CustomDialog);
        customDialog.setContentView(mView);
        customDialog.setCancelable(false);
        TextView okBtn = mView.findViewById(R.id.ok_btn);
        TextView cancelBtn = mView.findViewById(R.id.cancel_btn);
        TextView title = mView.findViewById(R.id.pd_title);
        okBtn.setText(MyApplication.getInstance().dbHelper.getString(R.string.pdf));
        title.setText(MyApplication.getInstance().dbHelper.getString(R.string.more_than_days));
        cancelBtn.setText(MyApplication.getInstance().dbHelper.getString(R.string.cancel));
        okBtn.setOnClickListener(v -> {
            if (isStoragePermissionGranted()) {
                convertPDF();
            }
            customDialog.dismiss();
        });
        cancelBtn.setOnClickListener(v -> {
            customDialog.dismiss();
        });
        customDialog.show();
    }

    private void updateLabel(int year, String monthOfYear, String dayOfMonth) {
        startDate = monthOfYear + "/" + dayOfMonth + "/" + year;
        StartDate.setText(startDate);
        StartEpochTime = milliseconds(startDate);
    }

    private void updateEndLabel(String dayOfMonth, String monthOfYear, int year) {
        endDatestr = dayOfMonth + "/" + monthOfYear + "/" + year;
        endDate = monthOfYear + "/" + dayOfMonth + "/" + year;
        EndDate.setText(endDate);
        EndEpochTime = milliseconds(endDate);
    }

    private long milliseconds(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        try {
            Date mDate = sdf.parse(date);
            return mDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    private void convertPDF() {
        if (StartEpochTime <= EndEpochTime) {
            if (details.equalsIgnoreCase("Please select Option")) {
                Toast.makeText(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.please_select_any_option), Toast.LENGTH_SHORT).show();
            } else {
                if (utils.isNetworkAvailable()) {
                    SupplierPaymentModel supplierPaymentModel = new SupplierPaymentModel(customerId, startDate, endDate, 1, false, details);
                    if (commonClassForAPI != null) {
                        Utils.showProgressDialog(this);
                        commonClassForAPI.CustomerLedgerPDF(customerLedgerPDF, supplierPaymentModel);
                    }
                } else {
                    Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
                }
            }
        } else {
            Utils.setToast(getApplicationContext(), "please select valid date");
        }
    }


    private DatePickerDialog createDialogforstartDate(int startYear, int startmonth, int startday, int endYear, boolean isStartYear) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(startYear, startmonth, startday);
        int sDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        int sMonth = startCalendar.get(Calendar.MONTH);
        int sYear = startCalendar.get(Calendar.YEAR);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endYear, 2, 31);

        DatePickerDialog datePickerDialog1 = new DatePickerDialog(this,
                (view, year1, month, dayOfMonth) -> {
                    month = month + 1;
                    String monthString = String.valueOf(month);
                    if (monthString.length() == 1) {
                        monthString = "0" + monthString;
                    }

                    String day = String.valueOf(dayOfMonth);
                    if (day.length() == 1) {
                        day = "0" + day;
                    }

                    if (isStartYear) {
                        BaseStartDate = monthString + "/" + day + "/" + year1;
                        Log.e("START_DATE", dayOfMonth + "-" + month + "-" + year1);
                        updateLabel(year1, monthString, day);
                    } else {
                        BaseEndDate = monthString + "/" + day + "/" + year1;
                        Log.e("endDATEEEE", dayOfMonth + "-" + month + "-" + year1);
                        updateEndLabel(day, monthString, year1);
                    }
                }, sYear, sMonth, sDay);
        datePickerDialog1.getDatePicker().setMinDate(startCalendar.getTimeInMillis() - 1000);
        datePickerDialog1.getDatePicker().setMaxDate(endCalendar.getTimeInMillis());
        datePickerDialog1.show();
        return datePickerDialog1;
    }

    private final BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            list.remove(referenceId);
            if (list.isEmpty()) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(LegerPaymentActivity.this)
                                .setSmallIcon(R.drawable.logo_sk)
                                .setContentTitle("GadgetSaint")
                                .setContentText("All Download completed");
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());
            }
        }
    };

    private final DisposableObserver<JsonObject> customerLedgerPDF = new DisposableObserver<JsonObject>() {

        @Override
        public void onNext(@NotNull JsonObject response) {
            Utils.hideProgressDialog();
            try {
                SupplierDocModel supplierDocModel = new Gson().fromJson(response.toString(), SupplierDocModel.class);
                if (supplierDocModel.isStatus()) {
                    if (supplierDocModel.isURL() != null) {
                        String Url = EndPointPref.getInstance(MyApplication.getInstance()).getBaseUrl() + supplierDocModel.isURL();
                        String fileName = Url.substring(Url.lastIndexOf("/") + 1);
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Url));
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDescription("Download " + fileName + " from " + Url);
                        request.setTitle("Document Downloading");
                        request.setVisibleInDownloadsUi(true);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Supplier");
                        referenceId = downloadManager.enqueue(request);
                        list.add(referenceId);
                    } else {
                        Utils.setToast(getApplicationContext(), supplierDocModel.getMessage());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog();
        }

        @Override
        public void onComplete() {
        }
    };

    private final DisposableObserver<SupplierPaymentResponce> customerLedger = new DisposableObserver<SupplierPaymentResponce>() {

        @Override
        public void onNext(@NotNull SupplierPaymentResponce response) {
            Utils.hideProgressDialog();
            try {
                if (response.isStatus()) {
                    if (response.getCustomerLedgerModel() != null) {
                        ArrayList<LadgerEntryListModel> ledgerEntryList = response.getCustomerLedgerModel().getLadgerEntryListModel();

                        if (ledgerEntryList.size() != 0) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            try {
                                Date date1 = simpleDateFormat.parse(startDate);
                                Date date2 = simpleDateFormat.parse(endDate);

                                long difference = Math.abs(date1.getTime() - date2.getTime());
                                differenceDates = difference / (24 * 60 * 60 * 1000);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (differenceDates >= 30.0) {
                                callLimitpopup();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), LedgerSummaryActivity.class);

                                Bundle args = new Bundle();
                                args.putSerializable("data", ledgerEntryList);
                                args.putString("open", "" + response.getCustomerLedgerModel().getOpeningBalance());
                                args.putString("close", "" + response.getCustomerLedgerModel().getClosingBalance());
                                args.putString("StartDate", startDate);
                                args.putString("endDate", endDatestr);
                                args.putString("type", details);
                                intent.putExtra("BUNDLE", args);

                                startActivity(intent);
                            }
                        } else {
                            Utils.setToast(getApplicationContext(), "No data available");
                        }
                    } else {
                        Utils.setToast(getApplicationContext(), response.getMessage());
                    }
                } else {
                    Utils.setToast(getApplicationContext(), response.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    private final DisposableObserver<JsonObject> pedingobj = new DisposableObserver<JsonObject>() {

        @Override
        public void onNext(@NotNull JsonObject response) {
            Utils.hideProgressDialog();
            try {
                Log.e("pedingobj", response.toString());
                Intent intent = new Intent(getApplicationContext(), PendingPaymentActivity.class);
                intent.putExtra("data", response.toString());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
}