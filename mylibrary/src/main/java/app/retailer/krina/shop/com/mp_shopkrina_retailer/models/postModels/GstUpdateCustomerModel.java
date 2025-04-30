package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GstUpdateCustomerModel {

    @Expose
    @SerializedName("GSTVerifiedRequestId")
    private int gstverifiedrequestid;
    @Expose
    @SerializedName("Comments")
    private String comments;
    @Expose
    @SerializedName("GSTImage")
    private String gstimage;
    @Expose
    @SerializedName("Status")
    private String status;
    @Expose
    @SerializedName("GSTNo")
    private String gstno;
    @Expose
    @SerializedName("CustomerId")
    private int customerid;

    @Expose
    @SerializedName("PANNo")
    private String sPanNumber;
    public GstUpdateCustomerModel(int gstverifiedrequestid, String comments, String gstimage, String status, String gstno, int customerid,String sPanNumber) {
        this.gstverifiedrequestid = gstverifiedrequestid;
        this.comments = comments;
        this.gstimage = gstimage;
        this.status = status;
        this.gstno = gstno;
        this.customerid = customerid;
        this.sPanNumber = sPanNumber;
    }

    public int getGstverifiedrequestid() {
        return gstverifiedrequestid;
    }

    public void setGstverifiedrequestid(int gstverifiedrequestid) {
        this.gstverifiedrequestid = gstverifiedrequestid;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getGstimage() {
        return gstimage;
    }

    public void setGstimage(String gstimage) {
        this.gstimage = gstimage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGstno() {
        return gstno;
    }

    public void setGstno(String gstno) {
        this.gstno = gstno;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }
}
