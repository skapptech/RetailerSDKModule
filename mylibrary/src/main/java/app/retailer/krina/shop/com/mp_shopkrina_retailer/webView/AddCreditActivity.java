package app.retailer.krina.shop.com.mp_shopkrina_retailer.webView;

import static com.google.firebase.messaging.Constants.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityAddCreditBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.HKImageAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HisabDetailModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HisabKitabImageModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HisabKitabNotesModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MarshmallowPermissions;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddCreditActivity extends AppCompatActivity implements View.OnClickListener {
    public final int CAPTURE_IMAGE_CAMERA = 1111;
    public final int CAPTURE_IMAGE_LIBRARY = 2222;
    public final int REQUEST_IMAGE = 100;

    private ActivityAddCreditBinding mBinding;
    private CommonClassForAPI commonClassForAPI;
    private Utils utils;
    private CustomerRes customersContactModel;
    private String GiveAccept = "Give";
    private String userImageUpload, uploadFilePath;
    private ArrayList<HisabKitabImageModel> irImagesModels;
    private HKImageAdapter hkImageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(AddCreditActivity.this, R.layout.activity_add_credit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            customersContactModel = (CustomerRes) getIntent().getSerializableExtra("list");
            GiveAccept = getIntent().getStringExtra("GiveAccept");
        }
        // init view
        initialization();

        mBinding.toolbarDream.back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_give_creadit:
                givePayment();
                break;
            case R.id.rl_credit_date:
                clickDOB();
                break;

            case R.id.iv_send_message:
                sendMessageUser();
                break;

            case R.id.iv_camera:
                openImageOptionsBottomMenu();
                break;
        }
    }


    private void initialization() {
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        utils = new Utils(this);

        irImagesModels = new ArrayList<>();
        mBinding.toolbarDream.tvUserName.setText(customersContactModel.getName() + "");
        mBinding.toolbarDream.tvUserNumber.setText(customersContactModel.getMobile() + "");

        if (GiveAccept.equals("Give")) {
            mBinding.etAmount.setHint(getResources().getString(R.string.add_credit));
        } else {
            mBinding.etAmount.setHint(getResources().getString(R.string.add_payment));
        }

        mBinding.rvImageList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBinding.rvImageList.setHasFixedSize(true);

        hkImageAdapter = new HKImageAdapter(AddCreditActivity.this, irImagesModels);
        mBinding.rvImageList.setAdapter(hkImageAdapter);

        mBinding.rlCreditDate.setOnClickListener(this);
        mBinding.ivSendMessage.setOnClickListener(this);
        mBinding.ivCamera.setOnClickListener(this);
        Date d = new Date();
        CharSequence currentDate = DateFormat.format("MMM d, yyyy ", d.getTime());
        mBinding.tvDateCredit.setText(currentDate);
        mBinding.etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length() > 2) {
                    mBinding.etAmount.setText(text.substring(0, text.length() - 1));
                    mBinding.etAmount.setSelection(mBinding.etAmount.getText().length());
                }
                eventCall(s);
            }
        });
    }

    private void eventCall(CharSequence charSequence) {
        if (charSequence.length() > 0) {
            mBinding.viewDivider.setVisibility(View.VISIBLE);
            mBinding.rlCreditDate.setVisibility(View.VISIBLE);
            mBinding.rlAddNoteChatView.setVisibility(View.VISIBLE);
        } else {
            mBinding.viewDivider.setVisibility(View.GONE);
            mBinding.rlCreditDate.setVisibility(View.GONE);
            mBinding.rlAddNoteChatView.setVisibility(View.GONE);
        }
    }

    public void openImageOptionsBottomMenu() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomTheme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        dialog.setContentView(view);

        LinearLayout cameraLL = dialog.findViewById(R.id.ll_menu_camera);
        LinearLayout galleryLL = dialog.findViewById(R.id.ll_menu_gallery);

        cameraLL.setOnClickListener(v -> {
            callForImage(CAPTURE_IMAGE_CAMERA);
            dialog.dismiss();
        });

        galleryLL.setOnClickListener(v -> {
            callForImage(CAPTURE_IMAGE_LIBRARY);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void sendMessageUser() {
        String addCreditEt = mBinding.etAmount.getText().toString().trim();
        String addNoteEt = mBinding.etAddNote.getText().toString().trim();
        String addDate = mBinding.tvDateCredit.getText().toString().trim();

        if (Double.parseDouble(addCreditEt) < 1) {
            Utils.setToast(this, getResources().getString(R.string.amount_validation));
        } else {
            ArrayList<HisabKitabNotesModel> hisabKitabNotesList = new ArrayList<>();
            HisabKitabNotesModel hisabKitabNotesModel = new HisabKitabNotesModel(
                    SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID),
                    SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID) + "",
                    addNoteEt);
            hisabKitabNotesList.add(hisabKitabNotesModel);

            HisabDetailModel hisabDetailModel;
            if (GiveAccept.equals("Give")) {
                hisabDetailModel = new HisabDetailModel(
                        SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID),
                        SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID),
                        customersContactModel.getId(),
                        addCreditEt, addDate, hisabKitabNotesList, irImagesModels, false);
            } else {
                hisabDetailModel = new HisabDetailModel(
                        SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID),
                        customersContactModel.getId(),
                        SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID),
                        addCreditEt, addDate, hisabKitabNotesList, irImagesModels, true);
            }

            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    Utils.showProgressDialog(this);
                    commonClassForAPI.AddHisabKitab(GetResponse, hisabDetailModel);
                }
            } else {
                Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
            }
        }
    }

    private void givePayment() {
        startActivity(new Intent(this, AddCreditActivity.class));
        Utils.leftTransaction(this);

    }

    private void clickDOB() {
        try {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new mDateSetListenerForDob(), mYear, mMonth, mDay);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog1, which) -> {

            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class mDateSetListenerForDob implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // TODO Auto-generated method stub
            // getCalender();
            try {

                String date = String.valueOf(new StringBuilder()
                        // Month is 0 based so add 1
                        .append(day).append("/").append(month + 1).append("/")
                        .append(year).append(" "));
                int monthStart = month + 1;
                String sCustDob = Utils.getSimpleDateFormat(date);
                mBinding.tvDateCredit.setText(Utils.getChangeDateFormatInProfile(sCustDob));
            }
                /*etCustDob.setText(new StringBuilder()
                        // Month is 0 based so add 1
                        .append(day).append("/").append(month + 1).append("/")
                        .append(year).append(" "));

                int monthStart = month + 1;
                sCustDob = Utils.getSimpleDateFormat(etCustDob.getText().toString().trim());
            */ catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void callForImage(int imageResource) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (MarshmallowPermissions.checkPermissionCamera(this)) {
                    if (MarshmallowPermissions.checkPermissionWriteExternalStorage(this)) {
                        if (imageResource == CAPTURE_IMAGE_LIBRARY) {
                            pickFromGallery();
                        } else if (imageResource == CAPTURE_IMAGE_CAMERA) {
                            pickFromCamera();
                        }
                    } else {
                        MarshmallowPermissions.requestPermissionWriteExternalStorage(this, MarshmallowPermissions.PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    MarshmallowPermissions.requestPermissionCameraAndWriteExternalStorage(this, MarshmallowPermissions.PERMISSION_REQUEST_CODE_CAMERA_AND_STORAGE);
                }
            } else {
                if (imageResource == CAPTURE_IMAGE_LIBRARY) {
                    pickFromGallery();
                } else if (imageResource == CAPTURE_IMAGE_CAMERA) {
                    pickFromCamera();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, CAPTURE_IMAGE_LIBRARY);
    }

    public void pickFromCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (pictureIntent.resolveActivity(AddCreditActivity.this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
//        }
    }

    private File createImageFile() {
        long tsLong = System.currentTimeMillis() / 1000;
        userImageUpload = "user_image" + customersContactModel.getCustId() + "_" + tsLong + ".jpg";
        File myDir = new File(Environment.getExternalStorageDirectory() + "/ShopKirana");
        myDir.mkdirs();
        File file = new File(myDir, userImageUpload);
        userImageUpload = file.getAbsolutePath();

        return file;
    }

    public String SavedImages(Bitmap bm) {
        long tsLong = System.currentTimeMillis() / 1000;
        userImageUpload = "user_image" + customersContactModel.getCustId() + "_" + tsLong + ".jpg";

        String root = Environment.getExternalStorageDirectory().toString();
        Uri imageUri = null;
        OutputStream fos;
        String directory = Environment.DIRECTORY_PICTURES;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            try{
                ContentResolver resolver = this.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, userImageUpload);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, directory);
                imageUri=resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(imageUri);
                boolean saved = bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            Log.d(TAG, "SavedImages:"+ root+ File.separator+directory+ File.separator + userImageUpload);
            return root+ File.separator+directory+ File.separator + userImageUpload;
        }
        else {
            File myDir = new File(Environment.getExternalStorageDirectory() + "/ShopKirana");
            myDir.mkdirs();
            File file = new File(myDir, userImageUpload);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                uploadFilePath = root + "/ShopKirana/" + userImageUpload;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return root + "/ShopKirana/" + userImageUpload;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == CAPTURE_IMAGE_LIBRARY && resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                uploadFilePath = SavedImages(bm);
                //showImageView.setImageBitmap(bm);
                uploadMultipart(uploadFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            //showImageView.setImageURI(Uri.parse(imageFilePath));
            uploadMultipart(userImageUpload);
        }
    }

    private void uploadMultipart(String uploadDocumentFilePath) {
        final File fileToUpload = new File(uploadDocumentFilePath);
        Disposable disposable = new Compressor(this)
                .compressToFileAsFlowable(fileToUpload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::uploadMultipart, throwable -> {
                    throwable.printStackTrace();
                    Toast.makeText(getApplicationContext(), "bhagwan" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadMultipart(File file) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Utils.showProgressDialog(this);
        commonClassForAPI.uploadHisabKitabImage(imageObserver, body);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }


    private final DisposableObserver<HisabDetailModel> GetResponse = new DisposableObserver<HisabDetailModel>() {

        @Override
        public void onNext(@NotNull HisabDetailModel hisabDetailModel) {
            Utils.hideProgressDialog();
            try {
                if (hisabDetailModel != null) {
                    Intent intent = new Intent();
                    intent.putExtra("list", hisabDetailModel);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };

    // upload image
    private final DisposableObserver<String> imageObserver = new DisposableObserver<String>() {
        @Override
        public void onNext(@NotNull String response) {
            try {
                Utils.hideProgressDialog();
                if (response != null) {
                    Log.e("Success", "Bhagwan  " + response);
                    String str1 = EndPointPref.getInstance(MyApplication.getInstance()).getTradeEndpoint();
                    str1 = str1.concat(response);
                    irImagesModels.add(new HisabKitabImageModel(SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.HISAB_KITAB_ID), str1));
                    hkImageAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Image Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };
}
