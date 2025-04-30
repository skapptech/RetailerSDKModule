package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import java.util.ArrayList;

public class RequestServiceModel {
    ArrayList<RServiveModel> itemList;
    private  String Skcode,WarehouseId,PeopleName;
    private  int PeopleID;



    public RequestServiceModel(ArrayList<RServiveModel> itemList, String skcode, String warehouseId, int peopleID, String peopleName) {
        this.itemList = itemList;
        Skcode = skcode;
        WarehouseId = warehouseId;
        PeopleID = peopleID;
        PeopleName = peopleName;
    }



    public ArrayList<RServiveModel> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<RServiveModel> itemList) {
        this.itemList = itemList;
    }

    public String getSkcode() {
        return Skcode;
    }

    public void setSkcode(String skcode) {
        Skcode = skcode;
    }

    public int getPeopleID() {
        return PeopleID;
    }

    public void setPeopleID(int peopleID) {
        PeopleID = peopleID;
    }

    public String getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        WarehouseId = warehouseId;
    }



    public String getPeopleName() {
        return PeopleName;
    }

    public void setPeopleName(String peopleName) {
        PeopleName = peopleName;
    }
}
