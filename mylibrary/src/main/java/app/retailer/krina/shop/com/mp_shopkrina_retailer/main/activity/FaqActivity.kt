package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityFaqBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.FaqAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.myIssues.MyIssuesActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.FaqModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class FaqActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityFaqBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_faq)
        mBinding.toolbarFaq.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.help)
        mBinding.tvIssueCat.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.my_issues)

        mBinding.toolbarFaq.back.setOnClickListener { onBackPressed() }
        val utils = Utils(this)
        val commonClassForAPI = CommonClassForAPI.getInstance(this)
        if (utils.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this)
                commonClassForAPI.getVideoList(video)
            } else {
                Toast.makeText(applicationContext, "null", Toast.LENGTH_SHORT).show()
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_SHOW_TICKET_MENU)) {
            mBinding.ticketCartView.visibility = View.VISIBLE
            mBinding.MyIssues.setOnClickListener {
                startActivity(
                    Intent(
                        applicationContext, MyIssuesActivity::class.java
                    )
                )
            }
        }
    }


    private val video: DisposableObserver<ArrayList<FaqModel>> =
        object : DisposableObserver<ArrayList<FaqModel>>() {
            override fun onNext(list: ArrayList<FaqModel>) {
                Utils.hideProgressDialog()
                if (list != null && list.size > 0) {
                    val faqAdapter = FaqAdapter(this@FaqActivity, list)
                    mBinding.rvFaq.adapter = faqAdapter
//                } else {
//                    Utils.setToast(
//                        applicationContext,
//                        MyApplication.getInstance().dbHelper.getString(R.string.help_and_support_will_start_soon)
//                    )
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
            }
        }
}