package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.BucketGameViewPagerAdapter;

public interface BucketGameInterface {
    void callBucketGameApi(RecyclerView.ViewHolder holder, BucketGameViewPagerAdapter bucketGameViewPagerAdapter, String url);
}
