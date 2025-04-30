package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.notification

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.notification.NotificationModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.NotificationListRowBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.FullNotificationActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.GamesListActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyWalletActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.VideoNotificationActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.mygullak.MyGullakActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order.MyOrderActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings.ChangeLanguageActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide

class NotificationAdapter(
    private val activity: NotificationActivity,
    private var notificationListArrayList: List<NotificationModel>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


    fun setNotificationItem(notificationListArrayList: List<NotificationModel>) {
        this.notificationListArrayList = notificationListArrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.notification_list_row, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val notificationModel = notificationListArrayList[i]
        holder.mBinding.notificationTitle.text = notificationModel.title
        holder.mBinding.notificationMessage.text = notificationModel.message
        if (notificationModel.notificationTime != null) {
            holder.mBinding.tvDateTime.text =
                Utils.getDateTimeFormate(notificationModel.notificationTime)
        } else {
            holder.mBinding.tvDateTime.text = ""
        }
        if (!TextUtils.isNullOrEmpty(notificationModel.notificationType) && notificationModel.notificationType == "Actionable") {
            holder.mBinding.btnAdd.visibility = View.VISIBLE
            if (notificationModel.notificationCategory.equals("Category", ignoreCase = true)) {
                holder.mBinding.btnAdd.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.add_to_cart_notification)
            } else if (notificationModel.notificationCategory.equals(
                    "SubCategory",
                    ignoreCase = true
                )
            ) {
                holder.mBinding.btnAdd.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.add_to_cart_notification)
            } else if (notificationModel.notificationCategory.equals(
                    "SubSubCategory",
                    ignoreCase = true
                )
            ) {
                holder.mBinding.btnAdd.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.add_to_cart_notification)
            } else if (notificationModel.notificationCategory.equals("Brand", ignoreCase = true)) {
                holder.mBinding.btnAdd.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.add_to_cart_notification)
            } else if (notificationModel.notificationCategory.equals("Item", ignoreCase = true)) {
                holder.mBinding.btnAdd.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.add_to_cart_notification)
            } else {
                holder.mBinding.btnAdd.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.text_click_here)
            }
        } else {
            holder.mBinding.btnAdd.visibility = View.GONE
            holder.mBinding.btnAdd.text =
                MyApplication.getInstance().dbHelper.getString(R.string.text_click_here)
        }
        var imageurl: String
        if (!TextUtils.isNullOrEmpty(notificationModel.notifyType)) {
            if (notificationModel.notifyType.equals("video", ignoreCase = true)) {
                imageurl = notificationModel.imageUrl!!
                if (!TextUtils.isNullOrEmpty(imageurl)) {
                    imageurl = imageurl.replace(" ".toRegex(), "%20")
                    Glide.with(activity).load(imageurl).placeholder(R.drawable.logo_grey)
                        .into(holder.mBinding.notificationImg1)
                } else {
                    holder.mBinding.notificationImg1.visibility = View.GONE
                }
                holder.mBinding.rlViewView.visibility = View.VISIBLE
                holder.mBinding.notificationImg.visibility = View.GONE
                holder.mBinding.notificationImgPlay.visibility = View.GONE
            } else if (notificationModel.notifyType.equals("murli", ignoreCase = true)) {
                holder.mBinding.notificationImgPlay.visibility = View.VISIBLE
                holder.mBinding.notificationImg.visibility = View.GONE
            } else {
                holder.mBinding.rlViewView.visibility = View.GONE
                holder.mBinding.notificationImg.visibility = View.VISIBLE
                holder.mBinding.notificationImgPlay.visibility = View.GONE
                if (!TextUtils.isNullOrEmpty(notificationModel.imageUrl)) {
                    imageurl = notificationModel.imageUrl!!
                    imageurl = imageurl.replace(" ".toRegex(), "%20")
                    if (!TextUtils.isNullOrEmpty(imageurl)) {
                        Glide.with(activity).load(imageurl)
                            .placeholder(R.drawable.logo_grey)
                            .error(R.drawable.logo_grey)
                            .into(holder.mBinding.notificationImg)
                    } else {
                        holder.mBinding.notificationImg.visibility = View.GONE
                    }
                } else {
                    holder.mBinding.notificationImg.visibility = View.GONE
                }
            }
        } else {
            holder.mBinding.rlViewView.visibility = View.GONE
            holder.mBinding.notificationImg.visibility = View.VISIBLE
            holder.mBinding.notificationImgPlay.visibility = View.GONE
            if (!TextUtils.isNullOrEmpty(notificationModel.imageUrl)) {
                imageurl = notificationModel.imageUrl!!
                imageurl = imageurl.replace(" ".toRegex(), "%20")
                if (!TextUtils.isNullOrEmpty(imageurl)) {
                    Glide.with(activity).load(imageurl)
                        .placeholder(R.drawable.logo_grey)
                        .error(R.drawable.logo_grey)
                        .into(holder.mBinding.notificationImg)
                } else {
                    holder.mBinding.notificationImg.visibility = View.GONE
                }
            } else {
                holder.mBinding.notificationImg.visibility = View.GONE
            }
        }
        holder.mBinding.rlVideoPicFirstImg.setOnClickListener {
            activity.startActivity(
                Intent(activity, VideoNotificationActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("url", notificationModel.imageUrl)
            )
            Utils.fadeTransaction(activity)
        }
        holder.mBinding.llLayout.setOnClickListener { holder.mBinding.btnAdd.callOnClick() }
        holder.mBinding.btnAdd.setOnClickListener {
            if (notificationModel.notificationCategory != null) {
                if (notificationModel.notificationCategory.equals(
                        "Flash Deal",
                        ignoreCase = true
                    ) || notificationModel.notificationCategory.equals("offer", ignoreCase = true)
                ) {
                    val intent = Intent(activity, HomeActivity::class.java)
                    intent.putExtra("notificationCategory", notificationModel.notificationCategory)
                    activity.startActivity(intent)
                } else if (notificationModel.notificationCategory.equals(
                        "murli",
                        ignoreCase = true
                    )
                ) {
                    activity.startActivity(
                        Intent(activity, HomeActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(
                                "audioFileName",
                                notificationModel.imageUrl
                            ).putExtra("notificationCategory", "")
                    )
                } else if (notificationModel.notificationCategory.equals(
                        "fullScreen",
                        ignoreCase = true
                    )
                ) {
                    activity.startActivity(
                        Intent(activity, FullNotificationActivity::class.java)
                            .putExtra("image", notificationModel.imageUrl)
                    )
                } else if (notificationModel.notificationCategory.equals(
                        "wallet",
                        ignoreCase = true
                    )
                ) {
                    activity.startActivity(Intent(activity, MyWalletActivity::class.java))
                } else if (notificationModel.notificationCategory.equals(
                        "gullak",
                        ignoreCase = true
                    )
                ) {
                    activity.startActivity(Intent(activity, MyGullakActivity::class.java))
                } else if (notificationModel.notificationCategory.equals(
                        "order",
                        ignoreCase = true
                    )
                ) {
                    activity.startActivity(Intent(activity, MyOrderActivity::class.java))
                } else if (notificationModel.notificationCategory.equals(
                        "cart",
                        ignoreCase = true
                    ) || notificationModel.notificationCategory.equals(
                        "shoppingCart",
                        ignoreCase = true
                    )
                ) {
                    activity.startActivity(Intent(activity, ShoppingCartActivity::class.java))
                } else if (notificationModel.notificationCategory.equals(
                        "game",
                        ignoreCase = true
                    )
                ) {
                    activity.startActivity(Intent(activity, GamesListActivity::class.java))
                } else if (notificationModel.notificationCategory.equals(
                        "direct",
                        ignoreCase = true
                    )
                ) {
                    if (EndPointPref.getInstance(activity)
                            .getBoolean(EndPointPref.showNewSocial)
                    )
                        activity.startActivity(
                            Intent(
                                activity,
                                FeedActivity::class.java
                            )
                        ) else activity.startActivity(
                        Intent(
                            activity,
                            TradeActivity::class.java
                        )
                    )
                } else if (notificationModel.notificationCategory.equals(
                        "updateLanguage",
                        ignoreCase = true
                    )
                ) {
                    activity.startActivity(Intent(activity, ChangeLanguageActivity::class.java))
                } else if (notificationModel.notificationCategory.equals(
                        "target",
                        ignoreCase = true
                    )
                ) {
                    activity.startActivity(
                        Intent(
                            activity,
                            CustomerSubCategoryTargetActivity::class.java
                        )
                    )
                } else if (notificationModel.notificationCategory.equals(
                        "extra",
                        ignoreCase = true
                    )
                ) {
                    activity.startActivity(
                        Intent(
                            activity,
                            CustomerSubCategoryTargetActivity::class.java
                        )
                    )
                } else {
                    val intent = Intent(activity, ActionBleNotificationActivity::class.java)
                    intent.putExtra("notificationCategory", notificationModel.notificationCategory)
                    intent.putExtra("typeId", notificationModel.objectId)
                    intent.putExtra("Notification", false)
                    activity.startActivity(intent)
                }
                Utils.fadeTransaction(activity)
            } else {
                if (notificationModel.notifyType != null) {
                    if (notificationModel.notifyType.equals("murli", ignoreCase = true)) {
                        activity.startActivity(
                            Intent(activity, HomeActivity::class.java)
                                .putExtra(
                                    "audioFileName",
                                    notificationModel.imageUrl
                                ).putExtra("notificationCategory", "")
                        )
                    } else if (notificationModel.notifyType.equals(
                            "fullScreen",
                            ignoreCase = true
                        )
                    ) {
                        activity.startActivity(
                            Intent(activity, FullNotificationActivity::class.java)
                                .putExtra("image", notificationModel.imageUrl)
                        )
                    }
                    Utils.fadeTransaction(
                        activity
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return notificationListArrayList.size
    }

    inner class ViewHolder(var mBinding: NotificationListRowBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}