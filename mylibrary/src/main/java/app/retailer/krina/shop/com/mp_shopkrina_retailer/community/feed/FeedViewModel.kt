package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment.PostCommentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw.MandiCheckModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw.MandiLocation
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.PostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.FollowingListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.FollowingResponceModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.UserFollowingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.UserLikeListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.UserProfileModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Network
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

 class FeedViewModel constructor(private val repository: FeedRepository) : ViewModel() {

    private var _FeedResponse = MutableLiveData<NetworkResult<FeedModel>>()
    val feedResponse: LiveData<NetworkResult<FeedModel>> = _FeedResponse

    private var _userResponse = MutableLiveData<NetworkResult<UserModel>>()
    val userResponse: LiveData<NetworkResult<UserModel>> = _userResponse

    private var _submitOptionResponse = MutableLiveData<NetworkResult<ArrayList<PollModel>>>()
    val submitPollResponse: LiveData<NetworkResult<ArrayList<PollModel>>> = _submitOptionResponse

    private var _likeResponse = MutableLiveData<NetworkResult<Boolean>>()
    val likeResponse: LiveData<NetworkResult<Boolean>> = _likeResponse

    private var _otherUserResponse = MutableLiveData<NetworkResult<UserProfileModel>>()
    val otherUserResponse: LiveData<NetworkResult<UserProfileModel>> = _otherUserResponse

    private var _userLikesResponse = MutableLiveData<NetworkResult<ArrayList<UserLikeListModel>>>()
    val userLikesResponse: LiveData<NetworkResult<ArrayList<UserLikeListModel>>> = _userLikesResponse

    private var _userFollowResponse = MutableLiveData<NetworkResult<FollowingResponceModel>>()
    val userFollowResponse: LiveData<NetworkResult<FollowingResponceModel>> = _userFollowResponse

    private var _userUnFollowResponse = MutableLiveData<NetworkResult<FollowingResponceModel>>()
    val userUnFollowResponse: LiveData<NetworkResult<FollowingResponceModel>> = _userUnFollowResponse

    private var _followerListResponse = MutableLiveData<NetworkResult<FollowingListModel>>()
    val followerListResponse: LiveData<NetworkResult<FollowingListModel>> = _followerListResponse

    private var _followingListResponse = MutableLiveData<NetworkResult<FollowingListModel>>()
    val followingListResponse: LiveData<NetworkResult<FollowingListModel>> = _followingListResponse

    private var _specificPostResponse = MutableLiveData<NetworkResult<FeedPostModel>>()
    val specificPostResponse: LiveData<NetworkResult<FeedPostModel>> = _specificPostResponse

    private var _notifictionCountResponse = MutableLiveData<NetworkResult<Int>>()
    val notifictionCountPostResponse: LiveData<NetworkResult<Int>> = _notifictionCountResponse

    private var _deletePostResponse = MutableLiveData<NetworkResult<String>>()
    val deletePostResponse: LiveData<NetworkResult<String>> = _deletePostResponse

    private var _getMandiBhavResponse = MutableLiveData<NetworkResult<MandiLocation>>()
    val mandiBhavResponse: LiveData<NetworkResult<MandiLocation>> = _getMandiBhavResponse


    fun getUser(customerID: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getUser(customerID).collect {
                    _userResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }


    fun getFeed(model: FeedRequestModel) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getFeed(model).collect {
                    _FeedResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }


    fun getSubmitPoll(pollModelResquest: PollModelResquest) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getSubmitPoll(pollModelResquest).collect {
                    _submitOptionResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun postLike(postLikeModelRequest: PostLikeModelRequest, likeCount: Int) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.postLike(postLikeModelRequest, likeCount).collect {
                    _likeResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun getOtherUser(customerID: String, otherUserID: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getOtherUser(customerID, otherUserID).collect {
                    _otherUserResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun getUserLikesList(postID: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getUserLikeList(postID).collect {
                    _userLikesResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun getUserFollowing(userFollowingModel: UserFollowingModel) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getUserFolling(userFollowingModel).collect {
                    _userFollowResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun getUserUnFlow(model: UserFollowingModel) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getUserUnFlow(model).collect {
                    _userUnFollowResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun getFollowerList(customerID: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getFollowerList(customerID).collect {
                    _followerListResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun getFollowingList(customerID: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getFollowingList(customerID).collect {
                    _followingListResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun getSpecificPost(postID: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getSpecificPost(postID).collect {
                    _specificPostResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun getNotifctionCount(userid: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getNotifctionCount(userid).collect {
                    _notifictionCountResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun postDelete(postId: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.postDelete(postId).collect {
                    _deletePostResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun getMandiUser(Longitute: Double,Laitutute: Double) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getMandiUser(Longitute,Laitutute).collect {
                    _getMandiBhavResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

}