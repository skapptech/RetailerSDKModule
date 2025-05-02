package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HisabKitabImageModel implements Serializable {

    @SerializedName("CustomerId")
    public String CustomerId;

    @SerializedName("ImagePath")
    public String ImagePath;
    @SerializedName("CreatedDate")
    public String CreatedDate;

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public HisabKitabImageModel(String customerId, String imagePath) {
        CustomerId = customerId;
        ImagePath = imagePath;
    }
}
