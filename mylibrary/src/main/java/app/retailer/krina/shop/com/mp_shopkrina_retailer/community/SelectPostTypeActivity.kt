package app.retailer.krina.shop.com.mp_shopkrina_retailer.community

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.AddPollActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post.AddPostActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivitySelectPostTypeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class SelectPostTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectPostTypeBinding
    val analyticPost = AnalyticPost()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_post_type)

        binding.toolbarPost.btnPost.visibility = View.GONE

        binding.toolbarPost.back.setOnClickListener {
            finish()
        }

        analyticPost.eventName = "newPostTypeClick"
        analyticPost.source = "SelectionPostType"
        binding.liPost.setOnClickListener {
            analyticPost.postTypeClick = "Text"
            RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
            startActivity(Intent(applicationContext, AddPostActivity::class.java))
        }
        binding.liQues.setOnClickListener {
            analyticPost.postTypeClick = "Poll"
            RetailerSDKApp.getInstance().updateAnalytics(analyticPost)
            startActivity(Intent(applicationContext, AddPollActivity::class.java))
        }
    }
}