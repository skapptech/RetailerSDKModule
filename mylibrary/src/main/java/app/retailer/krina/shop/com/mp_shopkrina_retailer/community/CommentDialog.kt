package app.retailer.krina.shop.com.mp_shopkrina_retailer.community

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment.CommentAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment.CommentFactory
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment.CommentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment.CommentRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment.CommentViewModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PostLikeModelRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.CommentPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.PostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile.ProfileActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.RestClient
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ProgressDialog
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Date

class CommentDialog : BottomSheetDialogFragment(), CommentListener {
    private lateinit var activity: AppCompatActivity

    private lateinit var commentViewModel: CommentViewModel

    private var list: ArrayList<CommentModel> = ArrayList()
    private lateinit var model: FeedPostModel
    private val analyticPost = AnalyticPost()

    private lateinit var etComment: EditText
    private var onButtonClick: OnButtonClick? = null

    private var isReply = false
    private var isEdit = false
    private var commentId = ""
    private var position = 0
    private var commentCount = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
        onButtonClick = context as OnButtonClick
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            position = requireArguments().getInt("position")
            model = requireArguments().getSerializable("list") as FeedPostModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_feed_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)

        commentCount = model.commentCount

        val repository = CommentRepository(RestClient.getInstance4().service4)
        commentViewModel =
            ViewModelProvider(
                this,
                CommentFactory(repository)
            )[CommentViewModel::class.java]

        val ivLike = view.findViewById<ImageView>(R.id.ivLike)
        val tvLikeCount = view.findViewById<TextView>(R.id.tvLikeCount)
        val tvComments = view.findViewById<TextView>(R.id.tvComments)
        // val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val rvComment = view.findViewById<RecyclerView>(R.id.rvComment)
        val progressCom = view.findViewById<ProgressBar>(R.id.progressCom)
        etComment = view.findViewById(R.id.etComment)!!
        val btnSend = view.findViewById<ImageView>(R.id.btnSend)
       // val replyingToUserName = view.findViewById<TextView>(R.id.tvReplying)!!
        val replyingCancel = view.findViewById<TextView>(R.id.tvReplyingCancel)!!

        tvLikeCount?.text = "" + model.likeCount
        tvComments?.text = "$commentCount कमेंट"

        if (model.likeStatus) {
            ivLike?.setImageResource(R.drawable.like_total_count)
        } else {
            ivLike?.setImageResource(R.drawable.like)
        }
//        val list = ArrayList<CommentModel>()
        val adapter = CommentAdapter(activity, list, this, model.postId)
        rvComment?.adapter = adapter
