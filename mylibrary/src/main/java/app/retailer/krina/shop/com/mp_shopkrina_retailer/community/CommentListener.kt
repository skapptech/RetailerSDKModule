package app.retailer.krina.shop.com.mp_shopkrina_retailer.community

import android.view.View
import android.widget.TextView

interface CommentListener {
    fun commentLikeClick(postId: String, commentId: String, status: Boolean)
    fun commentReplyClick(
        postId: String,
        commentId: String,
        commentCount: String?,
        userName: String?,
        view: View?
    )

    fun commentEditClick(postId: String, commentId: String, comment: String?, userName: String?)

    fun commentDeleteClick(postId: String, commentId: String, isReply: Boolean)
    fun commentUserProfileOpen(userID: String)
}