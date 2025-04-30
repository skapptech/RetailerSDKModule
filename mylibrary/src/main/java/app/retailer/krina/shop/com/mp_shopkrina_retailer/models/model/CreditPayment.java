package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class CreditPayment {
    @SerializedName("CompanyId")
    private final int companyId;
    @SerializedName("CustomerId")
    private final int customerId;
    @SerializedName("OrderId")
    private final long orderId;
    @SerializedName("AccountId")
    private final int accountId;
    @SerializedName("Amount")
    private final double amount;
    @SerializedName("PaymentMode")
    private final String paymentMode;

    public CreditPayment(int companyId, int customerId, long orderId, int accountId, double amount, String paymentMode) {
        this.companyId = companyId;
        this.customerId = customerId;
        this.orderId = orderId;
        this.accountId = accountId;
        this.amount = amount;
        this.paymentMode = paymentMode;
    }
}
