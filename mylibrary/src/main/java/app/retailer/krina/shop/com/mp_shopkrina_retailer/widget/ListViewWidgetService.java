package app.retailer.krina.shop.com.mp_shopkrina_retailer.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListViewWidgetService extends RemoteViewsService {

    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}