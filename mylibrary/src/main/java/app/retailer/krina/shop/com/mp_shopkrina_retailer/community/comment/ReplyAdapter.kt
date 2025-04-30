package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.CommentListener
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemCommentReplyListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DateUtilskotlin
import com.squareup.picasso.Picasso

class ReplyAdapter(
    private val activity: Activity,
    private val list: ArrayList<CommentModel>?,
    private val listener: CommentListener,
    private val postId: String
) : RecyclerView.Adapter<ReplyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_comment_reply_list, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list!![position]
        if (model.userName.isNullOrEmpty()) {
            holder.binding.tvName.text = "Guest User " + model.userId
        } else {
            holder.binding.tvName.text = model.userName
        }

        holder.binding.tvComment.text = model.comments
        holder.binding.tvLikeCount.text = "" + model.likeCount
        holder.binding.tvDate.text = DateUtilskotlin.commentConvertToTime(model.postOn)

        if (model.userId == SharePrefs.getInstance(activity)
                .getInt(SharePrefs.CUSTOMER_ID)
                .toString()
        ) {
            holder.binding.ivMore.visibility = View.VISIBLE
        } else {
            holder.binding.ivMore.visibility = View.INVISIBLE
        }
        if (!model.img.isNullOrEmpty()) {
            Picasso.get().load(model.img)
                .into(holder.binding.profileImage)
        } else {
            holder.binding.profileImage.setImageResource(R.drawable.sk_icon_rounded)
        }

        if (model.likeCount == 0) {
            holder.binding.ivPostLike.visibility = View.INVISIBLE
            holder.binding.tvLikeCount.visibility = View.INVISIBLE
        } else {
            holder.binding.ivPostLike.visibility = View.VISIBLE
            holder.binding.tvLikeCount.visibility = View.VISIBLE
        }
        if (model.userLikeStatus) {
            holder.binding.tvLike.setTextColor(activity.resources.getColor(R.color.like_color))
        } else {
            holder.binding.tvLike.setTextColor(activity.resources.getColor(R.color.textview_color))
        }

        holder.binding.tvLike.setOnClickListener {
            if (model.userLikeStatus) {
                holder.binding.tvLike.visibility=View.INVISIBLE
                listener.commentLikeClick(postId, model._id, false)
                list[position].likeCount = list[position].likeCount - 1
                list[position].userLikeStatus = false
            } else {
                holder.binding.tvLike.visibility=View.INVISIBLE
                listener.commentLikeClick(postId, model._id, true)
                list[position].likeCount = list[position].likeCount + 1
                list[position].userLikeStatus = true
            }
        }

        holder.binding.tvReply.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                holder.binding.tvReply.setTextColor(activity.resources.getColor(R.color.red_light))
                listener.commentReplyClick(postId, model._id, "", model.userName, view)
            }

        })
        holder.binding.ivMore.setOnClickListener {
            val popup = PopupMenu(activity, holder.binding.ivMore)
            popup.menuInflater.inflate(R.menu.popup_menu_comment, popup.menu)
            popup.menu.findItem(R.id.edit).isVisible = false
            popup.setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.edit -> {
                        listener.commentEditClick(postId, model._id, model.comments, "")
                    }

                    R.id.delete -> {
                        try {
                            popup.dismiss()
                            list.removeAt(holder.adapterPosition)
                            listener.commentDeleteClick(postId, model._id, true)
                        }catch (ex:Exception){
                        }

                    }
                }
                true
            }
            popup.show()
        }
        holder.binding.profileImage.setOnClickListener {
            listener.commentUserProfileOpen(model.userName!!)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(var binding: ItemCommentReplyListBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}