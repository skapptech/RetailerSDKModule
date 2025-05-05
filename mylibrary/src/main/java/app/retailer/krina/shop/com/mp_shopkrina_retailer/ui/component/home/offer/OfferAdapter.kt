package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.offer

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemOffersBillDiscountBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemOffersScratchCardListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.BillDiscountFreeItemAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.dialog.OfferInfoFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.CheckBillDiscountResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.scratchCard.ScratchCard
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CustomRunnable
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class OfferAdapter(
    private val activity: HomeActivity,
    private val list: ArrayList<BillDiscountModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val handler = Handler()
    var customRunnable: CustomRunnable? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            ScratchCardHolder(
                ItemOffersScratchCardListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            BillDiscountHolder(
                ItemOffersBillDiscountBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val model = list[pos]
        if (holder.itemViewType == 0) {
            (holder as ScratchCardHolder).mBinding.rlBill.backgroundTintList =
                ColorStateList.valueOf(
                    Color.parseColor(if (model.colorCode != null) model.colorCode else "#4D9654")
                )
            //set String
            holder.mBinding.tvDes.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.min_ord_value) + model.billAmount
            if (EndPointPref.getInstance(activity)
                    .getBoolean(EndPointPref.showOfferBtn)
            ) holder.mBinding.rlApply.visibility =
                View.VISIBLE else holder.mBinding.rlApply.visibility = View.INVISIBLE
            if (model.isScratchBDCode) {
                holder.mBinding.tvTime.tag = pos
                holder.mBinding.ivImage.setImageResource(R.drawable.logo_sk)
                if (model.billDiscountOfferOn != null && model.billDiscountOfferOn.equals(
                        "Percentage",
                        ignoreCase = true
                    )
                ) {
                    holder.mBinding.tvOffer.text =
                        DecimalFormat("##.##").format(model.discountPercentage) + "% " + RetailerSDKApp.getInstance().dbHelper.getString(
                            R.string.bill_discount
                        )
                } else if (model.billDiscountOfferOn != null && model.billDiscountOfferOn.equals(
                        "DynamicAmount",
                        ignoreCase = true
                    )
                ) {
                    holder.mBinding.tvOffer.text =
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.flat_rs) + DecimalFormat(
                            "##.##"
                        ).format(model.billDiscountWallet) + " " + RetailerSDKApp.getInstance().dbHelper.getString(
                            R.string.off
                        )
                } else {
                    holder.mBinding.tvOffer.text =
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.flat_rs) + DecimalFormat(
                            "##.##"
                        ).format(convertToAmount(model.billDiscountWallet)) + " " + RetailerSDKApp.getInstance().dbHelper.getString(
                            R.string.off
                        )
                }
                // ((ScratchCardHolder) holder).mBinding.tvTime.setTextColor(activity.getResources().getColor(R.color.text_color));
            } else {
                holder.mBinding.tvOffer.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_scratch_win)
                holder.mBinding.tvTime.text = "00:00"
                // ((ScratchCardHolder) holder).mBinding.tvTime.setTextColor(activity.getResources().getColor(android.R.color.holo_red_dark));
            }
            if (list[pos].ImagePath != null) {
                var path = model.ImagePath
                if (!model.ImagePath!!.contains("https")) path =
                    EndPointPref.getInstance(activity).baseUrl + model.ImagePath
                Glide.with(activity).load(path).placeholder(R.drawable.scratch_card).into(
                    holder.mBinding.ivImage
                )
            } else {
                holder.mBinding.ivImage.setImageResource(R.drawable.scratch_card)
            }
            val timestamp = getTimeStamp(model.end)
            val endTime = timestamp - Date().time

