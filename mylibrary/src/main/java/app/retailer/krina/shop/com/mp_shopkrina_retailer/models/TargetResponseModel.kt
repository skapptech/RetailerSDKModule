package app.retailer.krina.shop.com.mp_shopkrina_retailer.models

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TargetResponseModel: Serializable {
    @SerializedName("TotalItem")
    var message = 0

    @SerializedName("ItemDataDCs")
    var itemMasters: ArrayList<ItemListModel>? = null
}