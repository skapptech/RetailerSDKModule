package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.urlpreview

import android.view.View

interface LinkListener {
    fun onClicked(view: View?, meta: PreviewMetaData?)
}