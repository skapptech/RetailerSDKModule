package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.notification

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.RestClient
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.ProfileActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityUserNotificationBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult

class UserNotificationActivity : AppCompatActivity(),
    UserNotificationAdapter.UserNotificationListener {
    private lateinit var binding: ActivityUserNotificationBinding
    private lateinit var viewModel: UserNotificationViewModel
    private lateinit var userNotificationAdapter: UserNotificationAdapter
    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var PageCount = 0
    private var loading = true
    private var notificationList: ArrayList<UserNotificationModel>? = ArrayList()
    val analyticPost = AnalyticPost()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("नोटिफिकेशन")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = UserNotificationRepository(RestClient.getInstance4().service4)
        viewModel = ViewModelProvider(
            this,
            UserNotificationFactory(repository)
        )[UserNotificationViewModel::class.java]
        initView()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        SharePrefs.getInstance(this).putInt(SharePrefs.NOTIFICTION_User_COUNT, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        notificationList?.clear()
        userNotificationAdapter.notifyDataSetChanged()
        PageCount = 0
        callApi()
    }

    override fun onDestroy() {
        super.onDestroy()
        //analysis
        analyticPost.eventName = "notificationClickExit"
        analyticPost.source = "Notification"
        RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(applicationContext)
        binding.rvNotification.layoutManager = layoutManager
        userNotificationAdapter = UserNotificationAdapter(this, notificationList!!, this)
        binding.rvNotification.adapter = userNotificationAdapter

        binding.rvNotification.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            PageCount += 15
                            callApi()
                        }
                    }
                }
            }
        })

        binding.container.setOnRefreshListener {
            binding.container.isRefreshing = false
            notificationList!!.clear()
            userNotificationAdapter.notifyDataSetChanged()
            PageCount = 0
            callApi()
        }

        viewModel.userNotificationResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressLoad.visibility = View.VISIBLE
                }

                is NetworkResult.Failure -> {
                    binding.progressLoad.visibility = View.GONE
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    binding.progressLoad.visibility = View.GONE
                    if (it.data != null && it.data.size > 0) {
                        notificationList!!.addAll(it.data)
                        userNotificationAdapter.notifyDataSetChanged()
                        loading = true
                    } else {
                        loading = false
                        //binding.tvNoDataFound.visibility =View.VISIBLE
                    }

                    if (notificationList!!.size == 0) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                    }


                }
            }
        }
        viewModel.readNotificationResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressLoad.visibility = View.VISIBLE
                }

                is NetworkResult.Failure -> {
                    binding.progressLoad.visibility = View.GONE
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    binding.progressLoad.visibility = View.GONE


                }
            }
        }

    }

    private fun callApi() {
        viewModel.getNotification(
            PageCount,
            15,
            SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString()
        )

    }

    override fun readUserNotification(
        noticationID: String,
        notificationType: Int,
        userId: String,
        postedByUserId: String
    ) {
        viewModel.getNotificationReadNotification(noticationID)

        if (notificationType == 1) {
            startActivity(
                Intent(this, ProfileActivity::class.java)
                    .putExtra("comeFrom", "Follow")
                    .putExtra("userId", userId)
                    .putExtra("source", "notificationFollow")

            )
        } else if (notificationType == 2) {
            startActivity(
                Intent(this, ProfileActivity::class.java)
                    .putExtra("comeFrom", "Notifiction")
                    .putExtra("userId", postedByUserId)
                    .putExtra("postID", userId)
                    .putExtra("source", "notification")
            )
            analyticPost.postId = userId

        } else if (notificationType == 3) {
            startActivity(
                Intent(this, ProfileActivity::class.java)
                    .putExtra("comeFrom", "Notifiction")
                    .putExtra(
                        "userId",
                        SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString()
                    )
                    .putExtra("postID", postedByUserId)
                    .putExtra("source", "notification")
            )
            analyticPost.postId = userId

        } else if (notificationType == 4) {
            startActivity(
                Intent(this, ProfileActivity::class.java)
                    .putExtra("comeFrom", "Notifiction")
                    .putExtra(
                        "userId",
                        SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString()
                    )
                    .putExtra("postID", userId)
                    .putExtra("source", "notification")
            )
        }

        //analysis
        analyticPost.eventName = "notificationsDetailClick"
        analyticPost.source = "Notification"
        RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
    }

}