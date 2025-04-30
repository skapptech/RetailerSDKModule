package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemPollOptionBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick

class PollOptionAdapter(
    private val activity: AddPollActivity,
    private val list: ArrayList<PostModel.PollValueEntity>?,
    private val onButtonClick: OnButtonClick
) : RecyclerView.Adapter<PollOptionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_poll_option, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list!![position]
        holder.binding.etTitle.setText("" + model.optionsValue)

        if (list.size == 2 && (position == 0 || position == 1)) {
            holder.binding.ivImage.visibility = View.GONE
        } else {
            holder.binding.ivImage.visibility = View.VISIBLE
        }

        holder.binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                if (!editable.isNullOrEmpty() && editable.toString().trim().isNotEmpty()) {
                    list[holder.adapterPosition].optionsValue = editable.toString()
                } else {
                    list[holder.adapterPosition].optionsValue = ""
                }
            }
        })
        holder.binding.ivImage.setOnClickListener {
            onButtonClick.onButtonClick(position, false)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(var binding: ItemPollOptionBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}