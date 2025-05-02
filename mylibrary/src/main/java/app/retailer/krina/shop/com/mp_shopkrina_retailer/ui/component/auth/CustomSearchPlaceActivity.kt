package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityCustomSearchPlaceBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.PlacesAutoCompleteAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import java.util.Locale

class CustomSearchPlaceActivity : AppCompatActivity(), PlacesAutoCompleteAdapter.ClickListener {
    private lateinit var mBinding: ActivityCustomSearchPlaceBinding
    private var mAutoCompleteAdapter: PlacesAutoCompleteAdapter? = null
    private var cityName: String? = ""
    private var searchCity = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_custom_search_place)

        mBinding.back.setOnClickListener { onBackPressed() }
        mBinding.etSearchKeyword.text = MyApplication.getInstance().dbHelper.getString(R.string.search_city)
        mBinding.address.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.title_serach_address)
        if (intent != null) {
            cityName = intent.getStringExtra(Constant.CITY_NAME)
            searchCity = intent.getBooleanExtra(Constant.IS_SEARCH_CITY, false)
        }
        if (searchCity) {
            mBinding.address.hint =
                MyApplication.getInstance().dbHelper.getString(R.string.select_city_hint)
        }
        Places.initialize(
            applicationContext,
            resources.getString(R.string.google_maps_key),
            Locale.ENGLISH
        )

        mAutoCompleteAdapter = PlacesAutoCompleteAdapter(this, searchCity)
        mAutoCompleteAdapter!!.setClickListener(this)
        mBinding.placesRecyclerView.adapter = mAutoCompleteAdapter
        mAutoCompleteAdapter!!.notifyDataSetChanged()
        mBinding.etSearchPlace.requestFocus()
        mBinding.imSearchPlace.visibility = View.VISIBLE
        mBinding.progressSearch.visibility = View.INVISIBLE

        mBinding.etSearchPlace.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchPlace()
                return@setOnEditorActionListener true
            }
            false
        }
        mBinding.imSearchPlace.setOnClickListener { searchPlace() }
    }

    override fun onStart() {
        super.onStart()
        Utils.setLocale(applicationContext, "en")
    }

    override fun onResume() {
        super.onResume()
        Utils.setLocale(applicationContext, "en")
    }

    override fun click(place: Place, address: CharSequence, area: CharSequence) {
        val intent = Intent()
        intent.putExtra(Constant.PLACE_RESULT, place)
        intent.putExtra(Constant.ADDRESS, address.toString())
        intent.putExtra(Constant.AREA, area.toString())
        setResult(RESULT_OK, intent)
        Utils.rightTransaction(this)
        finish()
    }

    private fun searchPlace() {
        val s = mBinding.etSearchPlace.text.toString()
        if (TextUtils.isNullOrEmpty(s)) {
            if (searchCity) {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.please_enter_city_name)
                )
            } else {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.please_enter_address_p)
                )
            }
        } else {
            mBinding.imSearchPlace.visibility = View.INVISIBLE
            mBinding.progressSearch.visibility = View.VISIBLE
            if (searchCity) {
                mAutoCompleteAdapter!!.filter.filter(s)
            } else {
                mAutoCompleteAdapter!!.filter.filter("$cityName, $s")
            }
        }
        val handler = Handler()
        handler.postDelayed({
            mBinding.imSearchPlace.visibility = View.VISIBLE
            mBinding.progressSearch.visibility = View.INVISIBLE
        }, 2000)
    }
}