package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home

class CartAddItemModel(
    val CustomerId: Int,
    val WarehouseId: Int,
    val ItemId: Int,
    val qty: Int,
    val UnitPrice: Double,
    val IsFreeItem: Boolean,
    val IsCartRequire: Boolean,
    val lang: String,
    val IsPrimeItem: Boolean,
    val IsDealItem: Boolean
)
