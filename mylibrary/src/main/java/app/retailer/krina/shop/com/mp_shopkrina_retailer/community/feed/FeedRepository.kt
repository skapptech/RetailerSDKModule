package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.APIServiceCom
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.CommentPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.PostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.UserFollowingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class FeedRepository constructor(private val apiServices: APIServiceCom) {
    suspend fun getUser(customerID: String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.getUser(customerID)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getFeed(model: FeedRequestModel) =
        flow {
            emit(NetworkResult.Loading(true))
            val response = apiServices.getFeed(model)
            if (response.list != null && response.list.size > 0)
                MyApplication.getInstance().noteRepository.insertFeed(response.list)
            emit(NetworkResult.Success(response))
        }.catch { e ->
            emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
        }

    suspend fun getSubmitPoll(request: PollModelResquest) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.getSubmitPoll(request)
        if (response != null && response.size > 0)
            MyApplication.getInstance().noteRepository.updateFeedPoll(
                request.postId, response.size > 0, response
            )
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun postLike(postLikeModelRequest: PostLikeModelRequest, likeCount: Int) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.postLike(postLikeModelRequest)
        if (response)
            MyApplication.getInstance().noteRepository.updateLike(
                postLikeModelRequest.postId, postLikeModelRequest.likeStatus, likeCount
            )

        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getOtherUser(customerID: String, othertUserID: String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.getOtherUser(customerID, othertUserID)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getUserLikeList(postID: String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.getUserLikeList(postID)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getUserFolling(userFollowingModel: UserFollowingModel) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.getUserFolling(userFollowingModel)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getUserUnFlow(model: UserFollowingModel) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.getUserUnfollw(model)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getCommentList(postId: String, userId: Int) =
        flow {
            emit(NetworkResult.Loading(true))
            val response = apiServices.getCommentList(postId, userId)
            emit(NetworkResult.Success(response))
        }.catch { e ->
            emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
        }

    suspend fun postComment(commentCount: Int, model: CommentPostModel) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.postComment(model)
        if (response != null)
            MyApplication.getInstance().noteRepository.updateCommentCount(
                model.PostId,
                commentCount + 1
            )
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun postCommentLike(model: PostLikeModelRequest) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.postCommentLike(model)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun postCommentReply(postId: String, commentCount: Int, model: CommentPostModel) =
        flow {
            emit(NetworkResult.Loading(true))
            val response = apiServices.postCommentReply(model)
            if (response != null)
                MyApplication.getInstance().noteRepository.updateCommentCount(
                    postId,
                    commentCount + 1
                )
            emit(NetworkResult.Success(response))
        }.catch { e ->
            emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
        }

    suspend fun editComment(postId: String, commentId: String, model: PostModel) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.editComment(model)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun deleteComment(postId: String, commentId: String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.deleteComment(commentId)
        if (response != null) {
            val count = MyApplication.getInstance().noteRepository.getCommentCount(postId)
            MyApplication.getInstance().noteRepository.updateCommentCount(postId, count - 1)
        }
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun deleteReplyInComment(postId: String, commentId: String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.deleteReplyInComment(commentId)
        if (response != null) {
            val count = MyApplication.getInstance().noteRepository.getCommentCount(postId)
            MyApplication.getInstance().noteRepository.updateCommentCount(postId, count - 1)
        }
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getFollowerList(customerID: String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.getFollowerList(customerID)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getFollowingList(customerID: String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.getFollowingList(customerID)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getSpecificPost(postID: String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.getSpecificPost(postID)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getNotifctionCount(userId: String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.getNotificationCount(userId)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun postDelete(postId: String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.postDelete(postId)
        if (response != null)
            MyApplication.getInstance().noteRepository.deletePost(postId)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun editPost(model: PostModel) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.editPost(model)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getMandiUser(Longitute: Double, Laitutute: Double) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.GetUserDistrict(Longitute, Laitutute)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }
}