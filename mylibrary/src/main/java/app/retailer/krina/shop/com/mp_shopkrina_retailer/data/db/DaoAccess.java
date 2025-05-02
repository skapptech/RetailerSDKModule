package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;

@Dao
public interface DaoAccess {

    @Insert
    Long insertTask(ItemListModel note);

    @Transaction
    @Query("SELECT * FROM ItemListModel")
    LiveData<List<ItemListModel>> fetchAllTasks();

    @Query("SELECT * FROM ItemListModel WHERE ItemId =:productId")
    LiveData<ItemListModel> getTask(int productId);

    @Transaction
    @Query("SELECT EXISTS(SELECT * FROM ItemListModel WHERE ItemId = :productId)")
    Boolean isItemExist(int productId);

    @Transaction
    @Query("SELECT EXISTS(SELECT * FROM ItemListModel WHERE ItemId = :productId)")
    Boolean isItemNotify(int productId);

    @Update
    void updateTask(ItemListModel note);


    @Delete
    void deleteTask(ItemListModel note);
}
