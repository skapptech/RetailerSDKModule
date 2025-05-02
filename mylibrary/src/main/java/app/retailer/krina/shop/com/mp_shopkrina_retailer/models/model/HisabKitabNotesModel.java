package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HisabKitabNotesModel implements Serializable {

    @SerializedName("MyHisabKitabId")
    public String MyHisabKitabId;

    @SerializedName("CustomerId")
    public String CustomerId;


    @SerializedName("Notes")
    public String Notes;

    @SerializedName("CreatedDate")
    public String CreatedDate;

    public String getMyHisabKitabId() {
        return MyHisabKitabId;
    }

    public void setMyHisabKitabId(String myHisabKitabId) {
        MyHisabKitabId = myHisabKitabId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public HisabKitabNotesModel(String myHisabKitabId, String customerId, String notes) {
        MyHisabKitabId = myHisabKitabId;
        CustomerId = customerId;
        Notes = notes;
    }
}
