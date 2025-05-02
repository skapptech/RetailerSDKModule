package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils

import android.app.Activity
import android.content.Context

class DirectSDK {
    var otp: String = "1234"
    var activity: Activity? = null


    companion object {
        private var mInstance: DirectSDK? = null
        var context: Context? = null

        @get:Synchronized
        val instance: DirectSDK
            get() {
                if (mInstance == null) {
                    mInstance = DirectSDK()
                }
                return mInstance!!
            }

        fun initialize(context1: Context?) {
            context = context1
            mInstance = DirectSDK()
        }
    }
}