package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SectionIndexer
import android.widget.TextView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.indexingScroll.StringMatcher
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AllBrandsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands.BrandOrderFragment.Companion.newInstance
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso
import java.text.BreakIterator
import java.util.Locale

class AllBrandsAdapter(
    private val activity: HomeActivity,
    resourceId: Int,
    private val mitemList: List<AllBrandsModel>
) :
    ArrayAdapter<AllBrandsModel?>(activity, resourceId, mitemList), SectionIndexer {
    private val mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    private val mSectionsHindi = "#अइउएकखगघङचछजझञटठडढणतथदधनपफबभमयरलवशषसहक्षत्रज्ञ"
    private val hindi = Locale("hi", "IN")
    private val lang: String = LocaleHelper.getLanguage(activity)
    private val breaker: BreakIterator = BreakIterator.getCharacterInstance(hindi)


    override fun getPositionForSection(section: Int): Int {
        if (lang == "en") {
            for (i in section downTo 0) {
                for (j in 0..<count) {
                    if (i == 0) {
                        for (k in 0..9) {
                            if (StringMatcher.match(
                                    mitemList[j].subsubcategoryName.substring(0, 1), k.toString()
                                )
                            ) {
                                return j
                            }
                        }
                    } else {
                        if (StringMatcher.match(
                                mitemList[j].subsubcategoryName.substring(0, 1),
                                mSections[i].toString()
                            )
                        ) {
                            return j
                        }
                    }
                }
            }
        } else {
            for (i in section downTo 0) {
                for (j in 0..<count) {
                    if (i == 0) {
                        for (k in 0..9) {
                            if (StringMatcher.match(
                                    mitemList[j].subsubcategoryName.substring(0, 1), k.toString()
                                )
                            ) {
                                return j
                            }
                        }
                    } else {
                        if (StringMatcher.match(
                                mitemList[j].subsubcategoryName.substring(0, 1),
                                mSectionsHindi[i].toString()
                            )
                        ) {
                            return j
                        }
                    }
                }
            }
        }

        return 0
    }

    override fun getSectionForPosition(position: Int): Int {
        return 0
    }

    override fun getSections(): Array<String?> {
        if (lang == "en") {
            val sections = arrayOfNulls<String>(mSections.length)
            for (i in 0..<mSections.length) {
                sections[i] = mSections[i].toString()
            }
            return sections
        } else {
            breaker.setText(mSectionsHindi)
            var start = breaker.first()
            var end = breaker.next()
            val sections = arrayOfNulls<String>(mSectionsHindi.length)
            var i = start
            while (i < end) {
                if (end != BreakIterator.DONE) {
                    sections[i] = mSectionsHindi.substring(start, end)
                    start = end
                    end = breaker.next()
                }
                i++
            }
            return sections
        }
    }

    /*private view holder class*/
    private inner class ViewHolder {
        var ivItemImage: ImageView? = null
        var title: TextView? = null
        var mRowRl: LinearLayout? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        try {
            val holder: ViewHolder
            val rowItemn = getItem(position)
            val mInflater =
                activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.all_brands_item, null)
                holder = ViewHolder()
                holder.title = convertView.findViewById(R.id.title_view)
                holder.mRowRl = convertView.findViewById(R.id.home_frag_row)
                holder.ivItemImage = convertView.findViewById(R.id.item_row_item_logo)
                convertView.setTag(holder)
            } else {
                holder = convertView.tag as ViewHolder
            }
            holder.title!!.text = rowItemn!!.subsubcategoryName
            if (!TextUtils.isNullOrEmpty(
                    rowItemn.logoUrl
                )
            ) {
                Picasso.get().load(rowItemn.logoUrl)
                    .placeholder(R.drawable.logo_grey)
                    .error(R.drawable.logo_grey)
                    .into(holder.ivItemImage)
            } else {
                holder.ivItemImage!!.setImageResource(R.drawable.logo_grey)
            }
            holder.mRowRl!!.setOnClickListener {
                // update analytics
                val analyticPost = AnalyticPost()
                analyticPost.categoryId = rowItemn.categoryid
                analyticPost.subSubCatId = rowItemn.subsubCategoryid
                analyticPost.subSubCatName = rowItemn.subsubcategoryName
                MyApplication.getInstance().updateAnalytics("brand_click")
                val args = Bundle()
                args.putInt("subCatId", rowItemn.subsubCategoryid)
                args.putInt("Categoryid", rowItemn.categoryid)
                activity.pushFragments(newInstance(), false, true, args)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return convertView!!
    }
}