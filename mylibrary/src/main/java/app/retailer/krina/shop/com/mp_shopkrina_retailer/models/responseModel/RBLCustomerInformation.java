package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

public class RBLCustomerInformation {

    @SerializedName("customerId")
    private int customerId;

    @SerializedName("peopleId")
    private int peopleId;

    @SerializedName("gender")
    private String gender;

    @SerializedName("title")
    private String title;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("dateOfBirth")
    private String dateOfBirth;

    @SerializedName("motherMaidenName")
    private String motherMaidenName;

    @SerializedName("community")
    private String community;


    @SerializedName("maritalStatus")
    private String maritalStatus;

    @SerializedName("grossIncome")
    private String grossIncome;

    @SerializedName("doNotCall")
    private String doNotCall;

    @SerializedName("panNumber")
    private String panNumber;

    @SerializedName("address1")
    private String address1;

    @SerializedName("addressFormat1")
    private String addressFormat1;

    @SerializedName("addressType1")
    private String addressType1;

    @SerializedName("addressLabel1")
    private String addressLabel1;

    @SerializedName("addressLine11")
    private String addressLine11;

    @SerializedName("addressLine21")
    private String addressLine21;


    @SerializedName("addressLine31")
    private String addressLine31;


    @SerializedName("city1")
    private String city1;


    @SerializedName("state1")
    private String state1;


    @SerializedName("postalCode1")
    private String postalCode1;

    @SerializedName("phone1")
    private String phone1;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("shopImage")
    private String shopImage;

    public RBLCustomerInformation(int customerId, int peopleId, String gender, String title, String firstName, String lastName, String dateOfBirth, String motherMaidenName, String community, String maritalStatus, String grossIncome, String doNotCall, String panNumber, String address1, String addressFormat1, String addressType1, String addressLabel1, String addressLine11, String addressLine21, String addressLine31, String city1, String state1, String postalCode1, String phone1, String createdBy, String shopImage, String message) {
        this.customerId = customerId;
        this.peopleId = peopleId;
        this.gender = gender;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.motherMaidenName = motherMaidenName;
        this.community = community;
        this.maritalStatus = maritalStatus;
        this.grossIncome = grossIncome;
        this.doNotCall = doNotCall;
        this.panNumber = panNumber;
        this.address1 = address1;
        this.addressFormat1 = addressFormat1;
        this.addressType1 = addressType1;
        this.addressLabel1 = addressLabel1;
        this.addressLine11 = addressLine11;
        this.addressLine21 = addressLine21;
        this.addressLine31 = addressLine31;
        this.city1 = city1;
        this.state1 = state1;
        this.postalCode1 = postalCode1;
        this.phone1 = phone1;
        this.createdBy = createdBy;
        this.shopImage = shopImage;
        Message = message;
    }

    @SerializedName("Message")
    private String Message;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(int peopleId) {
        this.peopleId = peopleId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getGrossIncome() {
        return grossIncome;
    }

    public void setGrossIncome(String grossIncome) {
        this.grossIncome = grossIncome;
    }

    public String getDoNotCall() {
        return doNotCall;
    }

    public void setDoNotCall(String doNotCall) {
        this.doNotCall = doNotCall;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddressFormat1() {
        return addressFormat1;
    }

    public void setAddressFormat1(String addressFormat1) {
        this.addressFormat1 = addressFormat1;
    }

    public String getAddressType1() {
        return addressType1;
    }

    public void setAddressType1(String addressType1) {
        this.addressType1 = addressType1;
    }

    public String getAddressLabel1() {
        return addressLabel1;
    }

    public void setAddressLabel1(String addressLabel1) {
        this.addressLabel1 = addressLabel1;
    }

    public String getAddressLine11() {
        return addressLine11;
    }

    public void setAddressLine11(String addressLine11) {
        this.addressLine11 = addressLine11;
    }

    public String getAddressLine21() {
        return addressLine21;
    }

    public void setAddressLine21(String addressLine21) {
        this.addressLine21 = addressLine21;
    }

    public String getAddressLine31() {
        return addressLine31;
    }

    public void setAddressLine31(String addressLine31) {
        this.addressLine31 = addressLine31;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public String getState1() {
        return state1;
    }

    public void setState1(String state1) {
        this.state1 = state1;
    }

    public String getPostalCode1() {
        return postalCode1;
    }

    public void setPostalCode1(String postalCode1) {
        this.postalCode1 = postalCode1;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
