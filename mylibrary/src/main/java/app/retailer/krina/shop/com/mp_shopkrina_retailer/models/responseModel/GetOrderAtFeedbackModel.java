package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class GetOrderAtFeedbackModel {
    @Expose
    @SerializedName("OrderedDate")
    private String ordereddate;
    @Expose
    @SerializedName("GrossAmount")
    private double grossamount;
    @Expose
    @SerializedName("OrderId")
    private int orderid;

    public String getOrdereddate() {
        return ordereddate;
    }

    public double getGrossamount() {
        return grossamount;
    }

    public int getOrderid() {
        return orderid;
    }
}
