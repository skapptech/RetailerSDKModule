package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.saleReturn

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.AddRequestItemsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.CategoryOrderItemsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.ReturnOrderBatchItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.sk.user.agent.ui.component.returnOrder.OnCheckboxClick
import java.text.DecimalFormat

class AddRequestReturnItemAdapter(
    onCheckboxClick: OnCheckboxClick,
    mContext: Context,
    mList: ArrayList<ReturnOrderBatchItemModel>
) :
    RecyclerView.Adapter<AddRequestReturnItemAdapter.ViewHolder>() {
    private var mList = mList
    private var mContext = mContext
    private var onCheckboxClick = onCheckboxClick
    fun submitList(list: ArrayList<ReturnOrderBatchItemModel>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AddRequestItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(mList[position])

    inner class ViewHolder(val binding: AddRequestItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ReturnOrderBatchItemModel) {
            with(itemView) {
                if (position % 2 == 1) {
                    binding.llBody.setBackgroundColor(Color.parseColor("#EBFBF3"));
                } else {
                    binding.llBody.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                binding.tvOrderId.text = model.orderId.toString()
                binding.tvBatchCode.text = model.batchCode
                binding.tvQty.text = model.returnableQty.toString()
                binding.tvRate.text = "₹" + DecimalFormat("##.##").format(model.unitPrice)
                if (model.isChecked) {
                    binding.cbItemCheck.isChecked = true
                    binding.etReturnQty.isEnabled = false
                    binding.etReturnQty.isClickable = false
                    binding.etReturnQty.setText(model.returnQty.toString())

                } else {
                    binding.cbItemCheck.isChecked = false
                    binding.etReturnQty.isEnabled = true
                    binding.etReturnQty.isClickable = true
                    binding.etReturnQty.setText("")
                }

                binding.cbItemCheck.setOnClickListener {
                    val requestQty = binding.etReturnQty.text.toString()
                    if (TextUtils.isNullOrEmpty(requestQty)) {
                        Utils.setToast(
                            mContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.please_enter_return_qty)
                        )
                        binding.cbItemCheck.isChecked = false
                    } else if (requestQty.toLong() <= 0){
                        Utils.setToast(
                            mContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.please_enter_return_qty)
                        )
                        binding.cbItemCheck.isChecked = false
                    }else if (model.returnableQty < requestQty.toLong()) {
                        Utils.setToast(
                            mContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.msg_you_can_not_return_qty)
                        )
                        binding.cbItemCheck.isChecked = false
                    } else {
                        if (binding.cbItemCheck.isChecked) {
                            model.returnQty = requestQty.toLong()
                            model.isChecked = true
                            onCheckboxClick.onClick(false, model, true)
                        } else {
                            model.isChecked = false
                            model.mImageList.clear()
                            //onCheckboxClick.onClick(false, model, false)
                            notifyDataSetChanged()
                        }
                    }
                }
                binding.llAddMoreImage.setOnClickListener {
                    val noOfImage =
                        EndPointPref.getInstance(context).getLong(EndPointPref.IMAGE_UPLOAD_QTY)
                    if (noOfImage != 0L && model.mImageList.size >= noOfImage) {
                        var des =
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.valid_image_not_upload_more_then)
                        des = des.replace("@", "$noOfImage")
                        Utils.setToast(
                            context, des
                        )
                    } else {
                        onCheckboxClick.onClick(true, model, false)
                    }
                }
                if (model.mImageList.size != 0) {
                    binding.llImageUpload.visibility = View.VISIBLE
                    val multipleImageAdapter = MultipleImageAdapter(mContext, model.mImageList)
                    val layoutManager = LinearLayoutManager(mContext)
                    binding.rvImage.layoutManager = layoutManager
                    binding.rvImage.adapter = multipleImageAdapter
                } else {
                    binding.llImageUpload.visibility = View.GONE
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}