package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces;

import androidx.recyclerview.widget.RecyclerView;

public interface DynamicHtmlInterface {
    void loadDynamicHtml(RecyclerView.ViewHolder holder, int positon, String Url);
}