//            new CountDownTimer(expiryTime, 1000) {
//                @Override
//                public void onTick(long millis) {
//                    long day = TimeUnit.MILLISECONDS.toDays(millis);
//                    if (day > 0) {
//                        ((ScratchCardHolder) holder).mBinding.tvTime.setText("Expires in " + day + " day");
//                    } else {
//                        long hour = TimeUnit.MILLISECONDS.toHours(millis);
//                        if (hour > 0) {
//                            ((ScratchCardHolder) holder).mBinding.tvTime.setText("Expires in " + hour % 24 + " hour");
//                        } else {
//                            long sec = TimeUnit.MILLISECONDS.toSeconds(millis);
//                            long min = TimeUnit.MILLISECONDS.toMinutes(millis);
//                            ((ScratchCardHolder) holder).mBinding.tvTime.setText("Expires in " + min % 60 + ":" + sec % 60);
//                        }
//                    }
//                }
//
//                @Override
//                public void onFinish() {
//                    ((ScratchCardHolder) holder).mBinding.tvTime.setText(MyApplication.getInstance().dbHelper.getString(R.string.text_time_expire));
//                    try {
//                        list.remove(pos);
//                        notifyItemRangeRemoved(pos, list.size());
//                        notifyDataSetChanged();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
            startTimer(holder.mBinding.tvTime, endTime)
        } else {
            (holder as BillDiscountHolder).mBinding.rlBill.backgroundTintList =
                ColorStateList.valueOf(
                    Color.parseColor(if (model.colorCode != null) model.colorCode else "#4D9654")
                )
            if (EndPointPref.getInstance(activity)
                    .getBoolean(EndPointPref.showOfferBtn)
            ) holder.mBinding.rlApply.visibility =
                View.VISIBLE else holder.mBinding.rlApply.visibility = View.INVISIBLE
            if (list[pos].applyType.equals("PrimeCustomer", ignoreCase = true)) {
                holder.mBinding.tvPrimeOffer.visibility = View.VISIBLE
            } else {
                holder.mBinding.tvPrimeOffer.visibility = View.GONE
            }
            if (list[pos].ImagePath != null) {
                var path = model.ImagePath
                if (!model.ImagePath!!.contains("https")) path =
                    EndPointPref.getInstance(activity).baseUrl + model.ImagePath
                Glide.with(activity).load(path).placeholder(R.drawable.logo_grey).into(
                    holder.mBinding.ivImage
                )
            } else {
                holder.mBinding.ivImage.setImageResource(R.drawable.logo_sk)
            }
            holder.mBinding.tvOfferDes.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.min_ord_value) + list[pos].billAmount
            holder.mBinding.tvSelect.tag = pos
            holder.mBinding.rlBillItem.visibility = View.GONE
            if (model.billDiscountOfferOn.equals("Percentage", ignoreCase = true)) {
                holder.mBinding.tvOffer.text =
                    DecimalFormat("##.##").format(model.discountPercentage) + "% " + RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.bill_discount
                    )
                holder.mBinding.tvItemName.visibility = View.GONE
            } else if (model.billDiscountOfferOn.equals("FreeItem", ignoreCase = true)) {
                holder.mBinding.rlBillItem.visibility = View.VISIBLE
                holder.mBinding.tvOffer.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.free_item_offer)
                val discountItemAdapter = BillDiscountFreeItemAdapter(
                    activity, model.retailerBillDiscountFreeItemDcs!!
                )
                holder.mBinding.recyclerBillDiscountItem.adapter = discountItemAdapter
                holder.mBinding.tvItemName.visibility = View.VISIBLE
                if (model.isBillDiscountFreebiesItem) {
                    holder.mBinding.tvItemName.text = ("On every purchase of "
                            + model.offerMinOrderQty + " " + model.offeritemname + " Get free")
                } else if (model.isBillDiscountFreebiesValue) {
                    holder.mBinding.tvItemName.text = ("On every purchase worth rupees "
                            + model.billAmount + " Get free")
                } else holder.mBinding.tvItemName.visibility = View.GONE
            } else {
                val msgPostBill =
                    if (model.applyOn.equals("PostOffer", ignoreCase = true)) " PostOffer" else ""
                if (model.walletType.equals("WalletPercentage", ignoreCase = true)) {
                    holder.mBinding.tvOffer.text =
                        DecimalFormat("##.##").format(model.billDiscountWallet) + "%  " + RetailerSDKApp.getInstance().dbHelper.getString(
                            R.string.off
                        ) + msgPostBill
                } else {
                    holder.mBinding.tvOffer.text =
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.flat_rs) + DecimalFormat(
                            "##.##"
                        ).format(convertToAmount(model.billDiscountWallet)) + RetailerSDKApp.getInstance().dbHelper.getString(
                            R.string.off
                        ) + msgPostBill
                }
                holder.mBinding.tvItemName.visibility = View.GONE
            }
            holder.mBinding.tvMinQty.text =
                "( " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.min_ord_value) + DecimalFormat(
                    "##.##"
                ).format(model.billAmount) + " )"
            // timer
            val timestamp = getTimeStamp(model.end)
            val endTime = timestamp - Date().time
            //            new CountDownTimer(endTime, 1000) {
