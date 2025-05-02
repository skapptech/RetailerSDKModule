package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.app.ActivityManager
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityFullNotificationBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.OnSwipeTouchListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class FullNotificationActivity : AppCompatActivity(), View.OnTouchListener {
    private var previousFingerPosition = 0
    private var baseLayoutPosition = 0
    private var defaultViewHeight = 0
    private val isClosing = false
    private var isScrollingUp = false
    private var isScrollingDown = false
    private var mBinding: ActivityFullNotificationBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_full_notification)
        mBinding!!.btnClose.setOnClickListener { onBackPressed() }
        mBinding!!.ivImage.setOnTouchListener(OnSwipeTouchListener(this))
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (intent.extras != null && intent.hasExtra("notificationId")) {
            val notificationId = intent.extras!!.getInt("notificationId")
            RetailerSDKApp.getInstance().notificationView(notificationId)
            intent.extras!!.clear()
        }
    }

    override fun onResume() {
        super.onResume()
        if (intent.extras != null) {
            val image = intent.extras?.getString("image")
            Glide.with(this)
                .load(image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        p0: GlideException?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: Boolean
                    ): Boolean {
                        mBinding!!.progressFull.visibility = View.GONE
                        return true
                    }

                    override fun onResourceReady(
                        p0: Drawable?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: DataSource?,
                        p4: Boolean
                    ): Boolean {
                        mBinding!!.progressFull.visibility = View.GONE
                        return false
                    }
                })
                .into(mBinding!!.ivImage)
        } else {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val manager: ActivityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val taskList: List<ActivityManager.RunningTaskInfo> = manager.getRunningTasks(10)
        if (taskList[0].numActivities <= 1) {
            startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val Y = event!!.rawY.toInt()

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                // save default base layout height
                defaultViewHeight = mBinding!!.rlBas.height

                // Init finger and view position
                previousFingerPosition = Y
                baseLayoutPosition = mBinding!!.rlBas.y.toInt()
            }
            MotionEvent.ACTION_UP -> {
                // If user was doing a scroll up
                if (isScrollingUp) {
                    // Reset baselayout position
                    mBinding!!.rlBas.y = 0F
                    // We are not in scrolling up mode anymore
                    isScrollingUp = false
                }

                // If user was doing a scroll down
                if (isScrollingDown) {
                    // Reset baselayout position
                    mBinding!!.rlBas.y = 0F
                    // Reset base layout size
                    mBinding!!.rlBas.layoutParams.height = defaultViewHeight
                    mBinding!!.rlBas.requestLayout()
                    // We are not in scrolling down mode anymore
                    isScrollingDown = false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isClosing) {
                    val currentYPosition = mBinding!!.rlBas.y

                    // If we scroll up
                    if (previousFingerPosition > Y) {
                        // First time android rise an event for "up" move
                        if (!isScrollingUp) {
                            isScrollingUp = true
                        }

                        // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                        if (mBinding!!.rlBas.height < defaultViewHeight) {
                            mBinding!!.rlBas.layoutParams.height =
                                mBinding!!.rlBas.height - (Y - previousFingerPosition)
                            mBinding!!.rlBas.requestLayout()
                        } else {
                            // Has user scroll enough to "auto close" popup ?
                            if ((baseLayoutPosition - currentYPosition) > defaultViewHeight / 4) {
                                onBackPressed()
                                return true
                            }
                        }
                        mBinding!!.rlBas.y = mBinding!!.rlBas.y + (Y - previousFingerPosition)
                    }
                    // If we scroll down
                    else {
                        // First time android rise an event for "down" move
                        if (!isScrollingDown) {
                            isScrollingDown = true
                        }

                        // Has user scroll enough to "auto close" popup ?
                        if (Math.abs(baseLayoutPosition - currentYPosition) > defaultViewHeight / 2) {
                            onBackPressed()
                            return true
                        }
                        // Change base layout size and position (must change position because view anchor is top left corner)
                        mBinding!!.rlBas.y = mBinding!!.rlBas.y + (Y - previousFingerPosition)
                        mBinding!!.rlBas.layoutParams.height =
                            mBinding!!.rlBas.height - (Y - previousFingerPosition)
                        mBinding!!.rlBas.requestLayout()
                    }
                    // Update position
                    previousFingerPosition = Y
                }
            }
        }
        return true
    }
}