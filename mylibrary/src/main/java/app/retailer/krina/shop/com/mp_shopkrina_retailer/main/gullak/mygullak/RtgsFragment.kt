package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.mygullak

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentRtgsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.RTGSDataListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GullakModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class RtgsFragment : Fragment() {
    private lateinit var activity: MyGullakActivity

    lateinit var binding: FragmentRtgsBinding

    lateinit var sharePrefs: SharePrefs
    lateinit var utils: Utils
    lateinit var commonAPICall: CommonClassForAPI

    private var list: ArrayList<GullakModel>? = null
    private var adapter: RTGSDataListAdapter? = null

    private var pageCount = 0
    private var totalOrderCount = 10
    private var loading = true


    companion object {
        fun newInstance(): Fragment {
            return RtgsFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MyGullakActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasOptionsMenu()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rtgs, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvEmpty.text =
            MyApplication.getInstance().dbHelper.getString(R.string.no_items_available)

        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerGullak.layoutManager = layoutManager
        sharePrefs = SharePrefs(activity)
        utils = Utils(activity)
        commonAPICall = CommonClassForAPI.getInstance(activity)

        list = ArrayList()
        adapter = RTGSDataListAdapter(activity, list!!)
        binding.recyclerGullak.adapter = adapter

        binding.recyclerGullak.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loading = false
                            pageCount += 10
                            binding.progressGullak.visibility = View.VISIBLE
                            commonAPICall.fetchRTGSDataList(
                                listObserver,
                                sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                                pageCount,
                                totalOrderCount
                            )
                        }
                    }
                }
            }
        })

        commonAPICall.fetchRTGSDataList(
            listObserver,
            sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
            0,
            10
        )
        commonAPICall.getRTGSBalance(rtgsBalObserver, sharePrefs.getInt(SharePrefs.CUSTOMER_ID))
    }

    override fun onResume() {
        super.onResume()
        activity.tvInfo.text = MyApplication.getInstance().dbHelper.getString(R.string.details)
        activity.binding.toolbarG.title.text =
            MyApplication.getInstance().dbHelper.getString(R.string.van_rtgs)
    }


    private var rtgsBalObserver: DisposableObserver<Double> =
        object : DisposableObserver<Double>() {
            override fun onNext(list: Double) {

            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }

    private var listObserver: DisposableObserver<ArrayList<GullakModel>> =
        object : DisposableObserver<ArrayList<GullakModel>>() {
            override fun onNext(arrayList: ArrayList<GullakModel>) {
                binding.progressGullak.visibility = View.GONE
                binding.tvEmpty.visibility = View.GONE
                if (arrayList.size != 0) {
                    list?.addAll(arrayList)
                    adapter?.notifyDataSetChanged()
                }
                if (list!!.size == 0) {
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }
}