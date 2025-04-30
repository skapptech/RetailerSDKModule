package app.retailer.krina.shop.com.mp_shopkrina_retailer.epaylater;

import android.os.Parcel;
import android.os.Parcelable;

public final class PaymentResponse implements Parcelable {

    private int a;
    private String b;

    public static final Creator<PaymentResponse> CREATOR = new Creator<PaymentResponse>() {
        @Override
        public PaymentResponse createFromParcel(Parcel parcel) {
            return new PaymentResponse(parcel);
        }

        @Override
        public PaymentResponse[] newArray(int i) {
            return new PaymentResponse[2];
        }

        public PaymentResponse a(Parcel var1) {
            return new PaymentResponse(var1);
        }

        public PaymentResponse[] a(int var1) {
            return new PaymentResponse[var1];
        }
    };

    PaymentResponse(int var1, String var2) {
        this.a = var1;
        this.b = var2;
    }

    private PaymentResponse(Parcel var1) {
        this.a = var1.readInt();
        this.b = var1.readString();
    }

    public int getStatus() {
        return this.a;
    }

    public String getMessage() {
        return this.b;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel var1, int var2) {
        var1.writeInt(this.a);
        var1.writeString(this.b);
    }

    public static class Status {
        public static final int SUCCESS = 200;
        public static final int FAILURE = 400;
        public static final int ABNORMAL_EXIT = 600;
        public static final int INVALID_INPUTS = 800;

        public Status() {
        }
    }
}