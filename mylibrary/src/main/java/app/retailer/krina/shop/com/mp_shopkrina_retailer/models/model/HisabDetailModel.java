package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class HisabDetailModel implements Serializable {


    public static final int USER_SIDE = 0;
    public static final int SERVER_SIDE = 1;
    public static final int DATE_HEADER = 2;

    public HisabDetailModel(String id, String crCustomerId, String drCustomerId,
                            String amount, String dueDate,
                            ArrayList<HisabKitabNotesModel> hisabNotesList,
                            ArrayList<HisabKitabImageModel> hisabKitabImagesList,Boolean payment) {
        Id = id;
        CrCustomerId = crCustomerId;
        DrCustomerId = drCustomerId;
        Amount = amount;
        DueDate = dueDate;
        this.hisabNotesList = hisabNotesList;
        HisabKitabImagesList = hisabKitabImagesList;
        Payment = payment;
    }

    @SerializedName("Id")
    public String Id;

    @SerializedName("CreatedDate")
    public String CreatedDate;

    @SerializedName("CrCustomerId")
    public String CrCustomerId;

    @SerializedName("DrCustomerId")
    public String DrCustomerId;

    @SerializedName("Amount")
    public String Amount;

    @SerializedName("DueDate")
    public String DueDate;

    @SerializedName("HisabKitabNotes")
    public ArrayList<HisabKitabNotesModel> hisabNotesList;

    @SerializedName("HisabKitabImages")
    public ArrayList<HisabKitabImageModel> HisabKitabImagesList;

    @SerializedName("Payment")
    public Boolean Payment;

    public String headerDate;

    public String getHeaderDate() {
        return headerDate;
    }

    public void setHeaderDate(String headerDate) {
        this.headerDate = headerDate;
    }

    public Boolean getPayment() {
        return Payment;
    }

    public void setPayment(Boolean payment) {
        Payment = payment;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCrCustomerId() {
        return CrCustomerId;
    }

    public void setCrCustomerId(String crCustomerId) {
        CrCustomerId = crCustomerId;
    }

    public String getDrCustomerId() {
        return DrCustomerId;
    }

    public void setDrCustomerId(String drCustomerId) {
        DrCustomerId = drCustomerId;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public ArrayList<HisabKitabNotesModel> getHisabNotesList() {
        return hisabNotesList;
    }

    public void setHisabNotesList(ArrayList<HisabKitabNotesModel> hisabNotesList) {
        this.hisabNotesList = hisabNotesList;
    }

    public ArrayList<HisabKitabImageModel> getHisabKitabImagesList() {
        return HisabKitabImagesList;
    }

    public void setHisabKitabImagesList(ArrayList<HisabKitabImageModel> hisabKitabImagesList) {
        HisabKitabImagesList = hisabKitabImagesList;
    }





}
