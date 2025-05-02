package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn

data class PostKKReturnReplaceRequestDc(
    val CustomerId: Int?,
    val ExecutiveId: Int?,
    val Details: ArrayList<KKReturnReplaceDetailDC>?
)
