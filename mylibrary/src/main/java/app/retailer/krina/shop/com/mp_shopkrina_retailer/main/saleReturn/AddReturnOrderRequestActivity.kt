package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.saleReturn
import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityAddReturnOrderRequestBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.DialogClearImageInfoBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.flip.AphidLog.format
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.KKReturnReplaceDetailDC
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.PostKKReturnReplaceRequestDc
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.ReturnItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.ReturnOrderBatchItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.SaleReturnRequestResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MarshmallowPermissions
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.sk.user.agent.ui.component.returnOrder.OnCheckboxClick
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class AddReturnOrderRequestActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    OnCheckboxClick {
    private lateinit var binding: ActivityAddReturnOrderRequestBinding
    private lateinit var returnOrderViewModel: ReturnOrderViewModel
    private var addRequestReturnItemAdapter: AddRequestReturnItemAdapter? = null
    private var itemImageFileName: String = ""
    private var uploadFilePath: String? = null

    private var peopleId: Int? = 0
    private var customerId = 0
    private var wareHouseId = 0
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    lateinit var returnOrderBatchItemModel: ReturnOrderBatchItemModel
    var mReturnItemList: ArrayList<ReturnItemModel> = ArrayList()
    var mReturnOrderBatchItemList: ArrayList<ReturnOrderBatchItemModel> = ArrayList()
  //  var mAddedReturnOrderBatchItemList: ArrayList<ReturnOrderBatchItemModel> = ArrayList()

    private val WRITE_PERMISSION = 0x01

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_return_order_request)
        val appRepository = AppRepository(applicationContext)
        returnOrderViewModel = ViewModelProvider(
            this,
            ReturnOrderViewModelFactory(application, appRepository)
        )[ReturnOrderViewModel::class.java]
        init()
        binding.tvSubmitRequest.setOnClickListener {
            if (mReturnOrderBatchItemList.size==0){
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.valid_search_select_item)
                )
            }else {
                val mDetailsList : ArrayList<KKReturnReplaceDetailDC> = ArrayList()
                mDetailsList.clear()
                var isUploadImage = false
                for (item in mReturnOrderBatchItemList){
                    if (item.isChecked){
                        if (item.mImageList.size != 0) {
                            mDetailsList.add(
                                KKReturnReplaceDetailDC(
                                    item.orderId,
                                    item.orderDetailsId,
                                    item.returnQty,
                                    item.returnableQty,
                                    "",
                                    item.batchCode,
                                    item.batchMasterId,
                                    item.itemMultiMRPId,
                                    item.mImageList
                                )
                            )
                        }else {
                            isUploadImage = true
                            break
                        }
                    }
                }
                if (isUploadImage) {
                    Utils.setToast(applicationContext, MyApplication.getInstance().dbHelper.getString(R.string.valid_upload_image_in_items))
                } else if (mDetailsList.size == 0) {
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.valid_enter_qty_checked)
                    )
                }else{
                    val model =  PostKKReturnReplaceRequestDc(customerId,0,mDetailsList)
                    returnOrderViewModel.postSalesReturnRequest(model)
                }
            }
        }
    }


    private fun init() {
        customerId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        wareHouseId = SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID)
        observe(returnOrderViewModel.uploadImageData, ::handleImageUploadResult)
        observe(returnOrderViewModel.returnItemData, ::handleReturnItemResult)
        observe(returnOrderViewModel.returnOrderBatchItemData, ::handleReturnOrderBatchItemResult)
        observe(returnOrderViewModel.postSalesReturnRequestData, ::handleRequestReturnItemResult)
        binding.imBack.setOnClickListener {
            onBackPressed()
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvItems.layoutManager = layoutManager
        binding.rvItems.setHasFixedSize(true)
        addRequestReturnItemAdapter = AddRequestReturnItemAdapter(this, this, ArrayList())
        binding.rvItems.adapter = addRequestReturnItemAdapter
        binding.spinnerItems.setOnItemSelectedListener(this)

        binding.searchItem.setOnEditorActionListener(
            TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isNullOrEmpty(v.text.toString().trim())) {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.please_enter_item_name)
                        )
                    } else {
                        val keyValue = v.text.toString()
                        var isCheckAny = false
                        for (item in mReturnOrderBatchItemList){
                            if (item.isChecked){
                                isCheckAny = true
                                break
                            }
                        }
                        if (isCheckAny) {
                            alertMsg(keyValue,0)
                        } else {
                            Utils.hideKeyboard(this@AddReturnOrderRequestActivity,binding.searchItem)
                            returnOrderViewModel.getReturnItem(customerId, wareHouseId, keyValue)
                        }
                    }
                    return@OnEditorActionListener true
                }
                false
            })
      /*  binding.searchItem.setOnItemClickListener { parent, view, position, id ->
            var model = mReturnItemList[position]
            binding.searchItem.setText(model.itemname)
            callItemWiseOrderDetails(model)
            binding.spinnerItems.setSelection(0)
        }*/
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    val filePath = data?.getStringExtra(Constant.FILE_PATH).toString()
                    lifecycleScope.launch {
                        val fileToUpload = File(uploadFilePath)
                        lifecycleScope.launch {
                            try {
                                val compressedFile =
                                    Compressor.compress(applicationContext, fileToUpload) {
                                        quality(90)
                                        format(Bitmap.CompressFormat.JPEG)
                                    }
                                uploadImagePath(compressedFile)
                            } catch (e: Exception) {
                                e.printStackTrace()
                               // showError(e.message)
                            }
                        }}
                } else if (result.resultCode == RESULT_CANCELED) {
                    Log.d("Image Result:", "Canceled Result")
                }
            }

        binding.tvAddRequest.text = MyApplication.getInstance().dbHelper.getString(R.string.text_add_request)
        binding.tvSubmitRequest.text = MyApplication.getInstance().dbHelper.getString(R.string.submit)
        binding.tvOrderId.text = MyApplication.getInstance().dbHelper.getString(R.string.order_id)
        binding.tvBatchCode.text = MyApplication.getInstance().dbHelper.getString(R.string.batch_code)
        binding.tvQty.text = MyApplication.getInstance().dbHelper.getString(R.string.qty)
        binding.tvRate.text = MyApplication.getInstance().dbHelper.getString(R.string.rate)
        binding.tvReturnQty.text = MyApplication.getInstance().dbHelper.getString(R.string.return_qty)

    }
    private fun uploadImagePath(file: File) {
        val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        Utils.showProgressDialog(this)
        returnOrderViewModel.uploadImage(body)
    }
    fun callItemWiseOrderDetails(itemMultiMRPId: Int){
        returnOrderViewModel.getSaleReturnOrderBatchItem(customerId, itemMultiMRPId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ReturnOrderActivity::class.java))
        finish()
        Utils.rightTransaction(this)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
       // binding.searchItem.setText("")
        if (position==0){
            binding.rlReturnItemDetails.visibility = View.GONE
        }else{
            val model = mReturnItemList[position - 1]
            var isCheckAny = false
            for (item in mReturnOrderBatchItemList){
                if (item.isChecked){
                    isCheckAny = true
                    break
                }
            }
            if (isCheckAny) {
                alertMsg( "",model.itemMultiMRPId)
            } else {
                callItemWiseOrderDetails(model.itemMultiMRPId)
            }
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onClick(isAddMoreImage: Boolean, model: ReturnOrderBatchItemModel,isSelectedChecked:Boolean) {
        if (isAddMoreImage){
                openSomeActivityForResult()
        }else {
           // if (isSelectedChecked) {
            this.returnOrderBatchItemModel = model
            val mBottomDialog = BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog)
            val mDialogClearImageInfoBinding: DialogClearImageInfoBinding =
                DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.dialog_clear_image_info,
                    null,
                    false
                )
            mBottomDialog.setCancelable(false)
            mBottomDialog.setContentView(mDialogClearImageInfoBinding.root)
            /*  mBottomDialog.setOnCancelListener {
               mBottomDialog.dismiss()
             }*/
            mDialogClearImageInfoBinding.tvProceed.setOnClickListener {
                mBottomDialog.dismiss()
                    openSomeActivityForResult()
            }
            mBottomDialog.show()
          /*  }else{
                if (mAddedReturnOrderBatchItemList.size!=0){
                    for (itemPos in mAddedReturnOrderBatchItemList.indices){
                        if(mAddedReturnOrderBatchItemList[itemPos].orderDetailsId==model.orderDetailsId){
                            mAddedReturnOrderBatchItemList.removeAt(itemPos)
                            return
                        }
                    }
                }
            }*/
        }
    }

    private fun openSomeActivityForResult() {
        val tsLong = System.currentTimeMillis() / 1000
        itemImageFileName = "skSalesItemImage_" + customerId + "_" + tsLong + ".jpg"
        requestWritePermission()
    }

    private fun requestWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            try {
                val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                Permissions.check(
                    this,
                    permissions,
                    null,
                    null,
                    object : PermissionHandler() {
                        override fun onGranted() {
                            pickFromCamera()
                        }
                        override fun onDenied(
                            context: Context,
                            deniedPermissions: ArrayList<String>
                        ) {
                        }
                    })
            } catch (e: Exception) {
            }
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                if (MarshmallowPermissions.checkPermissionCamera(this)) {
                    if (MarshmallowPermissions.checkPermissionWriteExternalStorage(this)) {
                        pickFromCamera()
                    } else {
                        MarshmallowPermissions.requestPermissionWriteExternalStorage(
                            this,
                            MarshmallowPermissions.PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE
                        )
                    }
                } else {
                    MarshmallowPermissions.requestPermissionCameraAndWriteExternalStorage(
                        this,
                        MarshmallowPermissions.PERMISSION_REQUEST_CODE_CAMERA_AND_STORAGE
                    )
                }
            } else {
                pickFromCamera()
            }
        }
    }
    private fun pickFromCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File
        photoFile = createImageFile()
        val photoUri = FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".provider",
            photoFile
        )
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        resultLauncher?.launch(pictureIntent)
    }
    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var file = File(Environment.getExternalStorageDirectory().toString() + "/ShopKirana")
        if (!file.exists()) {
            file.mkdirs()
        }
        file = File(storageDir, itemImageFileName)
        uploadFilePath = file.absolutePath
        return file
    }
    private fun handleReturnItemResult(it: Response<ArrayList<ReturnItemModel>>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    mReturnItemList = it
                    setValuesInAdapter()
                    binding.llSpReturnItem.visibility = View.VISIBLE
                }
            }
            is Response.Error -> {
                Utils.hideProgressDialog()
                binding.llSpReturnItem.visibility = View.GONE
                Utils.setToast(this,it.errorMesssage.toString())
            }
        }
    }

    private fun handleReturnOrderBatchItemResult(it: Response<ArrayList<ReturnOrderBatchItemModel>>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    mReturnOrderBatchItemList.clear()
                    mReturnOrderBatchItemList = it
                    binding.rlReturnItemDetails.visibility = View.VISIBLE
                   /* if (mAddedReturnOrderBatchItemList.size!=0){
                        for (itemAdded in mAddedReturnOrderBatchItemList){
                            if (itemAdded.isChecked){
                                for (newItem in mReturnOrderBatchItemList){
                                    if (itemAdded.orderDetailsId==newItem.orderDetailsId){
                                        newItem.returnQty =itemAdded.returnQty
                                        newItem.isChecked =true
                                        if (newItem.mImageList.size==0){
                                            newItem.mImageList.addAll(itemAdded.mImageList)
                                        }
                                    }
                                }
                            }
                        }
                    }*/
                    addRequestReturnItemAdapter!!.submitList(mReturnOrderBatchItemList)
                }
            }
            is Response.Error -> {
                Utils.hideProgressDialog()
                binding.rlReturnItemDetails.visibility = View.GONE
                Utils.setToast(this,it.errorMesssage.toString())
            }
        }
    }

    private fun handleRequestReturnItemResult(it: Response<SaleReturnRequestResponseModel>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    if (it.status!!) {
                        returnItemMsg()
                    } else {
                        Utils.setToast(this, it.msg)
                    }
                 }
            }
            is Response.Error -> {
                Utils.hideProgressDialog()
                Utils.setToast(this,MyApplication.getInstance().dbHelper.getString(R.string.return_request_not_submitted))
            }
        }
    }

    private fun setValuesInAdapter() {
        val list :ArrayList<String> = ArrayList()
        list.add(getString(R.string.list_of_item))
        mReturnItemList.forEach {
            list.add(it.itemname!!)
        }
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerItems.adapter = aa
       /* val adapter =
            SearchSuggestionAdapter(
                applicationContext, android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1, mReturnItemList
            )
        binding.searchItem.setAdapter(adapter)
        binding.searchItem.threshold = 1*/
    }

    private fun handleImageUploadResult(it: Response<String>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                 //   val img: String = Constant.imageBaseUrl + it
                    returnOrderBatchItemModel.mImageList.add(it)
                   // mAddedReturnOrderBatchItemList.add(returnOrderBatchItemModel)
                    addRequestReturnItemAdapter!!.submitList(mReturnOrderBatchItemList)
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                Utils.setToast(this,it.errorMesssage.toString())
            }
        }
    }

    fun returnItemMsg(){
        val dialog = Dialog(this, R.style.CustomDialog)
        dialog.setContentView(R.layout.return_msg_popup)
        dialog.setCancelable(false)
        val okBtn = dialog.findViewById<TextView>(R.id.ok_btn)
        val description = dialog.findViewById<TextView>(R.id.pd_description)
        val pd_title = dialog.findViewById<TextView>(R.id.pd_title)
        pd_title.text = MyApplication.getInstance().dbHelper.getString(R.string.cart_item_clear)
        description.text = MyApplication.getInstance().dbHelper.getString(R.string.msg_return_request)
        okBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.ok)
        okBtn.setOnClickListener { v: View? ->
            startActivity(Intent(this,ReturnOrderActivity::class.java))
            Utils.rightTransaction(this)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun alertMsg(keyValue: String,itemMultiMRPId: Int) {
        val dialog = Dialog(this, R.style.CustomDialog)
        dialog.setContentView(R.layout.return_msg_clear_popup)
        dialog.setCancelable(false)
        val noBtn = dialog.findViewById<TextView>(R.id.noBtn)
        val yesBtn = dialog.findViewById<TextView>(R.id.yesBtn)
        val description = dialog.findViewById<TextView>(R.id.pd_description)
        description.text = getString(R.string.msg_search_request_clear)
        yesBtn.setOnClickListener { v: View? ->
          //  mAddedReturnOrderBatchItemList.clear()
            mReturnOrderBatchItemList.clear()
            if (!keyValue.isNullOrEmpty()) {
                Utils.hideKeyboard(this@AddReturnOrderRequestActivity,binding.searchItem)
                returnOrderViewModel.getReturnItem(customerId, wareHouseId, keyValue)
            }else{
                callItemWiseOrderDetails(itemMultiMRPId)
            }
            dialog.dismiss()
        }
        noBtn.setOnClickListener { v: View? ->
            dialog.dismiss()
        }
        dialog.show()
    }
}