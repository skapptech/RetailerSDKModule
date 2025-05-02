package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class ContactUploadModel {
    @SerializedName("customerId")
    private final int customerId;
    @SerializedName("contact")
    private final String contact;
    @SerializedName("contactName")
    private final String contactName;

    public ContactUploadModel(int customerId, String contact, String contactName) {
        this.customerId = customerId;
        this.contact = contact;
        this.contactName = contactName;
    }

    public String getContact() {
        return contact;
    }
}