package app.retailer.krina.shop.com.mp_shopkrina_retailer.viewHolder;

import androidx.recyclerview.widget.RecyclerView;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListStoreBinding;

public class BucketGameSectionHolder  extends RecyclerView.ViewHolder {
    public ItemMainListStoreBinding mBinding;

    public BucketGameSectionHolder(ItemMainListStoreBinding mBinding) {
        super(mBinding.getRoot());
        this.mBinding = mBinding;
    }
}