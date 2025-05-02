package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class HomeDataModel : Comparable<HomeDataModel>, Serializable {
    @SerializedName("AppItemsList")
    var appItemsList: ArrayList<AppItemsList>? = null

    @SerializedName("WebViewUrl")
    var webViewUrl: String? = null

    @SerializedName("ViewType")
    var viewType: String? = null

    @SerializedName("TileHeaderBackgroundImage")
    var tileHeaderBackgroundImage: String? = null

    @SerializedName("TileBackgroundImage")
    var tileBackgroundImage: String? = null

    @SerializedName("TileBackgroundColor")
    val tileBackgroundColor: String? = null

    @SerializedName("TileHeaderBackgroundColor")
    val tileHeaderBackgroundColor: String? = null

    @SerializedName("HasBackgroundColor")
    val hasBackgroundColor = false

    @SerializedName("ColumnCount")
    val columnCount = 0

    @SerializedName("RowCount")
    val rowCount = 0

    @SerializedName("Sequence")
    val sequence: Int

    @SerializedName("IsPopUp")
    val isPopUp = false

    @SerializedName("IsBanner")
    val isBanner = false

    @SerializedName("IsTile")
    val isTile = false

    @SerializedName("CreatedDate")
    private val createdDate: String? = null

    @SerializedName("SectionSubType")
    val sectionSubType: String

    @SerializedName("sectionBackgroundImage")
    val sectionBackgroundImage: String? = null

    @SerializedName("SectionName")
    var sectionName: String? = null

    @SerializedName("SectionID")
    var sectionID = 0

    @SerializedName("IsTileSlider")
    val isTileSlider = false

    @SerializedName("HeaderTextColor")
    var headerTextColor: String? = null

    @SerializedName("HeaderTextSize")
    val headerTextSize = 0

    @SerializedName("IsSingleBackgroundImage")
    val isSingleBackgroundImage: Boolean = false

    // cart variables
    val unitPrice: Double
    val price: Double
    var total = 0.0

    constructor(
        sectionSubType: String,
        sectionName: String?,
        headerTextColor: String?,
        unitPrice: Double,
        price: Double,
        tileHeaderBackgroundImage: String?,
        total: Double,
        sequence: Int
    ) {
        this.sectionSubType = sectionSubType
        this.sectionName = sectionName
        this.headerTextColor = headerTextColor
        this.unitPrice = unitPrice
        this.price = price
        this.tileHeaderBackgroundImage = tileHeaderBackgroundImage
        this.total = total
        this.sequence = sequence
    }

    constructor(
        sectionSubType: String,
        sectionName: String,
        headerTextColor: String,
        unitPrice: Double,
        price: Double,
        tileHeaderBackgroundImage: String?,
        sectionID: Int,
        sequence: Int,
        query: String?,
        appItemsList: ArrayList<AppItemsList>?
    ) {
        this.sectionSubType = sectionSubType
        this.sectionName = sectionName
        this.headerTextColor = headerTextColor
        this.unitPrice = unitPrice
        this.price = price
        this.tileHeaderBackgroundImage = tileHeaderBackgroundImage
        this.sectionID = sectionID
        this.sequence = sequence
        tileBackgroundImage = query
        this.appItemsList = appItemsList
    }

    class AppItemsList : Serializable {
        @SerializedName("RedirectionID")
        var redirectionID = 0
            private set

        @SerializedName("TileImage")
        var tileImage: String? = null
            private set

        @SerializedName("RedirectionUrl")
        val redirectionUrl: String? = null

        @SerializedName("TileSectionBackgroundImage")
        var tileSectionBackgroundImage: String? = null
            private set

        @SerializedName("TileName")
        var tileName: String? = null
            private set

        @SerializedName("RedirectionType")
        val redirectionType: String? = null

        @SerializedName("BannerImage")
        var bannerImage: String? = null
            private set

        @SerializedName("BaseCategoryId")
        var baseCategoryId = 0

        @SerializedName("CategoryId")
        var categoryId = 0

        @SerializedName("SubCategoryId")
        var subCategoryId = 0

        @SerializedName("SubsubCategoryId")
        val subsubCategoryId = 0

        @SerializedName("BannerActivity")
        val bannerActivity: String? = null

        // cart variables
        var unitPrice = 0.0
            private set
        var price = 0.0
            private set

        constructor() {}
        constructor(
            redirectionID: Int,
            tileImage: String?,
            tileSectionBackgroundImage: String?,
            tileName: String?,
            bannerImage: String?,
            unitPrice: Double,
            price: Double
        ) {
            this.redirectionID = redirectionID
            this.tileImage = tileImage
            this.tileSectionBackgroundImage = tileSectionBackgroundImage
            this.tileName = tileName
            this.bannerImage = bannerImage
            this.unitPrice = unitPrice
            this.price = price
        }
    }

    override fun compareTo(o: HomeDataModel): Int {
        return sequence - o.sequence
    }
}