package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityContactUsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityDeliveryConcernBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.DeliveryConcern
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat

class DeliveryConcernActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDeliveryConcernBinding
    private var orderId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDeliveryConcernBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            orderId = intent.getIntExtra("orderId", 0);
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }


    private fun init() {
        mBinding.tvCommentH.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.comment) + " :"

        Utils.showProgressDialog(this)
        CommonClassForAPI.getInstance(this).getOrderConcernByOrderId(observer, orderId)

        mBinding.btnAdd.setOnClickListener {
            postConcern()
        }
    }

    private fun postConcern() {
        if (mBinding.etComment.text.toString().isEmpty()) {
            mBinding.etComment.error =
                RetailerSDKApp.getInstance().noteRepository.getString(R.string.write_your_comment_here)
        } else {
            val jsonObject = JsonObject()
            jsonObject.addProperty("OrderId", orderId)
            jsonObject.addProperty("CustComment", mBinding.etComment.text.toString())
            CommonClassForAPI.getInstance(this).postOrderConcern(postObserver, jsonObject)
        }
    }


    var observer: DisposableObserver<DeliveryConcern> =
        object : DisposableObserver<DeliveryConcern>() {
            override fun onNext(jsonObject: DeliveryConcern) {
                Utils.hideProgressDialog()
                try {
                    mBinding.tvOrderId.text = "" + jsonObject.orderId
                    mBinding.tvSkCode.text = "" + jsonObject.skcode
                    mBinding.tvShopName.text = "" + jsonObject.shopName
                    mBinding.tvOrderDate.text = "" + Utils.getDateTimeFormate(jsonObject.orderDate)
                    mBinding.tvDeliveryDate.text =
                        "" + Utils.getDateTimeFormate(jsonObject.deliveryDate)
                    mBinding.tvAmount.text = "" + DecimalFormat("##.##").format(jsonObject.amount)
                    mBinding.tvIssueRaise.text = "" + jsonObject.custComment
                    mBinding.tvStatus.text = "" + jsonObject.status
                    mBinding.tvUpdateDate.text =
                        "" + Utils.getDateTimeFormate(jsonObject.modifiedDate)
                    mBinding.tvComment.text = "" + jsonObject.cDComment
//                    if (jsonObject.status == "Resolved") {
//                        mBinding.etComment.visibility = View.VISIBLE
//                        mBinding.btnAdd.visibility = View.VISIBLE
//                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }

    var postObserver: DisposableObserver<Boolean> =
        object : DisposableObserver<Boolean>() {
            override fun onNext(jsonObject: Boolean) {
                Utils.hideProgressDialog()
                try {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().noteRepository.getString(R.string.succesfullSubmitted)
                    )
                    onBackPressed()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }
}