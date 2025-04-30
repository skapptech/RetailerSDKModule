package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityFeedImageBinding

class FeedImageShowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedImageBinding
    private var list: ArrayList<ImageObjEntity>? = null
    private var pos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        supportActionBar?.title = ""
        if (intent.extras != null) {
            list = intent.getSerializableExtra("ImagePath") as ArrayList<ImageObjEntity>
            pos = intent.getIntExtra("pos", 0)
        }
        init()
    }

    private fun init() {
        val adapter = ImageAdapter(this, list)
        binding.viewPager.adapter = adapter
        if (list!!.size > 1)
            binding.indicator.setViewPager(binding.viewPager)
        binding.viewPager.currentItem = pos
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}