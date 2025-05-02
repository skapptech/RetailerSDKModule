package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.StoreItemAdapter;

public interface StoreApiInterface {
    void callStoreApi(RecyclerView.ViewHolder holder, StoreItemAdapter sItemAdapter, String url);
}
