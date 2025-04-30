package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.murli;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import java.io.File;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityHomeBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;

/**
 * Show/Hide Murli animation, audio file change gif images from this class
 */
public class ShowMurli implements AnimationListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private final HomeActivity activity;
    private final ActivityHomeBinding mBinding;
    private final MediaPlayer player;
    // listener for mic
    private Visualizer visualizer;
    private GifDrawable gifDrawable;
    private boolean isAudioFinished = false;


    public ShowMurli(HomeActivity activity, ActivityHomeBinding mBinding) {
        this.activity = activity;
        this.mBinding = mBinding;

        mBinding.toolbarH.liMurli.getRoot().setVisibility(View.VISIBLE);

        setMurliImage(1, Constant.IMAGE_FILE_NAME1);
        setMurliImage(2, Constant.IMAGE_FILE_NAME2);

        gifDrawable = (GifDrawable) mBinding.toolbarH.liMurli.murliSidewalkView.getDrawable();
        gifDrawable.addAnimationListener(this);

        player = new MediaPlayer();

        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        int level = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2;
        if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < level) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, level, 0);
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
        setMurliImage(3, Constant.IMAGE_FILE_NAME3);
    }

    @Override
    public void onAnimationCompleted(int loopNumber) {
        if (isAudioFinished) {
            mBinding.toolbarH.liMurli.getRoot().setVisibility(View.INVISIBLE);
            mBinding.toolbarH.btnMurli1.setEnabled(true);
            mBinding.toolbarH.btnMurli1.setClickable(true);
            new DeleteFile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            activity.closeMurli();
        } else {
            mBinding.toolbarH.fabClose.show();
            if (player != null)
                playAudio();
            isAudioFinished = true;
        }
    }

    public void onResume() {
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

    public void onPause() {
        try {
            if (player != null && player.isPlaying()) {
                player.pause();
                visualizer.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        mBinding.toolbarH.fabClose.hide();
        mBinding.toolbarH.liMurli.murliSpeakingView.setVisibility(View.INVISIBLE);
        try {
            if (visualizer != null) {
                visualizer.release();
                visualizer = null;
            }
            if (player != null) {
                player.release();
            }
            gifDrawable.removeAnimationListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMurliImage(int pos, String image) {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + Constant.IMAGE_FOLDER + image);
            if (file.exists()) {
                if (pos == 1) {
                    mBinding.toolbarH.liMurli.murliSidewalkView.setImageURI(Uri.fromFile(file));
                }
                if (pos == 2) {
                    mBinding.toolbarH.liMurli.murliSpeakingView.setImageURI(Uri.fromFile(file));
                }
                if (pos == 3) {
                    gifDrawable.removeAnimationListener(this);
                    mBinding.toolbarH.liMurli.murliSidewalkView.setImageURI(Uri.fromFile(file));
                    gifDrawable = (GifDrawable) mBinding.toolbarH.liMurli.murliSidewalkView.getDrawable();
                    gifDrawable.addAnimationListener(this);
                }
            } else {
                if (pos == 1) {
                    mBinding.toolbarH.liMurli.murliSidewalkView.setImageResource(R.drawable.murli_side_walk);
                }
                if (pos == 2) {
                    mBinding.toolbarH.liMurli.murliSpeakingView.setImageResource(R.drawable.murli_speak);
                }
                if (pos == 3) {
                    gifDrawable.removeAnimationListener(this);
                    mBinding.toolbarH.liMurli.murliSidewalkView.setImageResource(R.drawable.murli_hi);
                    gifDrawable = (GifDrawable) mBinding.toolbarH.liMurli.murliSidewalkView.getDrawable();
                    gifDrawable.addAnimationListener(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playAudio() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + Constant.AUDIO_FOLDER + Constant.AUDIO_FILE_NAME);
        if (file.exists()) {
            try {
                if (player != null) {
                    player.setDataSource(file.getAbsolutePath());
                    player.prepare();
                    player.setOnPreparedListener(this);
                    player.setOnCompletionListener(this);
                    // for get waves frequency of audio
                    visualizerController(player.getAudioSessionId());
                }
            } catch (Exception e) {
                e.printStackTrace();
                mBinding.toolbarH.liMurli.getRoot().setVisibility(View.INVISIBLE);
                mBinding.toolbarH.fabClose.hide();
            }
        } else {
            mBinding.toolbarH.liMurli.getRoot().setVisibility(View.INVISIBLE);
            mBinding.toolbarH.fabClose.hide();
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
                    mBinding.toolbarH.liMurli.murliSpeakingView.setVisibility(View.INVISIBLE);
                } else {
                    mBinding.toolbarH.liMurli.murliSpeakingView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {

            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
        visualizer.setEnabled(true);
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteFile extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + Constant.AUDIO_FOLDER);
            if (file.isDirectory()) {
                String[] children = file.list();
                if (children != null)
                    for (String child : children) {
                        if (!child.equals(Constant.AUDIO_FILE_NAME)) {
                            new File(file, child).delete();
                        }
                    }
            }
            return null;
        }
    }
}