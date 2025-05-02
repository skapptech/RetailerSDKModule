package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home

import android.annotation.SuppressLint
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ExpandableHeaderBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ExpandableListItemBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.BusinessCardActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ContactUsActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.FaqActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.FavouriteActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.FeedbackActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.GamesListActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.KissanDaanActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyAccountActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyDreamActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyUdharActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.RequestBrandActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings.SettingActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.saleReturn.ReturnOrderActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class HomeMenuChildAdapter(
    private val activity: HomeActivity,
    private val listDataChild: List<String>
) : RecyclerView.Adapter<HomeMenuChildAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ExpandableListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBinding.tvName.text = listDataChild[position]
        if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.myAccount)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.myaccountt)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.txt_My_Udhaar)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.myudharr)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.hishab_kitab_wudu)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.wuduu)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.kisan_dan)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.kisandann)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.title_activity_return_order)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.returnreplacee)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.title_business_card)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.bussinesscard)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.request_brand)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.requestbrand)
//        } else if (listDataChild[position] == SharePrefs.getInstance(activity).getString(SharePrefs.PRIME_NAME)) {
//            holder.mBinding.imgIcon.setImageResource(R.drawable.faydaa)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.feedback)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.feedbackk)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.contact_us)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.contactuss)
        } else if (listDataChild[position] == activity.resources.getString(R.string.help)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.helpsupportt)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.setting)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.settingg)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.call)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.ic_call_rounded)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.Whatsapp)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.whatsapp)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.share)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.sharee)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.title_game)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.gamee)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.txt_My_Favourite)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.myfav)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.txt_My_Dream)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.mydream)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.direct_udhar)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.ic_loan_icon)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.v_atm)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.ic_vatm)
        }
        holder.mBinding.llMain.setOnClickListener { setData(position, holder) }
    }

    override fun getItemCount(): Int {
        return listDataChild.size
    }

    @SuppressLint("WrongConstant")
    fun setData(position: Int, holder: ViewHolder) {
        if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.myAccount)) {
            MyApplication.getInstance().updateAnalytics("my_account_click")
            activity.startActivity(Intent(activity, MyAccountActivity::class.java))
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.txt_My_Udhaar)) {
            MyApplication.getInstance().updateAnalytics("my_udhaar_click")
            activity.startActivity(Intent(activity, MyUdharActivity::class.java))
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.kisan_dan)) {
            MyApplication.getInstance().updateAnalytics("kisaan_daan_click")
            activity.startActivity(Intent(activity, KissanDaanActivity::class.java))
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.title_activity_return_order)) {
            MyApplication.getInstance().updateAnalytics("return_replace_click")
            // activity.startActivity(Intent(activity, ReturnOrderListActivity::class.java))
            activity.startActivity(Intent(activity, ReturnOrderActivity::class.java))
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.title_business_card)) {
            MyApplication.getInstance().updateAnalytics("business_card_click")
            activity.startActivity(Intent(activity, BusinessCardActivity::class.java))
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.request_brand)) {
            MyApplication.getInstance().updateAnalytics("request_brand_click")
            activity.startActivity(Intent(activity, RequestBrandActivity::class.java))
//        } else if (listDataChild[position] == SharePrefs.getInstance(activity).getString(SharePrefs.PRIME_NAME)) {
//            MyApplication.getInstance().updateAnalytics("membership_click")
//            if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER)) {
//                activity.startActivity(Intent(activity, MembershipActivity::class.java))
//            } else {
//                activity.startActivity(Intent(activity, MembershipPlanActivity::class.java))
//            }
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.feedback)) {
            MyApplication.getInstance().updateAnalytics("feedback_click")
            activity.startActivity(Intent(activity, FeedbackActivity::class.java))
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.contact_us)) {
            MyApplication.getInstance().updateAnalytics("contact_us_click")
            activity.startActivity(
                Intent(activity, ContactUsActivity::class.java).putExtra(
                    "Type",
                    "ContactUsActivity"
                )
            )
        } else if (listDataChild[position] == activity.resources.getString(R.string.help)) {
            MyApplication.getInstance().updateAnalytics("return_replace_click")
            activity.startActivity(Intent(activity, FaqActivity::class.java))
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.setting)) {
            MyApplication.getInstance().updateAnalytics("settings_click")
            activity.startActivity(Intent(activity, SettingActivity::class.java))
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.call)) {
            holder.mBinding.imgIcon.setImageResource(R.drawable.ic_call_rounded)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.Whatsapp)) {
            MyApplication.getInstance().updateAnalytics("share_whatsApp_click")
            activity.shareOnWhatsApp(true)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.share)) {
            MyApplication.getInstance().updateAnalytics("share_click")
            activity.shareOnWhatsApp(false)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.title_game)) {
            MyApplication.getInstance().updateAnalytics("game_click")
            activity.startActivity(Intent(activity, GamesListActivity::class.java))
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.txt_My_Favourite)) {
            MyApplication.getInstance().updateAnalytics("myFavourite_click")
            val intent = Intent(activity, FavouriteActivity::class.java)
            activity.startActivity(intent)
            Utils.fadeTransaction(activity)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.txt_My_Dream)) {
            MyApplication.getInstance().updateAnalytics("my_dream_click")
            val intent = Intent(activity, MyDreamActivity::class.java)
            activity.startActivity(intent)
            Utils.fadeTransaction(activity)
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.direct_udhar)) {
            activity.callLeadApi(
                EndPointPref.getInstance(activity).baseUrl +
                        "/api/Udhar/GenerateLead?CustomerId=[CustomerId]"
            )
        } else if (listDataChild[position] == MyApplication.getInstance().dbHelper.getString(R.string.v_atm)) {
            MyApplication.getInstance().updateAnalytics("v_atm_click")
            activity.callVAtmApi()
        }
        activity.mDrawerLayout!!.closeDrawer(Gravity.START)
    }

    inner class ViewHolder internal constructor(var mBinding: ExpandableListItemBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        )
}