package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.murli;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant;
import okhttp3.ResponseBody;

/**
 * This class Download and create Audio file to Storage
 */
@SuppressLint("StaticFieldLeak")
public class WriteAudioFile extends AsyncTask<Void, Void, Boolean> {
    private final HomeActivity activity;
    private final ResponseBody response;


    public WriteAudioFile(HomeActivity activity, ResponseBody response) {
        this.activity = activity;
        this.response = response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean writtenToDisk = false;
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + Constant.AUDIO_FOLDER + Constant.AUDIO_FILE_NAME);
        if (!file.exists()) {
            writtenToDisk = writeAudioResponseBodyToDisk(response);
        }
        return writtenToDisk;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        activity.openMurli(result);
    }


    private boolean writeAudioResponseBodyToDisk(ResponseBody body) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + Constant.AUDIO_FOLDER);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, Constant.AUDIO_FILE_NAME);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + Constant.AUDIO_FOLDER);

                Uri uri = activity.getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);

                OutputStream outputStream = activity.getContentResolver().openOutputStream(uri);
                outputStream.write(body.bytes());
                outputStream.close();
                return true;
            } else {
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + Constant.AUDIO_FOLDER + Constant.AUDIO_FILE_NAME);
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    byte[] fileReader = new byte[4096];
                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(file);

                    while (true) {
                        int read = inputStream.read(fileReader);
                        if (read == -1) {
                            break;
                        }
                        outputStream.write(fileReader, 0, read);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}