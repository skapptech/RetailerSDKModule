package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel

interface MoqInterface {
    fun onSelect(model: ItemListModel?, pos: Int)
}