package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.murli;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMurliBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;

/**
 * Murli class for downloading audio files and playing
 */
public class MurliActivity extends Activity implements AnimationListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private ActivityMurliBinding mBinding;
    private MediaPlayer player;
    private String audioFileName = "test";  //MP-INDB-1_09172019125727.mp3
    // listener for mic
    private Visualizer visualizer;
    private GifDrawable gifDrawable;
    private boolean isAudioFinished = false;
    private CommonClassForAPI commonAPICall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMurliBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        player = new MediaPlayer();
        mBinding.ibClose.setOnClickListener(v -> onBackPressed());
        commonAPICall = CommonClassForAPI.getInstance(MyApplication.getInstance().activity);
        commonAPICall.getMurliAudioForMobile(audioObserver, SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
                SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID), "Shortcut Icon");
    }

    private void setupImages() {
        setMurliImage(1, Constant.IMAGE_FILE_NAME1);
        setMurliImage(2, Constant.IMAGE_FILE_NAME2);
        gifDrawable = (GifDrawable) mBinding.murliSidewalkView.getDrawable();
        gifDrawable.addAnimationListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        int level = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2;
        if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < level) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, level, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (player != null && !player.isPlaying()) {
                player.start();
            }
            if (visualizer != null) {
                visualizer.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (player != null && player.isPlaying()) {
                player.pause();
                visualizer.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new DeleteFile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        try {
            if (player != null) {
                player.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            if (visualizer != null) {
                visualizer.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        gifDrawable.removeAnimationListener(this);
        mBinding.murliSidewalkView.setImageResource(R.drawable.murli_hi);
        gifDrawable = (GifDrawable) mBinding.murliSidewalkView.getDrawable();
        gifDrawable.addAnimationListener(this);
    }

    @Override
    public void onAnimationCompleted(int loopNumber) {
        if (isAudioFinished) {
            onBackPressed();
        } else {
            playAudio();
            isAudioFinished = true;
        }
    }


    private void setMurliImage(int pos, String image) {
        File file = new File(Environment.getExternalStorageDirectory() + "/ShopKirana/Images/" + image);
        if (file.exists()) {
            try {
                if (pos == 1) {
                    mBinding.murliSidewalkView.setImageURI(Uri.fromFile(file));
                }
                if (pos == 2) {
                    mBinding.murliSpeakingView.setImageURI(Uri.fromFile(file));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void playAudio() {
        File file = new File(Environment.getExternalStorageDirectory() + "/ShopKirana/Audio/" + audioFileName);
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory() + "/ShopKirana/Audio");
            File directory = new File(file.getPath());
            String[] files = directory.list();
            if (files != null && files.length > 0) {
                file = new File(file, files[0]);
            }
        }
        if (file.exists()) {
            try {
                player.setDataSource(file.getAbsolutePath());
                player.prepare();
                player.setOnPreparedListener(this);
                player.setOnCompletionListener(this);
                // setup visualizer for audio waves
                visualizerController(player.getAudioSessionId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            onBackPressed();
        }
    }

    private void visualizerController(int audioSessionId) {
        visualizer = new Visualizer(audioSessionId);
        visualizer.setEnabled(false);
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                int data = 0;
                for (int i = 0; i < bytes.length - 1; i++) {
                    data = bytes[i];
                }
                if (data == -128 || data == 127) {
                    mBinding.murliSpeakingView.setVisibility(View.INVISIBLE);
                } else {
                    mBinding.murliSpeakingView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {

            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
        visualizer.setEnabled(true);
    }


    private final DisposableObserver<JsonObject> audioObserver = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject object) {
            try {
                if (object.has("Mp3url") && !object.get("Mp3url").isJsonNull()) {
                    String url = object.get("Mp3url").getAsString();
                    audioFileName = url.substring(url.lastIndexOf("/") + 1);
                    File file = new File(Environment.getExternalStorageDirectory() + Constant.AUDIO_FOLDER + audioFileName);
                    if (file.exists()) {
                        file = new File(Environment.getExternalStorageDirectory() + Constant.AUDIO_FOLDER + audioFileName);
                        setupImages();
                    } else {
                        audioFileName = url.substring(url.lastIndexOf("/") + 1);
                        commonAPICall.downloadFileWithUrl(fileObserver, url);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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

    private final DisposableObserver<ResponseBody> fileObserver = new DisposableObserver<ResponseBody>() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public void onNext(@NotNull ResponseBody response) {
            Utils.hideProgressDialog();
            if (Utils.getAvailableSpaceInMB() > 300) {
                new WriteAudioFile(response).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.space_not_availabe));
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

    @SuppressLint("StaticFieldLeak")
    public class WriteAudioFile extends AsyncTask<Void, Void, Boolean> {
        private final ResponseBody response;

        WriteAudioFile(ResponseBody response) {
            this.response = response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean writtenToDisk = false;
            File file = new File(Environment.getExternalStorageDirectory() + Constant.AUDIO_FOLDER + audioFileName);
            if (!file.exists()) {
                writtenToDisk = writeAudioResponseBodyToDisk(response);
            }
            return writtenToDisk;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            setupImages();
        }

        private boolean writeAudioResponseBodyToDisk(ResponseBody body) {
            try {
                File file = new File(Environment.getExternalStorageDirectory() + Constant.AUDIO_FOLDER);
                if (!file.exists()) {
                    file.mkdirs();
                }

                file = new File(Environment.getExternalStorageDirectory() + Constant.AUDIO_FOLDER + audioFileName);

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

    @SuppressLint("StaticFieldLeak")
    private class DeleteFile extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            File file = new File(Environment.getExternalStorageDirectory() + "/ShopKirana/Audio");
            if (file.isDirectory()) {
                String[] children = file.list();
                if (children != null)
                    for (String child : children) {
                        if (!child.equals(audioFileName)) {
                            new File(file, child).delete();
                        }
                    }
            }
            return null;
        }
    }
}