package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.HomeMenuHeaderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ExpandableHeaderBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ContactUsActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.FaqActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.LegerPaymentActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order.MyOrderActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ReferralActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings.SettingActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.agent.MyAgentActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ViewAnimationUtils

class HomeMenuAdapter(
    private val activity: HomeActivity,
    private val list: List<HomeMenuHeaderModel>
) : RecyclerView.Adapter<HomeMenuAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ExpandableHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBinding.lblListHeaderText.text = list[position].title
        if (list[position].list != null) {
            holder.mBinding.rvChild.adapter = HomeMenuChildAdapter(activity, list[position].list!!)
            holder.mBinding.expandableIcon.visibility = View.VISIBLE
        } else {
            holder.mBinding.expandableIcon.visibility = View.GONE
        }
        if (list[position].title.equals(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.scale_up),
                ignoreCase = true
            )
        ) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_direct_udhar)
        } else if (list[position].title.equals(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.direct_udhar),
                ignoreCase = true
            )
        ) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_direct_udhar)
        } else if (list[position].title.equals(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.myOrder),
                ignoreCase = true
            )
        ) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.shopping_bag)
        } else if (list[position].title.equals(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.my_target),
                ignoreCase = true
            )
        ) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.goal)
        } else if (list[position].title.equals(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.my_ledger),
                ignoreCase = true
            )
        ) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ledger)
        } else if (list[position].title.equals(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.more_tool),
                ignoreCase = true
            )
        ) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_new_setting)
        } else if (list[position].title.equals(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.contact),
                ignoreCase = true
            )
        ) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_contact_us)
        } else if (list[position].title.equals(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.my_agents),
                ignoreCase = true
            )
        ) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.person_icon)
        } else if (list[position].title.equals(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.help),
                ignoreCase = true
            )
        ) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.help)
        } else if (list[position].title.equals(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.refer_and_earn),
                ignoreCase = true
            )
        ) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_share_button)
        } else if (list[position].title.equals(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.setting),
                ignoreCase = true
            )
        ) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_new_setting)
        }
        if (list[position].isOpen) {
            holder.mBinding.expandableIcon.rotation = 270f
            ViewAnimationUtils.expand(holder.mBinding.rvChild, null, false)
            holder.mBinding.rvChild.visibility = View.VISIBLE
        } else {
            holder.mBinding.expandableIcon.rotation = 90f
            holder.mBinding.rvChild.visibility = View.GONE
        }
        holder.mBinding.lblListHeader.setOnClickListener {
            val intent: Intent
            if (list[position].title.equals(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.direct_udhar),
                    ignoreCase = true
                )
            ) {
                RetailerSDKApp.getInstance().updateAnalytics("direct_udhaar_click")
                activity.mDrawerLayout!!.closeDrawer(Gravity.START)
                activity.callLeadApi(
                    EndPointPref.getInstance(activity).baseUrl +
                            "/api/Udhar/GenerateLead?CustomerId=[CustomerId]"
                )
            } else if (list[position].title.equals(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.scale_up),
                    ignoreCase = true
                )
            ) {
                RetailerSDKApp.getInstance().updateAnalytics("scaleUp_click")
                activity.callScaleUpApi()
                Utils.fadeTransaction(activity)
            } else if (list[position].title.equals(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.myOrder),
                    ignoreCase = true
                )
            ) {
                RetailerSDKApp.getInstance().updateAnalytics("my_order_click")
                intent = Intent(activity, MyOrderActivity::class.java)
                activity.startActivity(intent)
                Utils.fadeTransaction(activity)
            } else if (list[position].title.equals(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.my_target),
                    ignoreCase = true
                )
            ) {
                RetailerSDKApp.getInstance().updateAnalytics("target_click")
                intent = Intent(activity, CustomerSubCategoryTargetActivity::class.java)
                activity.startActivity(intent)
                Utils.fadeTransaction(activity)
            } else if (list[position].title.equals(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.my_ledger),
                    ignoreCase = true
                )
            ) {
                RetailerSDKApp.getInstance().updateAnalytics("ledger_payment_click")
                intent = Intent(activity, LegerPaymentActivity::class.java)
                activity.startActivity(intent)
                Utils.fadeTransaction(activity)
            } else if (list[position].title.equals(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.my_agents),
                    ignoreCase = true
                )
            ) {
                RetailerSDKApp.getInstance().updateAnalytics("my_agent_click")
                activity.startActivity(Intent(activity, MyAgentActivity::class.java))
                Utils.fadeTransaction(activity)
            } else if (list[position].title.equals(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.more_tool),
                    ignoreCase = true
                )
            ) {
                list[position].isOpen = !list[position].isOpen
                notifyDataSetChanged()
            } else if (list[position].title.equals(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.contact),
                    ignoreCase = true
                )
            ) {
                RetailerSDKApp.getInstance().updateAnalytics("contact_us_click")
                activity.startActivity(
                    Intent(activity, ContactUsActivity::class.java)
                        .putExtra("Type", "ContactUsActivity")
                )
                Utils.fadeTransaction(activity)
            } else if (list[position].title.equals(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.refer_and_earn),
                    ignoreCase = true
                )
            ) {
                RetailerSDKApp.getInstance().updateAnalytics("refer_earn_click")
                activity.startActivity(Intent(activity, ReferralActivity::class.java))
                Utils.fadeTransaction(activity)
            } else if (list[position].title.equals(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.setting),
                    ignoreCase = true
                )
            ) {
                RetailerSDKApp.getInstance().updateAnalytics("settings_click")
                activity.startActivity(Intent(activity, SettingActivity::class.java))
                Utils.fadeTransaction(activity)
            } else if (list[position].title.equals(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.help),
                    ignoreCase = true
                )
            ) {
                RetailerSDKApp.getInstance().updateAnalytics("faq_click")
                activity.startActivity(Intent(activity, FaqActivity::class.java))
                Utils.fadeTransaction(activity)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder internal constructor(var mBinding: ExpandableHeaderBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        )
}