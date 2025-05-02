package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.RatingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.RatingModel.UserRatingDetailDc
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class DBoyRatingFragment : BottomSheetDialogFragment() {
    private lateinit var appCtx: RetailerSDKApp
    private lateinit var homeViewModel: HomeViewModel
    private var activity: AppCompatActivity? = null
    private var ratingList: ArrayList<RatingModel>? = null
    private var onButtonClick: OnButtonClick? = null
    private var position = 0
    private var clickedPos = 2


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
        appCtx = requireActivity().application as RetailerSDKApp
        onButtonClick = context as OnButtonClick
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            position = requireArguments().getInt("position")
            ratingList = requireArguments().getSerializable("list") as ArrayList<RatingModel>?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val appRepository = AppRepository(RetailerSDKApp.application)
        homeViewModel =
            ViewModelProvider(
                requireActivity(),
                HomeViewModelFactory(RetailerSDKApp.application, appRepository)
            )[HomeViewModel::class.java]
        return inflater.inflate(R.layout.fragment_d_boy_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvPleaseRateH = view.findViewById<TextView>(R.id.tvPleaseRateH)
        val ivImage = view.findViewById<ImageView>(R.id.ivImage)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvOrderId = view.findViewById<TextView>(R.id.tvOrderId)
        val ivFace1 = view.findViewById<ImageView>(R.id.ivFace1)
        val ivFace2 = view.findViewById<ImageView>(R.id.ivFace2)
        val ivFace3 = view.findViewById<ImageView>(R.id.ivFace3)
        val ivFace4 = view.findViewById<ImageView>(R.id.ivFace4)
        val ivFace5 = view.findViewById<ImageView>(R.id.ivFace5)
        val fbDeliver = view.findViewById<FlexboxLayout>(R.id.fbDeliver)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)
        tvPleaseRateH.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.please_rate_delivery_service)
        tvOrderId.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_id_colon) + " " + ratingList!![0].orderId
        btnSubmit.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.submit)
        Picasso.get().load(ratingList!![0].profilePic)
            .placeholder(R.drawable.logo_grey)
            .error(R.drawable.profile_round)
            .into(ivImage)
        tvName.text = ratingList!![0].displayName
        updateViews(fbDeliver, ratingList, 2)
        ivFace1.setOnClickListener {
            clickedPos = 0
            ivFace1.setImageResource(R.drawable.ic_face_o1)
            ivFace2.setImageResource(R.drawable.ic_face2)
            ivFace3.setImageResource(R.drawable.ic_face3)
            ivFace4.setImageResource(R.drawable.ic_face4)
            ivFace5.setImageResource(R.drawable.ic_face5)
            updateViewsM(fbDeliver, ratingList, 0)
        }
        ivFace2.setOnClickListener {
            clickedPos = 1
            ivFace2.setImageResource(R.drawable.ic_face_o2)
            ivFace1.setImageResource(R.drawable.ic_face1)
            ivFace3.setImageResource(R.drawable.ic_face3)
            ivFace4.setImageResource(R.drawable.ic_face4)
            ivFace5.setImageResource(R.drawable.ic_face5)
            updateViewsM(fbDeliver, ratingList, 1)
        }
        ivFace3.setOnClickListener {
            clickedPos = 2
            ivFace3.setImageResource(R.drawable.ic_face_o3)
            ivFace1.setImageResource(R.drawable.ic_face1)
            ivFace2.setImageResource(R.drawable.ic_face2)
            ivFace4.setImageResource(R.drawable.ic_face4)
            ivFace5.setImageResource(R.drawable.ic_face5)
            updateViewsM(fbDeliver, ratingList, 2)
        }
        ivFace4.setOnClickListener {
            clickedPos = 3
            ivFace4.setImageResource(R.drawable.ic_face_o4)
            ivFace1.setImageResource(R.drawable.ic_face1)
            ivFace2.setImageResource(R.drawable.ic_face2)
            ivFace3.setImageResource(R.drawable.ic_face3)
            ivFace5.setImageResource(R.drawable.ic_face5)
            updateViewsM(fbDeliver, ratingList, 3)
        }
        ivFace5.setOnClickListener {
            clickedPos = 4
            ivFace5.setImageResource(R.drawable.ic_face_o5)
            ivFace1.setImageResource(R.drawable.ic_face1)
            ivFace2.setImageResource(R.drawable.ic_face2)
            ivFace3.setImageResource(R.drawable.ic_face3)
            ivFace4.setImageResource(R.drawable.ic_face4)
            updateViewsM(fbDeliver, ratingList, 4)
        }
        btnSubmit.setOnClickListener {
            println("" + clickedPos + " " + ratingList!![clickedPos].ratingDetails!!.any { it.isSelect })
            RetailerSDKApp.getInstance().updateAnalytics("delivery_date_change_click")
            if (clickedPos == -1) {
                Utils.setToast(activity, "Please rate Delivery person's service's")
            } else if (clickedPos < 3 && !ratingList!![clickedPos].ratingDetails!!.any { it.isSelect }) {
                Utils.setToast(activity, "Select anyone Delivery person's service's")
            } else {
                btnSubmit.isEnabled = false
                if (ratingList!!.size > 0) {
                    Utils.showProgressDialog(activity)
                    val model = ratingList!![clickedPos]
                    val dcList: MutableList<UserRatingDetailDc> = ArrayList()
                    for (detailDc in ratingList!![clickedPos].ratingDetails!!) {
                        if (detailDc.isSelect) dcList.add(detailDc)
                    }
                    model.ratingDetails = dcList
                    homeViewModel.addRating(model)
                }
            }
        }
        ivFace3.setImageResource(R.drawable.ic_face_o3)
        clickedPos = 3

        dialog!!.setCanceledOnTouchOutside(false)
        RetailerSDKApp.getInstance().updateAnalytics("delivery_date_dialog")
        observe(homeViewModel.getAddRatingData, ::handleAddRatingResult)

    }

    // single selection
    private fun updateViews(
        flexboxLayout: FlexboxLayout,
        ratingList: ArrayList<RatingModel>?,
        pos: Int
    ) {
        flexboxLayout.removeAllViews()
        if (ratingList!!.size > 0) {
            val viewList: MutableList<TextView> = ArrayList()
            for (i in ratingList[pos].ratingDetails!!.indices) {
                val params = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(15, 10, 15, 10)
                val textView = TextView(activity)
                textView.id = i
                textView.layoutParams = params
                textView.background = resources.getDrawable(R.drawable.rectangle_grey)
                textView.setPadding(40, 20, 40, 20)
                textView.text = "" + ratingList[pos].ratingDetails!![i].Detail
                viewList.add(textView)
                textView.setOnClickListener { view: View ->
                    for (view1 in viewList) {
                        view1.background = resources.getDrawable(R.drawable.rectangle_grey)
                    }
                    view.background = resources.getDrawable(R.drawable.rectangle_orange)
                }
//                if (i == 0) {
//                    textView.background = resources.getDrawable(R.drawable.rectangle_orange)
//                }
                flexboxLayout.addView(textView)
            }
        }
    }

    // for multiple selection
    private fun updateViewsM(
        flexboxLayout: FlexboxLayout,
        ratingList: ArrayList<RatingModel>?,
        pos: Int
    ) {
        flexboxLayout.removeAllViews()
        flexboxLayout.removeAllViewsInLayout()
        try {
            if (ratingList!!.size > 0) {
                val viewList: MutableList<TextView> = ArrayList()
                for (i in ratingList[pos].ratingDetails!!.indices) {
                    val params = FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        FlexboxLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(15, 10, 15, 10)
                    val textView = TextView(activity)
                    textView.id = i
                    textView.layoutParams = params
                    textView.background = resources.getDrawable(R.drawable.rectangle_grey)
                    textView.setPadding(40, 20, 40, 20)
                    textView.text = "" + ratingList[pos].ratingDetails!![i].Detail
                    textView.tag = 0
                    viewList.add(textView)
                    textView.setOnClickListener { v: View ->
                        viewList[textView.id].tag = if (v.tag as Int == 0) 1 else 0
                        for (view1 in viewList) {
                            if (view1.tag as Int == 1) {
                                ratingList[pos].ratingDetails!![view1.id].isSelect = true
                                view1.background =
                                    resources.getDrawable(R.drawable.rectangle_orange)
                            } else {
                                ratingList[pos].ratingDetails!![view1.id].isSelect = false
                                view1.background = resources.getDrawable(R.drawable.rectangle_grey)
                            }
                        }
                    }
//                    if (i == 0) {
//                        ratingList[pos].ratingDetails!![textView.id].isSelect = true
//                        textView.background = resources.getDrawable(R.drawable.rectangle_orange)
//                        viewList[i].tag = 1
//                    }
                    flexboxLayout.addView(textView)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun handleAddRatingResult(it: Boolean) {
        if (it){
            dismiss()
            Utils.hideProgressDialog()
            Utils.setToast(activity, "Your feedback submitted successfully")
            clickedPos = -1
            onButtonClick!!.onButtonClick(position, true)
        }else{
            Utils.setToast(activity, "Your feedback not submitted")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int, ratingList: ArrayList<RatingModel>?): DBoyRatingFragment {
            val fragment = DBoyRatingFragment()
            val args = Bundle()
            args.putSerializable("list", ratingList)
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }
}