package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces;

import android.widget.Button;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.AppHomeItemAdapter;

public interface ItemsOfferInterface {

    void itemOffers(ProgressBar p, String sectionId, Button llLordMore, AppHomeItemAdapter appHomeItemAdapter, boolean tileSlider);

    void getOtherItems(RecyclerView.ViewHolder mBinding, String url, int sectionId, AppHomeItemAdapter appHomeItemAdapter, boolean tileSlider);
}
