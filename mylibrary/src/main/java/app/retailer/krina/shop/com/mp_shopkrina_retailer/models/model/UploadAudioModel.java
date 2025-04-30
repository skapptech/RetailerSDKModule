package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class UploadAudioModel {

    @SerializedName("TripId")
    public int TripId;

    @SerializedName("CustomerId")
    public int CustomerId;

    @SerializedName("RecordingUrl")
    public String RecordingUrl;

    @SerializedName("Comment")
    public String Comment;


    public UploadAudioModel(int tripId, int customerId, String recordingUrl, String comment) {
        TripId = tripId;
        CustomerId = customerId;
        RecordingUrl = recordingUrl;
        Comment = comment;
    }
}
