package app.retailer.krina.shop.com.mp_shopkrina_retailer.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;

public class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private ArrayList<String> records;


    public ListViewRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    // Initialize the data set.
    @Override
    public void onCreate() {
        // In onCreate() you set up any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
        records = new ArrayList<>();
        records.add("asdasd");
        records.add("Dfsfsdf");
        records.add("vvxvxcv");
    }

    // Given the position (index) of a WidgetItem in the array, use the item's text value in
    // combination with the app widget item XML file to construct a RemoteViews object.
    @Override
    public RemoteViews getViewAt(int position) {
        // position will always range from 0 to getCount() - 1.
        // Construct a RemoteViews item based on the app widget item XML file, and set the
        // text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);
        // feed row
        String data = records.get(position);
        rv.setTextViewText(R.id.tvName, data);
        // end feed row
        // Next, set a fill-intent, which will be used to fill in the pending intent template
        // that is set on the collection view in ListViewWidgetProvider.
        Bundle extras = new Bundle();
        extras.putInt(OfferWidget.EXTRA_ITEM, position);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("homescreen_meeting", data);
        fillInIntent.putExtras(extras);

        // Make it possible to distinguish the individual on-click
        // action of a given item
        rv.setOnClickFillInIntent(R.id.liWidget, fillInIntent);
        // Return the RemoteViews object.
        return rv;
    }

    @Override
    public int getCount() {
        Log.e("size=", records.size() + "");
        return records.size();
    }

    @Override
    public void onDataSetChanged() {
        // Fetching JSON data from server and add them to records arraylist

    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onDestroy() {
        records.clear();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }
}