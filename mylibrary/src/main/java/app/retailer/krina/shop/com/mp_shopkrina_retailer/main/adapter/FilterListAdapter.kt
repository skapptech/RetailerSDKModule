package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.FilterItemModel

class FilterListAdapter(
    context: Context?,
    private var items: ArrayList<FilterItemModel>,
    private val listener: AdapterInterface?
) : ArrayAdapter<FilterItemModel?>(
    context!!, 0,
    items as List<FilterItemModel?>
) {

    fun setItemListCategory(items: ArrayList<FilterItemModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getView(position: Int, v: View?, parent: ViewGroup): View {
        var v = v
        val cell = items[position]
        if (cell.isSectionHeader) {
            v = LayoutInflater.from(context).inflate(R.layout.sorting_section_header, null)
            v.isClickable = false
            val header = v.findViewById<TextView>(R.id.section_header)
            header.text = cell.name
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.list_item, null)
            val name = v.findViewById<TextView>(R.id.name)
            val category = v.findViewById<TextView>(R.id.category)
            val radioButton = v.findViewById<CheckBox>(R.id.cb_flter_check)
            val lerclick = v.findViewById<RelativeLayout>(R.id.lerclick)
           // val ivCheckbox = v.findViewById<ImageView>(R.id.ivCheckbox)
            name.text = cell.category + ":"
            category.text = cell.name
            radioButton.isChecked = cell.isChecked()

            radioButton.setOnClickListener {
                listener?.onClick(position)
            }
            lerclick.setOnClickListener {
                listener?.onClick(position)
            }
        }
        return v
    }
}