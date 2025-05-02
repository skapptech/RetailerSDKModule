package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.media.AudioManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedListener
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.galleryimage.GalleryImageAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.AddPostActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.urlpreview.ViewListener
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemAddPostFromGalleryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemFeedVideoBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemPollViewBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemSingleImageBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DateUtilskotlin
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DateUtilskotlin.Companion.isValidUrl
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DateUtilskotlin.Companion.setResizableText
import com.bumptech.glide.Glide

class FeedAdapter(
    private var context: Context,
    private var feedList: ArrayList<FeedPostModel>,
    private var listener: FeedListener,
    private var pollListener: PollAdapter.PollProgressListener,
    private var imagePostListener: GalleryImageAdapter.PostImageFromGallery,
    private val imagePaths: List<String>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val THE_SINGLE_POST_VIEW = 1
    private val THE_POLL = 2
    private val THE_Add_POST_Gallery = 3
    private val VIDEO_POST = 4



    companion object {
        lateinit var galleryImageAdapter: GalleryImageAdapter
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            THE_SINGLE_POST_VIEW -> SinGlePostView(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.item_single_image, parent, false
                )
            )

            THE_POLL -> POllView(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.item_poll_view, parent, false
                )
            )

            THE_Add_POST_Gallery -> AddPostFromGallery(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_add_post_from_gallery,
                    parent,
                    false
                )
            )

            VIDEO_POST -> VideoViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.item_feed_video, parent, false
                )
            )

            else -> SinGlePostView(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.item_single_image, parent, false
                )
            )
        }
    }


    override fun getItemCount(): Int {
        return feedList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val feedListModel = feedList[position]
        when (holder.itemViewType) {

            THE_SINGLE_POST_VIEW -> {
                if (feedListModel.userName.isNullOrEmpty()) {
                    (holder as SinGlePostView).binding.tvName.text =
                        "Guest User " + feedListModel.userId
                } else {
                    (holder as SinGlePostView).binding.tvName.text = feedListModel.userName
                }

                holder.binding.tvLocation.text = "" + feedListModel.city
                holder.binding.tvLikeCount.text = "" + feedListModel.likeCount
                holder.binding.tvCommentCount.text = "" + feedListModel.commentCount + " कमेंट "
                holder.binding.tvCreateDate.text =
                    DateUtilskotlin.dateConvertToTime(feedListModel.createdDate)

                if (feedListModel.desc!!.isNotEmpty() && feedListModel.desc!!.isValidUrl()) {
                    holder.binding.tvUrlPreview.visibility = View.VISIBLE
                    holder.binding.tvCaption.visibility = View.GONE
                    holder.binding.tvUrlPreview.loadUrl(feedListModel.desc, object : ViewListener {
                        override fun onPreviewSuccess(status: Boolean) {
                        }

                        override fun onFailedToLoad(e: Exception?) {

                        }
                    })

                } else {
                    holder.binding.tvUrlPreview.visibility = View.GONE
                    holder.binding.tvCaption.visibility = View.VISIBLE
                    if (!feedListModel.desc.isNullOrEmpty()) {
                        holder.binding.tvCaption.visibility = View.VISIBLE
                        holder.binding.tvCaption.text = "" + feedListModel.desc
                        holder.binding.tvCaption.setResizableText(
                            holder.binding.tvCaption.text.toString(), 4, true
                        )
                    } else {
                        holder.binding.tvCaption.visibility = View.GONE
                    }
                }

                if (feedListModel.likeCount > 0) {
                    holder.binding.tvLikes.visibility = View.VISIBLE
                } else {
                    holder.binding.tvLikes.visibility = View.INVISIBLE
                }

                if (feedListModel.commentCount > 0) {
                    holder.binding.tvCommentCount.visibility = View.VISIBLE
                } else {
                    holder.binding.tvCommentCount.visibility = View.GONE
                }


                if (feedListModel.img != null && feedListModel.img!!.isNotEmpty()) {
                    Glide.with(context)
                        .load(feedListModel.img)
                        .placeholder(R.drawable.logo_grey)
                        .error(R.drawable.logo_grey)
                        .into(holder.binding.profileImage)
                } else {
                    holder.binding.profileImage.setImageResource(R.drawable.sk_icon_rounded)
                }


                if (feedListModel.imageObj != null && feedListModel.imageObj!!.size > 0) {
                    holder.binding.viewPager.visibility = View.VISIBLE
                    val adapter = ImageAdapter(context, feedListModel.imageObj)
                    holder.binding.viewPager.adapter = adapter
                    val density: Float = context.resources.displayMetrics.density
                   // holder.binding.indicator.radius = 3 * density
                   // holder.binding.indicator.fillColor = -10110011
                    if (feedListModel.imageObj!!.size > 1)
                        holder.binding.indicator.setViewPager(holder.binding.viewPager)
                } else {
                    holder.binding.viewPager.visibility = View.GONE
                }

                if (SharePrefs.getInstance(context).getInt(SharePrefs.CUSTOMER_ID).toString() == feedListModel.userId) {
                    holder.binding.ivUpdatePost.visibility = View.VISIBLE
                    holder.binding.llFollowView.visibility = View.GONE
                } else {
                    holder.binding.ivUpdatePost.visibility = View.GONE
                    holder.binding.llFollowView.visibility = View.VISIBLE
                }
                if (feedListModel.imageObj!!.isEmpty() && feedListModel.imageObj!!.size == 0) {
                    holder.binding.rlImageView.visibility = View.GONE
                } else {
                    holder.binding.rlImageView.visibility = View.VISIBLE
                }


                if (SharePrefs.getInstance(context).getInt(SharePrefs.CUSTOMER_ID)
                        .toString() == feedListModel.userId
                ) {
                    holder.binding.tvFollowView.visibility = View.GONE
                } else {
                    if (feedListModel.isFollowed) {
                        holder.binding.tvFollowView.visibility = View.GONE
                    } else {
                        holder.binding.tvFollowView.visibility = View.VISIBLE
                    }
                }

                holder.binding.tvCommentCount.setOnClickListener {
                    holder.binding.tvCommentCount.isClickable = false
                    if (feedListModel.commentCount > 0) {
                        listener.openComments(
                            feedListModel
                        )
                    }
                }

                holder.binding.llProfileView.setOnClickListener {
                    listener.openProfile(feedListModel.userId)
                }

                holder.binding.llopenProfile.setOnClickListener {
                    listener.openProfile(feedListModel.userId)
                }

                holder.binding.ivUpdatePost.setOnClickListener {
                    listener.editPost(feedListModel)
                }

                if (!feedListModel.likeStatus) {
                    holder.binding.tvLikeLable.setTypeface(holder.binding.tvLikeLable.typeface, Typeface.NORMAL)
                    holder.binding.tvLikeLable.setTextColor(
                        ContextCompat.getColor(
                            context, R.color.textview_color
                        )
                    )
                    holder.binding.ivLike.setImageDrawable(
                        ContextCompat.getDrawable(
                            context, R.drawable.like
                        )
                    )
                } else {
                    if (feedListModel.likeCount > 1) {
                        holder.binding.tvLikeCount.text =
                            "You and " + feedListModel.likeCount.minus(1) + " " + "Others"
                    } else {
                        holder.binding.tvLikeCount.text = "You Liked "
                    }
                    holder.binding.tvLikeLable.setTypeface(
                        holder.binding.tvLikeLable.typeface, Typeface.BOLD
                    )
                    holder.binding.tvLikeLable.setTextColor(
                        ContextCompat.getColor(
                            context, R.color.color_blue_veriant
                        )
                    )
                    holder.binding.ivLike.setImageDrawable(
                        ContextCompat.getDrawable(
                            context, R.drawable.like_count
                        )
                    )
                }

                holder.binding.tvLikes.setOnClickListener {
                    listener.likeList(feedListModel)
                }

                holder.binding.tvFollowView.setOnClickListener {
                    holder.binding.tvFollowView.isClickable=false
                    listener.following(feedListModel.userId)
                }

                holder.binding.ivPostLike.setOnClickListener {
                    if (!feedListModel.likeStatus) {
                        listener.likePost(feedListModel.postId, 1, feedListModel.likeCount + 1, feedListModel.postType)
                        holder.binding.tvLikeLable.setTypeface(
                            holder.binding.tvLikeLable.typeface, Typeface.BOLD
                        )
                    } else {
                        holder.binding.tvLikeLable.setTypeface(
                            holder.binding.tvLikeLable.typeface, Typeface.NORMAL
                        )
                        listener.likePost(feedListModel.postId, 0, feedListModel.likeCount - 1, feedListModel.postType)

                    }
                }
                holder.binding.ivComment.setOnClickListener {
                    holder.binding.ivComment.isClickable = false
                    listener.openComments(feedListModel)
                }
                holder.binding.ivShare.setOnClickListener {
                    listener.sharePost(feedListModel)
                }
            }

            THE_POLL -> {
                if (feedListModel.userName.isNullOrEmpty()) {
                    (holder as POllView).binding.tvName.text = "Guest User " + feedListModel.userId
                } else {
                    (holder as POllView).binding.tvName.text = feedListModel.userName
                }
                holder.binding.tvLikeCount.text = "" + feedListModel.likeCount
                holder.binding.tvCommentCount.text = "" + feedListModel.commentCount + " कमेंट "
                if (feedListModel.desc!!.isNotEmpty()) {
                    holder.binding.tvCaption.visibility = View.VISIBLE
                    holder.binding.tvCaption.text = "" + feedListModel.desc
                    holder.binding.tvCaption.setResizableText(
                        holder.binding.tvCaption.text.toString(), 4, true
                    )
                } else {
                    holder.binding.tvCaption.visibility = View.GONE
                }

                if (feedListModel.commentCount > 0) {
                    holder.binding.tvCommentCount.visibility = View.VISIBLE
                } else {
                    holder.binding.tvCommentCount.visibility = View.GONE
                }
                if (feedListModel.likeCount > 0) {
                    holder.binding.tvLikes.visibility = View.VISIBLE
                } else {
                    holder.binding.tvLikes.visibility = View.INVISIBLE
                }
                holder.binding.tvLocation.text = "" + feedListModel.city
                holder.binding.tvCreateDate.text =
                    DateUtilskotlin.dateConvertToTime(feedListModel.createdDate)

                if (feedListModel.img != null && feedListModel.img!!.isNotEmpty()) {
                    Glide.with(context)
                        .load(feedListModel.img)
                        .placeholder(R.drawable.logo_grey)
                        .error(R.drawable.logo_grey)
                        .into(holder.binding.profileImage)
                } else {
                    holder.binding.profileImage.setImageResource(R.drawable.sk_icon_rounded)
                }
                if (feedListModel.imageObj != null && feedListModel.imageObj!!.size > 0) {
                    Glide.with(context)
                        .load(SharePrefs.getInstance(context).getString(SharePrefs.TRADE_WEB_URL) + feedListModel.imageObj!![0].imgFileFullPath)
                        .placeholder(R.drawable.logo_grey)
                        .into(holder.binding.ivImage)
                } else {
                    holder.binding.ivImage.setImageDrawable(null)
                }

                if (SharePrefs.getInstance(context).getInt(SharePrefs.CUSTOMER_ID)
                        .toString() == feedListModel.userId
                ) {
                    holder.binding.ivUpdatePost.visibility = View.VISIBLE
                    holder.binding.llFollowView.visibility = View.GONE
                } else {
                    holder.binding.ivUpdatePost.visibility = View.GONE
                    holder.binding.llFollowView.visibility = View.VISIBLE
                }

                if (SharePrefs.getInstance(context).getInt(SharePrefs.CUSTOMER_ID)
                        .toString() == feedListModel.userId
                ) {
                    holder.binding.tvFollowView.visibility = View.GONE
                } else {
                    if (feedListModel.isFollowed) {
                        holder.binding.tvFollowView.visibility = View.GONE
                    } else {
                        holder.binding.tvFollowView.visibility = View.VISIBLE
                    }
                }
                val adapter = PollAdapter(
                    context,
                    feedListModel.pollValue,
                    feedListModel.isPollAnswerGiven,
                    pollListener,
                    feedListModel
                )
                holder.binding.rvPoll.adapter = adapter
                holder.binding.rvPoll.isNestedScrollingEnabled = false

                holder.binding.ivComment.setOnClickListener {
                    holder.binding.ivComment.isClickable = false
                    listener.openComments(feedListModel)
                }


                holder.binding.tvCommentCount.setOnClickListener {
                    holder.binding.tvCommentCount.isClickable = false
                    if (feedListModel.commentCount > 0) {
                        listener.openComments(
                            feedListModel
                        )
                    }
                }
                holder.binding.llProfileView.setOnClickListener {
                    listener.openProfile(feedListModel.userId)
                }
                holder.binding.llopenProfile.setOnClickListener {
                    listener.openProfile(feedListModel.userId)
                }
                holder.binding.ivUpdatePost.setOnClickListener {
                    listener.editPost(feedListModel)
                }

                if (!feedListModel.likeStatus) {
                    holder.binding.tvLikeLable.setTypeface(
                        holder.binding.tvLikeLable.typeface, Typeface.NORMAL
                    )
                    holder.binding.tvLikeLable.setTextColor(
                        ContextCompat.getColor(
                            context, R.color.textview_color
                        )
                    )
                    holder.binding.ivLike.setImageDrawable(
                        ContextCompat.getDrawable(
                            context, R.drawable.like
                        )
                    )
                } else {
                    if (feedListModel.likeCount > 1) {
                        holder.binding.tvLikeCount.text =
                            "You and " + feedListModel.likeCount.minus(1) + " " + "Others"
                    } else {
                        holder.binding.tvLikeCount.text = "You Liked"
                    }
                    holder.binding.tvLikeLable.setTypeface(
                        holder.binding.tvLikeLable.typeface, Typeface.BOLD
                    )
                    holder.binding.tvLikeLable.setTextColor(
                        ContextCompat.getColor(
                            context, R.color.color_blue_veriant
                        )
                    )
                    holder.binding.ivLike.setImageDrawable(
                        ContextCompat.getDrawable(
                            context, R.drawable.like_count
                        )
                    )
                }
                holder.binding.ivPostLike.setOnClickListener {
                    if (!feedListModel.likeStatus) {
                        listener.likePost(feedListModel.postId, 1, feedListModel.likeCount + 1, feedListModel.postType)
                        holder.binding.tvLikeLable.setTypeface(
                            holder.binding.tvLikeLable.typeface, Typeface.BOLD
                        )

                    } else {
                        holder.binding.tvLikeLable.setTypeface(
                            holder.binding.tvLikeLable.typeface, Typeface.NORMAL
                        )
                        listener.likePost(feedListModel.postId, 0, feedListModel.likeCount - 1, feedListModel.postType)
                    }
                }
                holder.binding.tvLikes.setOnClickListener {
                    listener.likeList(feedListModel)
                }
                holder.binding.tvFollowView.setOnClickListener {
                    holder.binding.tvFollowView.isClickable=false
                    listener.following(feedListModel.userId)
                }
                holder.binding.ivShare.setOnClickListener {
                    listener.sharePost(feedListModel)
                }
                holder.binding.ivImage.setOnClickListener {
                    context.startActivity(
                        Intent(context, FeedImageShowActivity::class.java).putExtra(
                            "ImagePath",
                            feedListModel.imageObj
                        ).putExtra("pos", 0)
                    )
                }
            }

            VIDEO_POST -> {
                if (feedListModel.userName.isNullOrEmpty()) {
                    (holder as VideoViewHolder).binding.tvName.text =
                        "Guest User " + feedListModel.userId
                } else {
                    (holder as VideoViewHolder).binding.tvName.text = feedListModel.userName
                }
                holder.binding.tvLikeCount.text = "" + feedListModel.likeCount
                holder.binding.tvCommentCount.text = "" + feedListModel.commentCount + " कमेंट "
                holder.binding.tvCreateDate.text =
                    DateUtilskotlin.dateConvertToTime(feedListModel.createdDate)
                holder.binding.tvCaption.text = "" + feedListModel.desc
                if (feedListModel.likeCount > 0) {
                    holder.binding.tvLikes.visibility = View.VISIBLE
                } else {
                    holder.binding.tvLikes.visibility = View.INVISIBLE
                }
                if (feedListModel.commentCount > 0) {
                    holder.binding.tvCommentCount.visibility = View.VISIBLE
                } else {
                    holder.binding.tvCommentCount.visibility = View.GONE
                }


                if (!feedListModel.desc.isNullOrEmpty()) {
                    holder.binding.tvCaption.visibility = View.VISIBLE
                    holder.binding.tvCaption.text = "" + feedListModel.desc
                    holder.binding.tvCaption.setResizableText(
                        holder.binding.tvCaption.text.toString(), 4, true
                    )
                } else {
                    holder.binding.tvCaption.visibility = View.GONE
                }
                holder.binding.tvLocation.text = "" + feedListModel.city

                if (SharePrefs.getInstance(context).getInt(SharePrefs.CUSTOMER_ID)
                        .toString() == feedListModel.userId
                ) {
                    holder.binding.ivUpdatePost.visibility = View.VISIBLE
                    holder.binding.llFollowView.visibility = View.GONE
                } else {
                    holder.binding.ivUpdatePost.visibility = View.GONE
                    holder.binding.llFollowView.visibility = View.VISIBLE
                }

//                Glide.with(context).load(feedListModel.imgFileFullpath).into(holder.binding.imgPost)
                if (feedListModel.imageObj != null && feedListModel.imageObj!!.size > 0) {
                    holder.binding.videoView.setVideoPath(feedListModel.imageObj!![0].imgFileFullPath)
                    holder.binding.videoView.requestFocus()
                    holder.binding.videoView.start()
                    holder.binding.videoView.setOnPreparedListener {
                        holder.binding.imgPost.visibility = View.GONE
                    }
                    holder.binding.videoView.visibility = View.VISIBLE
                } else {
                    holder.binding.imgPost.visibility = View.GONE
                    holder.binding.videoView.visibility = View.GONE
                }

                if (SharePrefs.getInstance(context).getInt(SharePrefs.CUSTOMER_ID)
                        .toString() == feedListModel.userId
                ) {
                    holder.binding.tvFollowView.visibility = View.GONE
                } else {
                    if (feedListModel.isFollowed) {
                        holder.binding.tvFollowView.visibility = View.GONE
                    } else {
                        holder.binding.tvFollowView.visibility = View.VISIBLE
                    }
                }
                if (feedListModel.img != null && feedListModel.img!!.isNotEmpty()) {
                    Glide.with(context)
                        .load(feedListModel.img)
                        .placeholder(R.drawable.logo_grey)
                        .error(R.drawable.logo_grey)
                        .into(holder.binding.profileImage)
                } else {
                    holder.binding.profileImage.setImageResource(R.drawable.sk_icon_rounded)
                }
                holder.binding.ivComment.setOnClickListener {
                    holder.binding.ivComment.isClickable = false
                    listener.openComments(feedListModel)
                }
                holder.binding.tvCommentCount.setOnClickListener {
                    if (feedListModel.commentCount > 0) {
                        listener.openComments(
                            feedListModel
                        )
                    }
                }

                holder.binding.llProfileView.setOnClickListener {
                    listener.openProfile(feedListModel.userId)
                }
                holder.binding.llopenProfile.setOnClickListener {
                    listener.openProfile(feedListModel.userId)
                }

                if (!feedListModel.likeStatus) {
                    holder.binding.tvLikeLable.setTypeface(
                        holder.binding.tvLikeLable.typeface, Typeface.NORMAL
                    )
                    holder.binding.ivLike.setImageDrawable(
                        ContextCompat.getDrawable(
                            context, R.drawable.like
                        )
                    )
                } else {
                    if (feedListModel.likeCount > 1) {
                        holder.binding.tvLikeCount.text =
                            "You and " + feedListModel.likeCount.minus(1) + " " + "Others"
                    } else {
                        holder.binding.tvLikeCount.text = "You "
                    }
                    holder.binding.tvLikeLable.setTypeface(
                        holder.binding.tvLikeLable.typeface, Typeface.NORMAL
                    )
                    holder.binding.ivLike.setImageDrawable(
                        ContextCompat.getDrawable(
                            context, R.drawable.like_count
                        )
                    )
                }

                holder.binding.ivPostLike.setOnClickListener {
                    if (!feedListModel.likeStatus) {
                        listener.likePost(feedListModel.postId, 1, feedListModel.likeCount + 1, feedListModel.postType)
                        holder.binding.tvLikeLable.setTextColor(
                            ContextCompat.getColor(
                                context, R.color.textview_color
                            )
                        )
                        holder.binding.tvLikeLable.setTypeface(
                            holder.binding.tvLikeLable.typeface, Typeface.BOLD
                        )
                        holder.binding.ivLike.setImageDrawable(
                            ContextCompat.getDrawable(
                                context, R.drawable.like_count
                            )
                        )
                    } else {
                        holder.binding.tvLikeLable.setTypeface(
                            holder.binding.tvLikeLable.typeface, Typeface.NORMAL
                        )
                        listener.likePost(feedListModel.postId, 0, feedListModel.likeCount - 1, feedListModel.postType)
                        holder.binding.tvLikeLable.setTextColor(
                            ContextCompat.getColor(
                                context, R.color.color_blue_veriant
                            )
                        )
                        holder.binding.ivLike.setImageDrawable(
                            ContextCompat.getDrawable(
                                context, R.drawable.like
                            )
                        )
                    }
                }

                holder.binding.tvLikes.setOnClickListener {
                    listener.likeList(feedListModel)
                }

                holder.binding.tvFollowView.setOnClickListener {
                    holder.binding.tvFollowView.isClickable=false
                    listener.following(feedListModel.userId)
                }
                holder.binding.ivUpdatePost.setOnClickListener {
                    listener.editPost(feedListModel)
                }

                holder.binding.ivShare.setOnClickListener {
                    listener.sharePost(feedListModel)
                }
                holder.binding.ivMute.setOnClickListener {
                    val audioManager: AudioManager =
                        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                    if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
                        audioManager.adjustStreamVolume(
                            AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0
                        )
                        holder.binding.ivMute.setImageResource(R.drawable.baseline_volume_up_24)
                    } else {
                        audioManager.adjustStreamVolume(
                            AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0
                        )
                        holder.binding.ivMute.setImageResource(R.drawable.baseline_volume_off_24)
                    }
                }
            }

            THE_Add_POST_Gallery -> {
                galleryImageAdapter = GalleryImageAdapter(context, imagePaths, imagePostListener)
                (holder as AddPostFromGallery).binding.rvAddPostImage.adapter = galleryImageAdapter
                holder.binding.AddPost.setOnClickListener {
                    context.startActivity(
                        Intent(context, AddPostActivity::class.java)
                    )
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (feedList[position].postType) {
            "postType" -> THE_SINGLE_POST_VIEW
            "Poll" -> THE_POLL
            "Add_post_images" -> THE_Add_POST_Gallery
            "Video" -> VIDEO_POST
            else -> 1
        }
    }

    private inner class SinGlePostView(var binding: ItemSingleImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    private inner class POllView(var binding: ItemPollViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private inner class VideoViewHolder(var binding: ItemFeedVideoBinding) :
        RecyclerView.ViewHolder(binding.root)

    private inner class AddPostFromGallery(var binding: ItemAddPostFromGalleryBinding) :
        RecyclerView.ViewHolder(binding.root)

}