package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.clearance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.CheckBillDiscountResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CheckBookCreditLimitRes
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CreditLimit
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.EpayLaterResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.OrderPlacedNewResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.PrepaidOrder
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.ScaleUpResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet.WalletResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CreditPayment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.OrderPlacedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentReq
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClearanceViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val walletLiveData = SingleLiveEvent<Response<WalletResponse>>()
    val walletData: SingleLiveEvent<Response<WalletResponse>>
        get() = walletLiveData

    private val getUdharCreditLimitLiveData = SingleLiveEvent<Response<CreditLimit>>()
    val getUdharCreditLimitData: SingleLiveEvent<Response<CreditLimit>>
        get() = getUdharCreditLimitLiveData

    private val ePayLaterCustomerLimitLiveData = SingleLiveEvent<Response<JsonObject>>()
    val ePayLaterCustomerLimitData: SingleLiveEvent<Response<JsonObject>>
        get() = ePayLaterCustomerLimitLiveData


    private val checkbookLimitLiveData = SingleLiveEvent<Response<JsonObject>>()
    val checkbookLimitData: SingleLiveEvent<Response<JsonObject>>
        get() = checkbookLimitLiveData

    private val cODLimitLiveData = SingleLiveEvent<Response<JsonElement>>()
    val getCODLimitData: SingleLiveEvent<Response<JsonElement>>
        get() = cODLimitLiveData

    private val scaleUpLimitLiveData = SingleLiveEvent<Response<CreditLimit>>()
    val getScaleUpLimitData: SingleLiveEvent<Response<CreditLimit>>
        get() = scaleUpLimitLiveData

    private val offerLiveData = SingleLiveEvent<Response<BillDiscountListResponse>>()
    val getOfferData: SingleLiveEvent<Response<BillDiscountListResponse>>
        get() = offerLiveData

    private val prepaidOrderLiveData = SingleLiveEvent<Response<PrepaidOrder>>()
    val getPrepaidOrderData: SingleLiveEvent<Response<PrepaidOrder>>
        get() = prepaidOrderLiveData

    private val applyOfferLiveData = SingleLiveEvent<Response<CheckoutCartResponse>>()
    val getApplyOfferData: SingleLiveEvent<Response<CheckoutCartResponse>>
        get() = applyOfferLiveData

    private val updateScratchCardStatusLiveData = SingleLiveEvent<CheckBillDiscountResponse>()
    val updateScratchCardStatusData: SingleLiveEvent<CheckBillDiscountResponse>
        get() = updateScratchCardStatusLiveData

    private val checkOfferLiveData = SingleLiveEvent<Response<CheckBillDiscountResponse>>()
    val getCheckOfferData: SingleLiveEvent<Response<CheckBillDiscountResponse>>
        get() = checkOfferLiveData

    private val ePaylaterConfirmPaymentLiveData = SingleLiveEvent<Response<EpayLaterResponse>>()
    val getEPaylaterConfirmPaymentData: SingleLiveEvent<Response<EpayLaterResponse>>
        get() = ePaylaterConfirmPaymentLiveData

    private val hDFCRSAKeyLiveData = SingleLiveEvent<String>()
    val getHDFCRSAKeyData: SingleLiveEvent<String>
        get() = hDFCRSAKeyLiveData

    private val razorpayOrderIdLiveData = SingleLiveEvent<String>()
    val getRazorpayOrderIdData: SingleLiveEvent<String>
        get() = razorpayOrderIdLiveData

    private val creditPaymentLiveData = SingleLiveEvent<Response<CreditLimit>>()
    val getCreditPaymentData: SingleLiveEvent<Response<CreditLimit>>
        get() = creditPaymentLiveData

    private val scaleUpPaymentInitiateLiveData = SingleLiveEvent<Response<ScaleUpResponse>>()
    val getScaleUpPaymentInitiateData: SingleLiveEvent<Response<ScaleUpResponse>>
        get() = scaleUpPaymentInitiateLiveData

    private val orderPlaceLiveData = SingleLiveEvent<Response<OrderPlacedNewResponse>>()
    val getOrderPlaceData: SingleLiveEvent<Response<OrderPlacedNewResponse>>
        get() = orderPlaceLiveData

    private val insertOnlineTransactionLiveData = SingleLiveEvent<Response<Boolean>>()
    val getInsertOnlineTransactionData: SingleLiveEvent<Response<Boolean>>
        get() = insertOnlineTransactionLiveData

    private val updateOnlineTransactionLiveData = SingleLiveEvent<Response<Boolean>>()
    val getUpdateOnlineTransactionData: SingleLiveEvent<Response<Boolean>>
        get() = updateOnlineTransactionLiveData

    private val updateDeliveryETALiveData = SingleLiveEvent<JsonObject>()
    val getUpdateDeliveryETALiveData: SingleLiveEvent<JsonObject>
        get() = updateDeliveryETALiveData


    private val isGullakOptionEnableLiveData = SingleLiveEvent<Boolean>()
    val isGullakOptionEnableData: SingleLiveEvent<Boolean>
        get() = isGullakOptionEnableLiveData


    private val iciciPaymentCheckLiveData = SingleLiveEvent<Response<JsonObject>>()
    val getICICIPaymentCheckData: SingleLiveEvent<Response<JsonObject>>
        get() = iciciPaymentCheckLiveData

    fun getWalletPoint(customerId: Int,mSectionType:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            walletLiveData.postValue(Response.Loading())
            val result = repository.getWalletPoint(customerId,mSectionType)
            if (result.body() != null) {
                walletLiveData.postValue(Response.Success(result.body()))
            } else {
                walletLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.no_response)))
            }
        } else {
            walletLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun getUdharCreditLimit(customerId: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            getUdharCreditLimitLiveData.postValue(Response.Loading())
            val result = repository.getUdharCreditLimit(customerId)
            if (result.code() == 200 && result.body() != null) {
                getUdharCreditLimitLiveData.postValue(Response.Success(result.body()))
            } else {
                getUdharCreditLimitLiveData.postValue(
                    Response.Error(result.code().toString())
                )
            }
        } else {
            getUdharCreditLimitLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun ePayLaterCustomerLimit(
        customerId: String?,
        authHeaderQuery: String?
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            ePayLaterCustomerLimitLiveData.postValue(Response.Loading())
            val result = repository.ePayLaterCustomerLimit(customerId, authHeaderQuery)
            if (result.code() == 200 && result.body() != null) {
                ePayLaterCustomerLimitLiveData.postValue(Response.Success(result.body()))
            } else {
                ePayLaterCustomerLimitLiveData.postValue(
                    Response.Error(result.code().toString())
                )
            }
        } else {
            ePayLaterCustomerLimitLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun checkBookCustomerLimit(url: String?,apiKey: String?,bookCreditLimitRes: CheckBookCreditLimitRes) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            checkbookLimitLiveData.postValue(Response.Loading())
            val result = repository.checkBookCustomerLimit(url,apiKey,bookCreditLimitRes)
            if (result.code() == 200 && result.body() != null) {
                checkbookLimitLiveData.postValue(Response.Success(result.body()))
            } else {
                checkbookLimitLiveData.postValue(
                    Response.Error(result.code().toString())
                )
            }
        } else {
            checkbookLimitLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun getCustomerCODLimit(customerId: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            cODLimitLiveData.postValue(Response.Loading())
            val result = repository.getCustomerCODLimit(customerId)
            if (result.code() == 200 && result.body() != null) {
                cODLimitLiveData.postValue(Response.Success(result.body()))
            } else {
                cODLimitLiveData.postValue(
                    Response.Error(result.code().toString())
                )
            }
        } else {
            cODLimitLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun getScaleUpLimit(customerId: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            scaleUpLimitLiveData.postValue(Response.Loading())
            val result = repository.getScaleUpLimit(customerId)
            if (result.code() == 200 && result.body() != null) {
                scaleUpLimitLiveData.postValue(Response.Success(result.body()))
            } else {
                scaleUpLimitLiveData.postValue(
                    Response.Error(result.code().toString())
                )
            }
        } else {
            scaleUpLimitLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun getDiscountOffer(customerId: Int, mSectionType:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            offerLiveData.postValue(Response.Loading())
            val result = repository.getDiscountOffer(customerId,mSectionType)
            if (result.code() == 200 && result.body() != null) {
                offerLiveData.postValue(Response.Success(result.body()))
            } else {
                offerLiveData.postValue(
                    Response.Error(result.code().toString())
                )
            }
        } else {
            offerLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun getPrepaidOrder(warehouseId: Int,mSectionType:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            prepaidOrderLiveData.postValue(Response.Loading())
            val result = repository.getPrepaidOrder(warehouseId,mSectionType)
            if (result.code() == 200 && result.body() != null) {
                prepaidOrderLiveData.postValue(Response.Success(result.body()))
            } else {
                prepaidOrderLiveData.postValue(
                    Response.Error(getApplication<Application>().getString(
                        R.string.server_error
                    ))
                )
            }
        } else {
            prepaidOrderLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }
    fun applyOffer(customerId: Int,warehouseId: Int,offerId: Int,isApply: Boolean,lang: String?,mSectionType:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            applyOfferLiveData.postValue(Response.Loading())
            val result = repository.applyOffer(customerId,warehouseId,offerId,isApply,lang,mSectionType)
            if (result.code() == 200 && result.body() != null) {
                applyOfferLiveData.postValue(Response.Success(result.body()))
            } else {
                applyOfferLiveData.postValue(
                    Response.Error(result.code().toString())
                )
            }
        } else {
            applyOfferLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun updateScratchCardStatus(customerId: Int,offerId: Int,isScartched: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.updateScratchCardStatus(customerId,offerId,isScartched)
            updateScratchCardStatusLiveData.postValue(result.body())
        }
    }

    fun checkBillDiscountOffer(customerId: Int,offerId: String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            checkOfferLiveData.postValue(Response.Loading())
            val result = repository.checkBillDiscountOffer(customerId,offerId)
            if (result.code() == 200 && result.body() != null) {
                checkOfferLiveData.postValue(Response.Success(result.body()))
            } else {
                checkOfferLiveData.postValue(
                    Response.Error(getApplication<Application>().getString(
                        R.string.server_error
                    ))
                )
            }
        } else {
            checkOfferLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }
    fun ePayLaterConfirmOrder(authHeaderQuery: String,ePayLaterId: String,orderID:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            ePaylaterConfirmPaymentLiveData.postValue(Response.Loading())
            val result = repository.ePayLaterConfirmOrder(authHeaderQuery,ePayLaterId,orderID)
            if (result.code() == 200 && result.body() != null) {
                ePaylaterConfirmPaymentLiveData.postValue(Response.Success(result.body()))
            } else {
                ePaylaterConfirmPaymentLiveData.postValue(
                    Response.Error(result.code().toString())
                )
            }
        } else {
            ePaylaterConfirmPaymentLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun getHDFCRSAKey(hdfcOrderId: String?,amount: String?,isCredit: Boolean,mSectionType:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.getHDFCRSAKey(hdfcOrderId,amount,isCredit,mSectionType)
            hDFCRSAKeyLiveData.postValue(result.body())
        }
    }

    fun fetchRazorpayOrderId(orderId: String, amount: Double) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.fetchRazorpayOrderId(orderId,amount)
            razorpayOrderIdLiveData.postValue(result.body())
        }
    }

    fun creditPayment(model: CreditPayment) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            creditPaymentLiveData.postValue(Response.Loading())
            val result = repository.creditPayment(model)
            if (result.code() == 200 && result.body() != null) {
                creditPaymentLiveData.postValue(Response.Success(result.body()))
            } else {
                creditPaymentLiveData.postValue(
                    Response.Error(getApplication<Application>().getString(
                        R.string.server_error
                    ))
                )
            }
        } else {
            creditPaymentLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun scaleUpPaymentInitiate(custId: Int,orderId: Int, amount: Double) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            scaleUpPaymentInitiateLiveData.postValue(Response.Loading())
            val result = repository.scaleUpPaymentInitiate(custId,orderId, amount)
            if (result.code() == 200 && result.body() != null) {
                scaleUpPaymentInitiateLiveData.postValue(Response.Success(result.body()))
            } else {
                scaleUpPaymentInitiateLiveData.postValue(
                    Response.Error(getApplication<Application>().getString(
                        R.string.server_error
                    ))
                )
            }
        } else {
            scaleUpPaymentInitiateLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }
    fun getOrderPlaced(orderPlacedModel: OrderPlacedModel) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            orderPlaceLiveData.postValue(Response.Loading())
            val result = repository.getOrderPlaced(orderPlacedModel)
            if (result.code() == 200 && result.body() != null) {
                orderPlaceLiveData.postValue(Response.Success(result.body()))
            } else {
                orderPlaceLiveData.postValue(
                    Response.Error(getApplication<Application>().getString(
                        R.string.server_error
                    ))
                )
            }
        } else {
            orderPlaceLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun getInsertOnlineTransaction(paymentReq: PaymentReq?, mSectionType:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            insertOnlineTransactionLiveData.postValue(Response.Loading())
            val result = repository.getInsertOnlineTransaction(paymentReq,mSectionType)
            if (result.code() == 200 && result.body() != null) {
                insertOnlineTransactionLiveData.postValue(Response.Success(result.body()))
            } else {
                insertOnlineTransactionLiveData.postValue(
                    Response.Error(getApplication<Application>().getString(
                        R.string.server_error
                    ))
                )
            }
        } else {
            insertOnlineTransactionLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun getUpdateOnlineTransaction(paymentReq: PaymentReq?, mSectionType:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            updateOnlineTransactionLiveData.postValue(Response.Loading())
            val result = repository.getUpdateOnlineTransaction(paymentReq,mSectionType)
            if (result.code() == 200 && result.body() != null) {
                updateOnlineTransactionLiveData.postValue(Response.Success(result.body()))
            } else {
                updateOnlineTransactionLiveData.postValue(
                    Response.Error(getApplication<Application>().getString(
                        R.string.server_error
                    ))
                )
            }
        } else {
            updateOnlineTransactionLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun updateDeliveryETA(jsonObject: JsonObject, mSectionType:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.updateDeliveryETA(jsonObject,mSectionType)
            updateDeliveryETALiveData.postValue(result.body())
        }
    }

    fun isGullakOptionEnable(custId: Int,orderId: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.isGullakOptionEnable(custId,orderId)
            isGullakOptionEnableLiveData.postValue(result.body())
        }
    }


    fun getICICIPaymentCheck(url: String,merchantId:String,merchantTxnNo:String,originalTxnNo:String,transactionType:String,secureHash:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            iciciPaymentCheckLiveData.postValue(Response.Loading())
            val result = repository.iciciPaymentCheck(url,merchantId, merchantTxnNo, originalTxnNo, transactionType, secureHash)
            if (result.code() == 200 && result.body() != null) {
                iciciPaymentCheckLiveData.postValue(Response.Success(result.body()))
            } else {
                iciciPaymentCheckLiveData.postValue(
                    Response.Error(getApplication<Application>().getString(
                        R.string.server_error
                    ))
                )
            }
        } else {
            iciciPaymentCheckLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

}