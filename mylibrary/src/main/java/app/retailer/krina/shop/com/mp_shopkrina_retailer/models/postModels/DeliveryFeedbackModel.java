package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class DeliveryFeedbackModel {
    @SerializedName("CustomerId")
    private int customerId;

    @SerializedName("OrderId")
    private int orderId;

    @SerializedName("Rating")
    private int rating;

    @SerializedName("FeedbackQuestionIds")
    private String feedbackQuestionIds;

    @SerializedName("Comments")
    private String comment;

    public DeliveryFeedbackModel(int customerId, int orderId, int rating, String feedbackQuestionIds, String comment) {
        this.customerId = customerId;
        this.orderId = orderId;
        this.rating = rating;
        this.feedbackQuestionIds = feedbackQuestionIds;
        this.comment = comment;
    }
}
