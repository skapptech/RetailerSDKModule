package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemLikeListBinding
import com.squareup.picasso.Picasso

class FollowerListAdapter(
    private val activity: Activity,
    private val list: ArrayList<ListModel>,
    private var listener: FollowerListener,
) : RecyclerView.Adapter<FollowerListAdapter.ViewHolder>() {

interface FollowerListener{
   fun FollowerUserList(userID:String)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLikeListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        if (model.userName.isNullOrEmpty()) {
            holder.binding.tvName.text= "Guest User " + model.followerId
        } else {
            holder.binding.tvName.text=model.userName
        }
        if (model.img.isNotEmpty()) {
            Picasso.get().load(model.img)
                .into(holder.binding.profileImage)
        } else {
            holder.binding.profileImage.setImageResource(R.drawable.user_6)
        }
        holder.binding.llProfileView.setOnClickListener {

            listener.FollowerUserList(model.followerId)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(var binding: ItemLikeListBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}