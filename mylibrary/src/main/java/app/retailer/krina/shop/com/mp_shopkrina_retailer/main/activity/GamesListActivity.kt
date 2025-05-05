package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityFullNotificationBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityGamesListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.GameListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddGamePointModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GameModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import io.reactivex.observers.DisposableObserver

class GamesListActivity : AppCompatActivity(), OnButtonClick {
    private lateinit var mBinding: ActivityGamesListBinding

    private var adapter: GameListAdapter? = null
    private var list: ArrayList<GameModel>? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var pos = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#DE6037")
        }
        if (!RetailerSDKApp.getInstance().prefManager.isLoggedIn) {
            startActivity(Intent(applicationContext, MobileSignUpActivity::class.java))
            finish()
        }
        if (TextUtils.isNullOrEmpty(
                SharePrefs.getInstance(
                    applicationContext
                ).getString(SharePrefs.CLUSTER_ID)
            )
        ) {
            startActivity(Intent(applicationContext, TradeActivity::class.java))
            finish()
        }
        mBinding = ActivityGamesListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setSupportActionBar(mBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_activity_game)
        initView()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Picasso.get().load("https://uat.shopkirana.in/images/BackImage/bg_game.jpg")
            .into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom) {
                    mBinding.rvBg.background = BitmapDrawable(bitmap)
                    mBinding.rvBg.refreshDrawableState()
                }

                override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {}
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
        // Creates an instance of SplitInstallManager.
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        // Creates a request to install a module.
        val request = SplitInstallRequest
            .newBuilder() // You can download multiple on demand modules per
            // request by invoking the following method for each
            // module you want to install.
            .addModule("soliatare")
            .addModule("blockbuilder")
            .build()
        splitInstallManager.startInstall(request)
        if (intent.extras != null && intent.hasExtra("notificationId")) {
            val notificationId = intent.extras!!.getInt("notificationId")
            RetailerSDKApp.getInstance().notificationView(notificationId)
            intent.extras!!.clear()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.game, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_terms) {
            if (LocaleHelper.getLanguage(applicationContext).equals("hi", ignoreCase = true)) {
                startActivity(
                    Intent(applicationContext, WebViewActivity::class.java)
                        .putExtra(
                            "url",
                            EndPointPref.getInstance(RetailerSDKApp.application).baseUrl + "/images/game/game_terms_hindi.html"
                        )
                )
            } else {
                startActivity(
                    Intent(applicationContext, WebViewActivity::class.java)
                        .putExtra(
                            "url",
                            EndPointPref.getInstance(RetailerSDKApp.application).baseUrl + "/images/game/game_terms.html"
                        )
                )
            }
        } else onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val taskList = manager.getRunningTasks(10)
        if (taskList[0].numActivities <= 1) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
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

    override fun onButtonClick(pos: Int, itemAdded: Boolean) {
        this.pos = pos
        Utils.showProgressDialog(this)
        commonClassForAPI!!.addGamePoint(
            addWalletPoint,
            AddGamePointModel(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
                0,
                list!![pos].gameName,
                "Play"
            )
        )
    }

    private fun initView() {
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        mBinding.tvPlayGamesWallet.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.play_games_amp_get_wallet_points)
        list = ArrayList()
        adapter = GameListAdapter(this, list, this)
        mBinding.rvGames.adapter = adapter
        commonClassForAPI?.getRetailAppGame(
            gameObserver, SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
            SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID)
        )
    }

    private fun openGame() {
        if (list!![pos].gameUrl != null && list!![pos].gameUrl.length > 0) {
            RetailerSDKApp.getInstance().updateAnalytics(list!![pos].gameName)
            startActivity(
                Intent(applicationContext, GamesWebActivity::class.java)
                    .putExtra("title", list!![pos].gameName)
                    .putExtra("url", list!![pos].gameUrl)
            )
        } else {
            if (list!![pos].gameName.equals("solitare", ignoreCase = true)) {
                RetailerSDKApp.getInstance().updateAnalytics(list!![pos].gameName)
                val intent = Intent()
                intent.setClassName(applicationContext.packageName , "com.sk.solitare.SolitaireActivity")
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (list!![pos].gameName.equals("block builder", ignoreCase = true)) {
                RetailerSDKApp.getInstance().updateAnalytics(list!![pos].gameName)
                val intent = Intent()
                intent.setClassName(
                    applicationContext.packageName ,
                    "com.sk.blocks.activities.MainActivity"
                )
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private val gameObserver: DisposableObserver<ArrayList<GameModel>> =
        object : DisposableObserver<ArrayList<GameModel>>() {
            override fun onNext(arrayList: ArrayList<GameModel>) {
                mBinding.progressGame.visibility = View.GONE
                try {
                    list!!.addAll(arrayList)
                    adapter!!.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                mBinding.progressGame.visibility = View.GONE
            }

            override fun onComplete() {
            }
        }

    private val addWalletPoint: DisposableObserver<String> = object : DisposableObserver<String>() {
        override fun onNext(message: String) {
            try {
                Utils.hideProgressDialog()
                Utils.setToast(
                    applicationContext, message
                )
                openGame()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
            Utils.hideProgressDialog()
        }

        override fun onComplete() {}
    }
}