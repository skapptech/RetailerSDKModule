package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak

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

class AddPaymentViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val getUdharCreditLimitLiveData = SingleLiveEvent<Response<CreditLimit>>()
    val getUdharCreditLimitData: SingleLiveEvent<Response<CreditLimit>>
        get() = getUdharCreditLimitLiveData

    private val scaleUpLimitLiveData = SingleLiveEvent<Response<CreditLimit>>()
    val getScaleUpLimitData: SingleLiveEvent<Response<CreditLimit>>
        get() = scaleUpLimitLiveData

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


    private val insertOnlineTransactionLiveData = SingleLiveEvent<Response<Boolean>>()
    val getInsertOnlineTransactionData: SingleLiveEvent<Response<Boolean>>
        get() = insertOnlineTransactionLiveData

    private val updateOnlineTransactionLiveData = SingleLiveEvent<Response<Boolean>>()
    val getUpdateOnlineTransactionData: SingleLiveEvent<Response<Boolean>>
        get() = updateOnlineTransactionLiveData

    private val iciciPaymentCheckLiveData = SingleLiveEvent<Response<JsonObject>>()
    val getICICIPaymentCheckData: SingleLiveEvent<Response<JsonObject>>
        get() = iciciPaymentCheckLiveData

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

    fun getHDFCRSAKey(
        hdfcOrderId: String?,
        amount: String?,
        isCredit: Boolean,
        mSectionType: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.getHDFCRSAKey(hdfcOrderId, amount, isCredit, mSectionType)
            hDFCRSAKeyLiveData.postValue(result.body())
        }
    }

    fun fetchRazorpayOrderId(orderId: String, amount: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.fetchRazorpayOrderId(orderId, amount)
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
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.server_error
                        )
                    )
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

    fun scaleUpPaymentInitiate(custId: Int, orderId: Int, amount: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                scaleUpPaymentInitiateLiveData.postValue(Response.Loading())
                val result = repository.scaleUpPaymentInitiate(custId, orderId, amount)
                if (result.code() == 200 && result.body() != null) {
                    scaleUpPaymentInitiateLiveData.postValue(Response.Success(result.body()))
                } else {
                    scaleUpPaymentInitiateLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.server_error
                            )
                        )
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

    fun getInsertOnlineTransaction(paymentReq: PaymentReq?, mSectionType: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                insertOnlineTransactionLiveData.postValue(Response.Loading())
                val result = repository.getInsertOnlineTransaction(paymentReq, mSectionType)
                if (result.code() == 200 && result.body() != null) {
                    insertOnlineTransactionLiveData.postValue(Response.Success(result.body()))
                } else {
                    insertOnlineTransactionLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.server_error
                            )
                        )
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

    fun getUpdateOnlineTransaction(paymentReq: PaymentReq?, mSectionType: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                updateOnlineTransactionLiveData.postValue(Response.Loading())
                val result = repository.getUpdateOnlineTransaction(paymentReq, mSectionType)
                if (result.code() == 200 && result.body() != null) {
                    updateOnlineTransactionLiveData.postValue(Response.Success(result.body()))
                } else {
                    updateOnlineTransactionLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.server_error
                            )
                        )
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

    fun getICICIPaymentCheck(
        url: String,
        merchantId: String,
        merchantTxnNo: String,
        originalTxnNo: String,
        transactionType: String,
        secureHash: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            iciciPaymentCheckLiveData.postValue(Response.Loading())
            val result = repository.iciciPaymentCheck(
                url,
                merchantId,
                merchantTxnNo,
                originalTxnNo,
                transactionType,
                secureHash
            )
            if (result.code() == 200 && result.body() != null) {
                iciciPaymentCheckLiveData.postValue(Response.Success(result.body()))
            } else {
                iciciPaymentCheckLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.server_error
                        )
                    )
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