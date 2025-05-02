package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class MyDreamModel {
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("CreateDate")
    private String createDate;
    @SerializedName("ImageUrl")
    private String imageUrl;
    @SerializedName("Description")
    private String description;
    @SerializedName("rItem")
    private String rItem;
    @SerializedName("rPoint")
    private int rPoint;
    @SerializedName("rName")
    private String rName;
    @SerializedName("WarehouseId")
    private int WarehouseId;
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("rItemId")
    private int rItemId;
    public int qty;

    public boolean isDeleted() {
        return isDeleted;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getrItem() {
        return rItem;
    }

    public int getrPoint() {
        return rPoint;
    }

    public String getrName() {
        return rName;
    }

    public int getWarehouseId() {
        return WarehouseId;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public int getrItemId() {
        return rItemId;
    }
}