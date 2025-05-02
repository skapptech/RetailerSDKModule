package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.ImageObjEntity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.GridItemLayoutBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import com.bumptech.glide.Glide
import java.io.File

class ImageAdapter(
    var context: Context,
    private val list: List<ImageObjEntity>?,
    private var postImageListener: PostImageFromGallery
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    interface PostImageFromGallery {
        fun postImagesFromGalley(path: String?, pos: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            GridItemLayoutBinding.inflate(
                LayoutInflater.from(viewGroup.context),  viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list!![position]

        if (model.imgFileRelativePath.isNullOrEmpty()) {
            holder.mBinding.image.setImageURI(File(model.imgFileFullPath).toUri())
        } else {
            Glide.with(context)
                .load(SharePrefs.getInstance(RetailerSDKApp.getInstance()).getString(SharePrefs.TRADE_WEB_URL) + model.imgFileFullPath)
                .into(holder.mBinding.image)
        }

        holder.mBinding.ivDelete.setOnClickListener {
            postImageListener.postImagesFromGalley(model.imgFileFullPath!!, position)
        }
        if (context is AddPostActivity) {
            holder.mBinding.ivDelete.visibility = View.VISIBLE
        } else {
            holder.mBinding.ivDelete.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(var mBinding: GridItemLayoutBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}