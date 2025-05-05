package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.returnrefund;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityReturnItemDetailBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityReturnOrderDetailBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.ReturnOrderDetailAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderItemModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderStatusModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.stepform.MainStepperAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

@SuppressWarnings("ConstantConditions")
public class ReturnOrderDetailActivity extends AppCompatActivity {
    private ActivityReturnOrderDetailBinding mBinding;
    private ReturnOrderListModel model;
    private ArrayList<ReturnOrderStatusModel> list;
    private MainStepperAdapter adapter;
    private CommonClassForAPI commonClassForAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityReturnOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            model = getIntent().getParcelableExtra("list");
        }
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.return_order, menu);
        MenuItem item = menu.findItem(R.id.action_cancel);
        if (model != null && model.getStatus().equalsIgnoreCase("Pending to Pick")) {
            item.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_cancel) {
            showCancelDialog();
        } else {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.rightTransaction(this);
    }

    private void initViews() {
        if (model != null) {
            getSupportActionBar().setTitle(RetailerSDKApp.getInstance().dbHelper.getData("order_id"));
            getSupportActionBar().setSubtitle("" + model.getOrderId());
            mBinding.tvOrderId.setText(RetailerSDKApp.getInstance().dbHelper.getData("order_id_colon")+" " + model.getOrderId());
            mBinding.tvDate.setText(RetailerSDKApp.getInstance().dbHelper.getData("txt_date_colon")+" " + Utils.getChangeDateFormatInProfile(model.getModifiedDate()));
            mBinding.tvStatus.setText("" + model.getStatus());
            if (model.getRequestType() == 0) {
                mBinding.tvStatus.setTextColor(Color.RED);
                mBinding.tvStatus1.setTextColor(Color.RED);
            } else {
                mBinding.tvStatus.setTextColor(Color.GREEN);
                mBinding.tvStatus1.setTextColor(Color.GREEN);
            }
        }

        list = new ArrayList<>();
//        if (model.getRequestType() == 0) {
//            list.add(new ReturnOrderStatusModel(1, "Pending to Pick", "Your Item will picked by our Delivery Partner.", ""));
//            list.add(new ReturnOrderStatusModel(2, "Pending from Customer", "Your Item has been picked by our Delivery Partner.", ""));
//            list.add(new ReturnOrderStatusModel(3, "Return to Warehouse", "Your Item has been collected to Warehouse.", ""));
//            list.add(new ReturnOrderStatusModel(4, "Ready for Replacement", "Your Item Ready for replacement.", ""));
//            list.add(new ReturnOrderStatusModel(5, "Replacement Settle", "Order has been Settled.", ""));
//        } else {
//            list.add(new ReturnOrderStatusModel(1, "Pending to Pick", "Your Item will picked by our Delivery Partner.", ""));
//            list.add(new ReturnOrderStatusModel(2, "Pending from Customer", "Your Item has been picked by our Delivery Partner.", ""));
//            list.add(new ReturnOrderStatusModel(3, "Return to Warehouse", "Your Item has been collected to Warehouse.", ""));
//            list.add(new ReturnOrderStatusModel(4, "Ready for Replacement", "Your Item Ready for replacement.", ""));
//            list.add(new ReturnOrderStatusModel(5, "Issue for Replacement", "Your Item Issue for replacement.", ""));
//            list.add(new ReturnOrderStatusModel(6, "Shipped for Replacement", "Your Item has been shipped from Warehouse.", ""));
//            list.add(new ReturnOrderStatusModel(7, "Replacement Delivered", "Item has been delivered to you.", ""));
//            list.add(new ReturnOrderStatusModel(8, "Replacement Settle", "Order has been Settled.", ""));
//        }
        mBinding.recyclerReturnItem.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new MainStepperAdapter(this, list);
        mBinding.stepperList.setStepperAdapter(adapter);

        commonClassForAPI = CommonClassForAPI.getInstance(this);

        Utils.showProgressDialog(this);
        commonClassForAPI.getReturnReplaceStatusList(statusObserver, model.getKKRequestId());
        commonClassForAPI.getReturnReplaceItemList(itemObserver, model.getKKRequestId());
    }

    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + 22 + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(RetailerSDKApp.getInstance().dbHelper.getData("text_calcel_return_replace"));
        builder.setMessage(RetailerSDKApp.getInstance().dbHelper.getData("text_are_you_sure_calcel_return_replace"));

        builder.setPositiveButton(getResources().getString(R.string.cancel), (dialog, i) -> dialog.cancel());
        builder.setNegativeButton(getResources().getString(R.string.confirm), (dialog, i) -> {
            dialog.cancel();
            Utils.showProgressDialog(this);
            commonClassForAPI.updateReturnRequestStatus(updateOrderStatusObserver, model.getKKRequestId(), "Cancelled", 0);
        });

        builder.show();
    }


    private final DisposableObserver statusObserver = new DisposableObserver<ArrayList<ReturnOrderStatusModel>>() {
        @Override
        public void onNext(ArrayList<ReturnOrderStatusModel> arrayList) {
            try {
                Utils.hideProgressDialog();
                if (arrayList != null && arrayList.size() > 0) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        ReturnOrderStatusModel model = new ReturnOrderStatusModel();
                        model.setStatus(arrayList.get(i).getStatus());
                        model.setDate(arrayList.get(i).getDate());

                        list.add(model);
                    }
                    adapter.jumpTo(arrayList.size() <= 1 ? 0 : arrayList.size() - 1);
                    adapter.notifyDataSetChanged();
                    setListViewHeight(mBinding.stepperList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog();
            statusObserver.dispose();
        }

        @Override
        public void onComplete() {

        }
    };

    private final DisposableObserver itemObserver = new DisposableObserver<ArrayList<ReturnOrderItemModel>>() {
        @Override
        public void onNext(ArrayList<ReturnOrderItemModel> arrayList) {
            try {
                Utils.hideProgressDialog();
                mBinding.recyclerReturnItem.setAdapter(new ReturnOrderDetailAdapter(ReturnOrderDetailActivity.this, arrayList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog();
            itemObserver.dispose();
        }

        @Override
        public void onComplete() {

        }
    };

    private final DisposableObserver updateOrderStatusObserver = new DisposableObserver<Boolean>() {
        @Override
        public void onNext(Boolean b) {
            try {
                Utils.hideProgressDialog();
                if (b) {
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    onBackPressed();
                } else {
                    Utils.setToast(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getData("text_some_error_occured"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog();
            updateOrderStatusObserver.dispose();
        }

        @Override
        public void onComplete() {

        }
    };
}