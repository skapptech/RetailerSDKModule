package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.TargetOrderListActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.AlreadyBoughtItemsFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.TargetItemsFragment

class TargetViewPagerTabAdapter(activity: TargetOrderListActivity, storeId: Int) :
    FragmentStateAdapter(activity) {
    private var mStoreId = storeId
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt("storeId", mStoreId)
        return when (position) {
            0 -> {
                val targetItemFragment = TargetItemsFragment()
                targetItemFragment.arguments = bundle
                targetItemFragment
            }
            1 -> {
                val alreadyBoughtItemsFragment = AlreadyBoughtItemsFragment()
                alreadyBoughtItemsFragment.arguments = bundle
                alreadyBoughtItemsFragment
            }
            else -> TargetItemsFragment()
        }
    }
}