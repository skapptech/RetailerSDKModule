package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.MoqAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MoqSelectionFragment : BottomSheetDialogFragment() {
    private var activity: AppCompatActivity? = null
    private var model: ItemListModel? = null
    private var listener: AdapterInterface? = null
    private var position = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
        listener = context as AdapterInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            position = arguments!!.getInt("position")
            model = arguments!!.getSerializable("list") as ItemListModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.moq_price_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvItemName: TextView = view.findViewById<TextView>(R.id.itemName)
        val tvDSelectQty: TextView = view.findViewById<TextView>(R.id.tvDSelectQty)
        val tvDMoq: TextView = view.findViewById<TextView>(R.id.tvDMoq)
        val tvDMrp: TextView = view.findViewById<TextView>(R.id.tvDMrp)
        val tvDRs: TextView = view.findViewById<TextView>(R.id.tvDRs)
        val tvDMargin: TextView = view.findViewById<TextView>(R.id.tvDMargin)

        tvDSelectQty.text =
            MyApplication.getInstance().dbHelper.getString(R.string.select_quantities_for)
        tvDMoq.text = MyApplication.getInstance().dbHelper.getString(R.string.moq)
        tvDMrp.text = MyApplication.getInstance().dbHelper.getString(R.string.mrp)
        tvDRs.text = MyApplication.getInstance().dbHelper.getString(R.string.rs)
        tvDMargin.text = MyApplication.getInstance().dbHelper.getString(R.string.margins_d)

        tvItemName.text = model?.itemname
        val mMoqPriceList: ListView = view.findViewById(R.id.listview_moq_price)
        val adapter =
            MoqAdapter(
                activity!!,
                model!!.moqList,
                listener
            )
        mMoqPriceList.adapter = adapter

        MyApplication.getInstance().updateAnalytics("moq_dialog")
    }


    companion object {
        @JvmStatic
        fun newInstance(position: Int, list: ItemListModel?): MoqSelectionFragment {
            val fragment = MoqSelectionFragment()
            val args = Bundle()
            args.putSerializable("list", list)
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }
}