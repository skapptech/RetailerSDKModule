package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList: MutableList<Fragment?> = ArrayList()
    private val mFragmentTitleList: MutableList<String?> = ArrayList()

    fun addFragment(fragment: Fragment?, title: String?) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(pos: Int): CharSequence? {
        return mFragmentTitleList[pos]
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]!!
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}