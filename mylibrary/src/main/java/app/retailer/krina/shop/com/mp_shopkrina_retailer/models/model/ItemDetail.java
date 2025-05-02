package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class ItemDetail {
    @SerializedName("ItemId")
    String ItemId;
    @SerializedName("UnitId")
    String UnitId;
    @SerializedName("Categoryid")
    String Categoryid;
    @SerializedName("BaseCategoryId")
    String BaseCategoryId;
    @SerializedName("SubCategoryId")
    String SubCategoryId;
    @SerializedName("SubsubCategoryid")
    String SubsubCategoryid;
    @SerializedName("itemname")
    String itemname;
    @SerializedName("UnitName")
    String UnitName;
    @SerializedName("PurchaseUnitName")
    String PurchaseUnitName;
    @SerializedName("price")
    String price;
    @SerializedName("SellingUnitName")
    String SellingUnitName;
    @SerializedName("SellingSku")
    String SellingSku;
    @SerializedName("UnitPrice")
    String UnitPrice;
    @SerializedName("VATTax")
    String VATTax;
    @SerializedName("LogoUrl")
    String LogoUrl;
    @SerializedName("MinOrderQty")
    String MinOrderQty;
    @SerializedName("Discount")
    String Discount;
    @SerializedName("TotalTaxPercentage")
    String TotalTaxPercentage;
    @SerializedName("HindiName")
    String HindiName;
    @SerializedName("dreamPoint")
    String dreamPoint;
    @SerializedName("promoPerItems")
    String promoPerItems;
    @SerializedName("marginPoint")
    String marginPoint;
    @SerializedName("WarehouseId")
    String warehouseId;
    @SerializedName("CompanyId")
    String companyId;
    @SerializedName("ItemNumber")
    String itemNumber;

    @SerializedName("IsOffer")
    boolean IsOffer;

    @SerializedName("Deleted")
    boolean Deleted;

    @SerializedName("IsItemLimit")
    boolean IsItemLimit;

    @SerializedName("ItemlimitQty")
    int ItemlimitQty;


    @SerializedName("OfferFreeItemName")
    String OfferFreeItemName;

    @SerializedName("OfferFreeItemImage")
    String OfferFreeItemImage;

    @SerializedName("OfferFreeItemQuantity")
    String OfferFreeItemQuantity;

    @SerializedName("OfferId")
    int OfferId;

    @SerializedName("OfferWalletPoint")
    Double OfferWalletPoint;

    @SerializedName("OfferCategory")
    int OfferCategory;

    @SerializedName("OfferMinimumQty")
    int OfferMinimumQty;

    @SerializedName("OfferFreeItemId")
    int OfferFreeItemId;

    @SerializedName("OfferType")
    String OfferType;

    @SerializedName("OfferStartTime")
    String OfferStartTime;

    @SerializedName("OfferEndTime")
    String OfferEndTime;

    @SerializedName("OfferQtyAvaiable")
    int OfferQtyAvaiable;

    @SerializedName("OfferQtyConsumed")
    String OfferQtyConsumed;

    @SerializedName("FlashDealSpecialPrice")
    String FlashDealSpecialPrice;

    @SerializedName("FlashDealMaxQtyPersonCanTake")
    int FlashDealMaxQtyPersonCanTake;

    @SerializedName("ItemMultiMRPId")
    int ItemMultiMRPId;

    @SerializedName("BillLimitQty")
    int BillLimitQty;




    public boolean isItemLimit() {
        return IsItemLimit;
    }

    public void setItemLimit(boolean itemLimit) {
        IsItemLimit = itemLimit;
    }

    public int getItemlimitQty() {
        return ItemlimitQty;
    }

    public void setItemlimitQty(int itemlimitQty) {
        ItemlimitQty = itemlimitQty;
    }

    public String getFlashDealSpecialPrice() {
        return FlashDealSpecialPrice;
    }

    public void setFlashDealSpecialPrice(String flashDealSpecialPrice) {
        FlashDealSpecialPrice = flashDealSpecialPrice;
    }

    public int getFlashDealMaxQtyPersonCanTake() {
        return FlashDealMaxQtyPersonCanTake;
    }

    public void setFlashDealMaxQtyPersonCanTake(int flashDealMaxQtyPersonCanTake) {
        FlashDealMaxQtyPersonCanTake = flashDealMaxQtyPersonCanTake;
    }

    public int getOfferQtyAvaiable() {
        return OfferQtyAvaiable;
    }

    public void setOfferQtyAvaiable(int offerQtyAvaiable) {
        OfferQtyAvaiable = offerQtyAvaiable;
    }

    public String getOfferQtyConsumed() {
        return OfferQtyConsumed;
    }

    public void setOfferQtyConsumed(String offerQtyConsumed) {
        OfferQtyConsumed = offerQtyConsumed;
    }

    public String getOfferStartTime() {
        return OfferStartTime;
    }

    public void setOfferStartTime(String offerStartTime) {
        OfferStartTime = offerStartTime;
    }

    public String getOfferEndTime() {
        return OfferEndTime;
    }

    public void setOfferEndTime(String offerEndTime) {
        OfferEndTime = offerEndTime;
    }


    public String getOfferType() {
        return OfferType;
    }

    public void setOfferType(String offerType) {
        OfferType = offerType;
    }

    public int getOfferMinimumQty() {
        return OfferMinimumQty;
    }

    public void setOfferMinimumQty(int offerMinimumQty) {
        OfferMinimumQty = offerMinimumQty;
    }

    public int getOfferId() {
        return OfferId;
    }

    public void setOfferId(int offerId) {
        OfferId = offerId;
    }

    public Double getOfferWalletPoint() {
        return OfferWalletPoint;
    }

    public void setOfferWalletPoint(Double offerWalletPoint) {
        OfferWalletPoint = offerWalletPoint;
    }

    public int getOfferCategory() {
        return OfferCategory;
    }

    public void setOfferCategory(int offerCategory) {
        OfferCategory = offerCategory;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public String getOfferFreeItemName() {
        return OfferFreeItemName;
    }

    public void setOfferFreeItemName(String offerFreeItemName) {
        OfferFreeItemName = offerFreeItemName;
    }

    public String getOfferFreeItemImage() {
        return OfferFreeItemImage;
    }

    public void setOfferFreeItemImage(String offerFreeItemImage) {
        OfferFreeItemImage = offerFreeItemImage;
    }

    public String getOfferFreeItemQuantity() {
        return OfferFreeItemQuantity;
    }

    public void setOfferFreeItemQuantity(String offerFreeItemQuantity) {
        OfferFreeItemQuantity = offerFreeItemQuantity;
    }

    public boolean isIsoffer() {
        return IsOffer;
    }

    public void setIsoffer(boolean isoffer) {
        this.IsOffer = isoffer;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getHindiName() {
        return HindiName;
    }

    public void setHindiName(String hindiName) {
        HindiName = hindiName;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }

    public String getCategoryid() {
        return Categoryid;
    }

    public void setCategoryid(String categoryid) {
        Categoryid = categoryid;
    }

    public String getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public String getSubsubCategoryid() {
        return SubsubCategoryid;
    }


    public String getDreamPoint() {
        return dreamPoint;
    }

    public void setDreamPoint(String dreamPoint) {
        this.dreamPoint = dreamPoint;
    }

    public String getPromoPoint() {
        return promoPerItems;
    }

    public void setPromoPoint(String promoPoint) {
        this.promoPerItems = promoPoint;
    }

    public String getMarginPoint() {
        return marginPoint;
    }

    public void setMarginPoint(String marginPoint) {
        this.marginPoint = marginPoint;
    }


    public void setSubsubCategoryid(String subsubCategoryid) {
        SubsubCategoryid = subsubCategoryid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getPurchaseUnitName() {
        return PurchaseUnitName;
    }

    public void setPurchaseUnitName(String purchaseUnitName) {
        PurchaseUnitName = purchaseUnitName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSellingUnitName() {
        return SellingUnitName;
    }

    public void setSellingUnitName(String sellingUnitName) {
        SellingUnitName = sellingUnitName;
    }

    public String getSellingSku() {
        return SellingSku;
    }

    public void setSellingSku(String sellingSku) {
        SellingSku = sellingSku;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getVATTax() {
        return VATTax;
    }

    public void setVATTax(String VATTax) {
        this.VATTax = VATTax;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }

    public String getMinOrderQty() {
        return MinOrderQty;
    }

    public void setMinOrderQty(String minOrderQty) {
        MinOrderQty = minOrderQty;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getTotalTaxPercentage() {
        return TotalTaxPercentage;
    }

    public void setTotalTaxPercentage(String totalTaxPercentage) {
        TotalTaxPercentage = totalTaxPercentage;
    }

    public String toString() {
        return itemNumber;
    }


    public int getOfferFreeItemId() {
        return OfferFreeItemId;
    }

    public void setOfferFreeItemId(int offerFreeItemId) {
        OfferFreeItemId = offerFreeItemId;
    }
    public String getBaseCategoryId() {
        return BaseCategoryId;
    }

    public void setBaseCategoryId(String baseCategoryId) {
        BaseCategoryId = baseCategoryId;
    }

    public int getItemMultiMRPId() {
        return ItemMultiMRPId;
    }

    public int getBillLimitQty() {
        return BillLimitQty;
    }

    public void setBillLimitQty(int billLimitQty) {
        BillLimitQty = billLimitQty;
    }

    public void setItemMultiMRPId(int itemMultiMRPId) {
        ItemMultiMRPId = itemMultiMRPId;
    }
}
