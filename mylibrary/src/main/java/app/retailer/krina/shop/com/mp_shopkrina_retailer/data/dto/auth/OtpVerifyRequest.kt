package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth


data class OtpVerifyRequest(val MobileNumber: String,
                            val deviceId:String,
                            val Otp: String,
                            val trueCustomer: Boolean,
                            val CurrentAPKversion:String,
                            val PhoneOSversion:String,
                            val UserDeviceName:String,
                            val fcmId:String)
