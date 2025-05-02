package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMyAgentBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyAgentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso

class MyAgentListAdapter(
    private val activity: AppCompatActivity,
    val list: ArrayList<MyAgentModel>
) : RecyclerView.Adapter<MyAgentListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMyAgentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    inner class ViewHolder(val binding: ItemMyAgentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MyAgentModel) {
            with(itemView) {
                if (!TextUtils.isNullOrEmpty(model.profileImg)) {
                    Picasso.get().load(model.profileImg).into(binding.ivImage)
                } else {
                    binding.ivImage.setImageResource(R.drawable.logo_grey)
                }
                binding.tvName.text = model.salesPersonName
                binding.tvDesign.text = "" + model.groupName
                binding.ivCall.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:" + model.mobileNumber)
                    activity.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}