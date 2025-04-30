package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import java.io.Serializable;

public class CustomersContactModel implements Serializable {

    private String customerName;
    private String customerNumber;

    public CustomersContactModel(String customerName, String customerNumber) {
        this.customerName = customerName;
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }
}
