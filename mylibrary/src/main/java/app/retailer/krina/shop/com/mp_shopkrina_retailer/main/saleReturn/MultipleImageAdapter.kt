package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.saleReturn

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ReturnItemImageViewBinding

class MultipleImageAdapter(
    private val mContext: Context,
    List: ArrayList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mImageList: ArrayList<String> = List
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
            return ViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.return_item_image_view,
                    parent,
                    false
                )
            )
    }

    override fun getItemCount(): Int {
        return mImageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
            /* Picasso.get().load(
                SharePrefs.getInstance(activity).getString(SharePrefs.BASEURL) + invoiceImageList[i]
            )
                .into((holder as ViewHolder).mBinding.ivInvoice)*/
            (holder as ViewHolder).mBinding.tvImageName.text = mImageList[i]
            (holder as ViewHolder).mBinding.imDelete.setOnClickListener {
                mImageList.removeAt(i)
                notifyDataSetChanged()
            }
    }
    class ViewHolder(var mBinding: ReturnItemImageViewBinding) :
        RecyclerView.ViewHolder(mBinding.root)

}