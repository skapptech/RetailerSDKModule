package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DecimalFormat

class OfferInfoFragment : BottomSheetDialogFragment() {
    var appCompatActivity = activity as? AppCompatActivity
    private lateinit var model: BillDiscountModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        appCompatActivity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            model = arguments?.getSerializable("list") as BillDiscountModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_offer_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvDiscountMsg: TextView = view.findViewById(R.id.tvDiscountMsg)
        val tvDiscountDetailsMsg: TextView = view.findViewById(R.id.tvDiscountDetailsMsg)
        val tvMsg: TextView = view.findViewById(R.id.tvMsg)
        val ivClose: ImageView = view.findViewById(R.id.imClose)

        if (model.billDiscountOfferOn == "Percentage") {
            tvDiscountMsg.text = DecimalFormat(
                "##.##"
            ).format(model.discountPercentage) + "% " + RetailerSDKApp.getInstance().dbHelper.getString(
                R.string.bill_discount
            )
        } else if (model.billDiscountOfferOn == "FreeItem") {
            tvDiscountMsg.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.free_item_offer)
        } else if (model.billDiscountOfferOn.equals("DynamicAmount", ignoreCase = true)) {
            tvDiscountMsg.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.flat_rs) +
                        DecimalFormat("##.##").format(model.billDiscountWallet) + " " + RetailerSDKApp.getInstance().dbHelper.getString(
                    R.string.off
                )
        } else {
            val msgPostBill =
                if (model.applyOn.equals("PostOffer", ignoreCase = true)) " PostOffer" else ""

            if (model.walletType == "WalletPercentage") {
                tvDiscountMsg.text = DecimalFormat(
                    "##.##"
                ).format(model.billDiscountWallet) + "% " + RetailerSDKApp.getInstance().dbHelper.getString(
                    R.string.off
                ) + msgPostBill
            } else {
                tvDiscountMsg.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.flat_rs) + DecimalFormat(
                        "##.##"
                    ).format(model.billDiscountWallet / 10) + RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.off
                    ) + msgPostBill
            }
        }
        var msg = ""
        if (model.billAmount > 0) {
            msg = "Add Item worth Rs" + model.billAmount + " to unlock"
        }
        if (model.lineItem > 0) {
            msg += "\nAdd " + model.lineItem + " Line item to unlock"
        }
        msg += "\n"
        tvMsg.text = msg
//        if (model.isSelected) {
//            tvMsg.setTextColor(resources.getColor(R.color.black))
//            tvMsg.text = "Congrats !! You have Saved Rs" + DecimalFormat("##.##").format(model.billAmount)
//        } else {
//            if (model.isApplicable) {
//                tvMsg.setTextColor(
//                    resources.getColor(R.color.black)
//                )
//                tvMsg.text = "Save Rs." + DecimalFormat("##.##").format(model.billAmount) + " with this offer"
//            } else {
//                tvMsg.text = "Add Item worth Rs" + DecimalFormat(
//                    "##.##"
//                ).format(model.billAmount) + " to unlock"
//                tvMsg.setTextColor(
//                    resources.getColor(R.color.red)
//                )
//            }
//        }
        tvDiscountDetailsMsg.text = model.description
        ivClose.setOnClickListener { dialog?.dismiss() }

        RetailerSDKApp.getInstance().updateAnalytics("offer_info_dialog")
    }

companion object{
    @JvmStatic
    fun newInstance(position: Int, list: BillDiscountModel): OfferInfoFragment {
        val fragment = OfferInfoFragment()
        val args = Bundle()
        args.putSerializable("list", list)
        args.putInt("position", position)
        fragment.arguments = args
        return fragment
    }

}

}