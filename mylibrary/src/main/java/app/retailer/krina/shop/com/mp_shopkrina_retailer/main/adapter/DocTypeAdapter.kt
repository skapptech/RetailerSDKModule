package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.DocTypeModel

class DocTypeAdapter(
    var context1: Context,
    resouceId: Int,
    private val list: ArrayList<DocTypeModel>
) : ArrayAdapter<DocTypeModel>(context1, resouceId, list) {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemId(i: Int): Long {
        return list[i].id.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        val v = LayoutInflater.from(context).inflate(R.layout.simple_list_item_1, null)
        val names = v.findViewById<TextView>(R.id.text1)
        names.text = list[i].docType
        return v
    }

    override fun getDropDownView(i: Int, view: View?, parent: ViewGroup): View? {
        val v: View? = LayoutInflater.from(context).inflate(R.layout.simple_list_item_1, null)
        val names = v?.findViewById<TextView>(R.id.text1)
        names?.text = list[i].docType
        return v
    }
}