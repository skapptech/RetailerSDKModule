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
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemCommentListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DateUtilskotlin
import com.squareup.picasso.Picasso

class CommentAdapter(
    private val activity: Activity,
    private val list: ArrayList<CommentModel>,
    private val listener: CommentListener,
    private val postId: String
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCommentListBinding.inflate(
                LayoutInflater.from(parent.context),
                 parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        if (model.userName != null && !model.userName.isNullOrEmpty()) {
            holder.binding.tvName.text = model.userName
        } else {
            holder.binding.tvName.text = "Guest User " + model.userId
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
        if (model.img != null && !model.img.isNullOrEmpty()) {
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

        model.repliesInComment?.sortBy { it.postOn }
        val adapter = ReplyAdapter(activity, model.repliesInComment, listener, postId)
        holder.binding.rvReplies.adapter = adapter

        holder.binding.tvLike.setOnClickListener {
            if (model.userLikeStatus) {
                list[position].likeCount = list[position].likeCount - 1
                list[position].userLikeStatus = false
                listener.commentLikeClick(postId, model._id, false)
            } else {
                list[position].likeCount = list[position].likeCount + 1
                list[position].userLikeStatus = true
                listener.commentLikeClick(postId, model._id, true)
            }
        }

        holder.binding.tvReply.setOnClickListener { view ->
            listener.commentReplyClick(postId, model._id, "", model.userName, view)
        }

        holder.binding.ivMore.setOnClickListener {
            val popup = PopupMenu(activity, holder.binding.ivMore)
            popup.menuInflater.inflate(R.menu.popup_menu_comment, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.edit -> {
                        listener.commentEditClick(
                            postId,
                            model._id,
                            model.comments,
                            model.userName
                        )
                    }

                    R.id.delete -> {
                        try {
                            popup.dismiss()
                            list.removeAt(holder.adapterPosition)
                            listener.commentDeleteClick(postId, model._id, false)
                        }catch (ex:Exception){
                        }

                    }
                }
                true
            }
            popup.show()
        }
        holder.binding.profileImage.setOnClickListener {
            listener.commentUserProfileOpen(model.userId!!)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(var binding: ItemCommentListBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}