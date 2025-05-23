package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class HorizontalMarginItemDecoration(private val context: Context, @param:DimenRes private val horizontalMarginInDp: Int) : RecyclerView.ItemDecoration() {

    private val horizontalMarginInPx: Int = context.resources.getDimension(horizontalMarginInDp).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = horizontalMarginInPx
        outRect.left = horizontalMarginInPx
    }
}