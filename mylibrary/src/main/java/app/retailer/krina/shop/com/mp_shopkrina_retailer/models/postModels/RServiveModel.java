package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

public class RServiveModel {

   private String checkText;
    private int number;

    public RServiveModel(String checkText, int number) {
        this.checkText = checkText;
        this.number = number;
    }

    public String getCheckText() {
        return checkText;
    }

    public void setCheckText(String checkText) {
        this.checkText = checkText;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
