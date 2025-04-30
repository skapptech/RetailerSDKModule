package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MembershipModel {
    @SerializedName("takenMemberId")
    private int takenMemberId;
    @SerializedName("StartDate")
    private String startDate;
    @SerializedName("EndDate")
    private String EndDate;
    @SerializedName("MemberShipName")
    private String memberShipName;
    @SerializedName("Logo")
    private String logo;
    @SerializedName("TotalBanifit")
    private int totalBenefit;
    @SerializedName("BanifitList")
    private ArrayList<Benefit> benefit;
    @SerializedName("PrimeHtmL")
    private String primeHtmL;

    public int getTakenMemberId() {
        return takenMemberId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public String getMemberShipName() {
        return memberShipName;
    }

    public String getLogo() {
        return logo;
    }

    public int getTotalBenefit() {
        return totalBenefit;
    }

    public ArrayList<Benefit> getBenefit() {
        return benefit;
    }

    public String getPrimeHtmL() {
        return primeHtmL;
    }


    public class Benefit {
        @SerializedName("Id")
        public int Id;
        @SerializedName("text")
        public String text;
        @SerializedName("Amount")
        public int amount;

        public int getId() {
            return Id;
        }

        public String getText() {
            return text;
        }

        public int getAmount() {
            return amount;
        }
    }
}
