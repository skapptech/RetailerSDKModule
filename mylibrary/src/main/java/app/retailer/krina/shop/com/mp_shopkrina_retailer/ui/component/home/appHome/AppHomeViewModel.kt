package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.GameBannerModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeOfferFlashDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.store.StoreItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemGoldenDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppHomeViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val getAppHomeSectionLiveData = SingleLiveEvent<Response<ArrayList<HomeDataModel>>>()
    val getAppHomeSectionData: SingleLiveEvent<Response<ArrayList<HomeDataModel>>>
        get() = getAppHomeSectionLiveData

    private val getDynamicHtmlLiveData = SingleLiveEvent<Response<String>>()
    val getDynamicHtmlData: SingleLiveEvent<Response<String>>
        get() = getDynamicHtmlLiveData


    private val getItemBySectionLiveData = SingleLiveEvent<Response<JsonObject>>()
    val getItemBySectionData: SingleLiveEvent<Response<JsonObject>>
        get() = getItemBySectionLiveData

    private val getOtherItemsLiveData = SingleLiveEvent<Response<AppHomeItemGoldenDealModel>>()
    val getOtherItemsData: SingleLiveEvent<Response<AppHomeItemGoldenDealModel>>
        get() = getOtherItemsLiveData

    private val getAllStoreLiveData = SingleLiveEvent<Response<ArrayList<StoreItemModel>>>()
    val getAllStoreData: SingleLiveEvent<Response<ArrayList<StoreItemModel>>>
        get() = getAllStoreLiveData

    private val getGameBannersLiveData = SingleLiveEvent<Response<GameBannerModel>>()
    val getGameBannersData: SingleLiveEvent<Response<GameBannerModel>>
        get() = getGameBannersLiveData

    private val getFlashDealItemLiveData = SingleLiveEvent<Response<HomeOfferFlashDealModel>>()
    val getFlashDealItemData: SingleLiveEvent<Response<HomeOfferFlashDealModel>>
        get() = getFlashDealItemLiveData


    private val getSubCategoryOfferLiveData = SingleLiveEvent<Response<BillDiscountListResponse>>()
    val getSubCategoryOfferData: SingleLiveEvent<Response<BillDiscountListResponse>>
        get() = getSubCategoryOfferLiveData

    private val getStoreHomeLiveData = SingleLiveEvent<Response<ArrayList<HomeDataModel>>>()
    val getStoreHomeData: SingleLiveEvent<Response<ArrayList<HomeDataModel>>>
        get() = getStoreHomeLiveData


    private val getSubCategoryLiveData = SingleLiveEvent<Response<JsonObject>>()
    val getSubCategoryData: SingleLiveEvent<Response<JsonObject>>
        get() = getSubCategoryLiveData

    private val getFlashDealExistsTimeLiveData = SingleLiveEvent<Response<JsonObject>>()
    val getFlashDealExistsTimeData: SingleLiveEvent<Response<JsonObject>>
        get() = getFlashDealExistsTimeLiveData

    private val getFlashDealBannerImageLiveData = SingleLiveEvent<String>()
    val getFlashDealBannerImageData: SingleLiveEvent<String>
        get() = getFlashDealBannerImageLiveData
    fun getAppHomeSection(
        appType: String?,
        customerId: Int,
        wId: Int,
        lang: String?,
        lat: Double,
        lng: Double
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getAppHomeSectionLiveData.postValue(Response.Loading())
                val result = repository.getAppHomeSection(appType, customerId, wId, lang, lat, lng)
                if (result.body() != null && result.body()!!.size != 0) {
                    getAppHomeSectionLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    getAppHomeSectionLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.server_error
                            )
                        )
                    )
                }
            } else {
                getAppHomeSectionLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

   /* fun getDynamicHtm(url: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getDynamicHtmlLiveData.postValue(Response.Loading())
                val result = repository.getDynamicHtml(url)
                if (result!!.body() != null) {
                    getDynamicHtmlLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    getDynamicHtmlLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.server_error
                            )
                        )
                    )
                }
            } else {
                getDynamicHtmlLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }*/


    /*fun getItemBySection(customerId: Int, warehouseId: Int, sectionId: String, lang: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getItemBySectionLiveData.postValue(Response.Loading())
                val result = repository.getItemBySection(customerId, warehouseId, sectionId, lang)
                if (result?.body() != null) {
                    getItemBySectionLiveData.postValue(Response.Success(result.body()))
                } else {
                    getItemBySectionLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                getItemBySectionLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }*/

    fun getOtherItemsHome(url: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getOtherItemsLiveData.postValue(Response.Loading())
                val result = repository.getOtherItemsHome(url)
                if (result?.body() != null && result?.body()!!.itemListModels!!.size != 0) {
                    getOtherItemsLiveData.postValue(Response.Success(result.body()))
                } else {
                    getOtherItemsLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                getOtherItemsLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

   /* fun getAllStore(customerId: Int, warehouseId: Int, lang: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getAllStoreLiveData.postValue(Response.Loading())
                val result = repository.getAllStore(customerId, warehouseId, lang)
                if (result?.body() != null && result.body()!!.size != 0) {
                    getAllStoreLiveData.postValue(Response.Success(result.body()))
                } else {
                    getAllStoreLiveData.postValue(
                        Response.Error(MyApplication.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                getAllStoreLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }*/

    fun getGameBanners(customerId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getGameBannersLiveData.postValue(Response.Loading())
                val result = repository.getGameBanners(customerId)
                if (result?.body() != null) {
                    getGameBannersLiveData.postValue(Response.Success(result.body()))
                } else {
                    getGameBannersLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                getGameBannersLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getFlashDealItem(
        customerId: Int,
        wId: Int,
        sectionId: String?,
        lang: String?, mSectionType: String
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getFlashDealItemLiveData.postValue(Response.Loading())
                val result =
                    repository.getFlashDealItem(customerId, wId, sectionId, lang, mSectionType)
                if (result?.body() != null && result.body()!!.isStatus && result.body()!!.itemListModels!!.size != 0) {
                    getFlashDealItemLiveData.postValue(Response.Success(result.body()))
                } else {
                    getFlashDealItemLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                getFlashDealItemLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }


    fun getStoreFlashDeal(
        customerId: Int,
        wId: Int,
        sectionId: String?,
        subCategoryId:Int,
        lang: String?, mSectionType: String
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getFlashDealItemLiveData.postValue(Response.Loading())
                val result =
                    repository.getStoreFlashDeal(customerId, wId, sectionId,subCategoryId, lang, mSectionType)
                if (result?.body() != null && result.body()!!.isStatus && result.body()!!.itemListModels!!.size != 0) {
                    getFlashDealItemLiveData.postValue(Response.Success(result.body()))
                } else {
                    getFlashDealItemLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                getFlashDealItemLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }
    fun getSubCateOffer(
        customerId: Int,
        subCategoryId:Int
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getSubCategoryOfferLiveData.postValue(Response.Loading())
                val result =
                    repository.getSubCateOffer(customerId,subCategoryId)
                if (result?.body() != null && result.body()!!.isStatus && result.body()!!.billDiscountList!!.size != 0) {
                    getSubCategoryOfferLiveData.postValue(Response.Success(result.body()))
                } else {
                    getSubCategoryOfferLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                getSubCategoryOfferLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun storeDashboard(
        custId: Int,
        wId: Int,
        subCatId: Int,
        lang: String?
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getStoreHomeLiveData.postValue(Response.Loading())
                val result =
                    repository.storeDashboard( custId,wId,subCatId,lang)
                if (result.body() != null && result.body()!!.size != 0) {
                    getStoreHomeLiveData.postValue(Response.Success(result.body()))
                } else {
                    getStoreHomeLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                getStoreHomeLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getSubCategoryData(
        custId: Int,
        wId: Int,
        subCatId: Int,
        lang: String?
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getSubCategoryLiveData.postValue(Response.Loading())
                val result =
                    repository.getSubCategoryData( custId,wId,subCatId,lang)
                if (result.body() != null) {
                    getSubCategoryLiveData.postValue(Response.Success(result.body()))
                } else {
                    getSubCategoryLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                getSubCategoryLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getFlashDealExistsTime(
        custId: Int,
        wId: Int,
        mSectionType: String?
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getFlashDealExistsTimeLiveData.postValue(Response.Loading())
                val result =
                    repository.getFlashDealExistsTime(custId,wId,mSectionType)
                if (result.body() != null) {
                    getFlashDealExistsTimeLiveData.postValue(Response.Success(result.body()))
                } else {
                    getFlashDealExistsTimeLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                getFlashDealExistsTimeLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getFlashDealBannerImage(
        custId: Int,
        wId: Int
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.getFlashDealBannerImage(custId,wId)
                getFlashDealBannerImageLiveData.postValue(result.body())
            }
        }
    fun getMoqList(itemList: ArrayList<ItemListModel>): ArrayList<ItemListModel> {
        val list = ArrayList<ItemListModel>()
        if (itemList.size != 0) {
            for (i in itemList.indices) {
                var ispresent = false
                for (j in list.indices) {
                    if (list[j].itemNumber.equals(
                            itemList[i].itemNumber,
                            ignoreCase = true
                        )
                    ) {
                        ispresent = true
                        if (list[j].moqList.size == 0) {
                            list[j].moqList.add(list[j])
                            list[j].moqList[0].isChecked = true
                        }
                        list[j].moqList.add(itemList[i])
                        break
                    }
                }
                if (!ispresent) {
                    list.add(itemList[i])
                }
            }
        }
        return list
    }

}