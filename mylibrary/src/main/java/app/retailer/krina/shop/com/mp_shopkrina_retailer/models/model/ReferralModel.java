package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class ReferralModel {
    @SerializedName("ReferralSkCode")
    private String ReferralSkCode;
    @SerializedName("IsReferral")
    private boolean IsReferral;

    public ReferralModel(String referralSkCode, boolean isReferral) {
        this.ReferralSkCode = referralSkCode;
        this.IsReferral = isReferral;
    }

    public String getReferralSkCode() {
        return ReferralSkCode;
    }

    public void setReferralSkCode(String referralSkCode) {
        ReferralSkCode = referralSkCode;
    }

    public boolean getReferral() {
        return IsReferral;
    }

    public void setReferral(boolean referral) {
        IsReferral = referral;
    }
}