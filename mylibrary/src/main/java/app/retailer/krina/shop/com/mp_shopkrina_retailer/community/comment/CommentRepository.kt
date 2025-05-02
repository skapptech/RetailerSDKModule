package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.APIServiceCom
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PostLikeModelRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.CommentPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.PostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.UserFollowingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CommentRepository constructor(private val apiServices: APIServiceCom) {

    suspend fun getCommentList(postId: String, userId: Int) =
        flow {
            emit(NetworkResult.Loading(true))
            val response = apiServices.getCommentList(postId, userId)
            emit(NetworkResult.Success(response))
        }.catch { e ->
            emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
        }

    suspend fun postLike(postLikeModelRequest: PostLikeModelRequest, likeCount: Int) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.postLike(postLikeModelRequest)
        if (response)
            RetailerSDKApp.getInstance().noteRepository.updateLike(
                postLikeModelRequest.postId, postLikeModelRequest.likeStatus, likeCount
            )

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

    suspend fun postComment(commentCount: Int, model: CommentPostModel) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.postComment(model)
        if (response != null)
            RetailerSDKApp.getInstance().noteRepository.updateCommentCount(
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
                RetailerSDKApp.getInstance().noteRepository.updateCommentCount(
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
            val count = RetailerSDKApp.getInstance().noteRepository.getCommentCount(postId)
            RetailerSDKApp.getInstance().noteRepository.updateCommentCount(postId, count - 1)
        }
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun deleteReplyInComment(postId: String, commentId: String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.deleteReplyInComment(commentId)
        if (response != null) {
            val count = RetailerSDKApp.getInstance().noteRepository.getCommentCount(postId)
            RetailerSDKApp.getInstance().noteRepository.updateCommentCount(postId, count - 1)
        }
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }
}