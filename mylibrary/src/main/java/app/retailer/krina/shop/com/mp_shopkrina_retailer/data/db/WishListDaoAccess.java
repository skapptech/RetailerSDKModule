package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.FavItemsDetails;

@Dao
public interface WishListDaoAccess {

    @Insert
    Long insertTask(FavItemsDetails note);

    @Transaction
    @Query("SELECT * FROM WishList")
    LiveData<List<FavItemsDetails>> fetchAllTasks();

    @Query("SELECT * FROM WishList WHERE itemId =:productId")
    LiveData<FavItemsDetails> getTask(int productId);

    @Transaction
    @Query("SELECT EXISTS(SELECT * FROM WishList WHERE itemId = :productId)")
    Boolean isItemExist(int productId);

    @Update
    void updateTask(FavItemsDetails note);

    @Delete
    void deleteTask(FavItemsDetails note);
}