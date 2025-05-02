package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Lang")
public class LangModel {
    @NotNull
    @PrimaryKey
    private String key = "";
    private String value;

    public LangModel() {
    }

    @Ignore
    public LangModel(@NonNull String key, String value) {
        this.key = key;
        this.value = value;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    public void setKey(@NotNull String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}