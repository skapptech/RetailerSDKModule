package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils

import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref

class Constant {


    companion object {
        @JvmField
        var BASE_URL_PROFILE =
            EndPointPref.getInstance(MyApplication.getInstance()).baseUrl + "/UploadedImages/"

        @JvmField
        var CUSTOMER_ADDRESS = "customer_address"

        @JvmField
        var ACTIVATION_TITLE = "activation_title"

        @JvmField
        var ORDER_MODEL = "order_model"

        // hdfc parameters
        const val PARAMETER_SEP = "&"
        const val PARAMETER_EQUALS = "="

        @JvmField
        var AUDIO_FILE_NAME = "test.mp3" //MP-INDB-1_09172019125727.mp3

        @JvmField
        var IMAGE_FILE_NAME1 = "test1"

        @JvmField
        var IMAGE_FILE_NAME2 = "test2"

        @JvmField
        var IMAGE_FILE_NAME3 = "test3"

        // folder names
        @JvmField
        var AUDIO_FOLDER = "/ShopKirana/Audio/"

        @JvmField
        var IMAGE_FOLDER = "/ShopKirana/Images/"

        @JvmField
        var PRODUCT_IMAGE_FOLDER = "/ShopKirana/Product/Images/"

        @JvmField
        var CALLBACK_URL =
            EndPointPref.getInstance(MyApplication.getInstance()).baseUrl + "/api/Transaction/paymentcallback"

        //Share url
        @JvmField
        var SHARE_URL = "https://saral.shopkirana.in/#/shareitem?"

        @JvmField
        var IMAGE_OBSERVABLE_EXECUTION_COUNT = 0

        // firebase topics for subscription
        const val TOPIC_LOGIN = "login"
        const val TOPIC_GLOBAL = "global"
        const val TOPIC_WAREHOUSE = "warehouse"
        const val TOPIC_CART = "cart"
        const val TOPIC_TEST = "uat_testing"

        var FILE_PATH = "filePath"
        var ADDRESS_INFO = "model"
        var FILE_NAME = "fileName"
        var IS_GALLERY_OPTION = "isGalleryOption"
        var IS_NOT_SHOW_CANCEL_BTN = "isNotShowCancelBtn"
        var CITY_NAME = "cityname"
        var IS_SEARCH_CITY = "searchCity"
        var TYPE = "type"
        var LATITUDE = "lat"
        var LONGITUDE = "lng"
        var PLACE_RESULT = "PlaceResult"
        var ADDRESS = "Address"
        var AREA = "area"
        @kotlin.jvm.JvmField
        var VERSION_NAME = "1.0"

    }
}