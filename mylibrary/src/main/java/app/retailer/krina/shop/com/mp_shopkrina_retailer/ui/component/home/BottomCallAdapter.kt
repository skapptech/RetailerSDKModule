package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.BottomCall
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.RatingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemBottomArBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemBottomDbrBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemBottomTrackOrderBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.orderdetail.TrackOrderActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.io.File

class BottomCallAdapter(
    private val activity: HomeActivity,
    private val list: ArrayList<BottomCall>?,
    private val viewModel: HomeViewModel

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SALES_RATING = 1
    private val DELIVERY_RATING = 2
    private val ORDER_TRACK = 3
    private var adapterPos = 0
    private var isClosed = false
    var ratingList = ArrayList<RatingModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SALES_RATING) {
            SalesHolder(
                ItemBottomArBinding.inflate(
                    LayoutInflater.from(parent.context),
                     parent, false
                )
            )
        } else if (viewType == DELIVERY_RATING) {
            DeliveryHolder(
                ItemBottomDbrBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
        } else {
            TrackOrdHolder(
                ItemBottomTrackOrderBinding.inflate(
                    LayoutInflater.from(parent.context),
                     parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list!![position]
        ratingList.clear()
        if (holder.itemViewType == SALES_RATING) {
            (holder as SalesHolder).mBinding.tvPleaseRateH.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.please_rate_sales_person_bottom)
            var url =
                EndPointPref.getInstance(activity).baseUrl + File.separator + model.relativeUrl
            url = url.replace("{Id}", "" + model.id)
            viewModel.getGetDboyRatingOrder(url)
            viewModel.getDboyRatingOrderData.observe(activity) {
                ratingList = it
                if (it.size > 0) {
                    holder.mBinding.tvName.text = it[0].displayName
                }
            }
            holder.mBinding.liRate.setOnClickListener {
                adapterPos = position
                if (ratingList.size > 0) {
                    SalesRateFragment.newInstance(position, ratingList)
                        .show(activity.supportFragmentManager, "a")
                } else {
                    Utils.setToast(
                        activity,
                        RetailerSDKApp.getInstance().noteRepository.getString(R.string.text_some_error_occured)
                    )
                }
            }
            holder.mBinding.btnClose.setOnClickListener {
                isClosed = true
                if (ratingList.size > 0) {
                    val ratingModel = ratingList[0]
                    ratingModel.isRemoveFront = true
                    viewModel.getAddRatingData.observe(activity) {
                        Utils.hideProgressDialog()
                        if (!isClosed) Utils.setToast(
                            activity,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.your_feedback_submitted_suc)
                        )
                        isClosed = false
                    }
                    viewModel.addRating(ratingModel)
                    Utils.showProgressDialog(activity)

                }
                removeItem()
            }
        } else if (holder.itemViewType == DELIVERY_RATING) {
            (holder as DeliveryHolder).mBinding.tvPleaseRateH.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.please_rate_delivery_boy_bottom)
            var url =
                EndPointPref.getInstance(activity).baseUrl + File.separator + model.relativeUrl
            url = url.replace("{Id}", "" + model.id)
            viewModel.getGetDboyRatingOrder(url)
            viewModel.getDboyRatingOrderData.observe(activity) {
                ratingList = it
                if (it.size > 0) {
                    holder.mBinding.tvName.text = it[0].displayName
                }
            }
            holder.mBinding.liRate.setOnClickListener {
                adapterPos = position
                if (ratingList.size > 0) {
                    DBoyRatingFragment.newInstance(position, ratingList)
                        .show(activity.supportFragmentManager, "a")
                } else {
                    Utils.setToast(
                        activity,
                        RetailerSDKApp.getInstance().noteRepository.getString(R.string.text_some_error_occured)
                    )
                }
            }
            holder.mBinding.btnClose.setOnClickListener {
                isClosed = true
                if (ratingList.size > 0) {
                    val ratingModel = ratingList[0]
                    ratingModel.isRemoveFront = true
                    viewModel.getAddRatingData.observe(activity) {
                        Utils.hideProgressDialog()
                        if (!isClosed) Utils.setToast(
                            activity,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.your_feedback_submitted_suc)
                        )
                        isClosed = false
                    }
                    viewModel.addRating(ratingModel)
                    Utils.showProgressDialog(activity)

                }
                removeItem()
            }
        } else {
            (holder as TrackOrdHolder).mBinding.tvOrderH.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_is_arriving_today)
            var url =
                EndPointPref.getInstance(activity).baseUrl + File.separator + model.relativeUrl
            url = url.replace("{Id}", "" + model.id)
            url = url.replace(
                "{CustomerId}",
                "" + SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
            )
            viewModel.getGetDboyRatingOrderOther(url)
            viewModel.getDboyRatingOrderOtherData.observe(activity) {
                if (it != null)
                    holder.mBinding.tvName.text = it.dBoyName
            }
            holder.mBinding.btnTrack.setOnClickListener {
                adapterPos = position
                activity.startActivity(
                    Intent(activity, TrackOrderActivity::class.java)
                        .putExtra("id", model.id)
                )
            }
            holder.mBinding.btnClose.visibility = View.VISIBLE
            holder.mBinding.btnClose.setOnClickListener {
                adapterPos = position
                removeItem()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list!![position].type
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    private fun removeItem() {
        try {
            if (list != null && list.size > 0) {
                list.removeAt(adapterPos)
                notifyItemRemoved(adapterPos)
                notifyItemRangeChanged(adapterPos, list.size)
                if (list.size < 1)
                    activity.onButtonClick(adapterPos, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class SalesHolder(var mBinding: ItemBottomArBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )

    inner class DeliveryHolder(var mBinding: ItemBottomDbrBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )

    inner class TrackOrdHolder(var mBinding: ItemBottomTrackOrderBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )

}