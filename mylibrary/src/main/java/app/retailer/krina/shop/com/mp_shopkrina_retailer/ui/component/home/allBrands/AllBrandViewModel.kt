package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubCatImageModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AllBrandsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllBrandViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val allBrandsLiveData = SingleLiveEvent<Response<ArrayList<AllBrandsModel>>>()
    val getAllBrandsData: SingleLiveEvent<Response<ArrayList<AllBrandsModel>>>
        get() = allBrandsLiveData

    private val brandItemLiveData = SingleLiveEvent<Response<ItemListResponse>>()
    val getBrandItemData: SingleLiveEvent<Response<ItemListResponse>>
        get() = brandItemLiveData

    private val categoryImageLiveData = SingleLiveEvent<Response<ArrayList<SubCatImageModel>>>()
    val getCategoryImageData: SingleLiveEvent<Response<ArrayList<SubCatImageModel>>>
        get() = categoryImageLiveData


    fun getAllBrands(
        customerId: Int,
        wId: Int,
        lang: String?
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                allBrandsLiveData.postValue(Response.Loading())
                val result = repository.getAllBrands(customerId, wId, lang!!)
                if (result.body() != null && result.body()!!.size != 0) {
                    allBrandsLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    allBrandsLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.server_error
                            )
                        )
                    )
                }
            } else {
                allBrandsLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getBrandItem(
        customerId: Int,
        wId: Int,
        subSubCatId: Int,
        lang: String?
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                brandItemLiveData.postValue(Response.Loading())
                val result = repository.getBrandItem(customerId, wId,subSubCatId, lang!!)
                if (result.body() != null && result.body()?.isStatus!! && result.body()?.itemMasters!!.size!=0) {
                    brandItemLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    brandItemLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.item_not_found
                            )
                        )
                    )
                }
            } else {
                brandItemLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getImage(
        categoryId: Int
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                categoryImageLiveData.postValue(Response.Loading())
                val result = repository.getImage(categoryId)
                if (result.body() != null && result.body()?.size!=0) {
                    categoryImageLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    categoryImageLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.server_error
                            )
                        )
                    )
                }
            } else {
                categoryImageLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
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