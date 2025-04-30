package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import com.google.android.material.snackbar.Snackbar


class ViewUtils {
    companion object {
        var customDialog: Dialog? = null


        fun ProgressBar.show() {
            visibility = View.VISIBLE
        }

        fun ProgressBar.hide() {
            visibility = View.GONE
        }

        fun toast(context: Context?, str: String?) {
            val toast = Toast.makeText(context, str, Toast.LENGTH_SHORT)
         //   toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

        fun View.snackbar(message: String) {
            Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
                snackbar.setAction("Ok") {
                    snackbar.dismiss()
                }
            }.show()
        }

        fun getSafeSubstring(s: String): String {
            if (!TextUtils.isNullOrEmpty(s)) {
                if (s.length >= 80) {
                    return s.substring(0, 80)+"..."
                }
                return s
            }else{
                return ""
            }
        }

        fun hideProgressDialog() {
            if (customDialog != null) {
                customDialog!!.dismiss()
            }
        }

        fun showProgressDialog(activity: Activity) {
            try {
                if (customDialog != null) {
                    customDialog?.dismiss()
                    customDialog = null
                }
                customDialog = Dialog(activity, R.style.CustomDialog)
                val mView: View =
                    LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null)
                customDialog!!.setContentView(mView)
                customDialog!!.setCancelable(false)

                if (!activity.isFinishing && !customDialog!!.isShowing) {
                    customDialog!!.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun fromHtml(html: String?): Spanned? {
            return if (html == null) {
                // return an empty spannable if the html is null
                SpannableString("")
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
                // we are using this flag to give a consistent behaviour
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(html)
            }
        }
    }
}

