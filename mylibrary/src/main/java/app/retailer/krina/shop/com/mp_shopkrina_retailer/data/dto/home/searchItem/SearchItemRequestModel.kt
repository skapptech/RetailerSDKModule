package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem

data class SearchItemRequestModel(
    val brand: List<Int>,
    val subCat: List<Int>,
    val category: List<Int>,
    val baseCat: List<Int>,
    val maxPrice: Int,
    val minPrice: Int,
    val customerId: Int,
    val itemkeyword: String,
    val lang: String,
    val BarCode: String
)
