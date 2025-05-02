package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReferredModel implements Parcelable {
    public String SkCode;
    public String ReferralSkCode;
    public double ReferralWalletPoint;
    public double CustomerWalletPoint;
    public int OnOrder;
    public int OrderId;
    public String ShopName;
    public String Status;
    public int IsUsed;

    public String getSkCode() {
        return SkCode;
    }

    public String getReferralSkCode() {
        return ReferralSkCode;
    }

    public double getReferralWalletPoint() {
        return ReferralWalletPoint;
    }

    public double getCustomerWalletPoint() {
        return CustomerWalletPoint;
    }

    public int getOnOrder() {
        return OnOrder;
    }

    public int getOrderId() {
        return OrderId;
    }

    public String getShopName() {
        return ShopName;
    }

    public String getStatus() {
        return Status;
    }

    public int isUsed() {
        return IsUsed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SkCode);
        dest.writeString(ReferralSkCode);
        dest.writeDouble(ReferralWalletPoint);
        dest.writeDouble(CustomerWalletPoint);
        dest.writeInt(OnOrder);
        dest.writeInt(OrderId);
        dest.writeString(ShopName);
        dest.writeString(Status);
        dest.writeInt(IsUsed);
    }

    protected ReferredModel(Parcel in) {
        SkCode = in.readString();
        ReferralSkCode = in.readString();
        ReferralWalletPoint = in.readDouble();
        CustomerWalletPoint = in.readDouble();
        OnOrder = in.readInt();
        OrderId = in.readInt();
        ShopName = in.readString();
        Status = in.readString();
        IsUsed = in.readInt();
    }


    public static final Creator<ReferredModel> CREATOR = new Creator<ReferredModel>() {
        @Override
        public ReferredModel createFromParcel(Parcel in) {
            return new ReferredModel(in);
        }

        @Override
        public ReferredModel[] newArray(int size) {
            return new ReferredModel[size];
        }
    };
}