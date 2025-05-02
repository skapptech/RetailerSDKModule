package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubSubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.NewSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.BaseCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso

class SubCategoryItemAdapter(private val context: Context, mSectionType: String?) :
    RecyclerView.Adapter<SubCategoryItemAdapter.ViewHolder>() {

    private var list: ArrayList<SubSubCategoriesModel>? = null
    private val handler = Handler()
    private val activity: HomeActivity
    private var baseCategoryModel: BaseCategoriesModel? = null
    private var mSectionType: String? = ""


    fun setData(list: ArrayList<SubSubCategoriesModel>?, baseCategoryModel: BaseCategoriesModel?) {
        this.list = list
        this.baseCategoryModel = baseCategoryModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_gridview, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val model = list!![i]
        viewHolder.centuryNameTV.text = model.subsubcategoryname
        if (!TextUtils.isNullOrEmpty(model.logourl)) {
            Picasso.get().load(model.logourl)
                .placeholder(R.drawable.logo_grey).into(viewHolder.imageView)
        } else {
            viewHolder.imageView.setImageResource(R.drawable.ic_placeholder)
        }
        viewHolder.linearLayout.setOnClickListener { v: View? ->
            val args = Bundle()
            args.putInt("CATEGORY_ID", model.categoryid)
            args.putInt("SUB_CAT_ID", model.subcategoryid)
            args.putInt("SUB_SUB_CAT_ID", model.subsubcategoryid)
            args.putString("SectionType", mSectionType)
            args.putSerializable("BASE_CATE_MODEL", baseCategoryModel)
            handler.postDelayed({
                val fragmentManager = activity.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment: Fragment = NewSubCategoryFragment.newInstance()
                fragment.arguments = args
                fragmentTransaction.replace(R.id.content, fragment)
                    .addToBackStack(NewSubCategoryFragment::class.java.name)
                fragmentTransaction.commit()
            }, 400)
        }
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
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