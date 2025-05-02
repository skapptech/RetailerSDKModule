package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.murli;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.jetbrains.annotations.NotNull;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * This class Download Gif Image
 */
public class CreateImageFile {
    private final HomeActivity activity;
    private final String fileName;

    public CreateImageFile(HomeActivity activity, String fileName) {
        this.activity = activity;
        this.fileName = fileName;
    }

    public DisposableObserver<ResponseBody> imageFileObserver = new DisposableObserver<ResponseBody>() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public void onNext(@NotNull ResponseBody response) {
            if (Utils.getAvailableSpaceInMB() > 500) {
                new WriteImageFile(activity, response, fileName).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Utils.setToast(activity, RetailerSDKApp.getInstance().dbHelper.getString(R.string.space_not_availabe));
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
}