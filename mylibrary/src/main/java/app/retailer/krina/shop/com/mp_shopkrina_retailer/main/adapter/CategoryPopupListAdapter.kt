package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.CateListRowBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.CatePopupInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.CategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.bumptech.glide.Glide

class CategoryPopupListAdapter(
    private val context: Context,
    private val mListCate: List<CategoriesModel>?,
    private val cateInterface: CatePopupInterface
) : RecyclerView.Adapter<CategoryPopupListAdapter.ViewHolder>() {

    private val handler = Handler(Looper.getMainLooper())


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.cate_list_row, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        try {
            viewHolder.mBinding.cateTitle.text = mListCate!![i].categoryname
            viewHolder.letCateClicked.setOnClickListener {
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        cateInterface.catePopupSelected(
                            i, mListCate[i].categoryImg,
                            mListCate[i].categoryid, mListCate[i].categoryname
                        )
                        handler.postDelayed(this, 500)
                        handler.removeCallbacks(this)
                    }
                }, 200)
            }
            if (!TextUtils.isNullOrEmpty(
                    mListCate[i].logourl
                )
            ) {
                Glide.with(context).load(mListCate[i].logourl)
                    .placeholder(R.drawable.logo_grey)
                    .error(R.drawable.logo_grey)
                    .into(viewHolder.ivCatImg)
            } else {
                viewHolder.ivCatImg.setImageResource(R.drawable.ic_placeholder)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return mListCate?.size ?: 0
    }

    inner class ViewHolder(var mBinding: CateListRowBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        val letCateClicked: LinearLayout
        val ivCatImg: ImageView

        init {
            letCateClicked = mBinding.letCateClicked
            ivCatImg = mBinding.catImg
        }
    }
}