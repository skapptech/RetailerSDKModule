package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackQuestionsModel {

    @Expose
    @SerializedName("CreatedDate")
    private String createddate;
    @Expose
    @SerializedName("IsActive")
    private boolean isactive;
    @Expose
    @SerializedName("Deleted")
    private boolean deleted;
    @Expose
    @SerializedName("RatingTo")
    private int ratingto;
    @Expose
    @SerializedName("RatingFrom")
    private int ratingfrom;
    @Expose
    @SerializedName("Question")
    private String question;
    @Expose
    @SerializedName("WarehouseId")
    private int warehouseid;
    @Expose
    @SerializedName("Id")
    private int id;

    public String getCreateddate() {
        return createddate;
    }

    public boolean getIsactive() {
        return isactive;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public int getRatingto() {
        return ratingto;
    }

    public int getRatingfrom() {
        return ratingfrom;
    }

    public String getQuestion() {
        return question;
    }

    public int getWarehouseid() {
        return warehouseid;
    }

    public int getId() {
        return id;
    }
}
