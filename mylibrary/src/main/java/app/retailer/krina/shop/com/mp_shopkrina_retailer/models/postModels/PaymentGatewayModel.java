package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class PaymentGatewayModel {
    @SerializedName("OrderId")
    private int OrderId;

    @SerializedName("GatewayOrderId")
    private String GatewayOrderId;

    @SerializedName("GatewayTransId")
    private String GatewayTransId;

    @SerializedName("amount")
    private double amount;

    @SerializedName("currencyCode")
    private String currencyCode;

    @SerializedName("status")
    private String status;

    @SerializedName("statusDesc")
    private String statusDesc;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("PaymentFrom")
    private String PaymentFrom;

    @SerializedName("GatewayRequest")
    private String GatewayRequest;

    @SerializedName("GatewayResponse")
    private String GatewayResponse;

    @SerializedName("PaymentThrough")
    private String PaymentThrough;

    @SerializedName("IsPayLater")
    private boolean isPayLater;


    public PaymentGatewayModel(int orderId, String gatewayOrderId, String gatewayTransId, double amount, String currencyCode, String status, String statusDesc, String statusCode, String paymentFrom, String gatewayRequest, String gatewayResponse, String paymentThrough, boolean isPayLater) {
        OrderId = orderId;
        GatewayOrderId = gatewayOrderId;
        GatewayTransId = gatewayTransId;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.status = status;
        this.statusDesc = statusDesc;
        this.statusCode = statusCode;
        PaymentFrom = paymentFrom;
        GatewayRequest = gatewayRequest;
        GatewayResponse = gatewayResponse;
        PaymentThrough = paymentThrough;
        this.isPayLater = isPayLater;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getGatewayOrderId() {
        return GatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        GatewayOrderId = gatewayOrderId;
    }

    public String getGatewayTransId() {
        return GatewayTransId;
    }

    public void setGatewayTransId(String gatewayTransId) {
        GatewayTransId = gatewayTransId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getPaymentFrom() {
        return PaymentFrom;
    }

    public void setPaymentFrom(String paymentFrom) {
        PaymentFrom = paymentFrom;
    }


    public String getPaymentThrough() {
        return PaymentThrough;
    }

    public void setPaymentThrough(String paymentThrough) {
        PaymentThrough = paymentThrough;
    }

    public boolean isPayLater() {
        return isPayLater;
    }

    public void setPayLater(boolean payLater) {
        isPayLater = payLater;
    }
}