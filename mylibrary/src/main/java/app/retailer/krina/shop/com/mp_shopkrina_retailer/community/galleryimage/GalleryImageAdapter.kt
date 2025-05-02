package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.galleryimage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.AddPostActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.GridItemLayoutBinding
import com.bumptech.glide.Glide
import java.io.File

class GalleryImageAdapter(
    var context: Context,
    private val imagePaths: List<String?>?,
    private var postImageListner: PostImageFromGallery
) : RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {

    interface PostImageFromGallery {
        fun postImagesFromGalley(path: String, pos: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.grid_item_layout, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imagePath = imagePaths!![position]

        Glide.with(context)
            .load(File(imagePath).toUri())
            .into(holder.mBinding.image)

        holder.mBinding.image.setOnClickListener {
            postImageListner.postImagesFromGalley(imagePaths[position]!!, position)
        }
        if (context is AddPostActivity) {
            holder.mBinding.ivDelete.visibility = View.VISIBLE
        } else {
            holder.mBinding.ivDelete.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return imagePaths?.size ?: 0
    }

    inner class ViewHolder(var mBinding: GridItemLayoutBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}
