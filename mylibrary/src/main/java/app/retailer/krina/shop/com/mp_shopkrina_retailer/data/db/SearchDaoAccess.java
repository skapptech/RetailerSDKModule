package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface SearchDaoAccess {

    @Insert
    Long insertTask(SearchItemDTO note);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(List<SearchItemDTO> model);

    @Transaction
    @Query("SELECT * FROM RecentSearch ORDER BY id DESC")
    LiveData<List<SearchItemDTO>> fetchAllTasks();

    @Query("SELECT * FROM RecentSearch ORDER BY id")
    List<SearchItemDTO> fetchAllTask();

    @Query("SELECT * FROM RecentSearch  ORDER BY id LIMIT 1")
    SearchItemDTO fetchTask();

    @Query("SELECT * FROM RecentSearch WHERE ItemId =:productId")
    LiveData<SearchItemDTO> getTask(int productId);

    @Query("SELECT COUNT(1) FROM RecentSearch")
    int getCount();

    @Query("SELECT EXISTS(SELECT * FROM RecentSearch WHERE `query` = :query)")
    Boolean isItemExist(String query);

    @Delete
    void deleteTask(SearchItemDTO note);

    @Query("DELETE FROM RecentSearch")
    void truncateTable();
}