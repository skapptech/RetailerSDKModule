package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PostLikeModelRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.CommentPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.PostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Network
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentViewModel constructor(private val repository: CommentRepository) : ViewModel() {

    var _commentListResponse = SingleLiveEvent<NetworkResult<PostCommentModel>>()
    val commentListResponse: LiveData<NetworkResult<PostCommentModel>> = _commentListResponse

    private var _postCommentResponse = SingleLiveEvent<NetworkResult<Boolean>>()
    val postCommentResponse: LiveData<NetworkResult<Boolean>> = _postCommentResponse

    private var _likeCommentResponse = SingleLiveEvent<NetworkResult<Boolean>>()
    val likeCommentResponse: LiveData<NetworkResult<Boolean>> = _likeCommentResponse

    private var _postReplyCommentResponse = SingleLiveEvent<NetworkResult<Boolean>>()
    val postReplyCommentResponse: LiveData<NetworkResult<Boolean>> = _postReplyCommentResponse


    private var _editCommentResponse = SingleLiveEvent<NetworkResult<JsonObject>>()
    val editCommentResponse: LiveData<NetworkResult<JsonObject>> = _editCommentResponse


    private var _deleteCommentResponse = SingleLiveEvent<NetworkResult<Boolean>>()
    val deleteCommentResponse: LiveData<NetworkResult<Boolean>> = _deleteCommentResponse

    private var _deleteCommentReplyResponse = SingleLiveEvent<NetworkResult<Boolean>>()
    val deleteCommentReplyResponse: LiveData<NetworkResult<Boolean>> = _deleteCommentReplyResponse


    fun getCommentList(postId: String, userId: Int) {
        if (Network.checkConnectivity(MyApplication.getInstance())) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getCommentList(postId, userId).collect() {
                    _commentListResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun postComment(commentCount: Int, model: CommentPostModel) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.postComment(commentCount, model).collect() {
                    _postCommentResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun postCommentLike(postId: String, commentId: String, model: PostLikeModelRequest) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.postCommentLike(model).collect() {
                    _likeCommentResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun postCommentReply(
        postId: String,
        commentId: String,
        commentCount: Int,
        model: CommentPostModel
    ) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.postCommentReply(postId, commentCount, model).collect() {
                    _postReplyCommentResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun editComment(postId: String, commentId: String, model: PostModel) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.editComment(postId, commentId, model).collect() {
                    _editCommentResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun deleteComment(postId: String, commentId: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteComment(postId, commentId).collect() {
                    _deleteCommentResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun deleteReplyInComment(postId: String, commentId: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteReplyInComment(postId, commentId).collect() {
                    _deleteCommentReplyResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }
}