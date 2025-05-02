package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedPostModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PollModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;

@Dao
public interface FeedDao {

    @Insert
    Long insertTask(FeedPostModel note);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(List<FeedPostModel> model);


    @Transaction
    @Query("SELECT * FROM Feed order by createdDate DESC")
    LiveData<List<FeedPostModel>> fetchAllFeeds();

    @Transaction
    @Query("SELECT * FROM Feed WHERE userId=:customerID Order by createdDate DESC")
    LiveData<List<FeedPostModel>> fetchProfileFeeds(String customerID);

    @Query("SELECT * FROM Feed WHERE postId =:postId")
    LiveData<FeedPostModel> getTask(int postId);

    @Transaction
    @Query("SELECT EXISTS(SELECT * FROM Feed WHERE postId = :postId)")
    Boolean isItemExist(int postId);

    @Transaction
    @Query("SELECT EXISTS(SELECT * FROM Feed WHERE postId = :postId)")
    Boolean isItemNotify(int postId);

    @Update
    void updateTask(ItemListModel note);

    @Query("UPDATE Feed SET isPollAnswerGiven = :isPollAnswerGiven, pollValue = :pollList WHERE postId = :postId")
    void updateFeedPoll(String postId, Boolean isPollAnswerGiven, ArrayList<PollModel> pollList);

    @Query("UPDATE Feed SET likeStatus = :likeStatus, likeCount = :likeCount WHERE postId = :postId")
    void updateUserLike(String postId, int likeStatus, int likeCount);

    @Transaction
    @Query("UPDATE Feed SET isFollowed=:isFollow WHERE userId =:customerID")
    void updateFollowUser(int customerID, Boolean isFollow);

    @Transaction
    @Query("SELECT * FROM Feed WHERE userId=:customerID ORDER BY postId=:postID DESC")
    LiveData<List<FeedPostModel>> fetchNotictionFeedsOnTop(String customerID, String postID);

    @Query("UPDATE Feed SET commentCount = :commentCount WHERE postId = :postId")
    void updateCommentCount(String postId, int commentCount);

    @Query("SELECT commentCount FROM Feed WHERE postId = :postId")
    int getCommentCount(String postId);

    @Query("DELETE FROM Feed WHERE postId = :postId")
    void deletePost(String postId);

    @Query("DELETE FROM Feed")
    void deleteFeed();
}
