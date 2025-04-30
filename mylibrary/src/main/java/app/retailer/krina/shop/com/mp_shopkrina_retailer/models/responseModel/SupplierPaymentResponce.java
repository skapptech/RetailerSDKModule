package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SupplierPaymentResponce  {
    @SerializedName("Message")
    public String Message;
    @SerializedName("Status")
    public boolean Status;
    @SerializedName("CustomerLedger")
    private CustomerLedgerModel customerLedgerModel;


    public String getMessage() {
        return Message;
    }

    public boolean isStatus() {
        return Status;
    }

    public CustomerLedgerModel getCustomerLedgerModel() {
        return customerLedgerModel;
    }
}
