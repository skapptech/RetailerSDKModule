package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.orderdetail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityTrackOrderBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ContactUsActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.TrackOrderAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.DBoyTrackerModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.TrackOrderModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.UploadAudioModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DirectionsJSONParser;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class TrackOrderActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityTrackOrderBinding mBinding;

    private TrackOrderActivity activity;
    private LinearLayout liTapHold, liSendInstructions, liRateUI, liRateDirect, liNotNow, liCall, liShowHide;
    private EditText etInstruction;
    private ImageView imPlayStopIcon, imDeliveryBoy;
    private TextView tvSendInstructions, tvTapRecord, tvDeliveryBoy, tvDeliveryAddress, tvDeliveryBoyRating;
    private RelativeLayout rlHelp;
    private Chronometer chronometer;
    private SeekBar seekbarAudio;
    private RecyclerView rvOrderList;
    private BottomSheetBehavior sheetBehavior;

    private GoogleMap googleMapMain;
    private MediaRecorder mRecorder = null;
    private String mFileNamePath = null;
    private String mFileName = null;
    private LatLng dboyLatLong;
    private Marker marker = null;
    private int tripId;
    private LatLng latLng;
    private boolean recording = false;
    public String storageConnectionString = "";

    private double current_pos;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private String audioFileName;
    private String dboyNumber = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityTrackOrderBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(RetailerSDKApp.getInstance().dbHelper.getString(R.string.track_order));

        activity = this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LinearLayout layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initialization();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        } else
            super.onBackPressed();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMapMain = googleMap;
        googleMapMain.getUiSettings().setMyLocationButtonEnabled(true);
        CommonClassForAPI.getInstance(this).getTripDetails(tripObserver, tripId,
                SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CUSTOMER_ID));
    }


    public void initialization() {
        storageConnectionString = "DefaultEndpointsProtocol=https;" +
                "AccountName=" + SharePrefs.getInstance(this).getString(SharePrefs.AZURE_ACCOUNT_NAME) + ";" +
                "AccountKey=" + SharePrefs.getInstance(this).getString(SharePrefs.AZURE_ACCOUNT_KEY);
        mediaPlayer = new MediaPlayer();
        tripId = getIntent().getIntExtra("id", 0);

        callRunTimePermissions();

        liTapHold = findViewById(R.id.liTapHold);
        liSendInstructions = findViewById(R.id.liSendInstructions);
        liRateUI = findViewById(R.id.liRateUI);
        liRateDirect = findViewById(R.id.liRateDirect);
        liTapHold = findViewById(R.id.liTapHold);
        liNotNow = findViewById(R.id.liNotNow);
        liCall = findViewById(R.id.liCall);
        liShowHide = findViewById(R.id.liShowHide);
        etInstruction = findViewById(R.id.etInstruction);
        imPlayStopIcon = findViewById(R.id.imPlayStopIcon);
        imDeliveryBoy = findViewById(R.id.imDeliveryBoy);
        tvSendInstructions = findViewById(R.id.tvSendInstructions);
        tvTapRecord = findViewById(R.id.tvTapRecord);
        tvDeliveryBoy = findViewById(R.id.tvDeliveryBoy);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        tvDeliveryBoyRating = findViewById(R.id.tvDeliveryBoyRating);
        rlHelp = findViewById(R.id.rlHelp);
        chronometer = findViewById(R.id.chronometer);
        seekbarAudio = findViewById(R.id.seekbarAudio);
        rvOrderList = findViewById(R.id.rvOrderList);

        liTapHold.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (recording == false) {
                        recording = true;
                        tvTapRecord.setText("Recording message...Click to Stop");
                        startRecording();
                        chronometer.setVisibility(View.VISIBLE);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                        liShowHide.setVisibility(View.GONE);
                        if (isPlaying) {
                            stopPlaying();
                        }
                    } else {
                        recording = false;
                        tvTapRecord.setText("Tap to Record");
                        stopRecording();
                        chronometer.setVisibility(View.GONE);
                        liShowHide.setVisibility(View.VISIBLE);
                        chronometer.stop();
                    }
                } else {
                    callRunTimePermissions();
                }
            }
        });
        seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.out.println("progress" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                mediaPlayer.seekTo((int) current_pos);
            }
        });
        seekbarAudio.setOnTouchListener((view, motionEvent) -> true);
        imPlayStopIcon.setOnClickListener(view -> {
            if (isPlaying) {
                stopPlaying();
            } else {
                playAudio();
            }
        });
        liSendInstructions.setOnClickListener(view -> {
            if (mFileNamePath == null || mFileNamePath.equals("")) {
                Utils.setToast(getApplicationContext(), "Please Record Instructions");
            } else {
                uploadAudioFile();
            }
        });
        liRateDirect.setOnClickListener(view -> Utils.rateApp(this));
        liNotNow.setOnClickListener(view -> liRateUI.setVisibility(View.GONE));
        liCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + dboyNumber));
            startActivity(intent);
        });
        rlHelp.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ContactUsActivity.class)
                    .putExtra("Type", "ContactUsActivity"));
            Utils.fadeTransaction(activity);
        });
    }

    public void stopPlaying() {
        if (mediaPlayer != null) {
            imPlayStopIcon.setImageResource(R.drawable.ic_round_play_arrow_24);
            seekbarAudio.setProgress(0);
            current_pos = 0;
            mediaPlayer.seekTo((int) current_pos);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            isPlaying = false;
        }
    }

    public void playAudio() {
        try {
            isPlaying = true;
            current_pos = 0;
            mediaPlayer = new MediaPlayer();
            //mediaPlayer.reset();
            mediaPlayer.setDataSource(this, Uri.fromFile(new File(mFileNamePath)));
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.seekTo((int) current_pos);
            seekbarAudio.setProgress(0);
            imPlayStopIcon.setImageResource(R.drawable.ic_round_stop_24);
            mediaPlayer.setOnCompletionListener(mp -> stopPlaying());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAudioProgress();
    }

    public void setAudioProgress() {
        current_pos = mediaPlayer.getCurrentPosition();
        double total_duration = mediaPlayer.getDuration();

        seekbarAudio.setMax((int) total_duration);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = mediaPlayer.getCurrentPosition();
                    seekbarAudio.setProgress((int) current_pos);
                    handler.postDelayed(this, 10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 10);
    }

    public void callRunTimePermissions() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                boolean permissionResult = true;
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                boolean permissionResult = false;
            }
        });
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mFileNamePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File rootDirectoryMain = new File(Environment.getExternalStorageDirectory() + "/Download");
        if (!rootDirectoryMain.exists()) {
            rootDirectoryMain.mkdirs();
        }
        mFileName = SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CUSTOMER_ID) +
                "_" + System.currentTimeMillis() + "_recordedAudio.mp3";
        File file = new File(rootDirectoryMain, mFileName);

        mFileNamePath = file.getAbsolutePath();
        mRecorder.setOutputFile(mFileNamePath);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("LOG_TAG", "failed");
        }
    }

    private void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            Log.e("LOG_TAG", "stop Recording");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadAudioFile() {
        Utils.showProgressDialog(activity);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
                    CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
                    CloudBlobContainer container = blobClient.getContainerReference("mycontainer");
                    container.createIfNotExists();
                    BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
                    containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
                    container.uploadPermissions(containerPermissions);
                    File source = new File(mFileNamePath);
                    System.out.println("FILE NAME>>>" + "fProfile");
                    CloudBlockBlob blob = container.getBlockBlobReference(mFileName);
                    blob.upload(new FileInputStream(source), source.length());
                    System.out.println("Upload done>>");
                    CloudBlob cloudBlob = container.getBlobReferenceFromServer(mFileName);
                    System.out.println("StorageFile>>>>" + cloudBlob.getUri() + " " + cloudBlob.getName());
                    audioFileName = cloudBlob.getUri().toString();
                    callUploadAudio();
                } catch (Exception e) {
                    System.out.println("Exception>>" + e);
                    e.printStackTrace();
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void callUploadAudio() {
        UploadAudioModel uploadAudioModel = new UploadAudioModel(
                tripId, SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CUSTOMER_ID),
                audioFileName, etInstruction.getText().toString());
        CommonClassForAPI.getInstance(this).uploadAudioFileApi(uploadFileObserver, uploadAudioModel);
    }

    public void getDataUsingfireStore(TrackOrderModel trackOrderModel) {
        /*FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:911318288862:web:6b969a42e142129c88b54f") // Required for Analytics.
                .setApiKey("AIzaSyByLRWMQFd4AK_iYQJjNsV6kfLUI7LuOa8") // Required for Auth.
                .setDatabaseUrl("https://dboytracker-default-rtdb.firebaseio.com") // Required for RTDB.
                .setProjectId("dboytracker")
                .build();*/
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(SharePrefs.getInstance(this).getString(SharePrefs.FIRESTORE_APPLICATION_ID)) // Required for Analytics.
                .setApiKey(SharePrefs.getInstance(this).getString(SharePrefs.FIRESTORE_API_KEY)) // Required for Auth.
                .setDatabaseUrl(SharePrefs.getInstance(this).getString(SharePrefs.FIRESTORE_DATABASE_URL)) // Required for RTDB.
                .setProjectId(SharePrefs.getInstance(this).getString(SharePrefs.FIRESTORE_PROJECT_ID))
                .build();
        try {
            FirebaseApp.initializeApp(this, options, "secondary");
        } catch (Exception e) {
            e.printStackTrace();
        }
        FirebaseApp app = FirebaseApp.getInstance("secondary");
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance(app);
        firestoreDB.collection(trackOrderModel.collectionName)
                .whereEqualTo("TripPlannerVehicleId", trackOrderModel.getTripPlannerVehicleId())
                .orderBy("RecordTime", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w("TAG>>>>", "Listen failed.", e);
                        return;
                    }

                    List<DBoyTrackerModel> eventList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {
                        DBoyTrackerModel model = doc.toObject(DBoyTrackerModel.class);
                        eventList.add(model);
                        Log.d("TAG>>>>", "RecordTime: " + model.getRecordTime());
                        googleMapMain.clear();
                        dboyLatLong = new LatLng(model.getLat(), model.getLng());
                        Log.d("TAG>>>>", "Lat Long" + dboyLatLong);

                        if (marker == null) {
                            googleMapMain.moveCamera(CameraUpdateFactory.newLatLng(dboyLatLong));
                            googleMapMain.animateCamera(CameraUpdateFactory.zoomTo(10));
                        } else {
                            googleMapMain.moveCamera(CameraUpdateFactory.newLatLng(dboyLatLong));
                            googleMapMain.animateCamera(CameraUpdateFactory.zoomTo(16));
                        }

                        Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.delivery_truck);
                        markerBitmap = scaleBitmap(markerBitmap, 100, 100, "");
                        marker = googleMapMain.addMarker(new MarkerOptions().position(dboyLatLong)
                                .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap)));
                        Bitmap markerBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.store);
                        markerBitmap1 = scaleBitmap(markerBitmap1, 100, 100, "");
                        googleMapMain.addMarker(new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap1)));
                        marker.setPosition(dboyLatLong);
                    }
                    try {
                        drawRoute(dboyLatLong, latLng);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight, String skcode) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.WHITE);
        canvas.drawText(skcode, 30, 40, color);

        return scaledBitmap;
    }


    public void drawRoute(LatLng origin, LatLng dest) {
        String url = getUrl(origin, dest);
        new FetchUrl().execute(url);
    }

    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin;
        if (origin == null)
            str_origin = "origin=" + 0.0 + "," + 0.0;
        else
            str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=true";
        String wayPoints = "";
        String parameters = "";
        parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json" + "?" + parameters
                + "&key=" + getResources().getString(R.string.google_maps_key);

        Log.e("WAY_URLLL", url);
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                inputStream.close();
            urlConnection.disconnect();
        }

        Log.d("data downlaod", data);
        return data;
    }


    DisposableObserver<TrackOrderModel> tripObserver = new DisposableObserver<TrackOrderModel>() {
        @Override
        public void onNext(@NonNull TrackOrderModel model) {
            Utils.hideProgressDialog();
            latLng = new LatLng(model.getLat(), model.getLng());
            if (googleMapMain != null) {
                rvOrderList.setAdapter(new TrackOrderAdapter(activity, model.getTrackOrdersDetails()));
                tvDeliveryBoy.setText(model.getDBoyName());
                tvDeliveryAddress.setText(model.getShippingAddress());
                tvDeliveryBoyRating.setText(model.getDeliveryBoyRating() == null ? "" : "" + model.getDeliveryBoyRating());

                Picasso.get().load(EndPointPref.getInstance(getApplicationContext())
                        .getString(EndPointPref.API_ENDPOINT) + model.getDboyProfilePic())
                        .placeholder(R.drawable.logo_grey)
                        .error(R.drawable.logo_grey)
                        .into(imDeliveryBoy);
                dboyNumber = model.getDboyMobile();
                try {
                    getDataUsingfireStore(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    DisposableObserver<Boolean> uploadFileObserver = new DisposableObserver<Boolean>() {
        @Override
        public void onNext(@NonNull Boolean data) {
            Utils.hideProgressDialog();
            if (data) {
                Utils.setToast(getApplicationContext(), "Instruction Sent to Delivery Boy");
                liShowHide.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Utils.hideProgressDialog();
        }

        @Override
        public void onComplete() {

        }
    };


    @SuppressWarnings("deprecation")
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                data = downloadUrl(url[0]);
                Log.e("Background Task data", data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    @SuppressWarnings("deprecation")
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            //  ArrayList<LatLng> points;
            Utils.hideProgressDialog();
            PolylineOptions lineOptions = null;

            Log.d("ParserTask123", result.toString());

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                ArrayList<LatLng> points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(getResources().getColor(R.color.black));
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                Polyline polyline = googleMapMain.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }
}