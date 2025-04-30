package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import java.io.Serializable;

public class MyExpiringWalletModel implements Serializable {
    private  double Point;
    private  String ExpiringDate;
    private String TransactionDate;
    private String Through;

    public double getPoint() {
        return Point;
    }

    public void setPoint(double point) {
        Point = point;
    }

    public String getExpiringDate() {
        return ExpiringDate;
    }

    public void setExpiringDate(String expiringDate) {
        ExpiringDate = expiringDate;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }

    public String getThrough() {
        return Through;
    }

    public void setThrough(String through) {
        Through = through;
    }
}
