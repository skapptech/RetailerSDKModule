package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class HomeDataDetailsModel {
    private int id;
    private String titles;
    private String image;
    private String ItemId;
    private String ItemName;
    private boolean active;
    private String eventurl;
    private int parentid;
    @SerializedName("SliderSectionType")
    private String SliderSectionType;
    @SerializedName("BaseCategoryid")
    private String BaseCategoryid;
    @SerializedName("CategoryId")
    private int CategoryId;
    private String LogoUrl;

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getBaseCategoryid() {
        return BaseCategoryid;
    }

    public void setBaseCategoryid(String baseCategoryid) {
        BaseCategoryid = baseCategoryid;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getEventurl() {
        return eventurl;
    }

    public void setEventurl(String eventurl) {
        this.eventurl = eventurl;
    }

    public String getSliderSectionType() {
        return SliderSectionType;
    }

    public void setSliderSectionType(String sliderSectionType) {
        SliderSectionType = sliderSectionType;
    }
}

