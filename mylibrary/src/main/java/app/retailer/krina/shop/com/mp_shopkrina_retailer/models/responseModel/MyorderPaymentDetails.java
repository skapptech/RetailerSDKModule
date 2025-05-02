package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyorderPaymentDetails implements Serializable {
    @SerializedName("TransactionDate")
    private String TransactionDate;
    @SerializedName("TransactionNumber")
    private String TransactionNumber;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("PaymentFrom")
    private String PaymentFrom;
    @SerializedName("statusDesc")
    private String statusDesc;

    public String getTransactionDate() {
        return TransactionDate;
    }

    public String getTransactionNumber() {
        return TransactionNumber;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentFrom() {
        return PaymentFrom;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
}
