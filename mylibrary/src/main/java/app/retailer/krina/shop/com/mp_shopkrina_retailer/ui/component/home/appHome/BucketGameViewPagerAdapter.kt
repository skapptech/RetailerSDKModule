package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.GameBannerModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.RowItemBucketGamePagerBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class BucketGameViewPagerAdapter : RecyclerView.Adapter<BucketGameViewPagerAdapter.MyViewHolder> {
    private val activity: HomeActivity
    private var list: ArrayList<GameBannerModel>? = null
    private val dataDetailsList: HomeDataModel

    constructor(activity: HomeActivity, homeDataModel: HomeDataModel) {
        this.activity = activity
        dataDetailsList = homeDataModel
        list = ArrayList()
    }

    fun setData(list: ArrayList<GameBannerModel>?) {
        this.list = list
        notifyDataSetChanged()
    }


    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_item_bucket_game_pager, parent, false
            )
        )
    }

    override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        val model = list!![position]

        holder.binding.tvRewardType.text = "" + DecimalFormat("##.##").format(model.walletRewardValue)
        holder.binding.tvPoints.text = ""
        holder.binding.progressDays.visibility = View.INVISIBLE
        holder.binding.tvProgress.visibility = View.INVISIBLE
        holder.binding.ivEmoji.visibility = View.INVISIBLE
        if (model.individualDaysLeft < -1)
            holder.binding.tvDaysLeft.text = ""
        else if (model.individualDaysLeft == 0)
            holder.binding.tvDaysLeft.text = "Order Now"
        else
            holder.binding.tvDaysLeft.text = ""  + model.individualDaysLeft + " days left"

        Glide.with(activity)
            .load(dataDetailsList.appItemsList!![0].bannerImage?.trim())
            .placeholder(R.drawable.rectangle_grey)
            .into((holder.binding.ivBanner))

        holder.binding.root.setOnClickListener {
//            Intent().setClassName(
//                applicationContext.packageName ,
//                "com.sk.bucketgame.BucketGameActivity"
//            ).also {
//                activity.startActivity(it)
//            }
//            Utils.fadeTransaction(activity)
        }
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    inner class MyViewHolder(var binding: RowItemBucketGamePagerBinding) :
        RecyclerView.ViewHolder(binding.root)
}
