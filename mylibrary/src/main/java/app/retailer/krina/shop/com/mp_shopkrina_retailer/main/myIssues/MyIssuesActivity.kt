package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.myIssues

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyIssueBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.MyIssueListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyIssuesResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class MyIssuesActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMyIssueBinding

    private lateinit var adapter: MyIssueListAdapter
    private var issueList: ArrayList<MyIssuesResponseModel> = ArrayList()
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null

    private var loading = true
    private var skipCount: Int = 0
    private var takeCount: Int = 10
    private var pastVisibleItems = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var customerId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_issue)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()
        mBinding.tvNoTicket.text =
            MyApplication.getInstance().dbHelper.getString(R.string.no_ticket)
        title = MyApplication.getInstance().dbHelper.getString(R.string.title_activity_my_issue)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    fun init() {
        utils = Utils(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        mBinding.liFabAddIssue.setOnClickListener {
            startActivity(Intent(applicationContext, AddIssueActivity::class.java))
            finish()
        }
        customerId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        val layoutManager = LinearLayoutManager(this)
        mBinding.rvIssue.layoutManager = layoutManager
        adapter = MyIssueListAdapter(this, issueList)
        mBinding.rvIssue.adapter = adapter

        mBinding.rvIssue.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loading = false
                            skipCount += takeCount
                            mBinding!!.progressBar.visibility = View.VISIBLE
                            callApi()
                        }
                    }
                }
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    mBinding.liFabAddIssue.visibility = View.VISIBLE
                } else {
                    mBinding.liFabAddIssue.visibility = View.GONE
                }
            }
        })
    }

    fun callApi() {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                commonClassForAPI!!.fetchIssueTickets(
                    generateTicketDis,
                    customerId,
                    skipCount,
                    takeCount
                )
            }
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.no_internet)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        Utils.showProgressDialog(this)
        issueList.clear()
        skipCount = 0
        callApi()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private var generateTicketDis: DisposableObserver<ArrayList<MyIssuesResponseModel>> =
        object : DisposableObserver<ArrayList<MyIssuesResponseModel>>() {
            override fun onNext(response: ArrayList<MyIssuesResponseModel>) {
                Utils.hideProgressDialog()
                mBinding!!.progressBar.visibility = View.GONE
                if (response.size > 0) {
                    mBinding!!.rvIssue.visibility = View.VISIBLE
                    mBinding!!.emptyTicket.visibility = View.GONE
                    issueList.addAll(response)
                    adapter.notifyDataSetChanged()
                    loading = true
                } else {
                    loading = false
                }
            }

            override fun onError(e: Throwable) {
                mBinding!!.rvIssue.visibility = View.GONE
                mBinding!!.emptyTicket.visibility = View.VISIBLE
                Utils.hideProgressDialog()
                e.printStackTrace()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }
}