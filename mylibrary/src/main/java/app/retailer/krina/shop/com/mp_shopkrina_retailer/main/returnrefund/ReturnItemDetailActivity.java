package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.returnrefund;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityReturnItemDetailBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderItemModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;

public class ReturnItemDetailActivity extends AppCompatActivity {
    private ActivityReturnItemDetailBinding mBinding;
    private ReturnOrderItemModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityReturnItemDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_activity_return_order));

        if (getIntent().getExtras() != null) {
            model = getIntent().getParcelableExtra("list");
        }
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    private void initViews() {
        Glide.with(this).load(model.getItemPic()).into(mBinding.ivImage);
        mBinding.tvName.setText(model.getItemName());
        mBinding.tvPrice.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_mrp) + " " + model.getPrice());
        mBinding.tvQty.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_order_qty) + " " + model.getQty());
        mBinding.cbSelect.setChecked(model.isSelected());
    }
}