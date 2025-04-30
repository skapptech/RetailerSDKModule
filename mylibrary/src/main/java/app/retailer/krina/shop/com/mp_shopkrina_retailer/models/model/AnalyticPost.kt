package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

class AnalyticPost {
    @JvmField
    var eventName: String = ""
    @JvmField
    var baseCatId: String? = null

    @JvmField
    var categoryId = 0

    @JvmField
    var subCatId = 0

    @JvmField
    var subSubCatId = 0
    var subSubCatName: String? = null

    @JvmField
    var categoryName: String? = null
    @JvmField
    var source: String? = null

    // apphome
    @JvmField
    var sectionId: Int? = 0

    @JvmField
    var sectionName: String? = null

    @JvmField
    var sectionSubType: String? = null

    @JvmField
    var url: String? = null

    // Community
    @JvmField
    var postType: String? = null
    @JvmField
    var postId: String? = null
    @JvmField
    var commentCount: Int? = 0
    @JvmField
    var likeCount: Int? = 0

    @JvmField
    var section: String? = null

    @JvmField
    var postTypeClick: String? = null
}