package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityRtgsInfoBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication

class RtgsInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityRtgsInfoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rtgs_info)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = MyApplication.getInstance().noteRepository.getString(R.string.rtgs_details)

        if (intent.hasExtra("screen") && intent.getIntExtra("screen", 0) == 2) {
            binding.btnContinue.visibility = View.VISIBLE
        }

        binding.tvVAN.text =
            "SKETPL" + SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE)
        binding.tvVanH.text =
            MyApplication.getInstance().noteRepository.getString(R.string.virtual_account_number)
        binding.tvBeneficiaryH.text =
            MyApplication.getInstance().noteRepository.getString(R.string.beneficiary_name)
        binding.tvBeneficiary.text =
            MyApplication.getInstance().noteRepository.getString(R.string.van_beneficiary)
        binding.tvBankH.text =
            MyApplication.getInstance().noteRepository.getString(R.string.bank_name)
        binding.tvBank.text =
            MyApplication.getInstance().noteRepository.getString(R.string.van_bank)
        binding.tvBranchH.text =
            MyApplication.getInstance().noteRepository.getString(R.string.branch_name)
        binding.tvBranch.text =
            MyApplication.getInstance().noteRepository.getString(R.string.van_branch)
        binding.tvIfscH.text =
            MyApplication.getInstance().noteRepository.getString(R.string.ifsc_code)
        binding.tvIfsc.text =
            MyApplication.getInstance().noteRepository.getString(R.string.van_ifsc)
        binding.tvHowItWork.text =
            MyApplication.getInstance().noteRepository.getString(R.string.how_rtgs_work)
        binding.tvHowToAdd.text =
            MyApplication.getInstance().noteRepository.getString(R.string.how_to_add_rtgs_amount)
        binding.tvHowItWork.paintFlags = binding.tvHowItWork.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.tvHowToAdd.paintFlags = binding.tvHowItWork.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.btnContinue.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        binding.tvHowItWork.setOnClickListener {
            showBottomSheet(1)
        }
        binding.tvHowToAdd.setOnClickListener {
            showBottomSheet(2)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }


    private fun showBottomSheet(pos: Int) {
        val dialog = Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.bottom_sheet_rtgs_info)

        val webView = dialog.findViewById<WebView>(R.id.webView)

        class JsInterface {
            @JavascriptInterface
            fun nextClick() {
                runOnUiThread {
                    webView?.loadData(
                        MyApplication.getInstance().noteRepository.getString(R.string.how_rtgs_work_terms),
                        "text/html",
                        "UTF-8"
                    )
                }
            }

            @JavascriptInterface
            fun closeClick() {
                runOnUiThread {
                    dialog.dismiss()
                }
            }
        }

        webView.clearCache(true)
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = false
        webView.settings.javaScriptEnabled = true
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.addJavascriptInterface(JsInterface(), "Android")

        if (pos == 1)
            webView?.loadData(
                MyApplication.getInstance().noteRepository.getString(R.string.how_rtgs_work_terms),
                "text/html",
                "UTF-8"
            )
        else
            webView?.loadData(
                MyApplication.getInstance().noteRepository.getString(R.string.how_to_add_rtgs_amt_terms),
                "text/html",
                "UTF-8"
            )

        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.show()
    }
}