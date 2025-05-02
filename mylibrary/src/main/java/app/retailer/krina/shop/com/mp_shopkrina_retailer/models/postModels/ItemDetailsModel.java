package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ItemDetailsModel implements Parcelable {
    @SerializedName("ItemId")
    int ItemId;
    @SerializedName("qty")
    int qty;
    @SerializedName("UnitPrice")
    String unitPrice;
    @SerializedName("WarehouseId")
    int WarehouseId;
    @SerializedName("CompanyId")
    int CompanyId;
    @SerializedName("OfferId")
    int OfferId;
    @SerializedName("OfferWalletPoint")
    Double OfferWalletPoint;
    @SerializedName("OfferCategory")
    int OfferCategory;
    @SerializedName("FreeItemqty")
    int FreeItemqty;
    @SerializedName("IsOffer")
    boolean IsOffer;
    @SerializedName("FreeItemId")
    int FreeItemId;
    @SerializedName("IsPrimeItem")
    boolean isPrimeItem;


    public ItemDetailsModel(int itemId, int qty, int warehouseId, int companyId, int offerId, Double offerWalletPoint, int offerCategory, int freeItemqty, boolean isOffer, int freeItemId, String UnitPrice, boolean isPrimeItem) {
        ItemId = itemId;
        this.qty = qty;
        WarehouseId = warehouseId;
        CompanyId = companyId;
        OfferId = offerId;
        OfferWalletPoint = offerWalletPoint;
        OfferCategory = offerCategory;
        FreeItemqty = freeItemqty;
        IsOffer = isOffer;
        FreeItemId = freeItemId;
        unitPrice = UnitPrice;
        this.isPrimeItem = isPrimeItem;
    }


    protected ItemDetailsModel(Parcel in) {
        ItemId = in.readInt();
        qty = in.readInt();
        unitPrice = in.readString();
        WarehouseId = in.readInt();
        CompanyId = in.readInt();
        OfferId = in.readInt();
        if (in.readByte() == 0) {
            OfferWalletPoint = null;
        } else {
            OfferWalletPoint = in.readDouble();
        }
        OfferCategory = in.readInt();
        FreeItemqty = in.readInt();
        IsOffer = in.readByte() != 0;
        FreeItemId = in.readInt();
        isPrimeItem = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ItemId);
        dest.writeInt(qty);
        dest.writeString(unitPrice);
        dest.writeInt(WarehouseId);
        dest.writeInt(CompanyId);
        dest.writeInt(OfferId);
        if (OfferWalletPoint == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(OfferWalletPoint);
        }
        dest.writeInt(OfferCategory);
        dest.writeInt(FreeItemqty);
        dest.writeByte((byte) (IsOffer ? 1 : 0));
        dest.writeInt(FreeItemId);
        dest.writeByte((byte) (isPrimeItem ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemDetailsModel> CREATOR = new Creator<ItemDetailsModel>() {
        @Override
        public ItemDetailsModel createFromParcel(Parcel in) {
            return new ItemDetailsModel(in);
        }

        @Override
        public ItemDetailsModel[] newArray(int size) {
            return new ItemDetailsModel[size];
        }
    };

    public int getItemId() {
        return ItemId;
    }

    public int getQty() {
        return qty;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public int getWarehouseId() {
        return WarehouseId;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public int getOfferId() {
        return OfferId;
    }

    public Double getOfferWalletPoint() {
        return OfferWalletPoint;
    }

    public int getOfferCategory() {
        return OfferCategory;
    }

    public int getFreeItemqty() {
        return FreeItemqty;
    }

    public boolean isOffer() {
        return IsOffer;
    }

    public int getFreeItemId() {
        return FreeItemId;
    }

    public boolean isPrimeItem() {
        return isPrimeItem;
    }
}