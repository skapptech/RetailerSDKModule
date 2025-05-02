package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.urlpreview

interface ResponseListener {
    fun onData(previewMetaData: PreviewMetaData?)
    fun onError(e: Exception?)
}