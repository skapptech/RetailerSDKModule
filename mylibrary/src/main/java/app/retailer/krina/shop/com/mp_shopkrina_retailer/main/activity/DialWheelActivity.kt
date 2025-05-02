package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.OrderMaster
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityDialWheelBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.DialEarningResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class DialWheelActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDialWheelBinding
    private var count = 0
    private var winPoint = 0
    private var degree = 0
    private var degree_old = 0
    private var isRotating = 0
    private var exitDialog: Dialog? = null

    private var orderModel: OrderMaster? = null
    private var wheelCount = 0
    private var utils: Utils? = null
    private var countEndFlag = false
    private var btnSpin: Button? = null
    private var imPointer: ImageView? = null
    private var skipDial: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dial_wheel)
        utils = Utils(this)

        // get value using bundle
        val bundle = intent.extras
        if (bundle != null) {
            orderModel = bundle.getSerializable(Constant.ORDER_MODEL) as OrderMaster?
            if (orderModel != null) {
                wheelCount = orderModel!!.wheelcount
                orderModel!!.lang = LocaleHelper.getLanguage(applicationContext)
            } else {
                Toast.makeText(applicationContext, "server error", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("null", "null")
        }
        init()
        mBinding.btnSpin.setOnClickListener { wheelPlay() }
        mBinding.imPointer.setOnClickListener { wheelPlay() }
        mBinding.skipDial.setOnClickListener {
            if (utils!!.isNetworkAvailable) {
                skipSpinPopup()
            } else {
                Utils.setToast(
                    applicationContext, MyApplication.getInstance().dbHelper
                        .getString(R.string.internet_connection)
                )
            }
        }
    }

    override fun onBackPressed() {
        skipSpinPopup()
    }

    override fun onDestroy() {
        super.onDestroy()
        orderDialDes.dispose()
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private fun init() {
        mBinding.tvSpinToWin.text =
            MyApplication.getInstance().dbHelper.getString(R.string.spin_to_win)
        mBinding.skipDial.text = MyApplication.getInstance().dbHelper.getString(R.string.skip)
        btnSpin = mBinding.btnSpin
        imPointer = mBinding.imPointer
        skipDial = mBinding.skipDial
    }

    private fun wheelPlay() {
        if (wheelCount <= 0) {
            val newIntent = Intent(applicationContext, HomeActivity::class.java)
            newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(newIntent)
            finish()
            Utils.rightTransaction(this)
        } else {
            if (utils!!.isNetworkAvailable) {
                rotateWheel()
                btnSpin!!.isEnabled = false
                imPointer!!.isEnabled = false
                skipDial!!.isEnabled = false
            } else {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
                )
            }
        }
    }

    private fun rotateWheel() {
        try {
            if (isRotating == 0) {
                // Random rand = new Random();
                // winPoint = list.get(rand.nextInt(list.size()));
                for (i in orderModel!!.wheelList.indices) {
                    if (i == count) {
                        winPoint = orderModel!!.wheelList[i]
                        break
                    }
                }
                when (winPoint) {
                    10 -> degree = 360
                    20 -> degree = 315
                    40 -> degree = 270
                    80 -> degree = 225
                    120 -> degree = 180
                    140 -> degree = 135
                    160 -> degree = 90
                    200 -> degree = 45
                }
                val rotate = RotateAnimation(
                    degree_old.toFloat(), (degree + 1440).toFloat(),
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f
                )
                rotate.duration = 5000
                rotate.fillAfter = true
                rotate.interpolator = DecelerateInterpolator()
                rotate.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        isRotating = 1
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        degree_old = degree
                        isRotating = 0
                        wheelCount--
                        count++
                        MyApplication.getInstance().updateAnalyticWheel(
                            "wheel_play",
                            orderModel!!.wheelcount,
                            count,
                            winPoint
                        )
                        postDial(winPoint)
                        btnSpin!!.isEnabled = true
                        mBinding.tvRemainingDial.text = "$wheelCount " +
                                MyApplication.getInstance().dbHelper.getString(R.string.dial_remaining)
                        imPointer!!.isEnabled = true
                        skipDial!!.isEnabled = true
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                mBinding.imRoulette.startAnimation(rotate)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GoToHome()
        }
    }

    private fun skipSpinPopup() {
        exitDialog = Dialog(this)
        exitDialog!!.setContentView(R.layout.dialog_skip_wheel)
        exitDialog!!.setCancelable(false)
        val okBtn = exitDialog!!.findViewById<TextView>(R.id.exit_ok_btn)
        val cancelBtn = exitDialog!!.findViewById<TextView>(R.id.cancel_btn)
        val tvExitHere = exitDialog!!.findViewById<TextView>(R.id.tvExitHere)
        val tvSkipSpin = exitDialog!!.findViewById<TextView>(R.id.tvSkipSpin)
        okBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.ok)
        cancelBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.cancel)
        tvExitHere.text =
            MyApplication.getInstance().dbHelper.getString(R.string.without_earning_point_title_)
        tvSkipSpin.text = MyApplication.getInstance().dbHelper.getString(R.string.spin_skip_title)
        cancelBtn.setOnClickListener { exitDialog!!.dismiss() }
        okBtn.setOnClickListener {
            MyApplication.getInstance()
                .updateAnalyticWheel("wheel_skip", orderModel!!.wheelcount, count, 0)
            countEndFlag = true
            postDial(0)
            exitDialog!!.dismiss()
        }
        exitDialog!!.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        exitDialog!!.show()
    }

    private fun postDial(winPoints: Int) {
        if (utils!!.isNetworkAvailable) {
            winPoint = winPoints
            orderModel!!.dialearnigpoint = winPoints
            orderModel!!.playedWheelCount = count
            orderModel!!.customerId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
            Utils.showProgressDialog(this)
            CommonClassForAPI.getInstance(this)!!.postOrderDialValue(
                orderDialDes, orderModel,
                "Wheel Screen winPoints:$winPoints"
            )
        } else {
            Utils.setToast(
                applicationContext, MyApplication.getInstance().dbHelper
                    .getString(R.string.internet_connection)
            )
        }
    }

    private fun GoToHome() {
        // dialog crash handled - devendra
        if (exitDialog != null && exitDialog!!.isShowing) {
            exitDialog!!.dismiss()
        }
        val newIntent = Intent(applicationContext, HomeActivity::class.java)
        newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(newIntent)
        finish()
        Utils.rightTransaction(this)
    }

    private val orderDialDes: DisposableObserver<DialEarningResponse> =
        object : DisposableObserver<DialEarningResponse>() {
            override fun onNext(o: DialEarningResponse) {
                try {
                    btnSpin!!.isEnabled = true
                    imPointer!!.isEnabled = true
                    skipDial!!.isEnabled = true
                    Utils.hideProgressDialog()
                    if (o.isStatus) {
                        if (!countEndFlag) {
                            val textMeg =
                                (MyApplication.getInstance().dbHelper.getString(R.string.you_have_won)
                                        + " " + winPoint + " " + MyApplication.getInstance().dbHelper.getString(
                                    R.string.points
                                ))
                            Utils.setLongToast(
                                applicationContext, textMeg
                            )
                            if (wheelCount <= 0) {
                                val handler = Handler()
                                handler.postDelayed({

                                    // Do something after 5s = 5000ms
                                    Utils.setToast(
                                        applicationContext, o.message
                                    )
                                    GoToHome()
                                }, 2000)
                            }
                        } else {
                            GoToHome()
                        }
                    } else {
                        Utils.setToast(
                            applicationContext, o.message
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    GoToHome()
                }
            }

            override fun onError(e: Throwable) {
                this.dispose()
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }
}