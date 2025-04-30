package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "RecentSearch")
public class SearchItemDTO {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int itemId;
    private String itemNumber;
    private String itemName;
    private double price;
    private double unitPrice;
    private String logoUrl;
    private String marginPoint;
    private int dreamPoint;
    private int itemMultiMRPId;
    private int qty;
    private String query;


    public SearchItemDTO() {
    }

    @Ignore
    public SearchItemDTO(int itemId, String itemNumber, String itemName, double price, double unitPrice, String logoUrl, String marginPoint, int dreamPoint, int itemMultiMRPId, int qty, String query) {
        this.itemId = itemId;
        this.itemNumber = itemNumber;
        this.itemName = itemName;
        this.price = price;
        this.unitPrice = unitPrice;
        this.logoUrl = logoUrl;
        this.marginPoint = marginPoint;
        this.dreamPoint = dreamPoint;
        this.itemMultiMRPId = itemMultiMRPId;
        this.qty = qty;
        this.query = query;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getMarginPoint() {
        return marginPoint;
    }

    public void setMarginPoint(String marginPoint) {
        this.marginPoint = marginPoint;
    }

    public int getDreamPoint() {
        return dreamPoint;
    }

    public void setDreamPoint(int dreamPoint) {
        this.dreamPoint = dreamPoint;
    }

    public int getItemMultiMRPId() {
        return itemMultiMRPId;
    }

    public void setItemMultiMRPId(int itemMultiMRPId) {
        this.itemMultiMRPId = itemMultiMRPId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}