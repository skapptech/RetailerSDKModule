package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.searchItem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchItemHistoryTitleList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.SerachhintTitleBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity

class SearchHistoryItemTitleAdapter(
    private val activity: HomeActivity,
    var list: ArrayList<SearchItemHistoryTitleList>
) : RecyclerView.Adapter<SearchHistoryItemTitleAdapter.ViewHolder>() {

    fun setData(list: ArrayList<SearchItemHistoryTitleList>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.serachhint_title, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dto = list[position]
        holder.mbinding.title.text = dto.title
        holder.mbinding.rvItems.setHasFixedSize(true)
        holder.mbinding.rvItems.adapter = SearchHistoryItemAdapter(activity, dto.list)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(var mbinding: SerachhintTitleBinding) : RecyclerView.ViewHolder(
        mbinding.root
    )
}