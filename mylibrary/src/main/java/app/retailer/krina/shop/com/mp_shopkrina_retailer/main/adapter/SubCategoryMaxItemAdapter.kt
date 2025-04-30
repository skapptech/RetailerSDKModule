package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.HomeSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel.AppItemsList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso

class SubCategoryMaxItemAdapter(
    private val context: Context,
    private val list: ArrayList<AppItemsList>?,
    mSectionType: String
) : RecyclerView.Adapter<SubCategoryMaxItemAdapter.ViewHolder>() {

    private val activity: HomeActivity
    private var mSectionType = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_gridview, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        try {
            val model = list!![i]
            viewHolder.centuryNameTV.text = model.tileName
            viewHolder.linearLayout.setOnClickListener {
                val args = Bundle()
                args.putInt("BaseCategoryId", model.baseCategoryId)
                args.putInt("CATEGORY_ID", model.categoryId)
                args.putInt("SUB_CAT_ID", model.subCategoryId)
                args.putInt("SUB_SUB_CAT_ID", model.subsubCategoryId)
                args.putBoolean("HOME_FLAG", true)
                args.putString("ITEM_IMAGE", model.tileImage)
                args.putString("SectionType", mSectionType)
                activity.pushFragments(HomeSubCategoryFragment.newInstance(), false, true, args)
            }
            if (!TextUtils.isNullOrEmpty(model.tileImage)) {
                Picasso.get().load(model.tileImage)
                    .placeholder(R.drawable.logo_grey_wide)
                    .error(R.drawable.logo_grey_wide)
                    .into(viewHolder.imageView)
            } else {
                viewHolder.imageView.setImageResource(R.drawable.logo_grey_wide)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val centuryNameTV: TextView
        val imageView: ImageView
        val linearLayout: LinearLayout

        init {
            linearLayout = itemView.findViewById(R.id.catLinearLayout)
            centuryNameTV = itemView.findViewById(R.id.textViewGirdItem_home)
            imageView = itemView.findViewById(R.id.imageViewGirdItem_home)
        }
    }

    init {
        this.mSectionType = mSectionType
        activity = context as HomeActivity
    }
}