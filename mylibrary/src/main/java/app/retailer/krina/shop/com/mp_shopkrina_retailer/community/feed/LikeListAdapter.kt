package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.ProfileActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.UserLikeListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemLikeListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import com.squareup.picasso.Picasso

class LikeListAdapter(
    private val activity: Activity,
    private val list: ArrayList<UserLikeListModel>,
    private var listener: LikeUserCall,
) : RecyclerView.Adapter<LikeListAdapter.ViewHolder>() {

interface LikeUserCall{
   fun callLikeUser(userID:String)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_like_list, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        if (model.userName.isNullOrEmpty()) {
            holder.binding.tvName.text= "Guest User " + model.userId
        } else {
            holder.binding.tvName.text=model.userName
        }
        if (model.img.isNotEmpty()) {
            Picasso.get().load( model.img)
                .into(holder.binding.profileImage)
        } else {
            holder.binding.profileImage.setImageResource(R.drawable.user_6)
        }
        holder.binding.llProfileView.setOnClickListener {

            listener.callLikeUser(model.userId)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(var binding: ItemLikeListBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}