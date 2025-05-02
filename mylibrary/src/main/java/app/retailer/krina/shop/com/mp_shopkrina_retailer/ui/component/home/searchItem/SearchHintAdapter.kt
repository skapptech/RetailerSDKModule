package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.searchItem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.SearchHistoryRowAdapterBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.Searchclick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.SearchHistoryModel

class SearchHintAdapter(private val context: Context, private val searchclick: Searchclick) :
    RecyclerView.Adapter<SearchHintAdapter.ViewHolder>() {

    private var searchHistoryModel: ArrayList<SearchHistoryModel>? = null

    fun setData(searchHistoryModel: ArrayList<SearchHistoryModel>?) {
        this.searchHistoryModel = searchHistoryModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate<SearchHistoryRowAdapterBinding>(
                LayoutInflater.from(parent.context),
                R.layout.search_history_row_adapter, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Model = searchHistoryModel!![position]
        holder.mbinding.tvName.text = Model.name
        holder.mbinding.llSearchclick.setOnClickListener { view: View? ->
            searchclick.getSearchString(
                Model.name
            )
        }
        holder.mbinding.llClose.setOnClickListener { view: View? -> searchclick.getKeyword(Model.name) }
    }

    override fun getItemCount(): Int {
        return searchHistoryModel!!.size
    }

    inner class ViewHolder(var mbinding: SearchHistoryRowAdapterBinding) : RecyclerView.ViewHolder(
        mbinding.root
    )
}