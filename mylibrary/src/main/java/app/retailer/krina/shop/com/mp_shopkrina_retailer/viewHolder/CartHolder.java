package app.retailer.krina.shop.com.mp_shopkrina_retailer.viewHolder;

import androidx.recyclerview.widget.RecyclerView;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListCartBinding;

public class CartHolder extends RecyclerView.ViewHolder {
    public ItemMainListCartBinding mBinding;

    public CartHolder(ItemMainListCartBinding mBinding) {
        super(mBinding.getRoot());
        this.mBinding = mBinding;
    }
}
