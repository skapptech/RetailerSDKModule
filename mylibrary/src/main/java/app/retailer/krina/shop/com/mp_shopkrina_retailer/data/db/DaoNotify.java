package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NotifyModel;

@Dao
public interface DaoNotify {

    @Insert
    Long insertTask(NotifyModel note);

    @Query("SELECT * FROM NotifyTb WHERE ItemId =:productId")
    LiveData<NotifyModel> getTask(int productId);

    @Transaction
    @Query("SELECT EXISTS(SELECT * FROM NotifyTb WHERE ItemId = :productId)")
    Boolean isItemNotify(int productId);

    @Query("DELETE FROM NotifyTb")
    void deleteTask();
}