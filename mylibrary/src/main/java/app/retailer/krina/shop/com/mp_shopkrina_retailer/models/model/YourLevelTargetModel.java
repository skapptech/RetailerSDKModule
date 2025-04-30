package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class YourLevelTargetModel implements Serializable {

    @SerializedName("Id")
    private int Id;
    @SerializedName("LevelName")
    private String LevelName;
    @SerializedName("Volume")
    private int Volume;
    @SerializedName("OrderCount")
    private int OrderCount;
    @SerializedName("BrandCount")
    private int BrandCount;
    @SerializedName("KKVolume")
    private int KKVolume;
    @SerializedName("Selected")
    private boolean Selected;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLevelName() {
        return LevelName;
    }

    public void setLevelName(String levelName) {
        LevelName = levelName;
    }

    public int getVolume() {
        return Volume;
    }

    public void setVolume(int volume) {
        Volume = volume;
    }

    public int getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(int orderCount) {
        OrderCount = orderCount;
    }

    public int getBrandCount() {
        return BrandCount;
    }

    public void setBrandCount(int brandCount) {
        BrandCount = brandCount;
    }

    public int getKKVolume() {
        return KKVolume;
    }

    public void setKKVolume(int KKVolume) {
        this.KKVolume = KKVolume;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }
}
