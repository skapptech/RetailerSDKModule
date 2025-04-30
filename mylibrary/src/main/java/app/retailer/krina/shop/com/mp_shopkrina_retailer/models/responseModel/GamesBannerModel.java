package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GamesBannerModel implements Serializable {
    @SerializedName("Id")
    private String Id;
    @SerializedName("BannerImageUrl")
    private String BannerImageUrl;
    @SerializedName("BannerType")
    private String BannerType;
    @SerializedName("ObjectId")
    private String ObjectId;
    @SerializedName("IsActive")
    private boolean IsActive;
    @SerializedName("IsDeleted")
    private boolean IsDeleted;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBannerImageUrl() {
        return BannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        BannerImageUrl = bannerImageUrl;
    }

    public String getBannerType() {
        return BannerType;
    }

    public void setBannerType(String bannerType) {
        BannerType = bannerType;
    }

    public String getObjectId() {
        return ObjectId;
    }

    public void setObjectId(String objectId) {
        ObjectId = objectId;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }
}
