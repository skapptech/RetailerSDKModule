package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityPendingPaymentBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.PendingPaymentAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.PendingPaymentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat

class PendingPaymentActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPendingPaymentBinding
    private var pendingPaymentList: ArrayList<PendingPaymentModel>? = null
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPendingPaymentBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        initview()

        val data = intent.getStringExtra("data")
        try {
            val jsonObject = JSONObject(data)
            val jsonArray = jsonObject.getJSONArray("LadgerEntryList")
            if (jsonArray.length() > 0) {
                for (a in 0 until jsonArray.length()) {
                    val jsonObject1 = jsonArray.getJSONObject(a)
                    val ID = jsonObject1.optString("ID")
                    val Date = jsonObject1.optString("Date")
                    val Particulars = jsonObject1.optString("Particulars")
                    val LagerID = jsonObject1.optString("LagerID")
                    val VouchersTypeID = jsonObject1.optString("VouchersTypeID")
                    val VouchersNo = jsonObject1.optString("VouchersNo")
                    var Debit = 0.0
                    if (!jsonObject1.isNull("Debit")) Debit = jsonObject1.optDouble("Debit")
                    val Credit = jsonObject1.optString("Credit")
                    val ObjectID = jsonObject1.optString("ObjectID")
                    val ObjectType = jsonObject1.optString("ObjectType")
                    val AffectedLadgerID = jsonObject1.optString("AffectedLadgerID")
                    val Active = jsonObject1.optString("Active")
                    val CreatedBy = jsonObject1.optString("CreatedBy")
                    val CreatedDate = jsonObject1.optString("CreatedDate")
                    val CreditString = jsonObject1.optString("CreditString")
                    val DebitString = jsonObject1.optString("DebitString")
                    val LadgerName = jsonObject1.optString("LadgerName")
                    val VoucherName = jsonObject1.optString("VoucherName")
                    pendingPaymentList!!.add(
                        PendingPaymentModel(
                            ID,
                            Date,
                            Particulars,
                            LagerID,
                            VouchersTypeID,
                            VouchersNo,
                            Debit,
                            Credit,
                            ObjectID,
                            ObjectType,
                            AffectedLadgerID,
                            Active,
                            CreatedBy,
                            CreatedDate,
                            CreditString,
                            DebitString,
                            LadgerName,
                            VoucherName
                        )
                    )

//                    if (Debit != null) {
                    total = total + Debit
                    //                    }
                }
                val pendingPaymentAdapter = PendingPaymentAdapter(this, pendingPaymentList)
                mBinding.pendRv.adapter = pendingPaymentAdapter
                pendingPaymentAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getData("no_data_available"),
                    Toast.LENGTH_SHORT
                ).show()
            }
            val amount =
                Html.fromHtml("<font color=#FF4500>&#8377;" + DecimalFormat("#,###.##").format(total))
                    .toString()
            mBinding.txtCloseBal.text = amount
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private fun initview() {
        mBinding.toolbarPending.title.text =
            RetailerSDKApp.getInstance().dbHelper.getData("pending_payment")
        mBinding.tvTotalAmountP.text = RetailerSDKApp.getInstance().dbHelper.getData("total_amnt")
        pendingPaymentList = ArrayList()
        mBinding.toolbarPending.back.setOnClickListener { onBackPressed() }
    }
}