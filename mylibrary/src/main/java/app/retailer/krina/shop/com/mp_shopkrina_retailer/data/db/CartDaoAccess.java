package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;

@Dao
public interface CartDaoAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTask(ItemListModel model);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(List<ItemListModel> model);

    @Transaction
    @Query("SELECT * FROM ShoppingCart WHERE active==1")
    LiveData<List<ItemListModel>> fetchAllTasks();

    @Transaction
    @Query("SELECT * FROM ShoppingCart WHERE active==1 and qty > 0")
    LiveData<List<ItemListModel>> getCartNonZero();

    @Transaction
    @Query("SELECT * FROM ShoppingCart WHERE ItemNumber =:number AND active==1")
    List<ItemListModel> getItem(String number);

    @Query("SELECT * FROM ShoppingCart WHERE ItemId=:id AND active==1")
    ItemListModel getItem(int id);

    @Query("SELECT * FROM ShoppingCart WHERE ItemId=:id AND active==1")
    LiveData<ItemListModel> getCartItem(int id);

    @Transaction
    @Query("SELECT * FROM ShoppingCart WHERE ItemId=:id AND active==1")
    ItemListModel getCartItem1(int id);

    @Transaction
    @Query("SELECT * FROM ShoppingCart WHERE ItemId=:itemId AND ItemMultiMRPId=:id AND active==1")
    ItemListModel getItemByMrpId(int itemId, int id);

    @Transaction
    @Query("SELECT EXISTS(SELECT * FROM ShoppingCart WHERE ItemId=:id AND active==1)")
    Boolean isItemExist(int id);

    @Query("SELECT SUM((case when isoffer==1 and OfferCategory==2 then FlashDealSpecialPrice else UnitPrice end) * qty) FROM ShoppingCart WHERE qty > 0 AND active==1")
    LiveData<Double> getCartValue();

    @Query("SELECT SUM((case when isoffer==1 and OfferCategory==2 then FlashDealSpecialPrice else UnitPrice end) * qty) FROM ShoppingCart WHERE qty > 0 AND active==1")
    Double getCartValue1();

    @Transaction
    @Query("SELECT SUM(qty) FROM ShoppingCart WHERE ItemMultiMRPId=:multiMrpId AND active==1")
    int getQtyByMultiMrp(int multiMrpId);

    // for limit check exclude item
    @Transaction
    @Query("SELECT SUM((case when ItemId!=:id then qty else 0 end)) as quantity, SUM((case when ItemMultiMRPId==:multiMrpId then qty else 0 end)) as totalQuantity FROM ShoppingCart WHERE ItemMultiMRPId = :multiMrpId AND active==1")
    QtyDTO getQtyTotalQtyByMrpId(int id, int multiMrpId);

    @Transaction
    @Query("SELECT SUM(qty) FROM ShoppingCart WHERE ItemId!=:id AND ItemMultiMRPId=:multiMrpId AND active==1")
    int getQtyByMultiMrp(int id, int multiMrpId);

    @Transaction
    @Query("SELECT * FROM ShoppingCart WHERE ItemMultiMRPId IN(:idList) AND active==1")
    List<ItemListModel> getItemsByMultiMrpId(List<Integer> idList);


    @Query("SELECT SUM(qty) FROM ShoppingCart WHERE active==1")
    int getCartQtyCount();

    @Query("SELECT COUNT(*) FROM ShoppingCart WHERE active==1")
    int getCartCount();

    @Update
    void updateTask(ItemListModel model);

    @Delete
    void deleteTask(ItemListModel model);

    @Query("DELETE FROM ShoppingCart WHERE ItemId=:id AND active==1")
    void deleteItem(int id);

    @Query("DELETE FROM ShoppingCart")
    void truncateCart();
}