package app.retailer.krina.shop.com.mp_shopkrina_retailer.community

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedFactory
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedRequestModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedViewModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.LikeListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PollAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PollModelResquest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PostLikeModelRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.galleryimage.GalleryImageAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw.MandiBhavActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.notification.UserNotificationActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.AddPostActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.FollowingResponceModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.ProfileActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.UserFollowingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.RestClient
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityFeedBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.DialogLikeListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.DialogUpdatePostBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.GPSTracker
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ProgressDialog
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream

class FeedActivity : AppCompatActivity(), FeedListener, LikeListAdapter.LikeUserCall,
    PollAdapter.PollProgressListener, GalleryImageAdapter.PostImageFromGallery, OnButtonClick {

    lateinit var mBinding: ActivityFeedBinding
    private val imagePaths = mutableListOf<String>()
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var likeListBinding: DialogLikeListBinding
    private lateinit var likeListBottomDialog: BottomSheetDialog
    private lateinit var updatePostBinding: DialogUpdatePostBinding

    private var isPollSubmit: Boolean = true
    private var positionClick: Int = 0
    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var randomNumber = 0
    private var PageCount = 0
    private var loading = true
    val analyticPost = AnalyticPost()

    private var feedList: ArrayList<FeedPostModel>? = ArrayList()
    private var feedAdapter: FeedAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        init()
        setBottomBar()
        if (intent.extras != null) {
            try {
                val uri = intent.data
                val url = uri.toString()
                val list = url.split("/")
                val postId = list[list.size - 1]
                val userId = list[list.size - 2]
                println(postId + userId)

                startActivity(
                    Intent(this, ProfileActivity::class.java)
                        .putExtra("comeFrom", "Notifiction")
                        .putExtra("userId", userId)
                        .putExtra("postID", postId)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        randomNumber = (0..10).random()

        MyApplication.getInstance().noteRepository.fetchAllFeeds().observe(this) {
            if (feedList != null && feedList!!.isNotEmpty()) {
                feedList?.clear()
                feedList?.addAll(it)
                //   dynamicViewCreated(feedList!!)
                feedAdapter?.notifyDataSetChanged()
            }
        }
        if (SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID) == 0) {
            val intent = Intent(applicationContext, MobileSignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }


    override fun onResume() {
        super.onResume()
        try {
            MyApplication.getInstance().mixpanel.timeEvent("feedExit")
            MyApplication.getInstance().updateAnalytics("feedView")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        feedViewModel.getNotifctionCount(
            SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString()
        )
        feedAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.getInstance().updateAnalytics("feedExit")
    }


    private fun init() {
        mBinding.tvBrandStore.text =
            MyApplication.getInstance().dbHelper.getString(R.string.brand_store)
        mBinding.tvSellerStore.text =
            MyApplication.getInstance().dbHelper.getString(R.string.seller_store)

        val repository = FeedRepository(RestClient.getInstance4().service4)
        feedViewModel = ViewModelProvider(this, FeedFactory(repository))[FeedViewModel::class.java]
        val layoutManager = LinearLayoutManager(applicationContext)
        mBinding.rvFeed.layoutManager = layoutManager

        feedAdapter = FeedAdapter(this, feedList!!, this, this, this, imagePaths)
        mBinding.rvFeed.adapter = feedAdapter
        // loadGalleryImages(this)

        mBinding.llBrandStore.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        mBinding.rlsearchFeed.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    SelectPostTypeActivity::class.java
                )
            )
        }
        mBinding.rlnotifiction.setOnClickListener {
            notificationsActivity()
            //analysis
            analyticPost.eventName = "notificationClick"
            analyticPost.source = "Feed"
            MyApplication.getInstance().mixpanel.timeEvent("notificationClickExit")
            MyApplication.getInstance().updateAnalytics(analyticPost)
        }
        mBinding.profileImage.setOnClickListener {
            profileActivity("icon")
        }
        mBinding.createPostFB.setOnClickListener {
            analyticPost.eventName = "newPostClick"
            analyticPost.source = "feedScreen"
            MyApplication.getInstance().updateAnalytics(analyticPost)
            startActivity(
                Intent(
                    applicationContext, SelectPostTypeActivity::class.java
                )
            )
        }
        mBinding.rvFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            PageCount += 10
                            mBinding.progressLoad.visibility = View.VISIBLE
                            callApi()
                        }
                    }
                }

                if (dy > 0 && mBinding.bottomNav.isShown) {
                    slideDown()
                } else if (dy < 0) {
                    slideUp()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        mBinding.container.setOnRefreshListener {
            mBinding.container.isRefreshing = false
            PageCount = 0
            feedViewModel.getNotifctionCount(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString()
            )
            refreshCallApi()
        }

        feedViewModel.getUser(
            SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString()
        )
        feedViewModel.userResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                }

                is NetworkResult.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    if (it.data != null) {
                        SharePrefs.getInstance(this).putString(SharePrefs.USER_NAME, it.data.Name)
                        SharePrefs.getInstance(this)
                            .putString(SharePrefs.USER_SHOPNAME, it.data.ShopName)
                        SharePrefs.getInstance(this)
                            .putString(SharePrefs.USER_MOBILE_NUMBER, it.data.Mobile)
                        SharePrefs.getInstance(this)
                            .putString(SharePrefs.USER_CUTOMER_COMMUNITY, it.data.id_str)

                        if (it.data.imgfilefullpath!!.isNotEmpty()) {

                            Glide.with(this)
                                .load(it.data.imgfilefullpath)
                                .placeholder(R.drawable.sk_icon_rounded)
                                .error(R.drawable.sk_icon_rounded)
                                .into(mBinding.profileImage)
                            Picasso.get().load(it.data.imgfilefullpath).into(mBinding.profileImage)
                        } else {
                            mBinding.profileImage.setImageResource(R.drawable.sk_icon_rounded)
                        }
                        callApi()
                    }
                }
            }
        }
        feedViewModel.feedResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    mBinding.shimmerFrameLayout.startShimmer()
                    mBinding.progressLoad.visibility = View.VISIBLE
                }

                is NetworkResult.Failure -> {
                    mBinding.progressLoad.visibility = View.GONE
                    Log.e("TAG", "Failure: " + it.errorMessage)
                }

                is NetworkResult.Success -> {
                    mBinding.progressLoad.visibility = View.GONE
                    mBinding.rvFeed.visibility = View.VISIBLE
                    mBinding.llProfileView.visibility = View.VISIBLE
                    mBinding.shimmerFrameLayout.stopShimmer()
                    mBinding.shimmerFrameLayout.visibility = View.GONE

                    if (it.data != null) {
                        if (it.data.list != null && it.data.list.size > 0) {
                            feedList!!.addAll(it.data.list)
                            //dynamicViewCreated(feedList!!)
                            feedAdapter!!.notifyDataSetChanged()
                            loading = true
                        } else {
                            loading = false
                        }
                    }
                    if (feedList!!.size == 0) {
                        mBinding.tvNoPostAvailable.visibility = View.VISIBLE
                    } else {
                        mBinding.tvNoPostAvailable.visibility = View.GONE
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
                        val followingResponceModel: FollowingResponceModel = it.data
                        MyApplication.getInstance().noteRepository.followUpdate(
                            followingResponceModel.res.followeeId.toInt(), true
                        )
                    }
                }
            }
        }
        feedViewModel.deletePostResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                    mBinding.progressLoad.visibility = View.VISIBLE

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

    private fun slideDown() {
        mBinding.bottomNav.visibility = View.GONE
       // mBinding.createPostFB.collapse()
       // mBinding.createPostFB.setIconActionButton(R.drawable.collapes_image)
        val animate = TranslateAnimation(
            0f,
            0f,
            0f,
            0f
        )

        animate.duration = 300
        animate.fillAfter = true
        mBinding.bottomNav.startAnimation(animate)
        val animateTwo = TranslateAnimation(
            0f,
            0f,
            0f,
            mBinding.createPostFB.height.toFloat()
        ) // toYDelta

        animateTwo.duration = 300
        animateTwo.fillAfter = true
        mBinding.createPostFB.startAnimation(animate)
    }

    private fun slideUp() {
        mBinding.bottomNav.visibility = View.VISIBLE
       // mBinding.createPostFB.expand()
        val animateOne = TranslateAnimation(
            0f,  // fromXDelta
            0f,  // toXDelta
            mBinding.bottomNav.height.toFloat(),  // fromYDelta
            0f
        ) // toYDelta

        animateOne.duration = 300
        animateOne.fillAfter = true
        mBinding.createPostFB.startAnimation(animateOne)
        val animate = TranslateAnimation(
            0f,  // fromXDelta
            0f,  // toXDelta
            mBinding.bottomNav.height.toFloat(),  // fromYDelta
            0f
        ) // toYDelta

        animate.duration = 300
        animate.fillAfter = true
        mBinding.bottomNav.startAnimation(animate)
    }


    private fun dynamicViewCreated(feedList: ArrayList<FeedPostModel>) {
        feedList.add(
            if (randomNumber > feedList.size) 0 else randomNumber,
            (FeedPostModel(
                "",
                "",
                "",
                "",
                "",
                "",
                "Add_post_images",
                0,
                false,
                0,
                false,
                "",
                "",
                "",
                "",
                "",
                false,
                null
            ))
        )
    }

    private fun setBottomBar() {
        mBinding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    "" /*  R.id.search -> return "loadFragment(SearchFragment())"*/
                    analyticPost.eventName = "navBarClick"
                    analyticPost.section = "home"
                    MyApplication.getInstance().updateAnalytics(analyticPost)
                }

                R.id.notifications -> {
                    getCurrentLatLong()
                    analyticPost.eventName = "navBarClick"
                    analyticPost.section = "notifications"
                    MyApplication.getInstance().updateAnalytics(analyticPost)
                }

                R.id.profile -> {
                    analyticPost.eventName = "navBarClick"
                    analyticPost.section = "profile"
                    MyApplication.getInstance().updateAnalytics(analyticPost)
                    profileActivity("navbar")
                }
            }
            false
        }
        feedViewModel.getNotifctionCount(
            SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString()
        )
        feedViewModel.notifictionCountPostResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()

                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    if (it.data > 0) {
                        mBinding.notificationBadge.visibility = View.VISIBLE
                        mBinding.notificationBadge.text = "" + it.data
                    } else {
                        mBinding.notificationBadge.visibility = View.GONE
                        mBinding.notificationBadge.text = ""
                    }


                    //  notificationCount()
                }
            }
        }
    }

    private fun getCurrentLatLong() {
        val permissions =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            )

        Permissions.check(
            this@FeedActivity,
            permissions,
            null,
            null,
            object : PermissionHandler() {
                override fun onGranted() {
                    val gpsTracker = GPSTracker(this@FeedActivity)
                    if (gpsTracker.canGetLocation()) {
                        val latitude = gpsTracker.latitude
                        val longitude = gpsTracker.longitude
                        callMandiBhav(latitude, longitude)
                    } else {
                        gpsTracker.showSettingsAlert()
                    }
                }

                override fun onDenied(
                    context: Context,
                    deniedPermissions: ArrayList<String>
                ) {
                }
            })
    }

    private fun callMandiBhav(latitude: Double, longitude: Double) {
        feedViewModel.getMandiUser(longitude, latitude)
        feedViewModel.mandiBhavResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().show(this)
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()

                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().show(this)
                    startActivity(
                        Intent(this, MandiBhavActivity::class.java).putExtra(
                            "StateName",
                            it.data.StateName
                        ).putExtra("DistrictName", it.data.Name)
                    )

                }
            }
        }
    }

    private fun notificationCount() {
        if (SharePrefs.getInstance(this).getInt(SharePrefs.NOTIFICTION_User_COUNT) > 0) {
            mBinding.notificationBadge.visibility = View.VISIBLE
            mBinding.notificationBadge.text =
                SharePrefs.getInstance(this).getInt(SharePrefs.NOTIFICTION_User_COUNT).toString()
        } else {
            mBinding.notificationBadge.visibility = View.GONE
        }
    }

    private fun notificationsActivity() {
        startActivity(Intent(applicationContext, UserNotificationActivity::class.java))
    }

    private fun profileActivity(source: String?) {
        startActivity(
            Intent(applicationContext, ProfileActivity::class.java).putExtra("comeFrom", "Bottom")
                .putExtra("source", source)
        )
    }

    private fun callApi() {
        mBinding.shimmerFrameLayout.startShimmer()
        feedViewModel.getFeed(
            FeedRequestModel(
                feedList?.size ?: 0,
                10,
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                SharePrefs.getInstance(this).getString(SharePrefs.USER_CUTOMER_COMMUNITY),
                false,
                "0", "0", ""
            )
        )
    }

    private fun refreshCallApi() {
        mBinding.shimmerFrameLayout.startShimmer()
        feedViewModel.getFeed(
            FeedRequestModel(
                PageCount,
                10,
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                SharePrefs.getInstance(this).getString(SharePrefs.USER_CUTOMER_COMMUNITY),
                false,
                "0", "0", ""
            )
        )
    }


    override fun likePost(postId: String, like: Int, likeCount: Int, postType: String?) {
        feedViewModel.postLike(
            PostLikeModelRequest(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(), postId, like
            ), likeCount
        )

        //analysis
        if (like == 1) {
            analyticPost.eventName = "postLike"
        } else {
            analyticPost.eventName = "postUnlike"
        }
        analyticPost.postId = postId
        analyticPost.postType = postType
        analyticPost.source = "feed"
        MyApplication.getInstance().updateAnalytics(analyticPost)
    }

    override fun openComments(model: FeedPostModel) {
//        if (this::commentListDialog.isInitialized && commentListDialog.isShowing)
//            commentListDialog.dismiss()
        MyApplication.getInstance().isCommentOpen = true
        CommentDialog.newInstance(model).show(supportFragmentManager, "a")
    }

    override fun sharePost(model: FeedPostModel) {
        if (model.imageObj?.size!! > 0) {
            val imageUri = Uri.parse(
                SharePrefs.getInstance(this)
                    .getString(SharePrefs.TRADE_WEB_URL) + model.imageObj?.get(0)?.imgFileFullPath
            )
            Picasso.get().load(imageUri).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    // val shareIntent = Intent(Intent.ACTION_SEND)
                    //   shareIntent.type = "image/*"
                    //  shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap))
                    // shareIntent.putExtra(Intent.EXTRA_TEXT, "" + model.desc + "\n" + Constant.COMMUNITY_URL + "/" + model.userId + "/" + model.postId)
                    //   startActivity(Intent.createChooser(shareIntent, "Share Image"))

                    val textToShare =
                        model.desc + "\n" + "kisankirana.in" + "/" + model.userId + "/" + model.postId
                    ShareCompat.IntentBuilder(this@FeedActivity)
                        .setType("image/*")
                        .setText(textToShare)
                        .setSubject("Share Post")
                        .addStream(getLocalBitmapUri(bitmap)!!)
                        .setChooserTitle("Share Post")
                        .startChooser()
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    // Handle placeholder image, if needed
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    // Handle image loading failure, if needed
                }
            })
        } else {
            val textToShare =
                model.desc + "\n" + "kisankirana.in" + "/" + model.userId + "/" + model.postId
            ShareCompat.IntentBuilder(this@FeedActivity)
                .setType("text/*")
                .setText(textToShare)
                .setSubject("Share Post")
                .setChooserTitle("Share Post")
                .startChooser()
        }

        //analysis
        analyticPost.eventName = "share"
        analyticPost.postId = model.postId
        analyticPost.source = "Feed"
        analyticPost.likeCount = model.likeCount
        analyticPost.postType = model.postType
        analyticPost.commentCount = model.commentCount
        MyApplication.getInstance().updateAnalytics(analyticPost)
    }

    private fun getLocalBitmapUri(bitmap: Bitmap?): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + "image.png"
            )
            val outStream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 90, outStream)
            outStream.flush()
            outStream.close()
            bmpUri =
                FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bmpUri
    }


    override fun following(userID: String) {
        feedViewModel.getUserFollowing(
            UserFollowingModel(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(), userID
            )
        )

    }

    override fun openProfile(postId: String) {
        startActivity(
            Intent(this, ProfileActivity::class.java).putExtra("comeFrom", "Profile")
                .putExtra("userId", postId)
                .putExtra("source", "feed")
        )

    }

    override fun likeList(feedModel: FeedPostModel) {
        likeListBottomDialog = BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog)
        likeListBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_like_list, null, false)
        likeListBottomDialog.setContentView(likeListBinding.root)
        feedViewModel.getUserLikesList(feedModel.postId)
        likeListBottomDialog.show()

        likeListBinding.imClose.setOnClickListener {
            likeListBottomDialog.dismiss()
        }

        //analysis
        analyticPost.eventName = "likesUserPopUp"
        analyticPost.source = "feed"
        analyticPost.postId = feedModel.postId
        analyticPost.postType = feedModel.postType
        analyticPost.likeCount = feedModel.likeCount
        analyticPost.commentCount = feedModel.commentCount
        MyApplication.getInstance().updateAnalytics(analyticPost)
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
                Intent(applicationContext, AddPostActivity::class.java)
                    .putExtra("comeFrom", "EditProfile")
                    .putExtra("model", feeModel)
            )
        }

        updatePostBinding.llDeletePost.setOnClickListener {
            updatePostBottomDialog.dismiss()
            deletePost(feeModel)
        }

        updatePostBinding.imClose.setOnClickListener {
            updatePostBottomDialog.dismiss()
        }
    }

    private fun deletePost(feedModel: FeedPostModel) {
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
            feedViewModel.postDelete(feedModel.postId)

            //analysis
            analyticPost.eventName = "deletePost"
            analyticPost.postId = feedModel.postId
            analyticPost.postType = feedModel.postType
            MyApplication.getInstance().updateAnalytics(analyticPost)
        }
        alertDialog.show()
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
        likeListBottomDialog.dismiss()
        startActivity(
            Intent(applicationContext, ProfileActivity::class.java).putExtra("comeFrom", "Profile")
                .putExtra("userId", userID)
                .putExtra("source", "likesList")
        )
    }

    override fun postImagesFromGalley(path: String, pos: Int) {
        try {
            val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
            Permissions.check(this, permissions, null, null, object : PermissionHandler() {
                override fun onGranted() {
                    startActivity(
                        Intent(applicationContext, AddPostActivity::class.java)
                            .putExtra("comeFrom", "Feed")
                            .putExtra("imagePath", path)
                    )
                }

                override fun onDenied(
                    context: Context, deniedPermissions: java.util.ArrayList<String>
                ) {
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadGalleryImages(context: Context) {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )
        var count = 0
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                if (count >= 6) break
                val imagePath = it.getString(columnIndex)
                imagePaths.add(imagePath)
                count++
            }
        }

        feedAdapter!!.notifyDataSetChanged()
    }

    override fun onButtonClick(pos: Int, itemAdded: Boolean) {
        feedAdapter?.notifyDataSetChanged()
    }
}