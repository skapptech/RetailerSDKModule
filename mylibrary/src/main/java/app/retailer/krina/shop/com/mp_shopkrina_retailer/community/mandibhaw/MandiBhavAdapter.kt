package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMandiBhavBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMandiCityBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMandiDistrictBinding

class MandiBhavAdapter(
    private var context: Context,
    private var mandiDataModelList: ArrayList<MandiDataModel>?,
): RecyclerView.Adapter<MandiBhavAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMandiBhavBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val model= mandiDataModelList!![position]
        holder.mBinding.tvProductName.text=model.Commodity
        holder.mBinding.tvDateMandi.text=model.ArrivalDate+" का भाव"
        holder.mBinding.tvMandiName.text=model.Market+" मंडी"
        holder.mBinding.tvProductRate.text="₹"+model.ModalPrice+"/क्विंटल"
        holder.mBinding.tvMinimumRate.text="₹"+model.MinPrice
        holder.mBinding.tvMaxmimuRate.text="₹"+model.MaxPrice

    }

    override fun getItemCount(): Int {
        return mandiDataModelList?.size ?: 0
    }


    class ViewHolder(var mBinding: ItemMandiBhavBinding) :
        RecyclerView.ViewHolder(mBinding.root) {}
}