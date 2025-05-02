package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.urlpreview

interface ViewListener {
    fun onPreviewSuccess(status: Boolean)
    fun onFailedToLoad(e: Exception?)
}