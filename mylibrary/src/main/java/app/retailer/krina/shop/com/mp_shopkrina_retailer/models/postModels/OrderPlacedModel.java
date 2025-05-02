package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderPlacedModel {
    @SerializedName("CustomerId")
    private int CustomerId;
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @SerializedName("CustomerName")
    private String CustomerName;
    @SerializedName("CustomerType")
    private String CustomerType;
    @SerializedName("Customerphonenum")
    private String Customerphonenum;
    @SerializedName("SalesPersonId")
    private int SalesPersonId;
    @SerializedName("DialEarnigPoint")
    private int DialEarnigPoint;
    @SerializedName("ShippingAddress")
    private String ShippingAddress;
    @SerializedName("ShopName")
    private String ShopName;
    @SerializedName("Skcode")
    private String Skcode;
    @SerializedName("DreamPoint")
    private double DreamPoint;
    @SerializedName("TotalAmount")
    private double TotalAmount;
    @SerializedName("Savingamount")
    private double Savingamount;
    @SerializedName("deliveryCharge")
    private String deliveryCharge;
    @SerializedName("OnlineServiceTax")
    private String OnlineServiceTax;
    @SerializedName("WalletAmount")
    private double WalletAmount;
    @SerializedName("walletPointUsed")
    private double walletPointUsed;
    @SerializedName("Trupay")
    private boolean Trupay;
    @SerializedName("paymentThrough")
    private String paymentThrough;
    @SerializedName("TrupayTransactionId")
    private String trupayTransactionId;
    @SerializedName("paymentMode")
    private String paymentMode;
    @SerializedName("BillDiscountOfferId")
    private String BillDiscountOfferId;
    @SerializedName("APPVersion")
    private String APPVersion;
    @SerializedName("APPType")
    private String APPType;
    @SerializedName("BillDiscountAmount")
    private double BillDiscountAmount;
    @SerializedName("GulkAmount")
    private double gullakAmount = 0;
    @SerializedName("CODAmount")
    private double codAmount = 0;
    @SerializedName("lat")
    private double lat = 0;
    @SerializedName("lng")
    private double lng = 0;
    @SerializedName("ConvenienceFees")
    private double convenienceFees = 0;
    @SerializedName("PlatformFees")
    private double platformFees = 0;
    @SerializedName("Salespersonvisitcharges")
    private double salespersonvisitcharges = 0;
    @SerializedName("HikeCharges")
    private double hikeCharges = 0;
    //
    @SerializedName("itemDetails")
    private ArrayList<ItemDetailsModel> itemDetails;


    public OrderPlacedModel(int customerId, String createdDate, String customerName, String customerType, String customerphonenum, int salesPersonId, int dialEarnigPoint, String shippingAddress, String shopName, String skcode, double dreamPoint, double totalAmount, double savingamount, String deliveryCharge, String onlineServiceTax, double walletAmount, double walletPointUsed, boolean trupay, ArrayList<ItemDetailsModel> itemDetails, String trupayTransactionId, String paymentMode, String paymentThrough, String billDiscountOfferId, double billDiscountAmount, double gullakAmount, double codAmount, String aPPType, String aPPVersion, double lat, double lng, double convenienceFees, double platformFees, double salespersonvisitcharges, double hikeCharges) {
        CustomerId = customerId;
        CreatedDate = createdDate;
        CustomerName = customerName;
        CustomerType = customerType;
        Customerphonenum = customerphonenum;
        SalesPersonId = salesPersonId;
        DialEarnigPoint = dialEarnigPoint;
        ShippingAddress = shippingAddress;
        ShopName = shopName;
        Skcode = skcode;
        DreamPoint = dreamPoint;
        TotalAmount = totalAmount;
        Savingamount = savingamount;
        this.deliveryCharge = deliveryCharge;
        OnlineServiceTax = onlineServiceTax;
        WalletAmount = walletAmount;
        this.walletPointUsed = walletPointUsed;
        Trupay = trupay;
        this.itemDetails = itemDetails;
        this.trupayTransactionId = trupayTransactionId;
        this.paymentMode = paymentMode;
        this.paymentThrough = paymentThrough;
        BillDiscountOfferId = billDiscountOfferId;
        BillDiscountAmount = billDiscountAmount;
        this.gullakAmount = gullakAmount;
        this.codAmount = codAmount;
        APPVersion = aPPVersion;
        APPType = aPPType;
        this.lat = lat;
        this.lng = lng;
        this.convenienceFees = convenienceFees;
        this.platformFees = platformFees;
        this.salespersonvisitcharges = salespersonvisitcharges;
        this.hikeCharges = hikeCharges;
    }
}