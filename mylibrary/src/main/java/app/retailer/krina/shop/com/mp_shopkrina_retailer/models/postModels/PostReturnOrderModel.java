package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostReturnOrderModel {
    @Expose
    @SerializedName("Details")
    private List<DetailsEntity> details;
    @Expose
    @SerializedName("Cust_Comment")
    private String custComment;
    @Expose
    @SerializedName("Status")
    private String status;
    @Expose
    @SerializedName("RequestType")
    private int requesttype;
    @Expose
    @SerializedName("OrderId")
    private int orderid;
    @Expose
    @SerializedName("CustomerId")
    private int customerid;

    public List<DetailsEntity> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsEntity> details) {
        this.details = details;
    }

    public String getCustComment() {
        return custComment;
    }

    public void setCustComment(String custComment) {
        this.custComment = custComment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(int requesttype) {
        this.requesttype = requesttype;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public static class DetailsEntity {
        @Expose
        @SerializedName("ReturnQty")
        private int returnQty;
        @Expose
        @SerializedName("OrderDetailsId")
        private int orderDetailsId;
        @Expose
        @SerializedName("Cust_Comment")
        private String custComment;

        public void setReturnQty(int returnQty) {
            this.returnQty = returnQty;
        }

        public void setOrderDetailsId(int orderDetailsId) {
            this.orderDetailsId = orderDetailsId;
        }

        public void setCustComment(String custComment) {
            this.custComment = custComment;
        }
    }
}
