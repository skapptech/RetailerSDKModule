package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn

data class KKReturnReplaceDetailDC(
    val OrderId: Int?,
    val OrderDetailsId: Int?,
    val ReturnQty: Long?,
    val Qty: Int?,
    val ReturnReason: String?,
    val BatchCode:String?,
    val BatchMasterId:Int?,
    val ItemMultiMRPId:Int?,
    val ItemImages: ArrayList<String>?
)
