package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface LangDaoAccess {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTask(LangModel note);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(ArrayList<LangModel> list);

    @Transaction
    @Query("SELECT * FROM Lang")
    LiveData<List<LangModel>> fetchAllTasks();

//    @Query("SELECT * FROM Lang where `key` =:key")
//    LiveData<LangModel> getTask(String key);

    @Query("SELECT value FROM Lang WHERE `key` =:key")
    String getString(String key);

    @Query("SELECT COUNT(*) FROM Lang")
    int getCount();

    @Query("DELETE FROM Lang")
    void truncateLangTable();
}