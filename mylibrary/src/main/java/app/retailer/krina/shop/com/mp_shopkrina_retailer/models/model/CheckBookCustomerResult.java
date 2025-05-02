package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class CheckBookCustomerResult {
    @SerializedName("account_provider")
    public String account_provider;
    @SerializedName("account_no")
    public String account_no;
    @SerializedName("sanction_amount")
    public String sanction_amount;
    @SerializedName("available_amount")
    public String available_amount;

    public String getAccount_provider() {
        return account_provider;
    }

    public void setAccount_provider(String account_provider) {
        this.account_provider = account_provider;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getSanction_amount() {
        return sanction_amount;
    }

    public void setSanction_amount(String sanction_amount) {
        this.sanction_amount = sanction_amount;
    }

    public String getAvailable_amount() {
        return available_amount;
    }

    public void setAvailable_amount(String available_amount) {
        this.available_amount = available_amount;
    }
}
