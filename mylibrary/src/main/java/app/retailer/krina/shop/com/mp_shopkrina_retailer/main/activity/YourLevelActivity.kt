package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityYourLevelBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnTargetLevelClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.YourLevelAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.YourLevelTargetModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class YourLevelActivity : AppCompatActivity(), OnTargetLevelClick {
    var mBinding: ActivityYourLevelBinding? = null
    var activity: YourLevelActivity? = null
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    var yourLevelAdapter: YourLevelAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_your_level)
        activity = this
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        utils = Utils(this)
        initViews()
    }


    private fun initViews() {
        mBinding!!.back.setOnClickListener { view: View? -> onBackPressed() }
        mBinding!!.rvYourLevel.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding!!.title.hint = MyApplication.getInstance().dbHelper.getData("customer_level")
        mBinding!!.tvTargetHead.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.you_are_a_sk_customer)
        callYourLevel()
    }

    private fun callYourLevel() {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this)
                commonClassForAPI!!.fetchYourLevelData(
                    yourLevelObserver,
                    SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID),
                    SharePrefs.getInstance(activity).getString(SharePrefs.SK_CODE)
                )
            }
        } else {
            startActivity(Intent(this, NoInternetActivity::class.java))
        }
    }

    private fun viewData(position: Int, model: YourLevelTargetModel) {
        if (position == 0) {
            mBinding!!.tvTargetHead.text =
                MyApplication.getInstance().dbHelper.getData("you_r_sk_customer")
            mBinding!!.tvOrderCount.visibility = View.GONE
            mBinding!!.tvOrderValue.visibility = View.GONE
            mBinding!!.tvOrderBrandValue.visibility = View.GONE
            mBinding!!.tvOrderKKValue.visibility = View.GONE
        } else {
            mBinding!!.tvTargetHead.text =
                MyApplication.getInstance().dbHelper.getData("level_following_task")
            mBinding!!.tvOrderCount.visibility = View.VISIBLE
            mBinding!!.tvOrderValue.visibility = View.VISIBLE
            mBinding!!.tvOrderBrandValue.visibility = View.VISIBLE
            mBinding!!.tvOrderKKValue.visibility = View.VISIBLE
            mBinding!!.tvOrderCount.text =
                MyApplication.getInstance().dbHelper.getData("place_minimum") + " " + model.orderCount + " " + MyApplication.getInstance().dbHelper.getData(
                    "order_in_month"
                )
            mBinding!!.tvOrderValue.text =
                MyApplication.getInstance().dbHelper.getData("order_must_be") + " " + model.volume + MyApplication.getInstance().dbHelper.getData(
                    "in_this_month"
                )
            mBinding!!.tvOrderBrandValue.text =
                MyApplication.getInstance().dbHelper.getData("order_atleast") + " " + model.brandCount + " " + MyApplication.getInstance().dbHelper.getData(
                    "different_brands"
                )
            mBinding!!.tvOrderKKValue.text =
                MyApplication.getInstance().dbHelper.getData("order_kisan_kirana") + model.kkVolume + "/-"
        }
    }

    override fun onSelectClick(position: Int, model: YourLevelTargetModel) {
        viewData(position, model)
    }


    private val yourLevelObserver: DisposableObserver<ArrayList<YourLevelTargetModel>> =
        object : DisposableObserver<ArrayList<YourLevelTargetModel>>() {
            override fun onNext(response: ArrayList<YourLevelTargetModel>) {
                try {
                    Utils.hideProgressDialog()
                    for (i in response.indices) {
                        if (response[i].isSelected) {
                            mBinding!!.tvCustomerLevel.text =
                                "Currently your are on " + response[i].levelName
                            viewData(i, response[i])
                        }
                    }
                    yourLevelAdapter = YourLevelAdapter(activity!!, response, activity!!)
                    mBinding!!.rvYourLevel.adapter = yourLevelAdapter
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }
}