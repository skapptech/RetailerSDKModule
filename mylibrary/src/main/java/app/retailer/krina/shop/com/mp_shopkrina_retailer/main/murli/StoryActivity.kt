package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.murli

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.MurliStoryResponse.MurliStoryPageListBean
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityStoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.flip.FlipViewController.ViewFlipListener
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide

/**
 * Show murli story animation and page
 */
@SuppressLint("SetTextI18n")
class StoryActivity : AppCompatActivity(), View.OnClickListener, ViewFlipListener {
    private var adapter: MyBaseAdapter? = null
    private var mBinding: ActivityStoryBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_story)
        mBinding!!.flipView.isOverFlipEnabled = true
        mBinding!!.flipView.isFlipByTouchEnabled = true
        mBinding!!.flipView.onViewFlipListener = this
        mBinding!!.ivClose.setOnClickListener(this)
        mBinding!!.ivNext.setOnClickListener(this)
        mBinding!!.ivPrevious.setOnClickListener(this)

        if (intent.extras != null) {
            val list: ArrayList<MurliStoryPageListBean> =
                intent.getSerializableExtra("list") as ArrayList<MurliStoryPageListBean>
            try {
                if (list != null && list.size > 0) {
                    if (SharePrefs.getInstance(baseContext)
                            .getInt(SharePrefs.STORY_CURRENT_PAGE) > list.size
                    ) {
                        SharePrefs.getInstance(this).putInt(SharePrefs.STORY_CURRENT_PAGE, 0)
                    }
                    adapter = MyBaseAdapter(list)
                    mBinding!!.flipView.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                    if (adapter!!.count > SharePrefs.getInstance(baseContext)
                            .getInt(SharePrefs.STORY_CURRENT_PAGE) + 1
                    ) {
                        mBinding!!.flipView.setSelection(
                            SharePrefs.getInstance(baseContext)
                                .getInt(SharePrefs.STORY_CURRENT_PAGE)
                        )
                    } else {
                        mBinding!!.flipView.setSelection(
                            SharePrefs.getInstance(baseContext)
                                .getInt(SharePrefs.STORY_CURRENT_PAGE)
                        )
                    }
                    if (mBinding!!.flipView.selectedItemPosition == 0) {
                        mBinding!!.ivPrevious.visibility = View.INVISIBLE
                    }
                    if (mBinding!!.flipView.selectedItemPosition + 1 == list.size) {
                        mBinding!!.ivNext.visibility = View.INVISIBLE
                    }
                } else {
                    Utils.setToast(
                        baseContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.text_no_story_available)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_close -> {
                finish()
                Utils.rightTransaction(this)
            }

            R.id.iv_next -> if (adapter != null && mBinding!!.flipView.selectedItemPosition + 1 < adapter!!.count) {
                mBinding!!.flipView.setSelection(mBinding!!.flipView.selectedItemPosition + 1)
            }

            R.id.iv_previous -> if (mBinding!!.flipView.selectedItemPosition != 0) {
                mBinding!!.flipView.setSelection(mBinding!!.flipView.selectedItemPosition - 1)
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onViewFlipped(view: View, position: Int) {
//        Utils.showProgressDialogWithText(StoryActivity.this);
        if (position > SharePrefs.getInstance(this)
                .getInt(SharePrefs.STORY_CURRENT_PAGE) && position < adapter!!.count
        ) {
            SharePrefs.getInstance(this).putInt(SharePrefs.STORY_CURRENT_PAGE, position)
        }
        if (position == 0) {
            mBinding!!.ivPrevious.visibility = View.INVISIBLE
        } else {
            mBinding!!.ivPrevious.visibility = View.VISIBLE
        }
        if (position + 1 == adapter!!.count) {
            mBinding!!.ivNext.visibility = View.INVISIBLE
        } else {
            mBinding!!.ivNext.visibility = View.VISIBLE
        }

        val handler = Handler()
        handler.postDelayed({ mBinding!!.progressBar.visibility = View.GONE }, 5000)
    }

    private inner class MyBaseAdapter(private val list: List<MurliStoryPageListBean>) :
        BaseAdapter() {
        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): Any {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var convertView = convertView
            if (convertView == null) {
                convertView =
                    LayoutInflater.from(parent!!.context).inflate(R.layout.item_story, null)
            }
            val tvPage = convertView!!.findViewById<AppCompatTextView>(R.id.tv_page)
            val ivImage1 = convertView.findViewById<AppCompatImageView>(R.id.iv_image1)

            //            LinearLayout progress = convertView.findViewById(R.id.textProgress);

//            if (list.get(position).getPageNumber() == list.size()){
//                progress.setVisibility(View.GONE);
//            }
            if (list[position].imagePath != null) {
                mBinding!!.progressBar.visibility = View.VISIBLE

                Glide.with(this@StoryActivity)
                    .load(EndPointPref.getInstance(MyApplication.getInstance()).baseUrl + list[position].imagePath)
                    .error(R.drawable.murli_story_end_page)
                    .into(ivImage1)
            }

            tvPage.text = "Page -" + list[position].pageNumber
            convertView.findViewById<View>(R.id.rl_story).background =
                resources.getDrawable(R.drawable.story_background)

            return convertView
        }
    }
}