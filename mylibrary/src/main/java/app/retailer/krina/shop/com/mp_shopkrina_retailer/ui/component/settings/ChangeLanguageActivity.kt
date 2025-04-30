package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityChangeLanguageBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnLanguageClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.LanguageListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChangeLanguageActivity : AppCompatActivity(), View.OnClickListener, OnLanguageClick {
    private lateinit var mBinding: ActivityChangeLanguageBinding

    private val handler = Handler(Looper.myLooper()!!)
    private var hindiCB: CheckBox? = null
    private var englishCB: CheckBox? = null
    private var gujaratiCB: CheckBox? = null
    private var languageList: ArrayList<DataSnapshot>? = null
    private var adapter: LanguageListAdapter? = null
    private var activity: ChangeLanguageActivity? = null
    private var dataPostSnapshot: DataSnapshot? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_language)
        activity = this
        //init view
        initView()
        //checked hindi, english checkbox
        if (LocaleHelper.getLanguage(applicationContext).equals("en", ignoreCase = true)) {
            englishCB!!.isChecked = true
        } else if (LocaleHelper.getLanguage(applicationContext).equals("hi", ignoreCase = true)) {
            hindiCB!!.isChecked = true
        } else if (LocaleHelper.getLanguage(applicationContext).equals("gu", ignoreCase = true)) {
            gujaratiCB!!.isChecked = true
        }
        mBinding.tvHindi.text = MyApplication.getInstance().dbHelper.getString(R.string.hindi)
        mBinding.tvEnglish.text = MyApplication.getInstance().dbHelper.getString(R.string.english)
        mBinding.tvGujarati.text = MyApplication.getInstance().dbHelper.getString(R.string.Gujarati)
        mBinding.toolbarCl.title.text =
            MyApplication.getInstance().dbHelper.getString(R.string.changelang)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> onBackPressed()
            R.id.cb_english -> handler.postDelayed({
                if (englishCB!!.isChecked) {
                    hindiCB!!.isChecked = false
                    gujaratiCB!!.isChecked = false
                    LocaleHelper.setLocale(applicationContext, "en")
                    SharePrefs.getInstance(this).putString(SharePrefs.CURRENT_LANGUAGE, "en")
                    MyApplication.getInstance().clearLocalData()
                    MyApplication.getInstance().dbHelper.truncateLangTable()
                    startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
                } else {
                    englishCB!!.isChecked = true
                }
            }, 10)
            R.id.cb_hindi -> handler.postDelayed({
                if (hindiCB!!.isChecked) {
                    englishCB!!.isChecked = false
                    gujaratiCB!!.isChecked = false
                    LocaleHelper.setLocale(applicationContext, "hi")
                    SharePrefs.getInstance(this).putString(SharePrefs.CURRENT_LANGUAGE, "hi")
                    MyApplication.getInstance().clearLocalData()
                    MyApplication.getInstance().dbHelper.truncateLangTable()
                    startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
                } else {
                    hindiCB!!.isChecked = true
                }
            }, 10)
            R.id.cb_gujarati -> handler.postDelayed({
                if (gujaratiCB!!.isChecked) {
                    englishCB!!.isChecked = false
                    hindiCB!!.isChecked = false
                    LocaleHelper.setLocale(applicationContext, "gu")
                    MyApplication.getInstance().clearLocalData()
                    MyApplication.getInstance().dbHelper.truncateLangTable()
                    startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
                } else {
                    gujaratiCB!!.isChecked = true
                }
            }, 10)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.fadeTransaction(this)
    }


    override fun onSelectLanguage(position: Int) {
        Utils.showProgressDialog(activity)
        MyApplication.getInstance().clearLangData()
        MyApplication.getInstance().dbHelper.deleteAndUpdateTable(dataPostSnapshot)
    }

    private fun initView() {
        Utils.showProgressDialog(activity)
        hindiCB = mBinding.cbHindi
        englishCB = mBinding.cbEnglish
        gujaratiCB = mBinding.cbGujarati
        mBinding.toolbarCl.back.setOnClickListener(this)
        englishCB!!.setOnClickListener(this)
        hindiCB!!.setOnClickListener(this)
        gujaratiCB!!.setOnClickListener(this)
        languageList = ArrayList()
        adapter = LanguageListAdapter(this, languageList!!, this)
        mBinding.rvLanguage.adapter = adapter
        val database = FirebaseDatabase.getInstance()
        val language = database.reference

        language.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                dataPostSnapshot = snapshot
                for (postSnapshot in snapshot.children) {
                    languageList!!.add(postSnapshot)
                }
                adapter!!.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
        })
        Handler().postDelayed({ Utils.hideProgressDialog() }, 2000)
    }
}