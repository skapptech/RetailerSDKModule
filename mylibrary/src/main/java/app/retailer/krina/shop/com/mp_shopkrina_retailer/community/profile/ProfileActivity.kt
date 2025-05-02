package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.RestClient
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.CommentDialog
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedListener
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.SelectPostTypeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedFactory
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedRequestModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedViewModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.LikeListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PollAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PollModelResquest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PostLikeModelRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.AddPostActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityUserProfileBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.DialogFollowerListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.DialogLikeListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.DialogUnfollowUserBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.DialogUpdatePostBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ProgressDialog
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity(), FeedListener, LikeListAdapter.LikeUserCall,
    PollAdapter.PollProgressListener, FollowingListAdapter.FollowingListener,
    FollowerListAdapter.FollowerListener, OnButtonClick {

    private lateinit var feedViewModel: FeedViewModel
    lateinit var mBinding: ActivityUserProfileBinding
    private lateinit var likeListBinding: DialogLikeListBinding
    private lateinit var updatePostBinding: DialogUpdatePostBinding
    private lateinit var dialogUnfollowUserBinding: DialogUnfollowUserBinding
    private lateinit var unfollowBottomDialog: BottomSheetDialog
    private lateinit var followerListBinding: DialogFollowerListBinding
    private lateinit var followingDialog: BottomSheetDialog
    private var isPollSubmit: Boolean = true
    private var positionClick: Int = 0
    private var PageCount = 0
    private var loading = true
    private var followinglistSigleClick = true
    private var follwerslistSigleClick = true
    private var feedList: ArrayList<FeedPostModel>? = ArrayList()
    private var feedAdapter: ProfileFeedAdapter? = null
    private var comeFrom: String? = null
    private lateinit var userProfileModel: UserProfileModel
    val analyticPost = AnalyticPost()
    private var source: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        comeFrom = intent.getStringExtra("comeFrom")
        source = intent.getStringExtra("source")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        init()
        callApi()

        if (comeFrom.equals("Bottom")) {
            RetailerSDKApp.getInstance().noteRepository.fetchProfileFeeds(SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString()
            ).observe(this) {
                if (feedList != null && feedList!!.isNotEmpty()) {
                    feedList?.clear()
                    feedList?.addAll(it)
                    feedAdapter?.notifyDataSetChanged()
                }
            }
        } else if (comeFrom.equals("Notifiction")) {

            RetailerSDKApp.getInstance().noteRepository.fetchNotifctinFeedsontop(intent.getStringExtra("userId")!!, intent.getStringExtra("postID"))
                .observe(this) {
                if (feedList != null && feedList!!.isNotEmpty()) {
                    feedList?.clear()
                    feedList?.addAll(it)
                    Log.e("Data Base", "DataBase_Responce: "+it.size )
                    feedAdapter?.notifyDataSetChanged()
              }
           }
        } else if (comeFrom.equals("Profile")) {
            RetailerSDKApp.getInstance().noteRepository.fetchProfileFeeds(intent.getStringExtra("userId")!!).observe(this) {
                if (feedList != null && feedList!!.isNotEmpty()) {
                    feedList?.clear()
                    feedList?.addAll(it)
                    feedAdapter?.notifyDataSetChanged()
                }
            }
        }

        //for analysis
        if (source.equals("icon") || source.equals("navbar")) {
            analyticPost.eventName = "myProfileView"
            analyticPost.source = source
            RetailerSDKApp.getInstance().mixpanel.timeEvent("myProfileExit")
            RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
        } else {
            analyticPost.eventName = "userProfileView"
            analyticPost.source = source
            RetailerSDKApp.getInstance().mixpanel.timeEvent("userProfileExit")
            RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (source.equals("icon") || source.equals("navbar")) {
            analyticPost.eventName = "myProfileExit"
            analyticPost.source = source
            RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
        } else {
            analyticPost.eventName = "userProfileExit"
            analyticPost.source = source
            RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
        }
    }

    private fun init() {
        val repository = FeedRepository(RestClient.getInstance4().service4)
        feedViewModel = ViewModelProvider(this, FeedFactory(repository))[FeedViewModel::class.java]
        val layoutManager = LinearLayoutManager(applicationContext)
        mBinding.rvFeed.layoutManager = layoutManager

        mBinding.rvFeed.isNestedScrollingEnabled = false
        mBinding.toolbarPost.btnPost.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext, EditProfileActivity::class.java
                ).putExtra("UserData", userProfileModel)
            )

        }

        mBinding.toolbarPost.btnNewPost.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext, SelectPostTypeActivity::class.java
                )
            )
        }

        mBinding.toolbarPost.back.setOnClickListener {
            onBackPressed()
        }

        mBinding.container.setOnRefreshListener {
            mBinding.container.isRefreshing = false
            PageCount = 0
            callApi()
        }

        mBinding.rlUnfollow.setOnClickListener {
            mBinding.rlUnfollow.isClickable=false
            unFollowPost()
        }

        mBinding.FollwerBT.setOnClickListener {
            mBinding.FollwerBT.isClickable=false
            feedViewModel.getUserFollowing(
                UserFollowingModel(
                    SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                    userProfileModel.CustomerId
                )
            )
        }


        mBinding.followinglist.setOnClickListener {
            if (userProfileModel.totalFollowings > 0) {
                if (followinglistSigleClick) {
                    followinglistSigleClick = false
                    feedViewModel.getFollowingList(userProfileModel.CustomerId)

                }
            }

        }

        mBinding.follwerslist.setOnClickListener {
            if (userProfileModel.totalFollowers > 0) {
                if (follwerslistSigleClick) {
                    follwerslistSigleClick = false
                    feedViewModel.getFollowerList(userProfileModel.CustomerId)
                }
            }
        }

        feedAdapter = ProfileFeedAdapter(this, feedList!!, this, this)
        mBinding.rvFeed.adapter = feedAdapter


        mBinding.mScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            Log.i("Nested", "BOTTOM SCROLL")
                            PageCount += 10
                            checkWhereIsComing()
                        }
                    }
                }
            }
        }

        feedViewModel.feedResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    mBinding.progressLoad.visibility = View.VISIBLE
                }

                is NetworkResult.Failure -> {
                    mBinding.progressLoad.visibility = View.GONE
                    Log.e("TAG", "Failure: " + it.errorMessage)
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    mBinding.progressLoad.visibility = View.GONE
                    if (it.data != null) {
                        if (it.data.list != null && it.data.list.size > 0) {
                            mBinding.tvPost.text = "" + it.data.count

                            feedList!!.addAll(it.data.list)
                            feedAdapter!!.notifyDataSetChanged()
                            loading = true
                        } else {
                            loading = false
                        }
                    }
                    if (feedList!!.size == 0) {
                        mBinding.tvNoPostAvailable.visibility = View.VISIBLE
                        mBinding.tvUploadedPost.visibility = View.GONE
                    } else {
                        mBinding.tvNoPostAvailable.visibility = View.GONE
                        mBinding.tvUploadedPost.visibility = View.VISIBLE
                    }

                    if (comeFrom.equals("Notifiction")) {
                        mBinding.mScrollView.post { mBinding.mScrollView.scrollTo(0, 900) }
                    }
                }
            }
        }
        feedViewModel.submitPollResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    isPollSubmit = true
                    feedAdapter!!.notifyDataSetChanged()
                }
            }
        }
        feedViewModel.likeResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    if (it.data) {
                    }

                }
            }
        }
        feedViewModel.otherUserResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Log.e("otherUserResponse", "Failure: " + it.errorMessage)
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    userProfileModel = it.data
                    mBinding.totalFollowers.text = "" + userProfileModel.totalFollowers
                    mBinding.totalFollowings.text = "" + userProfileModel.totalFollowings
                    mBinding.TvShopName.text = "" + userProfileModel.ShopName
                    mBinding.tvMobileNumber.text = "" + userProfileModel.Mobile
                    mBinding.tvCity.text = "" + userProfileModel.City
                    try {
                        if (userProfileModel.Name.isNullOrEmpty()) {
                            mBinding.UserName.text = "Guest User " + userProfileModel.CustomerId
                        } else {
                            mBinding.UserName.text = "" + userProfileModel.Name
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    if (it.data.img.isNotEmpty()) {
                        Picasso.get().load(it.data.img).into(mBinding.profileImage)
                    } else {
                        mBinding.profileImage.setImageResource(R.drawable.profile)
                    }

                    if (userProfileModel.City.isNotEmpty()) {
                        mBinding.tvCity.visibility = View.VISIBLE
                    } else {
                        mBinding.tvCity.visibility = View.INVISIBLE
                    }
                    if (SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
                            .toString() == userProfileModel.CustomerId
                    ) {
                        mBinding.toolbarPost.btnPost.visibility = View.VISIBLE
                        mBinding.toolbarPost.btnNewPost.visibility = View.VISIBLE
                        mBinding.FollwerBT.visibility = View.GONE
                    } else {
                        mBinding.toolbarPost.btnPost.visibility = View.INVISIBLE
                        mBinding.toolbarPost.btnNewPost.visibility = View.INVISIBLE
                        if (it.data.isFollowed) {
                            followingUser()
                        } else {
                            unFollowingUser()
                        }
                    }
                    checkWhereIsComing()
                }
            }
        }
        feedViewModel.userLikesResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    val adapterLikeList = LikeListAdapter(this, it.data, this)
                    likeListBinding.rvLikeList.adapter = adapterLikeList

                }
            }
        }
        feedViewModel.userFollowResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    if (it.data.status) {
                        followingUser()
                        PageCount = 0
                        callApi()
                        ProgressDialog.getInstance().show(this)

                    }


                }
            }
        }
        feedViewModel.userUnFollowResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    if (it.data.status) {
                        ProgressDialog.getInstance().dismiss()
                        dialogUnfollowUserBinding.llDeletePost.isClickable =true
                        unfollowBottomDialog.dismiss()
                        unFollowingUser()
                        PageCount = 0
                        callApi()


                    }

                }
            }
        }
        feedViewModel.followingListResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    if (it.data.status) {
                        followinglistSigleClick = true
                        if (it.data.res != null && it.data.res.size > 0) {
                            followingList("following")
                            val adapterLikeList = FollowingListAdapter(this, it.data.res, this)
                            followerListBinding.rvLikeList.adapter = adapterLikeList
                        } else {
                            Toast.makeText(this, "No following ", Toast.LENGTH_SHORT).show()
                        }
                    }


                }
            }
        }
        feedViewModel.followerListResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    if (it.data.status) {
                        if (it.data.res != null && it.data.res.size > 0) {
                            follwerslistSigleClick = true
                            followingList("follwers")
                            val adapterLikeList = FollowerListAdapter(this, it.data.res, this)
                            followerListBinding.rvLikeList.adapter = adapterLikeList
                        } else {
                            Toast.makeText(this, "No Follower ", Toast.LENGTH_SHORT).show()
                        }

                    }


                }
            }
        }
        feedViewModel.specificPostResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    feedList!!.add(0, it.data)
                }
            }
        }
        feedViewModel.deletePostResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)

                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    if (it.data.equals("the post has been deleted")) {
                        feedAdapter!!.notifyDataSetChanged()
                        callApi()
                    }
                }
            }
        }
    }

    private fun checkWhereIsComing() {
        if (comeFrom.equals("Bottom")) {
            feedViewModel.getFeed(
                FeedRequestModel(
                    PageCount,
                    10,
                    SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                    SharePrefs.getInstance(this).getString(SharePrefs.USER_CUTOMER_COMMUNITY),
                    true,
                    SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                    "0", ""
                )
            )
        } else if (comeFrom.equals("Notifiction")) {
            feedViewModel.getFeed(
                FeedRequestModel(
                    PageCount,
                    10,
                    SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                    SharePrefs.getInstance(this).getString(SharePrefs.USER_CUTOMER_COMMUNITY),
                    true,
                    intent.getStringExtra("userId")!!,
                    "0", intent.getStringExtra("postID")!!
                )
            )

        } else if (comeFrom.equals("Profile")) {
            feedViewModel.getFeed(
                FeedRequestModel(
                    PageCount,
                    10,
                    SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                    SharePrefs.getInstance(this).getString(SharePrefs.USER_CUTOMER_COMMUNITY),
                    true,
                    intent.getStringExtra("userId")!!,
                    "0", ""
                )
            )

        } else if (comeFrom.equals("Follow")) {
            feedViewModel.getFeed(
                FeedRequestModel(
                    PageCount,
                    10,
                    SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                    SharePrefs.getInstance(this).getString(SharePrefs.USER_CUTOMER_COMMUNITY),
                    true,
                    intent.getStringExtra("userId")!!,
                    "0", ""
                )
            )

        }
    }

    private fun callApi() {
        if (comeFrom.equals("Bottom")) {
            feedViewModel.getOtherUser(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString()
            )
        } else if (comeFrom.equals("Notifiction")) {
            feedViewModel.getOtherUser(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                intent.getStringExtra("userId")!!
            )
        } else if (comeFrom.equals("Profile")) {
            feedViewModel.getOtherUser(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                intent.getStringExtra("userId")!!
            )
        } else if (comeFrom.equals("Follow")) {
            feedViewModel.getOtherUser(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                intent.getStringExtra("userId")!!
            )
        }
    }

    private fun followingUser() {
        mBinding.FollwerBT.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.drawable_button_unfolow
            )
        )
        mBinding.FollwerBT.text = "फॉलोइंग"
        mBinding.FollwerBT.isClickable = false
        val params: ViewGroup.LayoutParams? = mBinding.FollwerBT.layoutParams
        params!!.width = 500
        mBinding.FollwerBT.layoutParams = params
        mBinding.rlUnfollow.visibility = View.VISIBLE
    }

    private fun unFollowingUser() {
        mBinding.FollwerBT.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.drawable_button_bg_blue
            )
        )
        mBinding.FollwerBT.text = "फॉलो"
        mBinding.FollwerBT.isClickable = true
        val params: ViewGroup.LayoutParams? = mBinding.FollwerBT.layoutParams
        params!!.width = 1200
        mBinding.FollwerBT.layoutParams = params
        mBinding.rlUnfollow.visibility = View.GONE
    }

    override fun likePost(postId: String, like: Int, likeCount: Int, postType: String?) {
        feedViewModel.postLike(
            PostLikeModelRequest(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(), postId, like
            ), likeCount
        )

        if (like == 1) {
            analyticPost.eventName = "postLike"
        } else {
            analyticPost.eventName = "postUnlike"
        }
        analyticPost.postId = postId
        analyticPost.postType = postType
        analyticPost.source = "Profile"
        RetailerSDKApp.getInstance().updateAnalytics(analyticPost)

    }

    override fun openComments(model: FeedPostModel) {
        RetailerSDKApp.getInstance().isCommentOpen = true
        CommentDialog.newInstance(model).show(supportFragmentManager, "a")
    }

    override fun sharePost(model: FeedPostModel) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/*"
        share.putExtra(
            Intent.EXTRA_TEXT,
            "" + model.desc + "\n" + SharePrefs.getInstance(RetailerSDKApp.application).getString(SharePrefs.TRADE_WEB_URL) + "/" + model.userId + "/" + model.postId
        )
        startActivity(Intent.createChooser(share, "Share Post"))

        //analysis
        analyticPost.eventName = "share"
        analyticPost.postId = model.postId
        analyticPost.source = "Profile"
        analyticPost.likeCount = model.likeCount
        analyticPost.postType = model.postType
        analyticPost.commentCount = model.commentCount
        RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
    }

    override fun following(userID: String) {
        feedViewModel.getUserFollowing(
            UserFollowingModel(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(), userID
            )
        )

    }

    override fun openProfile(postId: String) {

    }

    override fun likeList(model: FeedPostModel) {
        val likeListBottomDialog = BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog)
        likeListBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_like_list, null, false)
        likeListBottomDialog.setContentView(likeListBinding.root)
        feedViewModel.getUserLikesList(model.postId)
        likeListBottomDialog.show()

        likeListBinding.imClose.setOnClickListener {
            likeListBottomDialog.dismiss()
        }

        analyticPost.eventName = "likesUserPopUp"
        analyticPost.source = "Profile"
        analyticPost.postId = model.postId
        analyticPost.postType = model.postType
        analyticPost.likeCount = model.likeCount
        analyticPost.commentCount = model.commentCount
        RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
    }

    override fun editPost(feeModel: FeedPostModel) {
        val updatePostBottomDialog = BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog)
        updatePostBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_update_post, null, false)
        updatePostBottomDialog.setContentView(updatePostBinding.root)
        updatePostBottomDialog.show()
        if (feeModel.postType == "Poll") {
            updatePostBinding.llEditPost.visibility = View.GONE
        } else {
            updatePostBinding.llEditPost.visibility = View.VISIBLE
        }

        updatePostBinding.llEditPost.setOnClickListener {
            updatePostBottomDialog.dismiss()
            startActivity(
                Intent(applicationContext, AddPostActivity::class.java).putExtra(
                    "comeFrom",
                    "EditProfile"
                ).putExtra("model", feeModel)
            )
        }

        updatePostBinding.llDeletePost.setOnClickListener {
            updatePostBottomDialog.dismiss()
            deletePost(feeModel.postId)
        }

        updatePostBinding.imClose.setOnClickListener {
            updatePostBottomDialog.dismiss()
        }
    }

    private fun deletePost(postId: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View = LayoutInflater.from(applicationContext)
            .inflate(R.layout.dialog_delete_post, viewGroup, false)
        val deletePost = dialogView.findViewById<TextView>(R.id.tvDelete)
        val cancleDialog = dialogView.findViewById<TextView>(R.id.tvCancle)
        builder.setView(dialogView)
        val alertDialog: AlertDialog = builder.create()
        cancleDialog.setOnClickListener { alertDialog.dismiss() }
        deletePost.setOnClickListener {
            alertDialog.dismiss()
            feedViewModel.postDelete(postId)
        }
        alertDialog.show()
    }

    private fun unFollowPost() {
         unfollowBottomDialog = BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog)
        dialogUnfollowUserBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_unfollow_user, null, false)
        unfollowBottomDialog.setContentView(dialogUnfollowUserBinding.root)
        unfollowBottomDialog.show()
        mBinding.rlUnfollow.isClickable=true


        dialogUnfollowUserBinding.llDeletePost.setOnClickListener {
            dialogUnfollowUserBinding.llDeletePost.isClickable =false
            unfollowBottomDialog.dismiss()
            feedViewModel.getUserUnFlow(
                UserFollowingModel(
                    SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                    userProfileModel.CustomerId
                )
            )

        }

        dialogUnfollowUserBinding.imClose.setOnClickListener {
            unfollowBottomDialog.dismiss()
        }
    }

    private fun followingList(comeFrom: String) {
        followingDialog = BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog)
        followerListBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_follower_list, null, false)
        followingDialog.setContentView(followerListBinding.root)
        if (comeFrom.equals("following")) {
            followerListBinding.tvLable.text = "फॉलोविंग लिस्ट"
        } else {
            followerListBinding.tvLable.text = "फॉलोवर्स लिस्ट"

        }
        followingDialog.show()

        followerListBinding.imClose.setOnClickListener {
            followingDialog.dismiss()
        }

        //analysis
        if (comeFrom.equals("following")) {
            analyticPost.eventName = "followingPopUp"
        } else {
            analyticPost.eventName = "followersPopUp"
        }
        analyticPost.source = "Profile"
        RetailerSDKApp.getInstance().updateAnalytics(analyticPost)

    }

    override fun progressCheck(postId: String, _id: String, position: Int, isPollSubmit: Boolean) {
        positionClick = position
        this.isPollSubmit = isPollSubmit
        feedViewModel.getSubmitPoll(
            PollModelResquest(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                Utils.currentDateTime(),
                _id,
                postId
            )
        )
    }

    override fun callLikeUser(userID: String) {
        startActivity(
            Intent(this, ProfileActivity::class.java).putExtra("comeFrom", "Profile")
                .putExtra("userId", userID)
                .putExtra("source", "likesList")
        )
    }

    override fun followingUserList(userID: String) {
        followingDialog.dismiss()
        startActivity(
            Intent(this, ProfileActivity::class.java).putExtra("comeFrom", "Profile")
                .putExtra("userId", userID)
                .putExtra("source", "followingsList")
        )
    }

    override fun FollowerUserList(userID: String) {
        followingDialog.dismiss()
        startActivity(
            Intent(this, ProfileActivity::class.java).putExtra("comeFrom", "Profile")
                .putExtra("userId", userID)
                .putExtra("source", "followersList")
        )
    }

    override fun onButtonClick(pos: Int, itemAdded: Boolean) {
        feedAdapter?.notifyDataSetChanged()
    }
}