package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class PrimePaymentModel {
    @SerializedName("MemberShipId")
    private int memberShipId;
    @SerializedName("GatewayTransId")
    String GatewayTransId;
    @SerializedName("Status")
    String Status;
    @SerializedName("GatewayRequest")
    String GatewayRequest;
    @SerializedName("GatewayResponse")
    String GatewayResponse;
    @SerializedName("PaymentFrom")
    String PaymentFrom;
    @SerializedName("CustomerId")
    int CustomerId;
    @SerializedName("TransId")
    long TransId;


    public PrimePaymentModel(int memberShipId, String gatewayTransId, String status, String gatewayRequest, String gatewayResponse, String paymentFrom, int customerId, long transId) {
        this.memberShipId = memberShipId;
        GatewayTransId = gatewayTransId;
        Status = status;
        GatewayRequest = gatewayRequest;
        GatewayResponse = gatewayResponse;
        PaymentFrom = paymentFrom;
        CustomerId = customerId;
        TransId = transId;
    }

    public String getGatewayTransId() {
        return GatewayTransId;
    }

    public void setGatewayTransId(String gatewayTransId) {
        GatewayTransId = gatewayTransId;
    }

    public String getGatewayRequest() {
        return GatewayRequest;
    }

    public void setGatewayRequest(String gatewayRequest) {
        GatewayRequest = gatewayRequest;
    }

    public String getGatewayResponse() {
        return GatewayResponse;
    }

    public void setGatewayResponse(String gatewayResponse) {
        GatewayResponse = gatewayResponse;
    }

    public String getPaymentFrom() {
        return PaymentFrom;
    }

    public void setPaymentFrom(String paymentFrom) {
        PaymentFrom = paymentFrom;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public long getTransId() {
        return TransId;
    }

    public void setTransId(long transId) {
        TransId = transId;
    }
}
