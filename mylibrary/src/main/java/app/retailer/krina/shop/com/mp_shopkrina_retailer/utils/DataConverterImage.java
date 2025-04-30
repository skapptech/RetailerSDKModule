package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.ImageObjEntity;

public class DataConverterImage {

    @TypeConverter
    public String fromPollList(ArrayList<ImageObjEntity> list) {
        if (list == null) {
            return null;
        }
        return new Gson().toJson(list, new TypeToken<ArrayList<ImageObjEntity>>() {
        }.getType());
    }

    @TypeConverter
    public ArrayList<ImageObjEntity> toPollList(String list) {
        if (list == null) {
            return null;
        }
        return new Gson().fromJson(list, new TypeToken<ArrayList<ImageObjEntity>>() {
        }.getType());
    }
}