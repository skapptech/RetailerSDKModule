package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UpdateContactModel {

    @SerializedName("LoginCustId")
    public String LoginCustId;

    @SerializedName("HKContacts")
    public ArrayList<HKContactModel> HKContactList;

    public UpdateContactModel(String loginCustId, ArrayList<HKContactModel> hkList) {
        LoginCustId = loginCustId;
        this.HKContactList = hkList;
    }

    public String getLoginCustId() {
        return LoginCustId;
    }

    public void setLoginCustId(String loginCustId) {
        LoginCustId = loginCustId;
    }

    public ArrayList<HKContactModel> getHKContactList() {
        return HKContactList;
    }

    public void setHKContactList(ArrayList<HKContactModel> HKContactList) {
        this.HKContactList = HKContactList;
    }
}
