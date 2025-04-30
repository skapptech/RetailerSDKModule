package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class GameModel {
    @SerializedName("ModifiedBy")
    private String ModifiedBy;
    @SerializedName("CreatedBy")
    private int CreatedBy;
    @SerializedName("IsDeleted")
    private boolean IsDeleted;
    @SerializedName("IsActive")
    private boolean IsActive;
    @SerializedName("ModifiedDate")
    private String ModifiedDate;
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @SerializedName("WalletPointOnGameOver")
    private boolean WalletPointOnGameOver;
    @SerializedName("WalletPointOnPlay")
    private boolean WalletPointOnPlay;
    @SerializedName("GameLogo")
    private String GameLogo;
    @SerializedName("GameUrl")
    private String GameUrl;
    @SerializedName("GameName")
    private String GameName;
    @SerializedName("Id")
    private String id;


    public String getModifiedBy() {
        return ModifiedBy;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public boolean getIsDeleted() {
        return IsDeleted;
    }

    public boolean getIsActive() {
        return IsActive;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public boolean getWalletPointOnGameOver() {
        return WalletPointOnGameOver;
    }

    public boolean getWalletPointOnPlay() {
        return WalletPointOnPlay;
    }

    public String getGameLogo() {
        return GameLogo;
    }

    public String getGameUrl() {
        return GameUrl;
    }

    public String getGameName() {
        return GameName;
    }

    public String getId() {
        return id;
    }
}
