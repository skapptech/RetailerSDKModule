package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api

import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment.PostCommentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedRequestModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PollModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PollModelResquest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PostLikeModelRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.UserModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw.MandiBhavModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw.MandiCheckModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw.MandiDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw.MandiLocation
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.notification.UserNotificationModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.CommentPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.PostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.FollowingListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.FollowingResponceModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.UserFollowingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.UserLikeListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.UserProfileModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.UserUpdate
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface APIServiceCom {

    @GET("api/User/GetUserById")
    suspend fun getUser(@Query("userId") customerId: String): UserModel

    @POST("api/Posts/GetPost")
    suspend fun getFeed(@Body model: FeedRequestModel): FeedModel

    @POST("api/posts/newPostV2")
    suspend fun newPost(@Body model: PostModel): JsonObject

    @Multipart
    @POST("api/uploadimage/upload")
    suspend fun uploadPostImg(@Part body: MultipartBody.Part?): JsonObject

    @POST("api/posts/submitOption")
    suspend fun getSubmitPoll(@Body pollModelResquest: PollModelResquest): ArrayList<PollModel>

    @POST("api/posts/likePost")
    suspend fun postLike(@Body likeModelRequest: PostLikeModelRequest): Boolean

    @GET("api/Follow/GetUserProfileWithFollow/{loggedinUserId}/{profileUserId}")
    suspend fun getOtherUser(
        @Path("loggedinUserId") customerId: String,
        @Path("profileUserId") otherUserProfileId: String
    ): UserProfileModel

    @GET("api/posts/GetLikesByPostID/{id}")
    suspend fun getUserLikeList(@Path("id") customerId: String): ArrayList<UserLikeListModel>

    @POST("api/Follow/FollowRequest")
    suspend fun getUserFolling(@Body userFollowingModel: UserFollowingModel): FollowingResponceModel

    @POST("api/Follow/UnfollowRequest")
    suspend fun getUserUnfollw(@Body model: UserFollowingModel): FollowingResponceModel

    @GET("api/Posts/GetPostById")
    suspend fun getCommentList(
        @Query("postId") id: String,
        @Query("userid") userId: Int
    ): PostCommentModel

    @POST("api/posts/addComment")
    suspend fun postComment(@Body model: CommentPostModel): Boolean

    @POST("api/posts/likePost")
    suspend fun postCommentLike(@Body model: PostLikeModelRequest): Boolean

    @POST("api/posts/addComment")
    suspend fun postCommentReply(@Body model: CommentPostModel): Boolean

    @POST("api/posts/EditPost")
    suspend fun editComment(@Body model: PostModel): JsonObject

    @POST("api/posts/DeletePost/{id}")
    suspend fun deleteComment(
        @Path("id") postId: String
    ): Boolean

    @POST("api/posts/DeletePost/{id}")
    suspend fun deleteReplyInComment(
        @Path("id") postId: String,
    ): Boolean

    @GET("api/userNotification/GetNotification/{skip}/{take}/{CustomerId}")
    suspend fun getNotification(
        @Path("skip") skip: Int,
        @Path("take") take: Int,
        @Path("CustomerId") CustomerId: String,
    ): ArrayList<UserNotificationModel>


    @GET("api/Follow/FollowerList/{userId}")
    suspend fun getFollowerList(@Path("userId") customerId: String): FollowingListModel

    @GET("api/Follow/FolloweeList/{userId}")
    suspend fun getFollowingList(@Path("userId") customerId: String): FollowingListModel

    @GET("api/userNotification/Read/{id}")
    suspend fun getNotificationReadNotification(@Path("id") notificationId: String): Boolean

    @GET("api/posts/getSpecificPost/{postID}")
    suspend fun getSpecificPost(@Path("postID") customerId: String): FeedPostModel

    @GET("api/userNotification/GetNotificationCount/{userid}")
    suspend fun getNotificationCount(@Path("userid") customerId: String): Int

    @POST("api/posts/DeletePost/{userid}")
    suspend fun postDelete(@Path("userid") customerId: String): String

    @POST("api/posts/EditPost")
    suspend fun editPost(@Body model: PostModel): JsonObject

    @POST("api/User/UpdateUser")
    suspend fun UpdateUser(@Body pollModelResquest: UserUpdate): UserUpdate

    @GET("api/MandiBhav/GetNearByDistrict")
    suspend fun GetUserDistrict(
        @Query("Longitute") Longitute: Double,
        @Query("Latitute") Latitute: Double
    ): MandiLocation

    @POST("api/MandiBhav/AddUserMandiBhavDistrict")
    suspend fun addUserMandiBhavDistrict(@Body model: MandiBhavModel): MandiCheckModel

    @GET("api/MandiBhav/GetStates")
    suspend fun GetStates(): ArrayList<String>

    @GET("api/MandiBhav/GetDistrict")
    suspend fun GetDistrict(@Query("StateName") StateName: String): ArrayList<String>

    @GET("api/MandiBhav/GetMandiBhav")
    suspend fun getMandiBhav(
        @Query("StateName") StateName: String,
        @Query("DistrictName") DistrictName: String
    ): ArrayList<MandiDataModel>
}