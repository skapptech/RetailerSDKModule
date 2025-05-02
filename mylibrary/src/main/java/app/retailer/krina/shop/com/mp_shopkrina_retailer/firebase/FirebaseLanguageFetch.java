package app.retailer.krina.shop.com.mp_shopkrina_retailer.firebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db.LangModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;

public class FirebaseLanguageFetch {
    private final Context context;

    public FirebaseLanguageFetch(Context context) {
        this.context = context;
    }

    public void fetchLanguage() {
        try {
            new UpdateLanguageAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateLanguageAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            RetailerSDKApp.getInstance().dbHelper.truncateLangTable();
            DatabaseReference language = database.getReference();
            language.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.e("Count-", dataSnapshot.getChildrenCount() + "");
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String selectedLanguage = SharePrefs.getInstance(context).getString(SharePrefs.SELECTED_LANGUAGE);
                        if (selectedLanguage == null || selectedLanguage.equals("")) {
                            LocaleHelper.setLocale(context, "hi");
                            SharePrefs.getInstance(context).putString(SharePrefs.SELECTED_LANGUAGE, "हिन्दी");
                        }
                        if (selectedLanguage.equals(postSnapshot.getKey())) {
                            ArrayList<LangModel> list = new ArrayList<>();
                            for (DataSnapshot langSnapshot : postSnapshot.getChildren()) {
                                list.add(new LangModel(langSnapshot.getKey(), langSnapshot.getValue() + ""));
                            }
                            RetailerSDKApp.getInstance().noteRepository.insertLangs(list);
                        }
                    }
                    SharePrefs.getInstance(context).putBoolean(SharePrefs.IS_FETCH_LANGUAGE, false);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}