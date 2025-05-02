package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class PostOfferItemsList {

    @SerializedName("ItemId")
    public int ItemId ;

    @SerializedName("IsOffer")
    public boolean IsOffer;

    @SerializedName("OfferCategory")
    public int OfferCategory;

    @SerializedName("OfferType")
    public String OfferType;

    @SerializedName("OfferQtyAvaiable")
    public int OfferQtyAvaiable;

    public PostOfferItemsList(int itemId, int offerCategory, boolean isOffer) {
        ItemId = itemId;
        IsOffer = isOffer;
        OfferCategory = offerCategory;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public boolean isOffer() {
        return IsOffer;
    }

    public void setOffer(boolean offer) {
        IsOffer = offer;
    }

    public int getOfferCategory() {
        return OfferCategory;
    }

    public void setOfferCategory(int offerCategory) {
        OfferCategory = offerCategory;
    }

    public String getOfferType() {
        return OfferType;
    }

    public void setOfferType(String offerType) {
        OfferType = offerType;
    }

    public int getOfferQtyAvaiable() {
        return OfferQtyAvaiable;
    }

    public void setOfferQtyAvaiable(int offerQtyAvaiable) {
        OfferQtyAvaiable = offerQtyAvaiable;
    }
}
