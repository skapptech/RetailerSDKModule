package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.murli;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant;
import okhttp3.ResponseBody;

/**
 * This class Create and save Gif Image's to Storage
 */

@SuppressLint("StaticFieldLeak")
public class WriteImageFile extends AsyncTask<Void, Void, Boolean> {
    private ResponseBody response;
    private String fileName;
    private HomeActivity activity;

    WriteImageFile(HomeActivity activity, ResponseBody response, String fileName) {
        this.response = response;
        this.fileName = fileName;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean writtenToDisk = false;
        File file = new File(Environment.getExternalStorageDirectory() + Constant.IMAGE_FOLDER + fileName);
        if (!file.exists()) {
            writtenToDisk = writeImageResponseBodyToDisk(response, fileName);
        }
        return writtenToDisk;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Constant.IMAGE_OBSERVABLE_EXECUTION_COUNT++;

        if (Constant.IMAGE_OBSERVABLE_EXECUTION_COUNT == 3) {
            activity.postImageObserver();
            Constant.IMAGE_OBSERVABLE_EXECUTION_COUNT = 0;
        }
    }

    private boolean writeImageResponseBodyToDisk(ResponseBody body, String fileName) {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + Constant.IMAGE_FOLDER);
            if (!file.exists()) {
                file.mkdirs();
            }

            file = new File(Environment.getExternalStorageDirectory() + Constant.IMAGE_FOLDER + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}