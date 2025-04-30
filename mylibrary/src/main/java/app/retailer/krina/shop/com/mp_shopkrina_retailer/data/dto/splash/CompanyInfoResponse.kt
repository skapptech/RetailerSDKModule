package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash

import com.google.gson.annotations.SerializedName

class CompanyInfoResponse {
    @SerializedName("CompanyDetails")
    val companyDetails: CompanyDetails? = null

    @SerializedName("Status")
    val isStatus = false

    @SerializedName("Message")
    val message: String? = null

    class CompanyDetails {
        @SerializedName("Id")
        val id = 0

        @SerializedName("Name")
        val name: String? = null

        @SerializedName("Contact")
        val contact: String? = null

        @SerializedName("Email")
        val email: String? = null

        @SerializedName("Website")
        val website: String? = null

        @SerializedName("LogoUrl")
        val logoUrl: String? = null

        @SerializedName("GatewayName")
        val gatewayName: String? = null

        @SerializedName("GatewayURL")
        val gatewayURL: String? = null

        @SerializedName("HDFCWorkingKey")
        val hDFCWorkingKey: String? = null

        @SerializedName("HDFCAccessCode")
        val hDFCAccessCode: String? = null

        @SerializedName("HDFCMerchantId")
        val hDFCMerchantId: String? = null

        @SerializedName("TrupayAccessToken")
        private val TrupayAccessToken: String? = null

        @SerializedName("TruepayCollectorId")
        private val TruepayCollectorId: String? = null

        @SerializedName("TruepayPaymentMethod")
        private val TruepayPaymentMethod: String? = null

        @SerializedName("ePayLaterEndpoint")
        val ePayLaterEndpoint: String? = null

        @SerializedName("E_PAY_LATER_URL")
        val ePAYLATERURL: String? = null

        @SerializedName("ENCODED_KEY")
        val eNCODEDKEY: String? = null

        @SerializedName("BEARER_TOKEN")
        val bEARERTOKEN: String? = null

        @SerializedName("IV")
        val iV: String? = null

        @SerializedName("M_CODE")
        val mCODE: String? = null

        @SerializedName("category")
        val category: String? = null

        @SerializedName("redirect_url")
        val redirect_url: String? = null

        @SerializedName("cancel_url")
        val cancel_url: String? = null

        @SerializedName("IsShowLedger")
        val isShowLedger = false

        @SerializedName("IsShowTarget")
        val isShowTarget = false

        @SerializedName("MaxWalletPointUsed")
        val maxWalletPointUsed: String? = null

        @SerializedName("WebViewBaseUrl")
        val webViewBaseUrl: String? = null

        @SerializedName("IsShowReturn")
        val isShowReturn = false

        @SerializedName("IsShowHisabKitab")
        val isShowHisabKitab = false

        @SerializedName("TradeWebViewBaseUrl")
        val tradeWebViewURL: String? = null

        @SerializedName("IsShowTicketMenu")
        val isShowTicketMenu = false

        @SerializedName("IsShowCreditOption")
        val isShowCreditOption = false

        @SerializedName("CreditOptionName")
        val creditOptionName: String? = null

        @SerializedName("CreditGatewayURL")
        val creditGatewayURL: String? = null

        @SerializedName("CreditWorkingKey")
        val creditWorkingKey: String? = null

        @SerializedName("CreditAccessCode")
        val creditAccessCode: String? = null

        @SerializedName("CreditMerchantId")
        val creditMerchantId: String? = null

        @SerializedName("Creditredirect_url")
        val creditRedirectUrl: String? = null

        @SerializedName("Creditcancel_url")
        val creditCancelUrl: String? = null

        @SerializedName("IsPrimeActive")
        val isPrimeActive = false

        @SerializedName("PrimeAmount")
        val primeAmount = 0.0

        @SerializedName("PrimeMemberShipInMonth")
        val primeMemberShipInMonth = 0

        @SerializedName("MemberShipName")
        val memberShipName: String? = null

        @SerializedName("MemberShipHindiName")
        val memberShipHindiName: String? = null

        @SerializedName("ischeckBookShow")
        var isIscheckBookShow: Boolean? = false

        @SerializedName("CheckBookApiKey")
        var checkBookAPIKey: String? = null

        @SerializedName("ischeckBookMinAmt")
        var ischeckBookMinAmt = 0

        @SerializedName("IsOnlinePayment")
        var isOnlinePayment = false

        @SerializedName("checkBookBaseURl")
        var checkBookBaseURl: String? = null

        @SerializedName("IsePayLaterShow")
        var isIsePayLaterShow = false

        @SerializedName("LanguageLastUpdated")
        var languageLastUpdated: String? = null

        @SerializedName("IsFinBox")
        var isFinBox = false

        @SerializedName("FinboxclientApikey")
        var finboxclientApikey: String? = null

        @SerializedName("IsCreditLineShow")
        val isCreditLineShow = false

        @SerializedName("FinBoxCreditKey")
        val finBoxCreditKey: String? = null

        @SerializedName("IsShowVATM")
        val isShowVATM = false

        @SerializedName("IsCreditLendingEnable")
        val isCreditLendingEnable = false

        @SerializedName("firestoreApplicationId")
        var firestoreApplicationId: String? = null

        @SerializedName("firestoreApiKey")
        var firestoreApiKey: String? = null

        @SerializedName("firestoreDatabaseUrl")
        var firestoreDatabaseUrl: String? = null

        @SerializedName("firestoreProjectId")
        var firestoreProjectId: String? = null

        @SerializedName("azureAccountName")
        var azureAccountName: String? = null

        @SerializedName("azureAccountKey")
        var azureAccountKey: String? = null

        @SerializedName("RazorpayApiKeyId")
        var razorpayApiKeyId: String? = null

        @SerializedName("ICICIAppId")
        var iCICIAppId: String? = null

        @SerializedName("ICICIMerchantId")
        var iCICIMerchantId: String? = null

        @SerializedName("ICICISecretKey")
        var iCICISecretKey: String? = null

        @SerializedName("ICICIPaymentResultUrl")
        var iCICIPaymentResultUrl: String? = null

        @JvmField
        @SerializedName("IsDirectUdharGullakShow")
        var isDUdharGullakShow = false
    }
}