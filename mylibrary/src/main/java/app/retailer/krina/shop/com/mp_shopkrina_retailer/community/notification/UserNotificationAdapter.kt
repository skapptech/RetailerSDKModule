package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.notification

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemNotificationListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DateUtilskotlin
import com.squareup.picasso.Picasso

class UserNotificationAdapter(
    private val activity: Activity,
    private val list: ArrayList<UserNotificationModel>,
    private val listenr: UserNotificationListener
) : RecyclerView.Adapter<UserNotificationAdapter.ViewHolder>() {

    interface UserNotificationListener {
        fun readUserNotification(
            userID: String,
            notificationType: Int,
            userId: String,
            postedByUserId: String
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNotificationListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvName.text = model.Heading
        holder.binding.tvsubhading.text = model.Subheading
        holder.binding.tvDateTime.text = DateUtilskotlin.notifictiondateConvertToTime(model.createdAt)
        if (model.img!=null&&model.img!!.isNotEmpty()) {
            Picasso.get().load(model.img)
                .into(holder.binding.profileImage)

        } else {
            holder.binding.profileImage.setImageResource(R.drawable.user_6)
        }

        holder.binding.llnotifiction.setOnClickListener {
            if (model.NotificationType == 1) {//follow
                listenr.readUserNotification(
                    model.NotificationId,
                    model.NotificationType,
                    model.UserId,
                    ""
                )
            } else if (model.NotificationType == 2) {//New Post
                listenr.readUserNotification(
                    model.NotificationId,
                    model.NotificationType,
                    model.NewPost.PostId,
                    model.NewPost.PostedByUserId
                )
            } else if (model.NotificationType == 3) {//Comment
                listenr.readUserNotification(
                    model.NotificationId,
                    model.NotificationType,
                    model.UserId,
                    model.Comment.PostId,
                )
            } else if (model.NotificationType == 4) {//like
                listenr.readUserNotification(
                    model.NotificationId,
                    model.NotificationType,
                    model.Like.PostId,
                    ""
                )
            }


        }
        if (model.IsRead) {
            holder.binding.llnotifiction.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.background_read_notifiction));
            holder.binding.viewRedNotRead.visibility = View.GONE
        } else {
            holder.binding.viewRedNotRead.visibility = View.VISIBLE
            holder.binding.llnotifiction.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.background_notifiction));
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(var binding: ItemNotificationListBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}