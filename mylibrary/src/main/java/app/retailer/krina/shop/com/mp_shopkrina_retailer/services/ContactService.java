package app.retailer.krina.shop.com.mp_shopkrina_retailer.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ContactUploadModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import io.reactivex.observers.DisposableObserver;

public class ContactService extends IntentService {


    public ContactService() {
        super("");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                new FetchContact().execute();
            }
        } else {
            new FetchContact().execute();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class FetchContact extends AsyncTask<String, Void, String> {
        private Cursor cursor;
        private ArrayList<ContactUploadModel> contactList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ContentResolver contentResolver = getApplicationContext().getContentResolver();

            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER;
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
            cursor = contentResolver.query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.Contacts._ID}, selection, null, sortOrder);
        }

        @Override
        protected String doInBackground(String... strings) {
            String contactName, phoneNumber;
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    contactList.add(new ContactUploadModel(SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CUSTOMER_ID), contactName, phoneNumber));
                }
            }
            return null;
        }

        private ArrayList<ContactUploadModel> clearListFromDuplicateFirstName(List<ContactUploadModel> list1) {
            Map<String, ContactUploadModel> cleanMap = new LinkedHashMap<>();
            for (int i = 0; i < list1.size(); i++) {
                cleanMap.put(list1.get(i).getContact(), list1.get(i));
            }
            return new ArrayList<>(cleanMap.values());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (contactList != null && contactList.size() > 0) {
                    contactList = clearListFromDuplicateFirstName(contactList);
                    CommonClassForAPI.getInstance(RetailerSDKApp.getInstance().activity).uploadContacts(observer, contactList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private final DisposableObserver<JsonElement> observer = new DisposableObserver<JsonElement>() {
        @Override
        public void onNext(@NotNull JsonElement response) {

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