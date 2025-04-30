package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class AddGamePointModel {
    @SerializedName("CustomerId")
    public int CustomerId;
    @SerializedName("Score")
    public int Score;
    @SerializedName("GameName")
    public String gameName;
    @SerializedName("GamePointOn")
    public String gamePointOn;

    public AddGamePointModel(int customerId, int score, String gameName, String gamePointOn) {
        CustomerId = customerId;
        Score = score;
        this.gameName = gameName;
        this.gamePointOn = gamePointOn;
    }
}
