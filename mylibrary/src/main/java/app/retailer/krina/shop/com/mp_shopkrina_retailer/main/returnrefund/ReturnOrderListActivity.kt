package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.returnrefund


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityReturnOrderListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.ReturnOrderListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.StoryBordSharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.DismissType
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.Gravity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.GuideListener
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.GuideView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class ReturnOrderListActivity : AppCompatActivity(), View.OnClickListener {
    private var mBinding: ActivityReturnOrderListBinding? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var mGuideView: GuideView? = null
    private var builder: GuideView.Builder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityReturnOrderListBinding.inflate(getLayoutInflater());
        setContentView(mBinding!!.getRoot());
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = RetailerSDKApp.getInstance().dbHelper.getData("title_activity_return_order")
        initViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_new_request) {
            startActivityForResult(Intent(applicationContext, ReturnReplaceActivity::class.java), 9)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9 && resultCode == Activity.RESULT_OK) {
            callAPI()
        }
    }


    private fun initViews() {

        mBinding!!.tvEmpty.text =
            RetailerSDKApp.getInstance().dbHelper.getData("text_you_have_nt_placed_any_return_replace_order_request")
        mBinding!!.btnNewRequest.text =
            RetailerSDKApp.getInstance().dbHelper.getData("text_new_request")

        mBinding!!.btnNewRequest.setOnClickListener(this)
        mBinding!!.recyclerOffer.layoutManager = LinearLayoutManager(applicationContext)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        callAPI()
        if (!StoryBordSharePrefs.getInstance(this@ReturnOrderListActivity)
                .getBoolean(StoryBordSharePrefs.RETURNORDER)
        ) {
            appStoryView()
        }
    }

    private fun appStoryView() {
        builder = GuideView.Builder(this)
            .setTitle(RetailerSDKApp.getInstance().dbHelper.getData("New_Request"))
            .setContentText(RetailerSDKApp.getInstance().dbHelper.getData("New_Request_detail"))
            .setGravity(Gravity.center)
            .setDismissType(DismissType.anywhere)
            .setTargetView(mBinding!!.btnNewRequest)
            .setGuideListener(GuideListener { view ->
                when (view.id) {
                    R.id.btn_new_request -> {
                        StoryBordSharePrefs.getInstance(this@ReturnOrderListActivity)
                            .putBoolean(StoryBordSharePrefs.RETURNORDER, true)
                        return@GuideListener
                    }
                }
                mGuideView = builder!!.build()
                mGuideView!!.show()
            })
        mGuideView = builder!!.build()
        mGuideView!!.show()
    }

    private fun callAPI() {
        Utils.showProgressDialog(this)
        commonClassForAPI!!.getReturnReplaceOrders(
            orderObserver,
            SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        )
    }

    private val orderObserver: DisposableObserver<ArrayList<ReturnOrderListModel>> =
        object : DisposableObserver<ArrayList<ReturnOrderListModel>>() {
            override fun onNext(list: ArrayList<ReturnOrderListModel>) {
                try {
                    Utils.hideProgressDialog()
                    if (list.size > 0) {
                        mBinding!!.tvEmpty.visibility = View.GONE
                        val adapter = ReturnOrderListAdapter(this@ReturnOrderListActivity, list)
                        mBinding!!.recyclerOffer.adapter = adapter
                    } else {
                        mBinding!!.tvEmpty.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    mBinding!!.tvEmpty.visibility = View.VISIBLE
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                mBinding!!.tvEmpty.visibility = View.VISIBLE
            }

            override fun onComplete() {}
        }
}