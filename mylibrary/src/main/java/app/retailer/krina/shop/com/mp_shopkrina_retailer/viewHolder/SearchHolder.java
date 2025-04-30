package app.retailer.krina.shop.com.mp_shopkrina_retailer.viewHolder;

import androidx.recyclerview.widget.RecyclerView;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListSearchBinding;

public class SearchHolder extends RecyclerView.ViewHolder {
    public ItemMainListSearchBinding mBinding;

    public SearchHolder(ItemMainListSearchBinding mBinding) {
        super(mBinding.getRoot());
        this.mBinding = mBinding;
    }
}