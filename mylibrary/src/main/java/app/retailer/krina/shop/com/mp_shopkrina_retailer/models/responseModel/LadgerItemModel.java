package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

public class LadgerItemModel {

    private int ID;
    private String Name;
    private String Alias;
    private String GroupID;
    private boolean InventoryValuesAreAffected;
    private String Address;
    private String Country;
    private String PinCode;
    private boolean ProvidedBankDetails;
    private String PAN;
    private String RegistrationType;
    private String GSTno;
    private int ObjectID;
    private String ObjectType;
    private int LadgertypeID;
    private boolean Active;
    private String CreatedBy;
    private String CreatedDate;
    private String UpdatedBy;
    private String UpdatedDate;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public boolean isInventoryValuesAreAffected() {
        return InventoryValuesAreAffected;
    }

    public void setInventoryValuesAreAffected(boolean inventoryValuesAreAffected) {
        InventoryValuesAreAffected = inventoryValuesAreAffected;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public boolean isProvidedBankDetails() {
        return ProvidedBankDetails;
    }

    public void setProvidedBankDetails(boolean providedBankDetails) {
        ProvidedBankDetails = providedBankDetails;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getRegistrationType() {
        return RegistrationType;
    }

    public void setRegistrationType(String registrationType) {
        RegistrationType = registrationType;
    }

    public String getGSTno() {
        return GSTno;
    }

    public void setGSTno(String GSTno) {
        this.GSTno = GSTno;
    }

    public int getObjectID() {
        return ObjectID;
    }

    public void setObjectID(int objectID) {
        ObjectID = objectID;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public int getLadgertypeID() {
        return LadgertypeID;
    }

    public void setLadgertypeID(int ladgertypeID) {
        LadgertypeID = ladgertypeID;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }


}
