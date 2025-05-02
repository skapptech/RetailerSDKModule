package app.retailer.krina.shop.com.mp_shopkrina_retailer.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity

// Implementation of App Widget functionality.
class OfferWidget : AppWidgetProvider() {
    val UPDATE_MEETING_ACTION = "android.appwidget.action.APPWIDGET_UPDATE"

    companion object {
        const val EXTRA_ITEM = "com.edockh.EXTRA_ITEM"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val mgr = AppWidgetManager.getInstance(context)

        if (intent!!.action.equals(UPDATE_MEETING_ACTION)) {
            val appWidgetIds =
                mgr.getAppWidgetIds(ComponentName(context!!, OfferWidget::class.java))
            Log.e("received", intent.action!!)
            mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listView)
        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // update each of the app widgets with the remote adapter
        for (i in appWidgetIds.indices) {
            // Set up the intent that starts the ListViewService, which will
            // provide the views for this collection.
            val intent = Intent(context, ListViewWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i])
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            // Instantiate the RemoteViews object for the app widget layout.
            val rv = RemoteViews(context.packageName, R.layout.offer_widget)
            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects
            // to a RemoteViewsService  through the specified intent.
            // This is how you populate the data.
            rv.setRemoteAdapter(appWidgetIds[i], R.id.listView, intent)

            // Trigger listView item click
            val startActivityIntent = Intent(context, HomeActivity::class.java)
            val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
            val startActivityPendingIntent =
                PendingIntent.getActivity(context, 0, startActivityIntent, flag)
            rv.setPendingIntentTemplate(R.id.listView, startActivityPendingIntent)
            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews  object above.
            rv.setEmptyView(R.id.listView, R.id.tvEmpty)
            // Do additional processing specific to this app widget...
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}