package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMandiDistrictBinding

class DistrictAdapter(
    private var context: Context,
    private var districtList: ArrayList<String>?,
    private var lisner: MandiBhavLisner
): RecyclerView.Adapter<DistrictAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_mandi_district, parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val model= districtList!![position]
        holder.mBinding.tvMandiCity.text=model
        holder.mBinding.llDistrict.setOnClickListener {
            lisner.districtName(model)
        }
    }

    override fun getItemCount(): Int {
        return districtList?.size ?: 0
    }


    class ViewHolder(var mBinding: ItemMandiDistrictBinding) :
        RecyclerView.ViewHolder(mBinding.root) {}
}