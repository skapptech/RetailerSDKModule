package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces

import androidx.recyclerview.widget.RecyclerView

interface FlashDealsOfferInterface {
    fun flashDealsOffer(
        mBinding: RecyclerView.ViewHolder?,
        id: String?,
        flashDealBackImages: String?
    )
}
