package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemsDocImageBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HisabKitabImageModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.webView.ShowImageActivity
import com.bumptech.glide.Glide

class HKImageAdapter(
    private val context: Context,
    private val ImagesModel: ArrayList<HisabKitabImageModel>
) : RecyclerView.Adapter<HKImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            ItemsDocImageBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val irImagesModel = ImagesModel[i]
        Glide.with(context).load(irImagesModel.imagePath.replace("\"", ""))
            .into(viewHolder.mBinding.ivTaskImage)

        viewHolder.mBinding.llMainDoc.setOnClickListener { v: View? ->
            context.startActivity(
                Intent(
                    context, ShowImageActivity::class.java
                ).putExtra("ImageData", ImagesModel)
            )
        }
    }

    override fun getItemCount(): Int {
        return ImagesModel.size
    }

    inner class ViewHolder(val mBinding: ItemsDocImageBinding) :
        RecyclerView.ViewHolder(mBinding.root)
}