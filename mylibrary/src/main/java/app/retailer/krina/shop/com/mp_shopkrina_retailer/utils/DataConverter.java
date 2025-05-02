package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;

public class DataConverter {

    @TypeConverter
    public String fromMoqList(ArrayList<ItemListModel> list) {
        if (list == null) {
            return null;
        }
        return new Gson().toJson(list, new TypeToken<ArrayList<ItemListModel>>() {
        }.getType());
    }

    @TypeConverter
    public ArrayList<ItemListModel> toMoqList(String list) {
        if (list == null) {
            return null;
        }
        return new Gson().fromJson(list, new TypeToken<ArrayList<ItemListModel>>() {
        }.getType());
    }
}