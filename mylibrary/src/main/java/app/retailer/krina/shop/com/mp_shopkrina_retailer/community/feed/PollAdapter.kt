package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemsPollProgressBinding


class PollAdapter(
    var context: Context,
    private var pollValue: ArrayList<PollModel>?,
    private var isPollAnswerGiven: Boolean,
    var listener: PollProgressListener,
    private var feedListModel: FeedPostModel
) : RecyclerView.Adapter<PollAdapter.ViewHolder?>() {
    private var isPollSubmit:Boolean =true


    interface PollProgressListener {
        fun progressCheck(postId: String, _id: String, position: Int, isPollSubmit: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.items_poll_progress, parent, false
            )
        )
    }

    class ViewHolder(var mBinding: ItemsPollProgressBinding) :
        RecyclerView.ViewHolder(mBinding.root) {}

    override fun getItemCount(): Int {
        return pollValue?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pollProgressModel = pollValue!![position]
        if (isPollAnswerGiven) {
            holder.mBinding.llShowLable.visibility = View.GONE
            holder.mBinding.llprogress.visibility = View.VISIBLE
            holder.mBinding.tvPollProgress.text = "" + pollProgressModel.answerPercentage + " %"
            holder.mBinding.tvProgress.text = pollProgressModel.optionValue
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.mBinding.progress.setProgress(pollProgressModel.answerPercentage.toInt(), true)
            } else {
                setProgressAnimate(holder.mBinding.progress,
                    pollProgressModel.answerPercentage.toInt()
                )
            }
            if (pollProgressModel.isAnswered) {
                holder.mBinding.ivCheck.visibility = View.VISIBLE
            }

        } else {
            holder.mBinding.llprogress.visibility = View.GONE
            holder.mBinding.llShowLable.visibility = View.VISIBLE
            holder.mBinding.tvpollText.text = pollProgressModel.optionValue
        }

        holder.mBinding.llShowLable.setOnClickListener {
            isPollAnswerGiven = true
            if (isPollSubmit){
                isPollSubmit = false
                listener.progressCheck(feedListModel.postId, pollProgressModel.id, position,isPollSubmit)
            }
        }
    }


    private fun setProgressAnimate(pb: ProgressBar, progressTo: Int) {
        val animation = ObjectAnimator.ofInt(pb, "progress", pb.progress, progressTo * 100)
        animation.duration = 500
        animation.setAutoCancel(true)
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }
}