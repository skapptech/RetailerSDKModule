package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.app.ActivityManager
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.Surface
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityVideoNotificationBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class VideoNotificationActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityVideoNotificationBinding

    private var previousFingerPosition = 0
    private var baseLayoutPosition = 0
    private var defaultViewHeight = 0
    private val isClosing = false
    private var isScrollingUp = false
    private var isScrollingDown = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        mBinding = ActivityVideoNotificationBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        val manager = getSystemService(AUDIO_SERVICE) as AudioManager
        manager.setStreamMute(AudioManager.STREAM_MUSIC, false)
        mBinding.btnAudio.setOnClickListener { view: View? ->
            if (mBinding.videoView.isPlaying) {
                if (mBinding.btnAudio.tag as Int == 1) {
                    mBinding.btnAudio.tag = 0
                    mBinding.btnAudio.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
                    manager.setStreamMute(AudioManager.STREAM_MUSIC, false)
                } else {
                    mBinding.btnAudio.tag = 1
                    mBinding.btnAudio.setImageResource(android.R.drawable.ic_lock_silent_mode)
                    manager.setStreamMute(AudioManager.STREAM_MUSIC, true)
                }
            }
        }
        mBinding.fullScreen.setOnClickListener { v: View? ->
            val display = (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay
            val orientation = display.orientation
            when (orientation) {
                Surface.ROTATION_0 -> requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                Surface.ROTATION_90 -> requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
        mBinding.closeBtn.setOnClickListener { view: View? ->
            finish()
            Utils.fadeTransaction(this)
        }
        if (intent.extras != null) {
            val urlPath = intent.extras!!.getString("url")
            // MediaController mediacontroller = new MediaController(this);
            // mediacontroller.setAnchorView(mBinding.videoView);
            val uri = Uri.parse(urlPath)
            mBinding.progress.visibility = View.VISIBLE
            // mBinding.videoView.setMediaController(mediacontroller);
            mBinding.videoView.setVideoURI(uri)
            mBinding.videoView.requestFocus()
            mBinding.videoView.start()
            mBinding.btnAudio.tag = 0
            mBinding.videoView.setOnCompletionListener { mp: MediaPlayer? -> finish() }
            // Close the progress bar and play the video
            mBinding.videoView.setOnPreparedListener { mp: MediaPlayer? ->
                mBinding.progress.visibility = View.GONE
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (intent.extras != null && intent.hasExtra("notificationId")) {
            val notificationId = intent.extras!!.getInt("notificationId")
            RetailerSDKApp.getInstance().notificationView(notificationId)
            intent.extras!!.clear()
        }
    }

    override fun onBackPressed() {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val taskList = manager.getRunningTasks(10)
        if (taskList[0].numActivities <= 1) {
            startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun onTouch(view: View?, event: MotionEvent): Boolean {
        // Get finger position on screen
        val Y = event.rawY.toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                // save default base layout height
                defaultViewHeight = mBinding.baseLayout.height
                // Init finger and view position
                previousFingerPosition = Y
                baseLayoutPosition = mBinding.baseLayout.y.toInt()
            }
            MotionEvent.ACTION_UP -> {
                // If user was doing a scroll up
                if (isScrollingUp) {
                    // Reset baselayout position
                    mBinding.baseLayout.y = 0f
                    // We are not in scrolling up mode anymore
                    isScrollingUp = false
                }
                // If user was doing a scroll down
                if (isScrollingDown) {
                    // Reset baselayout position
                    mBinding.baseLayout.y = 0f
                    // Reset base layout size
                    mBinding.baseLayout.layoutParams.height = defaultViewHeight
                    mBinding.baseLayout.requestLayout()
                    // We are not in scrolling down mode anymore
                    isScrollingDown = false
                }
            }
            MotionEvent.ACTION_MOVE -> if (!isClosing) {
                val currentYPosition = mBinding.baseLayout.y.toInt()
                // If we scroll up
                if (previousFingerPosition > Y) {
                    // First time android rise an event for "up" move
                    if (!isScrollingUp) {
                        isScrollingUp = true
                    }

                    // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                    if (mBinding.baseLayout.height < defaultViewHeight) {
                        mBinding.baseLayout.layoutParams.height =
                            mBinding.baseLayout.height - (Y - previousFingerPosition)
                        mBinding.baseLayout.requestLayout()
                    } else {
                        // Has user scroll enough to "auto close" popup ?
                        if (baseLayoutPosition - currentYPosition > defaultViewHeight / 2) {
                            finish()
                            return true
                        }
                    }
                    mBinding.baseLayout.y = mBinding.baseLayout.y + (Y - previousFingerPosition)
                } else {
                    // First time android rise an event for "down" move
                    if (!isScrollingDown) {
                        isScrollingDown = true
                    }
                    // Has user scroll enough to "auto close" popup ?
                    if (Math.abs(baseLayoutPosition - currentYPosition) > defaultViewHeight / 2) {
                        finish()
                        return true
                    }
                    // Change base layout size and position (must change position because view anchor is top left corner)
                    mBinding.baseLayout.y = mBinding.baseLayout.y + (Y - previousFingerPosition)
                    mBinding.baseLayout.layoutParams.height =
                        mBinding.baseLayout.height - (Y - previousFingerPosition)
                    mBinding.baseLayout.requestLayout()
                }
                // Update position
                previousFingerPosition = Y
            }
        }
        return true
    }
}