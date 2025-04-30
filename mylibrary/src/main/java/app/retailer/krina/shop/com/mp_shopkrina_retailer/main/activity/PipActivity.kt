package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.View
import android.view.WindowManager
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityPipBinding

class PipActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPipBinding
    private var videoView: VideoView? = null
    private var pictureInPictureParamsBuilder: PictureInPictureParams.Builder? = null
    private var mediacontroller: MediaController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        }
        mBinding = DataBindingUtil.setContentView(this@PipActivity, R.layout.activity_pip)
        mBinding.progress.visibility = View.VISIBLE
        try {
            videoView = mBinding.videoView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pictureInPictureParamsBuilder = PictureInPictureParams.Builder()
            }
            mediacontroller = MediaController(this@PipActivity)
            mediacontroller!!.setAnchorView(videoView)
            val videoUrl = intent.getStringExtra("videoUrl")
            val title = intent.getStringExtra("title")
            val title_tv = mBinding.title
            val imageView = mBinding.backFaq
            title_tv.text = title
            videoView!!.setMediaController(mediacontroller)
            videoView!!.setVideoPath(videoUrl)
            videoView!!.requestFocus()
//            videoView!!.start()
            imageView.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startPictureInPictureFeature()
                } else {
                    if (videoView != null) {
                        if (videoView!!.isPlaying) {
                            videoView!!.stopPlayback()
                            videoView = null
                        }
                    }
                    onBackPressed()
                }
            }
            mBinding.videoView.setOnCompletionListener { mp: MediaPlayer? -> finish() }
            videoView!!.setOnPreparedListener { mp: MediaPlayer ->
                mBinding.progress.visibility = View.GONE
                mp.setOnVideoSizeChangedListener { mp1: MediaPlayer?, width: Int, height: Int ->
                    videoView!!.setMediaController(mediacontroller)
                    mediacontroller!!.setAnchorView(videoView)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startPictureInPictureFeature()
        } else {
            super.onBackPressed()
        }
    }

    public override fun onNewIntent(i: Intent) {
        super.onNewIntent(i)
        if (videoView != null) {
            if (videoView!!.isPlaying) {
                videoView!!.stopPlayback()
                videoView = mBinding.videoView
                updateVideoView(i)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && videoView != null) {
            if (videoView!!.isPlaying) {
                videoView!!.stopPlayback()
                videoView = null
                mediacontroller = null
            }
            finishAndRemoveTask()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (videoView != null) {
            if (videoView!!.isPlaying) {
                videoView!!.stopPlayback()
                videoView = null
                mediacontroller = null
            }
        }
        finish()
    }

    public override fun onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!isInPictureInPictureMode) {
                val aspectRatio = Rational(videoView!!.width, videoView!!.height)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pictureInPictureParamsBuilder!!.setAspectRatio(aspectRatio).build()
                    enterPictureInPictureMode(pictureInPictureParamsBuilder!!.build())
                }
                Log.e("isInPictureInPictureMode", "true")
            } else {
                finish()
            }
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        if (isInPictureInPictureMode) {
            mBinding.arrowToolbar.visibility = View.GONE
        } else {
            mBinding.arrowToolbar.visibility = View.VISIBLE
        }
    }


    private fun startPictureInPictureFeature() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(videoView!!.width, videoView!!.height)
            pictureInPictureParamsBuilder!!.setAspectRatio(aspectRatio).build()
            enterPictureInPictureMode(pictureInPictureParamsBuilder!!.build())
        }
    }

    private fun updateVideoView(i: Intent) {
        try {
            val videoUrl = i.getStringExtra("videoUrl")
            mediacontroller = MediaController(this@PipActivity)
            mediacontroller!!.setAnchorView(videoView)
            videoView!!.setMediaController(mediacontroller)
            videoView!!.setVideoURI(Uri.parse(videoUrl))
            videoView!!.requestFocus()
            videoView!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}