package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw

import java.io.Serializable

data class MandiBhavModel(
    private var UserId: String,
    private var StateName: String,
    private var DistrictName:String
):Serializable


data class MandiCheckModel(var status:Boolean,var res:MandiBhavUserModel)
data class MandiBhavUserModel(
     var Id: String,
     var UserId: String,
     var StateName: String,
     var DistrictName:String,
     var CreatedDate:String
)
data class MandiDataModel(
     var Id: String,
     var State: String,
     var StateImagePath: String,
     var District:String,
     var DistrictImagePath:String,
     var Market:String,
     var Commodity:String,
     var CommodityImagePath:String,
     var Variety:String,
     var Grade:String,
     var ArrivalDate:String,
     var MinPrice:String,
     var MaxPrice:String,
     var ModalPrice:String,
     var Year:String,
     var Month:String,
     var Day:String,
     var Date:String,
     var CreatedDate:String,
)
data class MandiLocation(var Name:String,var StateName:String)