//        val count = list.flatMap { it.comments!! }.groupingBy { it }.eachCount()
//            .filterValues { it >= 1 }.keys
//        println("asdfgh1 $count.")

        commentViewModel.commentListResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    progressCom.visibility = View.VISIBLE
                }

                is NetworkResult.Failure -> {
                    progressCom.visibility = View.GONE
                    Toast.makeText(activity, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    progressCom.visibility = View.GONE
                    list.clear()
                    list.addAll(it.data.list)
                    list.sortBy { it.postOn }
                    adapter.notifyDataSetChanged()
                    rvComment?.scrollToPosition(list.size - 1)
                    tvComments?.text = "${it.data.commentCount} कमेंट"
                }
            }
        }
        commentViewModel.postCommentResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(activity)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(activity, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    if (it.data != null) {
                        commentViewModel.getCommentList(
                            model.postId,
                            SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
                        )
                        rvComment?.scrollToPosition(list.size - 1)
                    }
                }
            }
        }
        commentViewModel.likeCommentResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(activity)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Utils.setToast(activity, it.errorMessage)
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    if (it.data)
                        adapter.notifyDataSetChanged()
                }
            }
        }
        commentViewModel.postReplyCommentResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(activity)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(activity, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    etComment.hint = "कमेंट लिखें.. "
                    commentViewModel._commentListResponse.postValue(null)
                    commentViewModel.getCommentList(
                        model.postId,
                        SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
                    )
                    rvComment?.scrollToPosition(list.size - 1)
                }
            }
        }
        commentViewModel.editCommentResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(activity)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(activity, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    if (it.data.get("status").asBoolean) {
                        commentViewModel.getCommentList(
                            model.postId,
                            SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
                        )
                        rvComment?.scrollToPosition(list.size - 1)
                    } else {
                        Utils.setToast(context, it.data.get("res").asString)
                    }

                }
            }
        }
        commentViewModel.deleteCommentResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(activity)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(activity, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    adapter.notifyDataSetChanged()
                    commentViewModel.getCommentList(
                        model.postId,
                        SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
                    )
                }
            }
        }
        commentViewModel.deleteCommentReplyResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(activity)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(activity, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    adapter.notifyDataSetChanged()
                    commentViewModel.getCommentList(
                        model.postId,
                        SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
                    )
                }
            }
        }

        commentViewModel.getCommentList(
            model.postId, SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        )

        replyingCancel.setOnClickListener { v ->
            isReply = false
            Utils.hideKeyboard(activity, v)
            etComment.setText("")
        }
        btnSend?.setOnClickListener { view1 ->
            Utils.hideKeyboard(activity, view1)
            val comment = etComment.text.toString().trim()
            if (comment.isNotEmpty()) {
                val commentModel = CommentPostModel()
                commentModel.userId =
                    "" + SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
                commentModel.userName =
                    SharePrefs.getInstance(activity).getString(SharePrefs.USER_NAME)
                commentModel.comments = comment
                commentModel.postOn =
                    "" + SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Date())
                commentModel.PostId = model.postId
                if (isReply) {
                    isReply = false
                    commentModel.PostId = commentId
                    commentViewModel.postCommentReply(
                        model.postId, commentId, commentCount, commentModel
                    )
                } else if (isEdit) {
                    isEdit = false
                    val postModel = PostModel()
                    postModel.userId =
                        SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
                    postModel.userName =
                        SharePrefs.getInstance(activity).getString(SharePrefs.USER_NAME)
                    postModel.desc = comment
                    postModel.title = ""
                    postModel.imageObj = null
                    postModel.pollValue = null
                    postModel.postId = commentId

                    commentViewModel.editComment(model.postId, commentId, postModel)
                    analyticPost.eventName = "editComment"

                } else {
                    commentViewModel.postComment(commentCount, commentModel)
                    analyticPost.eventName = "addComment"
                }

                //analysis
                analyticPost.postId = model.postId
                analyticPost.postType = model.postType
                analyticPost.source = "Profile"
                analyticPost.likeCount = model.likeCount
                analyticPost.commentCount = model.commentCount
                RetailerSDKApp.getInstance().updateAnalytics(analyticPost)


                etComment.setText("")
                dialog!!.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            } else {
                Utils.setToast(activity, "कमेंट करें")
            }
        }
        dialog?.setOnShowListener { dialog ->
            (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
        dialog?.setOnDismissListener {
            RetailerSDKApp.getInstance().isCommentOpen = false
            dialog?.dismiss()
            // notify changes
            onButtonClick?.onButtonClick(position, true)
            // analytic
            analyticPost.eventName = "CommentPopUpExit"
            analyticPost.likeCount = model.likeCount
            analyticPost.commentCount = model.commentCount
            RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
        }

        // analytic
        analyticPost.eventName = "CommentPopUp"
        analyticPost.source = "Feed"
        analyticPost.postId = model.postId
        analyticPost.postType = model.postType
        analyticPost.likeCount = model.likeCount
        analyticPost.commentCount = model.commentCount
        RetailerSDKApp.getInstance().mixpanel.timeEvent("CommentPopUpExit")
        RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
    }

    override fun getDialog(): Dialog? {
        return super.getDialog()
    }

    override fun onResume() {
        super.onResume()
        if (!RetailerSDKApp.getInstance().isCommentOpen) {
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        dialog?.cancel()
    }

    override fun commentLikeClick(
        postId: String, commentId: String, status: Boolean
    ) {
        val model = PostLikeModelRequest(
            SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID).toString(),
            commentId,
            if (status) 1 else 0
        )

        commentViewModel.postCommentLike(postId, commentId, model)

        //analysis
        if (status) {
            analyticPost.eventName = "commentLike"
        } else {
            analyticPost.eventName = "commentUnLike"
        }
        analyticPost.postId = postId
        analyticPost.source = "Profile"
        RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
    }

    override fun commentReplyClick(
        postId: String, commentId: String, commentCount: String?, userName: String?, view: View?
    ) {
        isReply = true
        this.commentId = commentId
        Utils.showKeyboard(activity)
        etComment.requestFocus()
        etComment.hint = "रिप्लाई करें... "
        //etComment.setSelection(etComment.text.length)
    }

    override fun commentEditClick(
        postId: String, commentId: String, comment: String?, userName: String?
    ) {
        isEdit = true
        this.commentId = commentId
        Utils.showKeyboard(activity)
        etComment.requestFocus()
        etComment.setText("$comment ")
        etComment.setSelection(etComment.text.length)
    }

    override fun commentDeleteClick(postId: String, commentId: String, isReply: Boolean) {
        if (isReply) {
            commentViewModel.deleteReplyInComment(postId, commentId)
            analyticPost.eventName = "deleteComment"
        } else {
            commentViewModel.deleteComment(postId, commentId)
            analyticPost.eventName = "deleteReplyComment"
        }
        analyticPost.postId = postId
        analyticPost.source = "Profile"
        RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
    }

    override fun commentUserProfileOpen(userID: String) {
        dialog?.dismiss()
        startActivity(
            Intent(activity, ProfileActivity::class.java).putExtra("comeFrom", "Profile")
                .putExtra("userId", userID).putExtra("source", "comments")
        )
    }

    companion object {
        fun newInstance(model: FeedPostModel): CommentDialog {
            val fragment = CommentDialog()
            val args = Bundle()
            args.putSerializable("list", model)
//            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }
}