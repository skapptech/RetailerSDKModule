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
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentGullakBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentHome1Binding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.GullakDataListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GullakModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class GullakFragment : Fragment() {
    private lateinit var activity: MyGullakActivity

    lateinit var binding: FragmentGullakBinding

    lateinit var sharePrefs: SharePrefs
    lateinit var utils: Utils
    lateinit var commonAPICall: CommonClassForAPI

    private var list: ArrayList<GullakModel>? = null
    private var gullakDataListAdapter: GullakDataListAdapter? = null
    private var pageCount = 1
    private var totalOrderCount = 15


    companion object {
        fun newInstance(): Fragment {
            return GullakFragment()
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
        binding = FragmentGullakBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvEmpty.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_items_available)

        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerGullak.layoutManager = layoutManager
        sharePrefs = SharePrefs(activity)
        utils = Utils(activity)
        commonAPICall = CommonClassForAPI.getInstance(activity)

        list = ArrayList()
        gullakDataListAdapter = GullakDataListAdapter(activity, list!!)
        binding.recyclerGullak.adapter = gullakDataListAdapter

        binding.recyclerGullak.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        pageCount++
                        binding.progressGullak.visibility = View.VISIBLE
                        commonAPICall.fetchGullakDataList(
                            gullakDataObserver,
                            sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                            pageCount,
                            totalOrderCount
                        )
                    }
                }
            }
        })

        commonAPICall.fetchGullakDataList(
            gullakDataObserver,
            sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
            pageCount,
            totalOrderCount
        )
    }

    override fun onResume() {
        super.onResume()
        activity.tvInfo.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.add_money)
        activity.binding.toolbarG.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.my_gullak)
    }


    private var gullakDataObserver: DisposableObserver<ArrayList<GullakModel>> =
        object : DisposableObserver<ArrayList<GullakModel>>() {
            override fun onNext(arrayList: ArrayList<GullakModel>) {
                binding.progressGullak.visibility = View.GONE
                binding.tvEmpty.visibility = View.GONE
                if (arrayList != null && arrayList.size != 0) {
                    list?.addAll(arrayList)
                    gullakDataListAdapter?.notifyDataSetChanged()
                }
                if (list!!.size == 0) {
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {}
        }
}