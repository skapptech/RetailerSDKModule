package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class UpdateOrderPlacedModel {
    @SerializedName("OrderId")
    private int orderId;
    @SerializedName("IsSuccess")
    private boolean isSuccess;

    @SerializedName("OrderPaymentMode")
    private String paymentMode;
    @SerializedName("TransactionId")
    private String transactionId;
    @SerializedName("PaymentThrough")
    private String paymentThrough;

    @SerializedName("IsPayLater")
    private boolean isPayLater;

    public UpdateOrderPlacedModel(int orderId, boolean isSuccess, String paymentMode, String transactionId, String paymentThrough, Boolean isPayLater) {
        this.orderId = orderId;
        this.isSuccess = isSuccess;
        this.paymentMode = paymentMode;
        this.transactionId = transactionId;
        this.paymentThrough = paymentThrough;
        this.isPayLater = isPayLater;
    }
}