//                @Override
//                public void onTick(long millis) {
//                    long day = TimeUnit.MILLISECONDS.toDays(millis);
//                    if (day > 0) {
//                        ((BillDiscountHolder) holder).mBinding.tvTime.setText("Expires in " + day + " day");
//                    } else {
//                        long hour = TimeUnit.MILLISECONDS.toHours(millis);
//                        if (hour > 0) {
//                            ((BillDiscountHolder) holder).mBinding.tvTime.setText("Expires in " + hour % 24 + " hour");
//                        } else {
//                            long sec = TimeUnit.MILLISECONDS.toSeconds(millis);
//                            long min = TimeUnit.MILLISECONDS.toMinutes(millis);
//                            ((BillDiscountHolder) holder).mBinding.tvTime.setText("Expires in " + min % 60 + ":" + sec % 60);
//                        }
//                    }
//                }
//
//                @Override
//                public void onFinish() {
//                    ((BillDiscountHolder) holder).mBinding.tvTime.setText("Time Expired!");
//                    try {
//                        list.remove(pos);
//                        notifyItemRangeRemoved(pos, list.size());
//                        notifyDataSetChanged();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
            startTimer(holder.mBinding.tvTime, endTime)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].offerOn.equals("ScratchBillDiscount", ignoreCase = true)) 0 else 1
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class BillDiscountHolder internal constructor(var mBinding: ItemOffersBillDiscountBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        init {
            mBinding.llMain.setOnClickListener { v: View? ->
                if (EndPointPref.getInstance(
                        activity
                    ).getBoolean(EndPointPref.showOfferBtn)
                ) {
                    val args = Bundle()
                    args.putSerializable("model", list[adapterPosition])
                    activity.pushFragments(OfferDetailFragment(), false, true, args)
                }
            }
            mBinding.ivInfo.setOnClickListener { view: View? ->
                OfferInfoFragment.newInstance(0, list[adapterPosition]).show(
                    activity.supportFragmentManager, "a"
                )
            }
            //
            customRunnable = CustomRunnable(handler, mBinding.tvTime, 10000)
        }
    }

    inner class ScratchCardHolder internal constructor(var mBinding: ItemOffersScratchCardListBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        init {
            mBinding.root.setOnClickListener { v: View? ->
                if (!list[adapterPosition].isScratchBDCode) {
                    openScratchDialog(adapterPosition, list)
                } else {
                    if (EndPointPref.getInstance(activity).getBoolean(EndPointPref.showOfferBtn)) {
                        val args = Bundle()
                        args.putSerializable("model", list[adapterPosition])
                        activity.pushFragments(OfferDetailFragment(), false, true, args)
                    }
                }
            }
            mBinding.ivInfo.setOnClickListener { view: View? ->
                if (list[adapterPosition].isScratchBDCode) OfferInfoFragment.newInstance(
                    0,
                    list[adapterPosition]
                ).show(
                    activity.supportFragmentManager, "a"
                ) else Utils.setToast(
                    activity, "Scratch the card first"
                )
            }
            //
            customRunnable = CustomRunnable(handler, mBinding.tvTime, 10000)
        }
    }

    private fun convertToAmount(amount: Double): Double {
        return amount / 10
    }

    private fun startTimer(textView: TextView, endTime: Long) {
        handler.removeCallbacks(customRunnable!!)
        customRunnable!!.holder = textView
        customRunnable!!.millisUntilFinished = endTime //Current time - received time
        handler.postDelayed(customRunnable!!, 1000)
    }

    private fun openScratchDialog(position: Int, discountList: ArrayList<BillDiscountModel>?) {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.activity_scratch_card)
        dialog.setCancelable(true)
        val card = dialog.findViewById<ScratchCard>(R.id.scratchCard)
        val tvTime = dialog.findViewById<TextView>(R.id.tv_time)
        val tvName = dialog.findViewById<TextView>(R.id.tv_name)
        val tvCoupon = dialog.findViewById<TextView>(R.id.tv_coupon)
        val tvDynamicAmt = dialog.findViewById<TextView>(R.id.tvDynamicAmt)
        val tvDetail = dialog.findViewById<TextView>(R.id.tv_detail)
        val btnApply = dialog.findViewById<Button>(R.id.btn_apply)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        ivClose.setOnClickListener { v: View? -> dialog.dismiss() }
        btnApply.setOnClickListener { v: View? ->
            dialog.dismiss()
            notifyDataSetChanged()
        }
        card.setOnScratchListener { scratchCard: ScratchCard?, visiblePercent: Float ->
            if (visiblePercent > 0.3) {
                card.visibility = View.GONE
                tvTime.visibility = View.GONE
                btnApply.visibility = View.VISIBLE
                tvName.setText(R.string.congratulations)
                tvDetail.text = discountList!![position].offerName
                list[position].isScratchBDCode = true
                notifyDataSetChanged()
                CommonClassForAPI.getInstance(activity).updateScratchCardStatus(
                    updateScratchCardObserver, SharePrefs.getInstance(
                        activity
                    ).getInt(SharePrefs.CUSTOMER_ID), discountList[position].offerId, true
                )
            }
        }
        if (discountList != null) {
            if (discountList[position].billDiscountOfferOn != null && discountList[position].billDiscountOfferOn.equals(
                    "DynamicAmount",
                    ignoreCase = true
                )
            ) {
                tvCoupon.text = "₹" + discountList[position].billDiscountWallet
            } else tvCoupon.text = discountList[position].offerCode
            val timestamp = getTimeStamp(discountList[position].end)
            val expiryTime = timestamp - Date().time
            object : CountDownTimer(expiryTime, 1000) {
                override fun onTick(millis: Long) {
                    val sec = TimeUnit.MILLISECONDS.toSeconds(millis)
                    val min = TimeUnit.MILLISECONDS.toMinutes(millis)
                    val hour = TimeUnit.MILLISECONDS.toHours(millis)
                    tvTime.text = """Offer Date
Offer Expires in ${hour % 24}:${min % 60}:${sec % 60} hrs"""
                }

                override fun onFinish() {
                    tvTime.text = "Time Expired!"
                }
            }.start()
        }
        RetailerSDKApp.getInstance().updateAnalytics("scratch_dialog_open")
        dialog.show()
    }

    // scratch card
    var updateScratchCardObserver: DisposableObserver<CheckBillDiscountResponse> =
        object : DisposableObserver<CheckBillDiscountResponse>() {
            override fun onNext(response: CheckBillDiscountResponse) {
                Utils.hideProgressDialog()
            }
            override fun onError(e: Throwable) {
                e.printStackTrace()
                dispose()
                Utils.hideProgressDialog()
            }
            override fun onComplete() {}
        }

    companion object {
        private fun getTimeStamp(dateStr: String?): Long {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val all = dateStr!!.replace("\\+0([0-9]){1}\\:00".toRegex(), "+0$100")
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