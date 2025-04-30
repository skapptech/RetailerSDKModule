package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.myIssues

import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.MyIssueDetailBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.IssueDetailAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyIssueDetailModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyIssuesResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class MyIssueDetailActivity : AppCompatActivity() {

    private var mBinding: MyIssueDetailBinding? = null

    private var commonClassForAPI: CommonClassForAPI? = null
    private var utils: Utils? = null
    private var model: MyIssuesResponseModel? = null

    private var list: ArrayList<MyIssueDetailModel.CommentList>? = null
    private var adapter: IssueDetailAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.my_issue_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent.extras != null)
            model = intent.getSerializableExtra("model") as MyIssuesResponseModel?

        title =
            MyApplication.getInstance().dbHelper.getString(R.string.title_activity_Issue_with_the_cashback)
        supportActionBar!!.subtitle =
            Html.fromHtml(
                "<font color='#212121'>" + getString(R.string.ticket__id) +
                        "</font>" + "<font color='#F56B3B'>" + "#" + model?.TicketId + "</font>"
            )
        init()
        callApi()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }


    private fun init() {
        utils = Utils(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.reverseLayout = false
        mBinding!!.rvHeader.layoutManager = layoutManager

        list = ArrayList()
        list?.add(
            MyIssueDetailModel.CommentList(
                model?.TicketDescription,
                "me",
                model?.CreatedDate,
                model?.TicketDescription
            )
        )

        adapter = IssueDetailAdapter(this, list)
        mBinding!!.rvHeader.adapter = adapter
    }

    fun callApi() {
        if (utils!!.isNetworkAvailable) {
            commonClassForAPI!!.fetchIssueDetail(
                generateTicketDis, model?.TicketId!!
            )
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.no_internet)
            )
        }
    }


    private var generateTicketDis: DisposableObserver<MyIssueDetailModel> =
        object : DisposableObserver<MyIssueDetailModel>() {
            override fun onNext(response: MyIssueDetailModel) {
                Utils.hideProgressDialog()
                if (response.ticketActivityLogDcs.isNotEmpty()) {
                    response.ticketActivityLogDcs?.forEach { list?.add(it) }
                    adapter?.updateData(list)
                    adapter?.notifyDataSetChanged()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }
}