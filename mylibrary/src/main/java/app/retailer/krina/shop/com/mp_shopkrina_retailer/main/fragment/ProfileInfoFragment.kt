package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentProfileInfoBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.EditProfileActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.CustomerAddressActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.auth.PlacesBillingSearchActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddressModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver
import java.util.*

class ProfileInfoFragment : Fragment(), View.OnClickListener {
    private lateinit var mBinding: FragmentProfileInfoBinding

    private var activity: EditProfileActivity? = null
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var editProfileModel: EditProfileModel? = null

    private var isGPS = false
    private var sCustName: String? = ""
    private var sCustEmail: String? = ""
    private var sCustDob: String? = ""
    private var sCustAnni: String? = ""
    private var sContactNo: String? = ""
    private var sWhatsappNo: String? = ""
    private var sCustShippingAddress: String? = ""
    private var sCustBillingAddress: String? = ""
    private var lat: String? = ""
    private var lg: String? = ""
    private var pincode: String? = ""
    private var flateOrFloorNumber: String? = ""
    private var landmark: String? = ""
    private var billingAddress: String? = ""
    private var billingCity: String? = ""
    private var billingState: String? = ""
    private var billingZipCode: String? = ""


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as EditProfileActivity
    }

    override fun onResume() {
        super.onResume()
        MyApplication.getInstance().mFirebaseAnalytics.setCurrentScreen(
            activity!!,
            this.javaClass.simpleName,
            null
        )
        mBinding.ivMapIcon.isClickable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_info, container, false)
        // initialize var
        isGPS = Utils.gpsPermission(activity, "runtime")
        initView()
        // set UI view
        setView()

        mBinding.cbCheckSameNumber.setOnClickListener {
            if (mBinding.cbCheckSameNumber.isChecked) {
                mBinding.etCustWhatsappNumber.setText(
                    mBinding.etCustContact.text.toString().trim())
            } else {
                mBinding.etCustWhatsappNumber.setText("")
            }
        }
        mBinding.cbCheckSameAddress.setOnClickListener {
            if (mBinding.cbCheckSameAddress.isChecked) {
                val add = mBinding.etCustShippingAddress.text.toString().trim()
                if (add != null && add != "") {
                    mBinding.etCustBillingAddress.setText(add)
                    billingAddress = editProfileModel!!.shippingAddress
                    billingCity = editProfileModel!!.city
                    billingState = editProfileModel!!.state
                    billingZipCode = editProfileModel!!.zipCode
                } else {
                    mBinding.etCustBillingAddress.setText("")
                }
            }
        }
        return mBinding.root
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_map_icon -> {
                isGPS = Utils.gpsPermission(activity, "clicktime")
                if (isGPS) {
                    val isVerified =
                        SharePrefs.getInstance(activity).getString(SharePrefs.CUSTOMER_VERIFY)
                    if (!isVerified.equals("Full Verified", ignoreCase = true)) {
                        val intent = Intent(activity, CustomerAddressActivity::class.java)
                        intent.putExtra("REDIRECT_FLAG", 2)
                        intent.putExtra(
                            "cityName",
                            SharePrefs.getInstance(activity).getString(SharePrefs.CITY_NAME)
                        )
                        intent.putExtra("CUSTOMER_DETAILS", activity!!.editProfileModel)
                        startActivityForResult(intent, REQUST_FOR_ADDRESS)
                        Utils.leftTransaction(activity)
                    } else {
                        Utils.setToast(
                            activity,
                            MyApplication.getInstance().dbHelper.getString(R.string.txt_note_verified)
                        )
                    }
                } else {
                    Utils.setToast(
                        activity,
                        MyApplication.getInstance().dbHelper.getString(R.string.gps_permission)
                    )
                }
            }
            R.id.imBillingAddress -> {
                isGPS = Utils.gpsPermission(activity, "clicktime")
                if (isGPS) {
                    val IsVerified =
                        SharePrefs.getInstance(activity).getString(SharePrefs.CUSTOMER_VERIFY)
                    if (!IsVerified.equals("Full Verified", ignoreCase = true)) {
                        val intent = Intent(activity, PlacesBillingSearchActivity::class.java)
                        intent.putExtra("REDIRECT_FLAG", 4)
                        intent.putExtra(
                            "cityName",
                            SharePrefs.getInstance(activity).getString(SharePrefs.CITY_NAME)
                        )
                        intent.putExtra("CUSTOMER_DETAILS", editProfileModel)
                        startActivityForResult(intent, REQUST_FOR_BILLING_ADDRESS)
                        Utils.leftTransaction(activity)
                    } else {
                        Utils.setToast(
                            activity,
                            MyApplication.getInstance().dbHelper.getString(R.string.txt_note_verified)
                        )
                    }
                } else {
                    Utils.setToast(
                        activity,
                        MyApplication.getInstance().dbHelper.getString(R.string.gps_permission)
                    )
                }
            }
            R.id.et_cust_anni -> clickAnni()
            R.id.et_cust_dob -> clickDOB()
            R.id.btnUpdate -> {
                sCustName = mBinding.etCustName.text.toString().trim { it <= ' ' }
                sCustEmail = mBinding.etCustEmail.text.toString().trim { it <= ' ' }
                sContactNo = mBinding.etCustContact.text.toString().trim { it <= ' ' }
                sWhatsappNo = mBinding.etCustWhatsappNumber.text.toString().trim { it <= ' ' }
                sCustShippingAddress =
                    mBinding.etCustShippingAddress.text.toString().trim { it <= ' ' }
                sCustBillingAddress =
                    mBinding.etCustBillingAddress.text.toString().trim { it <= ' ' }
                val sDOB = mBinding.etCustDob.text.toString().trim { it <= ' ' }
                val panNumber = mBinding.etpanNo.text.toString().trim { it <= ' ' }
                val aadharNumber = mBinding.etAadharNo.text.toString().trim { it <= ' ' }
                if (sCustName!!.isEmpty()) {
                    Utils.setToast(
                        activity,
                        MyApplication.getInstance().dbHelper.getString(R.string.entername))
                    mBinding.etCustName.requestFocus()
                } else if (sCustEmail!!.isEmpty()) {
                    Utils.setToast(
                        activity,
                        MyApplication.getInstance().dbHelper.getString(R.string.valid_email_address))
                    mBinding.etCustEmail.requestFocus()
                } else if (!Utils.isValidEmail(sCustEmail)) {
                    Utils.setToast(
                        activity,
                        MyApplication.getInstance().dbHelper.getString(R.string.valid_email_address)
                    )
                    mBinding.etCustEmail.requestFocus()
//                } else if (sDOB.isEmpty()) {
//                    Utils.setToast(
//                        activity,
//                        MyApplication.getInstance().dbHelper.getString(R.string.enterdob)
//                    )
//                    mBinding.etCustDob.requestFocus()
                } else if (sCustShippingAddress!!.isEmpty()) {
                    mBinding.tilCustSAdd.error =
                        MyApplication.getInstance().dbHelper.getString(R.string.hint_shipping_address)
                    mBinding.etCustShippingAddress.requestFocus()
                } else if (panNumber != "" && panNumber.length < 10) {
                    mBinding.tilPanNo.error =
                        MyApplication.getInstance().dbHelper.getString(R.string.valid_pan_number)
                    mBinding.etpanNo.requestFocus()
                } else if (aadharNumber != "" && aadharNumber.length < 12) {
                    mBinding.tilAadharNo.error =
                        MyApplication.getInstance().dbHelper.getString(R.string.valid_aadhar_number)
                    mBinding.tilAadharNo.requestFocus()
                } else {
                    editProfileModel!!.name = sCustName
                    editProfileModel!!.emailid = sCustEmail
                    editProfileModel!!.whatsappNumber = sWhatsappNo
                    editProfileModel!!.dOB = sCustDob
                    editProfileModel!!.anniversaryDate = sCustAnni
                    editProfileModel!!.shippingAddress = sCustShippingAddress
                    editProfileModel!!.shippingAddress1 = flateOrFloorNumber
                    editProfileModel!!.billingAddress = sCustBillingAddress
                    editProfileModel!!.lat = lat
                    editProfileModel!!.lg = lg
                    editProfileModel!!.zipCode = pincode
                    editProfileModel!!.landMark = landmark
                    editProfileModel!!.aadharNo = aadharNumber
                    editProfileModel!!.panNo = panNumber
                    editProfileModel!!.billingAddress = billingAddress
                    editProfileModel!!.billingCity = billingCity
                    editProfileModel!!.billingState = billingState
                    editProfileModel!!.billingZipCode = billingZipCode
                    if (utils!!.isNetworkAvailable) {
                        if (commonClassForAPI != null) {
                            Utils.showProgressDialog(activity)
                            commonClassForAPI!!.editProfile(
                                editProfile,
                                editProfileModel,
                                "Customer all info"
                            )
                        }
                    } else {
                        Utils.setToast(
                            activity,
                            MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
                        )
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == REQUST_FOR_ADDRESS && resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val addressModel =
                        data.getParcelableExtra<AddressModel>(Constant.CUSTOMER_ADDRESS)
                    lat = addressModel!!.latitude.toString()
                    lg = addressModel.longitude.toString()
                    pincode = addressModel.pincode
                    flateOrFloorNumber = addressModel.flateOrFloorNumber
                    landmark = addressModel.landmark
                    mBinding.etCustShippingAddress.setText(addressModel.address)
                }
            } else if (requestCode == REQUST_FOR_BILLING_ADDRESS && resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val addressModel =
                        data.getParcelableExtra<AddressModel>(Constant.CUSTOMER_ADDRESS)
                    billingAddress = addressModel!!.billingAddress
                    billingCity = addressModel.billingCity
                    billingState = addressModel.billingState
                    billingZipCode = addressModel.billingZipCode
                    mBinding.etCustBillingAddress.setText(addressModel.billingAddress)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setView() {
        mBinding.etCustName.setText(editProfileModel!!.name)
        mBinding.etCustEmail.setText(editProfileModel!!.emailid)
        if (editProfileModel!!.dOB != null) {
            mBinding.etCustDob.setText(
                Utils.getChangeDateFormatInProfile(
                    editProfileModel!!.dOB
                )
            )
        }
        if (editProfileModel!!.anniversaryDate != null) {
            mBinding.etCustAnni.setText(
                Utils.getChangeDateFormatInProfile(
                    editProfileModel!!.anniversaryDate
                )
            )
        }
        mBinding.etCustContact.setText(editProfileModel!!.mobile)
        mBinding.etCustWhatsappNumber.setText(editProfileModel!!.whatsappNumber)
        mBinding.etCustShippingAddress.setText(editProfileModel!!.shippingAddress)
        mBinding.etCustBillingAddress.setText(editProfileModel!!.billingAddress)
        mBinding.etAadharNo.setText(editProfileModel!!.aadharNo)
        mBinding.etpanNo.setText(editProfileModel!!.panNo)
        if (editProfileModel!!.aadharNo != null && editProfileModel!!.aadharNo != "") {
            mBinding.etAadharNo.isEnabled = false
            mBinding.tilAadharNo.isEnabled = false
        }
        if (editProfileModel!!.panNo != null && editProfileModel!!.panNo != "") {
            mBinding.etpanNo.isEnabled = false
            mBinding.tilPanNo.isEnabled = false
        }
        lat = editProfileModel!!.lat
        lg = editProfileModel!!.lg
        pincode = editProfileModel!!.zipCode
        flateOrFloorNumber = editProfileModel!!.shippingAddress1
        landmark = editProfileModel!!.landMark

        if (!TextUtils.isNullOrEmpty(editProfileModel!!.refNo) && editProfileModel!!.refNo != "NA") {
            mBinding.etCustBillingAddress.isEnabled = false
            mBinding.imBillingAddress.isEnabled = false
            mBinding.LLBillingAdd.visibility = View.GONE
        }
        if (editProfileModel!!.mobile == editProfileModel!!.whatsappNumber) {
            mBinding.cbCheckSameNumber.isChecked = true
        }
        if (editProfileModel!!.shippingAddress == editProfileModel!!.billingAddress) {
            mBinding.cbCheckSameAddress.isChecked = true
        }
        val isVerified = SharePrefs.getInstance(getActivity()).getString(SharePrefs.CUSTOMER_VERIFY)
        if (isVerified.equals("Full Verified", ignoreCase = true)) {
            mBinding.etCustContact.isEnabled = false
            mBinding.etCustName.isEnabled = false
            mBinding.etCustBillingAddress.isEnabled = false
            mBinding.etCustShippingAddress.isEnabled = false
        }
    }

    private fun initView() {
        activity!!.tv_title!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_personal_informations)
        utils = Utils(activity)
        commonClassForAPI = CommonClassForAPI.getInstance(activity)
        editProfileModel = activity!!.editProfileModel
        sCustDob = editProfileModel!!.dOB
        sCustAnni = editProfileModel!!.anniversaryDate
        mBinding.tilCustName.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_cust_name)
        mBinding.tilCustEmail.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_cust_email)
        mBinding.tilCustDob.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_cust_dob)
        mBinding.tilCustAnni.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_cust_anniversary_date)
        mBinding.tilCustContact.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_cust_contact_number)
        mBinding.tilWhatsapp.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_cust_whatsApp_number)
        mBinding.tilCustSAdd.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_cust_shipping_address)
        mBinding.tilCustBAdd.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_cust_billing_address)
        mBinding.checkboxCN.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_check_whatsapp)
        mBinding.txtCbCheckSameAddress.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_check_shiping_address)
        mBinding.tilAadharNo.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.aadhar_number)
        mBinding.tilPanNo.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.pannumber)

        mBinding.btnUpdate.setOnClickListener(this)
        mBinding.etCustDob.setOnClickListener(this)
        mBinding.etCustAnni.setOnClickListener(this)
        mBinding.ivMapIcon.setOnClickListener(this)
        mBinding.imBillingAddress.setOnClickListener(this)
        val isVerified = SharePrefs.getInstance(getActivity()).getString(SharePrefs.CUSTOMER_VERIFY)
        if (isVerified != null && isVerified.equals("Full Verified", ignoreCase = true)) {
            mBinding.btnUpdate.visibility = View.GONE
        }
    }

    private fun clickDOB() {
        try {
            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR]
            val mMonth = c[Calendar.MONTH]
            val mDay = c[Calendar.DAY_OF_MONTH]
            val dialog = DatePickerDialog(
                activity!!,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListenerForDob(),
                mYear,
                mMonth,
                mDay
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setButton(
                DialogInterface.BUTTON_NEGATIVE,
                MyApplication.getInstance().dbHelper.getString(R.string.cancel)
            ) { dialog1: DialogInterface?, which: Int -> }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickAnni() {
        try {
            val c1 = Calendar.getInstance()
            val mYear = c1[Calendar.YEAR]
            val mMonth = c1[Calendar.MONTH]
            val mDay = c1[Calendar.DAY_OF_MONTH]
            val dialog = DatePickerDialog(
                activity!!,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListenerForAnni(),
                mYear,
                mMonth,
                mDay
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setButton(
                DialogInterface.BUTTON_NEGATIVE,
                MyApplication.getInstance().dbHelper.getString(R.string.cancel)
            ) { dialog1: DialogInterface?, which: Int -> mBinding.etCustAnni.setText("") }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    internal inner class mDateSetListenerForDob : OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            try {
                val date = StringBuilder() // Month is 0 based so add 1
                    .append(day).append("/").append(month + 1).append("/")
                    .append(year).append(" ").toString()
                sCustDob = Utils.getSimpleDateFormat(date)
                mBinding.etCustDob.setText(Utils.getChangeDateFormatInProfile(sCustDob))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    internal inner class mDateSetListenerForAnni : OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            try {
                val date = StringBuilder() // Month is 0 based so add 1
                    .append(dayOfMonth).append("/").append(monthOfYear + 1).append("/")
                    .append(year).append(" ").toString()
                sCustAnni = Utils.getSimpleDateFormat(date)
                mBinding.etCustAnni.setText(Utils.getChangeDateFormatInProfile(sCustAnni))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // edit profile response
    private val editProfile: DisposableObserver<MyProfileResponse> =
        object : DisposableObserver<MyProfileResponse>() {
            override fun onNext(model: MyProfileResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (model.isStatus && model.customers != null) {
                        val customer = model.customers
                        SharePrefs.getInstance(activity)
                            .putString(SharePrefs.CUSTOMER_NAME, customer?.name)
                        SharePrefs.getInstance(activity)
                            .putString(SharePrefs.CUSTOMER_EMAIL, customer?.emailid)
                        SharePrefs.getInstance(activity)
                            .putString(SharePrefs.SHOP_NAME, customer?.shopName)
                        SharePrefs.getInstance(activity)
                            .putString(SharePrefs.SHIPPING_ADDRESS, customer?.shippingAddress)
                        SharePrefs.getInstance(activity)
                            .putString(SharePrefs.USER_PROFILE_IMAGE, customer?.uploadProfilePichure)
                        Utils.setToast(
                            activity,
                            MyApplication.getInstance().dbHelper.getString(R.string.toast_succesfull)
                        )
                        startActivity(Intent(activity, HomeActivity::class.java))
                        activity!!.finish()
                    } else {
                        Utils.setToast(activity, model.message)
                    }
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

    companion object {
        private const val REQUST_FOR_ADDRESS = 1001
        private const val REQUST_FOR_BILLING_ADDRESS = 1002
        fun newInstance(): ProfileInfoFragment {
            return ProfileInfoFragment()
        }
    }
}