package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnOrderListModel implements Parcelable {
    @Expose
    @SerializedName("RequestType")
    private int requestType;
    @Expose
    @SerializedName("Status")
    private String status;
    @Expose
    @SerializedName("ModifiedDate")
    private String modifiedDate;
    @Expose
    @SerializedName("OrderId")
    private int orderId;
    @Expose
    @SerializedName("KKRequstId")
    private int KKRequestId;

    private ReturnOrderListModel(Parcel in) {
        requestType = in.readInt();
        status = in.readString();
        modifiedDate = in.readString();
        orderId = in.readInt();
        KKRequestId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(requestType);
        dest.writeString(status);
        dest.writeString(modifiedDate);
        dest.writeInt(orderId);
        dest.writeInt(KKRequestId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReturnOrderListModel> CREATOR = new Creator<ReturnOrderListModel>() {
        @Override
        public ReturnOrderListModel createFromParcel(Parcel in) {
            return new ReturnOrderListModel(in);
        }

        @Override
        public ReturnOrderListModel[] newArray(int size) {
            return new ReturnOrderListModel[size];
        }
    };

    public int getRequestType() {
        return requestType;
    }

    public String getStatus() {
        return status;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getKKRequestId() {
        return KKRequestId;
    }
}