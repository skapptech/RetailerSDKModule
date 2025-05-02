package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemLanguageBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnLanguageClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import com.google.firebase.database.DataSnapshot

class LanguageListAdapter(
    private val activity: Activity,
    private val list: ArrayList<DataSnapshot>,
    private val onLanguageClick: OnLanguageClick
) : RecyclerView.Adapter<LanguageListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_language, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataSnapshot = list[position]
        holder.mBinding.tvLanguage.text = dataSnapshot.key
        holder.mBinding.cbLanguage.isChecked =
            SharePrefs.getInstance(activity).getString(SharePrefs.SELECTED_LANGUAGE)
                .equals(dataSnapshot.key, ignoreCase = true)
        holder.mBinding.RLLanguage.setOnClickListener { view: View? ->
            SharePrefs.getInstance(activity)
                .putString(SharePrefs.SELECTED_LANGUAGE, dataSnapshot.key)
            onLanguageClick.onSelectLanguage(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(var mBinding: ItemLanguageBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}