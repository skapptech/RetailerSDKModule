package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.Manifest
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityLedgerSummaryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.SupplierPaymentAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.SupplierPaymentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.LadgerEntryListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.SupplierDocModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class LedgerSummaryActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLedgerSummaryBinding

    private var ladgerEntryList: ArrayList<LadgerEntryListModel>? = null
    private var customerId = 0
    private var startDate: String? = ""
    private var EndDate: String? = ""
    private var type: String? = ""
    private var utils: Utils? = null
    private var referenceId: Long = 0
    private var downloadManager: DownloadManager? = null
    private val list = ArrayList<Long>()
    private var commonClassForAPI: CommonClassForAPI? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ledger_summary)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val args = intent.getBundleExtra("BUNDLE")
        ladgerEntryList = args!!.getSerializable("data") as ArrayList<LadgerEntryListModel>?

        initviews()

        startDate = chngeFormat(args.getString("StartDate"))
        EndDate = chngeFormat(args.getString("endDate"))
        type = args.getString("type")
        if (type.equals("SR", ignoreCase = true)) {
            supportActionBar!!.title =
                RetailerSDKApp.getInstance().dbHelper.getData("ledger_summary")
        } else {
            supportActionBar!!.title = RetailerSDKApp.getInstance().dbHelper.getData("details")
        }
        mBinding.tvFromD.text = RetailerSDKApp.getInstance().dbHelper.getData("from")
        mBinding.tvToD.text = RetailerSDKApp.getInstance().dbHelper.getData("to")
        mBinding.tvOpeningBal.text = RetailerSDKApp.getInstance().dbHelper.getData("opening_balance")
        mBinding.tvClosingBal.text = RetailerSDKApp.getInstance().dbHelper.getData("closing_balance")
        val openBal = args.getString("open")
        val closeBal = args.getString("close")
        mBinding.txtOpenBal.text = Html.fromHtml(
            "<font color=#FF4500>&#8377;" + DecimalFormat("#,###.##").format(
                openBal!!.toDouble()
            )
        ).toString()
        mBinding.txtCloseBal.text = Html.fromHtml(
            "<font color=#FF4500>&#8377;" + DecimalFormat("#,###.##").format(
                closeBal!!.toDouble()
            )
        ).toString()
        mBinding.txtFrmDate.text = args.getString("StartDate")
        mBinding.txtEndDate.text = args.getString("endDate")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_SHOW_LEDGER)) {
            menuInflater.inflate(R.menu.ledger, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.convert_pdf -> if (isStoragePermissionGranted) {
                convertPDF()
            }
            R.id.convert_excel -> if (isStoragePermissionGranted) {
                convertExcel()
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onComplete)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private fun convertPDF() {
        if (utils!!.isNetworkAvailable) {
            val supplierPaymentModel =
                SupplierPaymentModel(customerId, startDate, EndDate, 1, false, type)
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this)
                commonClassForAPI!!.CustomerLedgerPDF(customerLedgerPDF, supplierPaymentModel)
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getData("internet_connection")
            )
        }
    }

    private fun initviews() {
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        customerId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        utils = Utils(this)
        mBinding.rvSupplierPayment.layoutManager = LinearLayoutManager(applicationContext)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED)
        if (ladgerEntryList!!.size > 0) {
            val supplierPaymentAdapter = SupplierPaymentAdapter(this, ladgerEntryList)
            mBinding.rvSupplierPayment.adapter = supplierPaymentAdapter
            supplierPaymentAdapter.notifyDataSetChanged()
        } else {
            Utils.setToast(
                applicationContext, "No data available"
            )
        }
        isStoragePermissionGranted
    }


    //permission is automatically granted on sdk<23 upon installation
    val isStoragePermissionGranted: Boolean
        get() = if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }

    private fun convertExcel() {
        if (utils!!.isNetworkAvailable) {
            val supplierPaymentModel =
                SupplierPaymentModel(customerId, startDate, EndDate, 1, true, type)
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this)
                commonClassForAPI!!.CustomerLedgerPDF(customerLedgerPDF, supplierPaymentModel)
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getData("internet_connection")
            )
        }
    }

    fun chngeFormat(givendate: String?): String? {
        val inputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(givendate)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    private val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            list.remove(referenceId)
            if (list.isEmpty()) {
                val mBuilder = NotificationCompat.Builder(applicationContext)
                    .setSmallIcon(R.drawable.logo_sk)
                    .setContentTitle("GadgetSaint")
                    .setContentText("All Download completed")
                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(455, mBuilder.build())
            }
        }
    }
    private val customerLedgerPDF: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(response: JsonObject) {
                Utils.hideProgressDialog()
                try {
                    val obj = JSONObject(response.toString())
                    val supplierDocModel =
                        Gson().fromJson(obj.toString(), SupplierDocModel::class.java)
                    if (supplierDocModel.isStatus) {
                        if (supplierDocModel.isURL != null) {
                            val Url =
                                EndPointPref.getInstance(RetailerSDKApp.getInstance()).baseUrl + supplierDocModel.isURL
                            val fileName = Url.substring(Url.lastIndexOf("/") + 1)
                            val request = DownloadManager.Request(Uri.parse(Url))
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            request.setDescription("Download...")
                            //                    request.setDescription("Download " + fileName + " from " + Url);
                            request.setTitle("Document Downloading")
                            request.setVisibleInDownloadsUi(true)
                            request.setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS,
                                "/Supplier/" + "/" + supplierDocModel.isURL
                            )
                            referenceId = downloadManager!!.enqueue(request)
                            list.add(referenceId)
                        } else {
                            Utils.setToast(
                                applicationContext, supplierDocModel.message
                            )
                        }
                    }
                } catch (e: JSONException) {
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