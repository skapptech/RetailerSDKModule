package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AddressModel implements Parcelable {
    private int CityId;
    private String areaName;
    private String CityName;
    private String Landmark;
    private String Address;
    private double Latitude;
    private double Longitude;
    private String Pincode;
    private String FlateOrFloorNumber;
    private String BillingAddress;
    private String BillingCity;
    private String BillingState;
    private String BillingZipCode;

    public AddressModel() {
    }

    public AddressModel(int cityId, String areaName, String cityName, String landmark, String address, double latitude, double longitude, String pincode, String flateOrFloorNumber, String billingAddress, String billingCity, String billingState, String billingZipCode) {
        CityId = cityId;
        this.areaName = areaName;
        CityName = cityName;
        Landmark = landmark;
        Address = address;
        Latitude = latitude;
        Longitude = longitude;
        Pincode = pincode;
        FlateOrFloorNumber = flateOrFloorNumber;
        BillingAddress = billingAddress;
        BillingCity = billingCity;
        BillingState = billingState;
        BillingZipCode = billingZipCode;
    }

    protected AddressModel(Parcel in) {
        CityId = in.readInt();
        areaName = in.readString();
        CityName = in.readString();
        Landmark = in.readString();
        Address = in.readString();
        Latitude = in.readDouble();
        Longitude = in.readDouble();
        Pincode = in.readString();
        FlateOrFloorNumber = in.readString();
        BillingAddress = in.readString();
        BillingCity = in.readString();
        BillingState = in.readString();
        BillingZipCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(CityId);
        dest.writeString(areaName);
        dest.writeString(CityName);
        dest.writeString(Landmark);
        dest.writeString(Address);
        dest.writeDouble(Latitude);
        dest.writeDouble(Longitude);
        dest.writeString(Pincode);
        dest.writeString(FlateOrFloorNumber);
        dest.writeString(BillingAddress);
        dest.writeString(BillingCity);
        dest.writeString(BillingState);
        dest.writeString(BillingZipCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };


    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getFlateOrFloorNumber() {
        return FlateOrFloorNumber;
    }

    public void setFlateOrFloorNumber(String flateOrFloorNumber) {
        FlateOrFloorNumber = flateOrFloorNumber;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public static Creator<AddressModel> getCREATOR() {
        return CREATOR;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getBillingAddress() {
        return BillingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        BillingAddress = billingAddress;
    }

    public String getBillingCity() {
        return BillingCity;
    }

    public void setBillingCity(String billingCity) {
        BillingCity = billingCity;
    }

    public String getBillingState() {
        return BillingState;
    }

    public void setBillingState(String billingState) {
        BillingState = billingState;
    }

    public String getBillingZipCode() {
        return BillingZipCode;
    }

    public void setBillingZipCode(String billingZipCode) {
        BillingZipCode = billingZipCode;
    }
}