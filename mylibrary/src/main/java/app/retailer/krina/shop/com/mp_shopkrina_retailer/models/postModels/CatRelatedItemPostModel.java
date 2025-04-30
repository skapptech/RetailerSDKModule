package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CatRelatedItemPostModel {

    @SerializedName("warehouseId")
    int warehouseId;

    @SerializedName("customerId")
    int customerId;

    @SerializedName("skip")
    int skip ;

    @SerializedName("take")
    int take  ;

    @SerializedName("itemIds")
    private ArrayList<Integer> itemIds;

    @SerializedName("lang")
    String lang;

    public CatRelatedItemPostModel(int warehouseId, int customerId, ArrayList<Integer> itemIds, String lang,int skip,int take) {
        this.warehouseId = warehouseId;
        this.customerId = customerId;
        this.itemIds = itemIds;
        this.lang = lang;
        this.skip = skip;
        this.take = take;
    }

}
