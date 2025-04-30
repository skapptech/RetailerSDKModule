package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

public class TargetConditionsModel {

    @SerializedName("ConditionText")
    private String ConditionText;

    @SerializedName("ConditionCompleteText")
    private String ConditionCompleteText;

    @SerializedName("Target")
    private int Target;

    @SerializedName("CurrentValue")
    private int CurrentValue;

    @SerializedName("AchivePercent")
    private double AchivePercent;

    @SerializedName("Message")
    private String Message;

    public String getConditionText() {
        return ConditionText;
    }

    public void setConditionText(String conditionText) {
        ConditionText = conditionText;
    }

    public String getConditionCompleteText() {
        return ConditionCompleteText;
    }

    public void setConditionCompleteText(String conditionCompleteText) {
        ConditionCompleteText = conditionCompleteText;
    }

    public int getTarget() {
        return Target;
    }

    public void setTarget(int target) {
        Target = target;
    }

    public int getCurrentValue() {
        return CurrentValue;
    }

    public void setCurrentValue(int currentValue) {
        CurrentValue = currentValue;
    }

    public double getAchivePercent() {
        return AchivePercent;
    }

    public void setAchivePercent(double achivePercent) {
        AchivePercent = achivePercent;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
