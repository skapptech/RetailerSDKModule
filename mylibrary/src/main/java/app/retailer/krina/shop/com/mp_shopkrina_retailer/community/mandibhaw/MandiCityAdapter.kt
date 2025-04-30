package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMandiCityBinding

class MandiCityAdapter(
    private var context: Context,
    private var cityList: ArrayList<String>,
    private var lisner: MandiBhavLisner
) : RecyclerView.Adapter<MandiCityAdapter.ViewHolder?>() {

    private var isview = false
    private var viewPosition:Int = 0
    private var districtList: ArrayList<String>? = null

    fun submitDistrictList(data: ArrayList<String>) {
        districtList = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_mandi_city, parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = cityList[position]
        holder.mBinding.tvMandiCity.text = model
        holder.mBinding.rlMainCity.setOnClickListener {
            if (!isview) {
                holder.mBinding.rvMandiDistrict.visibility=View.VISIBLE
                isview = true
                viewPosition =position
                holder.mBinding.ivUpdownArrow.rotation = 270f
                lisner.cityClick(model)
            } else {
                isview = false
                viewPosition =position
                holder.mBinding.ivUpdownArrow.rotation = 0f
                holder.mBinding.rvMandiDistrict.visibility=View.GONE
            }
        }

        if (position==viewPosition){
            holder.mBinding.rvMandiDistrict.visibility=View.VISIBLE
        }else{
            holder.mBinding.rvMandiDistrict.visibility=View.GONE
        }

        val adapter = DistrictAdapter(context, districtList,lisner)
        holder.mBinding.rvMandiDistrict.adapter = adapter
    }

    override fun getItemCount(): Int {
        return cityList.size
    }




    class ViewHolder(var mBinding: ItemMandiCityBinding) :
        RecyclerView.ViewHolder(mBinding.root)
}