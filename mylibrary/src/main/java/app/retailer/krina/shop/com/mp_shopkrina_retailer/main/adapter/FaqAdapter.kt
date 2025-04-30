package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FaqItemBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.PipActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.FaqModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication

class FaqAdapter(private val context: Context, private val mylist: ArrayList<FaqModel>) :
    RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.faq_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_title.text = mylist[position].title
        holder.txt_see_video.text = MyApplication.getInstance().dbHelper.getData("text_watch_video")
        holder.ll_video.setOnClickListener {
            if (mylist[position].videoUrl.isNotEmpty()) {
                context.startActivity(
                    Intent(context, PipActivity::class.java)
                        .putExtra("title", mylist[position].title)
                        .putExtra("videoUrl", mylist[position].videoUrl)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return mylist.size
    }

    inner class ViewHolder(var mBinding: FaqItemBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        var txt_title: TextView
        var txt_see_video: TextView
        var ll_video: LinearLayout

        init {
            txt_title = mBinding.txtTitle
            txt_see_video = mBinding.txtSeeVideo
            ll_video = mBinding.llVideo
        }
    }
}