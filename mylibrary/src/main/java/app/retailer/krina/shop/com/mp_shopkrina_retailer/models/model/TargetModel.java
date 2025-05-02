package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class TargetModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private int type;

    public TargetModel(String name, int type) {
        this.name = name;
        this.type = type;
    }

}
