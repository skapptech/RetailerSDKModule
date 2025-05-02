package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.CountDownTimer
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemClearanceBillDiscountBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemClearanceScratchCardListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnClearanceOfferClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ClearanceDiscountAdapter(
    private val activity: Activity,
    private var list: ArrayList<BillDiscountModel>?,
    private val onOfferClick: OnClearanceOfferClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lang = ""

    init {
        lang = LocaleHelper.getLanguage(activity)
    }

    fun setBillDiscount(list: ArrayList<BillDiscountModel>?) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            ScratchCardHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_clearance_scratch_card_list, parent, false
                )
            )
        } else {
            BillDiscountHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_clearance_bill_discount, parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val model = list!![pos]
        if (holder.itemViewType == 0) {
            //set String
            (holder as ScratchCardHolder).mBinding.tvTimeLeft.text =
                MyApplication.getInstance().dbHelper.getString(R.string.time_left)
            holder.mBinding.tvDes.text = model.description
            if (model.isScratchBDCode) {
                holder.mBinding.tvTime.tag = pos
                holder.mBinding.tvOffer.textSize = 12f
                holder.mBinding.tvOffer.setTypeface(
                    holder.mBinding.tvOffer.typeface,
                    Typeface.NORMAL
                )
                holder.mBinding.tvOfferCode.visibility = View.VISIBLE
                holder.mBinding.tvOfferCode.text = model.offerCode
                holder.mBinding.ivImage.setImageResource(R.drawable.logo_sk)
                if (model.billDiscountOfferOn.equals("Percentage", ignoreCase = true)) {
                    holder.mBinding.tvOffer.text =
                        (DecimalFormat("##.##").format(model.discountPercentage)
                                + MyApplication.getInstance().dbHelper.getString(R.string.per_of_min_per) + DecimalFormat(
                            "##.##"
                        ).format(model.billAmount))
                } else {
                    holder.mBinding.tvOffer.text =
                        (MyApplication.getInstance().dbHelper.getString(R.string.flat_rs) + DecimalFormat(
                            "##.##"
                        ).format(convertToAmount(model.billDiscountWallet))
                                + " " + MyApplication.getInstance().dbHelper.getString(R.string.per_of_min_per_wallet) + DecimalFormat(
                            "##.##"
                        ).format(model.billAmount))
                }
                holder.mBinding.tvTime.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.btn_text_apply)
                holder.mBinding.tvTime.setTextColor(
                    activity.resources.getColor(R.color.text_color)
                )
                holder.mBinding.tvTimeLeft.visibility = View.GONE
            } else {
                holder.mBinding.tvOfferCode.visibility = View.GONE
                holder.mBinding.tvOffer.textSize = 15f
                holder.mBinding.tvOffer.setTypeface(holder.mBinding.tvOffer.typeface, Typeface.BOLD)
                holder.mBinding.ivImage.setImageResource(R.drawable.scratch_card)
                holder.mBinding.tvOffer.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.text_scratch_win)
                holder.mBinding.tvTime.text = "00:00"
                holder.mBinding.tvTime.setTextColor(
                    activity.resources.getColor(android.R.color.holo_red_dark)
                )
                holder.mBinding.tvTimeLeft.visibility = View.VISIBLE
                val timestamp = getTimeStamp(model.end!!)
                val expiryTime = timestamp - Date().time
                object : CountDownTimer(expiryTime, 1000) {
                    override fun onTick(millis: Long) {
                        val day = TimeUnit.MILLISECONDS.toDays(millis)
                        if (day > 0) {
                            holder.mBinding.tvTime.text = "Expires in $day day"
                        } else {
                            val hour = TimeUnit.MILLISECONDS.toHours(millis)
                            if (hour > 0) {
                                holder.mBinding.tvTime.setText("" + (hour % 24) + " hour")
                            } else {
                                val sec = TimeUnit.MILLISECONDS.toSeconds(millis)
                                val min = TimeUnit.MILLISECONDS.toMinutes(millis)
                                holder.mBinding.tvTime.text = (min % 60).toString() + ":" + sec % 60
                            }
                        }
                    }

                    override fun onFinish() {
                        holder.mBinding.tvTime.text =
                            MyApplication.getInstance().dbHelper.getString(R.string.text_time_expire)
                    }
                }.start()
            }
            if (model.isSelected) {
                holder.mBinding.rlBill.background =
                    activity.resources.getDrawable(R.drawable.rectangle_orange)
                holder.mBinding.tvTime.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.text_btn_applied)
            } else {
                holder.mBinding.rlBill.background =
                    activity.resources.getDrawable(R.drawable.rectangle_grey)
                holder.mBinding.tvTime.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.btn_text_apply)
            }
        } else {
            if (!TextUtils.isNullOrEmpty(model.description)) {
                (holder as BillDiscountHolder).mBinding.tvOfferDes.text = "" + model.description
            } else {
                (holder as BillDiscountHolder).mBinding.tvOfferDes.visibility = View.GONE
            }
            holder.mBinding.tvSelect.tag = pos
            holder.mBinding.rlBillItem.visibility = View.GONE
            if (model.billDiscountOfferOn.equals("Percentage", ignoreCase = true)) {
                holder.mBinding.tvOffer.text =
                    (DecimalFormat("##.##").format(model.discountPercentage)
                            + MyApplication.getInstance().dbHelper.getString(R.string.per_of_min_per) + model.billAmount)
            } else if (model.billDiscountOfferOn.equals("FreeItem", ignoreCase = true)) {
                holder.mBinding.rlBillItem.visibility = View.VISIBLE
                holder.mBinding.tvOffer.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.bill_free_item) + " " + model.billAmount
                holder.mBinding.recyclerBillDiscountItem.layoutManager = LinearLayoutManager(
                    activity
                )
                val discountItemAdapter = BillDiscountFreeItemAdapter(
                    activity, model.retailerBillDiscountFreeItemDcs!!
                )
                holder.mBinding.recyclerBillDiscountItem.adapter = discountItemAdapter
            } else {
                val msgPostBill = if (model.applyOn.equals(
                        "PostOffer",
                        ignoreCase = true
                    )
                ) " " + MyApplication.getInstance().dbHelper.getString(R.string.post_bill_text) else ""
                if (model.walletType.equals("WalletPercentage", ignoreCase = true)) {
                    holder.mBinding.tvOffer.text =
                        (DecimalFormat("##.##").format(model.billDiscountWallet)
                                + MyApplication.getInstance().dbHelper.getString(R.string.per_of_min_per) + DecimalFormat(
                            "##.##"
                        ).format(model.billAmount) + msgPostBill)
                } else {
                    holder.mBinding.tvOffer.text =
                        (MyApplication.getInstance().dbHelper.getString(R.string.flat_rs) +
                                DecimalFormat("##.##").format(convertToAmount(model.billDiscountWallet))
                                + " " + MyApplication.getInstance().dbHelper.getString(R.string.per_of_min_per_wallet) + DecimalFormat(
                            "##.##"
                        ).format(model.billAmount) + msgPostBill)
                }
            }
            holder.mBinding.tvMinQty.text =
                "( " + MyApplication.getInstance().dbHelper.getString(R.string.min_ord_value) + DecimalFormat(
                    "##.##"
                ).format(model.billAmount) + " )"
            val timestamp = getTimeStamp(model.end!!)
            val expiryTime = timestamp - Date().time
            object : CountDownTimer(expiryTime, 1000) {
                override fun onTick(millis: Long) {
                    val day = TimeUnit.MILLISECONDS.toDays(millis)
                    if (day > 0) {
                        holder.mBinding.tvTime.text = "Expires in $day day"
                    } else {
                        val hour = TimeUnit.MILLISECONDS.toHours(millis)
                        if (hour > 0) {
                            holder.mBinding.tvTime.text = "Expires in " + hour % 24 + " hour"
                        } else {
                            val sec = TimeUnit.MILLISECONDS.toSeconds(millis)
                            val min = TimeUnit.MILLISECONDS.toMinutes(millis)
                            holder.mBinding.tvTime.text = "Expires in " + min % 60 + ":" + sec % 60
                        }
                    }
                }

                override fun onFinish() {
                    holder.mBinding.tvTime.text = "Time Expired!"
                }
            }.start()
            if (model.isSelected) {
                holder.mBinding.rlBill.background =
                    activity.resources.getDrawable(R.drawable.rectangle_orange)
                holder.mBinding.tvSelect.background =
                    activity.resources.getDrawable(R.drawable.rectangle_orange)
                holder.mBinding.tvSelect.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.text_btn_applied)
            } else {
                holder.mBinding.rlBill.background =
                    activity.resources.getDrawable(R.drawable.rectangle_grey)
                holder.mBinding.tvSelect.background =
                    activity.resources.getDrawable(R.drawable.rectangle_grey)
                holder.mBinding.tvSelect.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.apply)
            }
            if (model.billDiscountType != "ClearanceStock") {
                holder.mBinding.tvSelect.visibility = View.INVISIBLE
            } else {
                holder.mBinding.tvSelect.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list!![position].offerOn.equals(
                "ScratchBillDiscount",
                ignoreCase = true
            )
        ) 0 else 1
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    inner class BillDiscountHolder internal constructor(var mBinding: ItemClearanceBillDiscountBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        init {
            mBinding.ivInfo.setOnClickListener { view: View? ->
                AlertDialog.Builder(
                    activity
                ).setTitle(MyApplication.getInstance().dbHelper.getString(R.string.offer_terms))
                    .setMessage(
                        """${list!![adapterPosition].offerName}
    Discount Type: ${list!![adapterPosition].billDiscountType}
    ${Html.fromHtml("" + list!![adapterPosition].description)}
    """.trimIndent()
                    )
                    .setPositiveButton(MyApplication.getInstance().dbHelper.getString(R.string.ok)) { dialog: DialogInterface, i: Int -> dialog.dismiss() }
                    .show()
            }
            mBinding.tvSelect.setOnClickListener {
                val isApplied = !list!![adapterPosition].isSelected
                onOfferClick.onApplyOfferClick(list!![adapterPosition].offerId, isApplied)
            }
        }
    }

    inner class ScratchCardHolder internal constructor(var mBinding: ItemClearanceScratchCardListBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
    }

    private fun convertToAmount(amount: Double): Double {
        return amount / 10
    }

    companion object {
        private fun getTimeStamp(dateStr: String): Long {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val all = dateStr.replace("\\+0([0-9]){1}\\:00".toRegex(), "+0$100")
            val s = all.replace("T".toRegex(), " ")
            var timestamp: Long = 0
            try {
                val date = format.parse(s)
                timestamp = date.time
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return timestamp
        }
    }
}