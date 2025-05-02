package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;

public interface AddToCartInterface {
    void addToCart(int itemId, int qty, double itemUnitPrice, ItemListModel model, int freeItemQuantity, double freeTotalWalletPoint, boolean isPrimeItem, OnItemClick onItemClick);
}