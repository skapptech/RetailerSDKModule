package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.Manifest.permission
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityBusinessCardBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.Constants
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class BusinessCardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityBusinessCardBinding

    private val STORAGE_REQUEST = 99
    private var filePath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_business_card)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title =
            MyApplication.getInstance().dbHelper.getString(R.string.title_activity_business_card)

        initViews()

        if (!TextUtils.isEmpty(
                SharePrefs.getInstance(this).getString(SharePrefs.USER_PROFILE_IMAGE)
            ) && SharePrefs.getInstance(this).getString(SharePrefs.USER_PROFILE_IMAGE) != ""
        ) {
            mBinding.ivImage.visibility = View.VISIBLE
            Picasso.get().load(
                Constant.BASE_URL_PROFILE + SharePrefs.getInstance(this)
                    .getString(SharePrefs.USER_PROFILE_IMAGE)
            ).into(mBinding.ivImage)
        } else {
            mBinding.ivImage.visibility = View.GONE
        }
        mBinding.tvShop.text = SharePrefs.getInstance(this).getString(SharePrefs.SHOP_NAME)
        mBinding.tvName.text = SharePrefs.getInstance(this).getString(SharePrefs.CUSTOMER_NAME)
        mBinding.tvPhone.text = SharePrefs.getInstance(this).getString(SharePrefs.MOBILE_NUMBER)
        mBinding.tvAddress.text =
            SharePrefs.getInstance(this).getString(SharePrefs.SHIPPING_ADDRESS)
        mBinding.tvEmail.text = SharePrefs.getInstance(this).getString(SharePrefs.CUSTOMER_EMAIL)
        mBinding.etName.setText(SharePrefs.getInstance(this).getString(SharePrefs.CUSTOMER_NAME))
        mBinding.etPhone.setText(SharePrefs.getInstance(this).getString(SharePrefs.MOBILE_NUMBER))
        //mBinding.etTagLine.setText(SharePrefs.getInstance(this).getString(SharePrefs.TAG_LINE));
        mBinding.etAddress.setText(
            SharePrefs.getInstance(this).getString(SharePrefs.SHIPPING_ADDRESS)
        )
        mBinding.etEmail.setText(SharePrefs.getInstance(this).getString(SharePrefs.CUSTOMER_EMAIL))
        mBinding.etName.addTextChangedListener(GenericTextWatcher(mBinding.etName))
        mBinding.etPhone.addTextChangedListener(GenericTextWatcher(mBinding.etPhone))
        mBinding.etTagLine.addTextChangedListener(GenericTextWatcher(mBinding.etTagLine))
        mBinding.etAddress.addTextChangedListener(GenericTextWatcher(mBinding.etAddress))
        mBinding.etEmail.addTextChangedListener(GenericTextWatcher(mBinding.etEmail))
        mBinding.cardShare.setOnClickListener(this)
        updateCardViews()
    }

    override fun onClick(view: View) {
        if (mayRequestAccess()) {
            val bitmap = getScreenShot(findViewById(R.id.li_card))
            store(bitmap)
            showBottomSheetDialog(bitmap)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.fadeTransaction(this)
    }


    private fun initViews() {
        mBinding.tvPoweredBy.text =
            MyApplication.getInstance().dbHelper.getString(R.string.powered_by_shopkirana)
        mBinding.tvCreatedandShare.text =
            MyApplication.getInstance().dbHelper.getString(R.string.create_and_share_your_business_card)
        mBinding.tvCardName.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_cust_name)
        mBinding.tvCardNumber.text =
            MyApplication.getInstance().dbHelper.getString(R.string.mobile_number)
        mBinding.tvCardEmail.text =
            MyApplication.getInstance().dbHelper.getString(R.string.email_address)
        mBinding.tvCardAddress.text =
            MyApplication.getInstance().dbHelper.getString(R.string.shop_office_address)
        mBinding.tvShareCard.text =
            MyApplication.getInstance().dbHelper.getString(R.string.share_your_business_card)
    }

    private fun updateCardViews() {
        if (TextUtils.isEmpty(mBinding.etPhone.text)) {
            mBinding.tvPhone.visibility = View.INVISIBLE
        } else {
            mBinding.tvPhone.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(mBinding.etEmail.text)) {
            mBinding.tvEmail.visibility = View.INVISIBLE
        } else {
            mBinding.tvEmail.visibility = View.VISIBLE
        }
    }

    private fun mayRequestAccess(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (ContextCompat.checkSelfPermission(
                this,
                permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                permission.READ_EXTERNAL_STORAGE
            )
            && ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            requestPermissions(
                arrayOf(
                    permission.READ_EXTERNAL_STORAGE,
                    permission.WRITE_EXTERNAL_STORAGE
                ), STORAGE_REQUEST
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    permission.READ_EXTERNAL_STORAGE,
                    permission.WRITE_EXTERNAL_STORAGE
                ), STORAGE_REQUEST
            )
        }
        return false
    }

    fun getScreenShot(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    fun store(bm: Bitmap) {
        val root = Environment.getExternalStorageDirectory().toString()
        val fos: OutputStream?
        val directory = Environment.DIRECTORY_PICTURES
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                val resolver = this.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "card.jpg")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
                val imageUri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = resolver.openOutputStream(imageUri!!)
                if (fos != null) {
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                }
                fos!!.flush()
                fos.close()
                Log.d(
                    Constants.TAG,
                    "SavedImages:" + root + File.separator + directory + File.separator + "card.jpg"
                )
                filePath = root + File.separator + directory + File.separator + "card.jpg"
            } else {
                val myDir =
                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_PICTURES)
                if (!myDir.exists()) myDir.mkdirs()
                val file = File(myDir, "card.jpg")
                if (file.exists()) file.delete()

                fos = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
                filePath = root + File.separator + "ShopKirana" + File.separator + "card.jpg"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareOnWhatsapp() {
        val uri = FileProvider.getUriForFile(
            applicationContext,
            applicationContext.packageName + ".provider",
            File(
                Environment.getExternalStorageDirectory().toString() +
                        File.separator + Environment.DIRECTORY_PICTURES + File.separator + "card.jpg"
            )
        )
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.setPackage("com.whatsapp")
        intent.putExtra(
            Intent.EXTRA_TEXT, """
     Make Your Own Business Card by Using Direct App. http://bit.ly/2Gf3VFP
     Contact us for any enquiries.
     """.trimIndent()
        )
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "image/jpeg"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.whatsapp_not_installed),
                Toast.LENGTH_SHORT
            ).show()
        }
        MyApplication.getInstance()
            .updateAnalyticShare(javaClass.simpleName, "BusinessCard WhatsApp Share")
    }

    private fun shareImage() {
        val uri = FileProvider.getUriForFile(
            applicationContext,
            applicationContext.packageName  + ".provider",
            File(
                Environment.getExternalStorageDirectory().toString() +
                        File.separator + Environment.DIRECTORY_PICTURES + File.separator + "card.jpg"
            )
        )
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Business card")
        intent.type = "image/jpeg"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Make Your Own Business Card by Using Direct App. http://bit.ly/2Gf3VFP"
        )
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "Share Business Card"))
        MyApplication.getInstance().updateAnalyticShare(javaClass.simpleName, "BusinessCard Share")
    }

    private fun showBottomSheetDialog(bitmap: Bitmap?) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_card, null)
        dialog.setContentView(view)
        val ivImage = view.findViewById<AppCompatImageView>(R.id.iv_image)
        val tvShareYourBCard = view.findViewById<TextView>(R.id.tvShareYourBCard)
        val tvWhatsapp = view.findViewById<TextView>(R.id.tvWhatsapp)
        val btnShare = view.findViewById<AppCompatButton>(R.id.btn_share)
        btnShare.text = MyApplication.getInstance().dbHelper.getString(R.string.other)
        tvShareYourBCard.text =
            MyApplication.getInstance().dbHelper.getString(R.string.share_your_business_card)
        tvWhatsapp.text = MyApplication.getInstance().dbHelper.getString(R.string.share_on_whatsapp)
        ivImage.setImageBitmap(bitmap)
        view.findViewById<View>(R.id.li_share).setOnClickListener { shareOnWhatsapp() }
        view.findViewById<View>(R.id.btn_share).setOnClickListener { shareImage() }
        dialog.show()
    }

    inner class GenericTextWatcher(private val view: View) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.et_name -> mBinding.tvName.text = editable.toString()
                R.id.et_phone -> {
                    mBinding.tvPhone.text = editable.toString()
                    updateCardViews()
                }
                R.id.et_tag_line ->                     //  mBinding.tvTagLine.setText(editable.toString());
                    // SharePrefs.getInstance(getBaseContext()).putString(SharePrefs.TAG_LINE, editable.toString());
                    updateCardViews()
                R.id.et_address -> mBinding.tvAddress.text = editable.toString()
                R.id.et_email -> {
                    mBinding.tvEmail.text = editable.toString()
                    updateCardViews()
                }
                else -> {}
            }
        }
    }
}