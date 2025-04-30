package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;


public class ProgressDialog {
    private Dialog dialog;
    private static ProgressDialog mInstance;

    public static synchronized ProgressDialog getInstance() {
        if (mInstance == null) {
            mInstance = new ProgressDialog();
        }
        return mInstance;
    }

    public void show(Context context) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.community_progress_dialog);
        dialog.setCancelable(false);
        ImageView progressBarImage=dialog.findViewById(R.id.pBar);
        Glide.with(context)
                .load(R.drawable.loader)
                .into(progressBarImage);
        if (dialog != null) {
            dialog.show();
        }

    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
