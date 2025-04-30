package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class MembershipPlanModel {
    @SerializedName("Id")
    private int id;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("MemberShipInMonth")
    private int memberShipInMonth;
    @SerializedName("MemberShipLogo")
    private String memberShipLogo;
    @SerializedName("MemberShipName")
    private String memberShipName;
    @SerializedName("MemberShipDescription")
    private String memberShipDescription;
    @SerializedName("MemberShipSequence")
    private int memberShipSequence;
    @SerializedName("taken")
    private boolean taken;
    @SerializedName("takenMemberId")
    private int takenMemberId;
    @SerializedName("StartDate")
    private String startDate;
    @SerializedName("EndDate")
    private String endDate;

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getMemberShipInMonth() {
        return memberShipInMonth;
    }

    public String getMemberShipLogo() {
        return memberShipLogo;
    }

    public String getMemberShipName() {
        return memberShipName;
    }

    public String getMemberShipDescription() {
        return memberShipDescription;
    }

    public int getMemberShipSequence() {
        return memberShipSequence;
    }

    public boolean isTaken() {
        return taken;
    }

    public int getTakenMemberId() {
        return takenMemberId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
