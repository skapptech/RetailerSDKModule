package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemExpiringWalletPointsBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyExpiringWalletModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;

public class MyExpiringWalletAdapter extends RecyclerView.Adapter<MyExpiringWalletAdapter.ViewHolder> {
    private final ArrayList<MyExpiringWalletModel> mylist;
    private final Context _context;


    public MyExpiringWalletAdapter(Context _context, ArrayList<MyExpiringWalletModel> models) {
        this._context = _context;
        this.mylist = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemExpiringWalletPointsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyExpiringWalletModel model = mylist.get(position);
        holder.mBinding.txtEarnPoint.setText(mylist.get(position).getThrough());
        holder.mBinding.txtDate.setText("");
        if (mylist.get(position).getExpiringDate() != null && !model.getExpiringDate().equals("null")) {
            holder.mBinding.txtOrderId.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.expire_on) + " " + Utils.getDateTimeFormate(mylist.get(position).getExpiringDate()));
        } else {
            holder.mBinding.txtOrderId.setText("");
        }
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_group_1105_rupe_new);
            holder.mBinding.txtRedAmt.setText("" + model.getPoint());
            holder.mBinding.txtRedAmt.setTextColor(_context.getResources().getColor(R.color.red));

    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemExpiringWalletPointsBinding mBinding;

        public ViewHolder(ItemExpiringWalletPointsBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}