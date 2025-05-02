package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.google.firebase.database.DataSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedPostModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PollModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.FavItemsDetails;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NotifyModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;

public class NoteRepository {
    private final String DB_NAME = "sk_central";

    private final NoteDatabase noteDatabase;
    private final Context context;


    public NoteRepository(Context context) {
        this.context = context;
        RoomDatabase.Builder<NoteDatabase> builder = Room.databaseBuilder(context, NoteDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration();
        builder.setQueryCallback((sqlQuery, bindArgs) -> {
            System.out.println("query " + sqlQuery);
        }, command -> {

        });
        noteDatabase = builder.build();
    }

    /*WishList operations*/

    public void insertTask(String title, double price, String password) {
        ItemListModel model = new ItemListModel();
        model.itemname = title;
        model.price = price;
        model.itemname = title;

        insertTask(model);
    }

    public void insertTask(ItemListModel model) {
        noteDatabase.daoAccess().insertTask(new FavItemsDetails(model.getItemId()));
    }

    public void insertTask(ArrayList<ItemListModel> list) {
        for (ItemListModel model : list)
            noteDatabase.daoAccess().insertTask(new FavItemsDetails(model.getItemId()));
    }

    public void updateTask(final ItemListModel model) {
        noteDatabase.daoAccess().updateTask(new FavItemsDetails(model.getItemId()));
    }

    public void deleteTask(final int id) {
        final LiveData<FavItemsDetails> task = getTask(id);
        if (task != null) {
            noteDatabase.daoAccess().deleteTask(task.getValue());
        }
    }

    public void deleteTask(final ItemListModel model) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoAccess().deleteTask(new FavItemsDetails(model.getItemId()));
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public LiveData<FavItemsDetails> getTask(int id) {
        return noteDatabase.daoAccess().getTask(id);
    }

    public LiveData<List<FavItemsDetails>> getTasks() {
        return noteDatabase.daoAccess().fetchAllTasks();
    }

    public boolean isItemWishList(int id) {
        return noteDatabase.daoAccess().isItemExist(id);
    }

    /*Cart operations*/

    public void addToCart(ItemListModel model) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.cartDaoAccess().insertTask(model);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void addToCart(final ArrayList<ItemListModel> list) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.cartDaoAccess().truncateCart();
                noteDatabase.cartDaoAccess().insertTask(list);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void updateCartItem(final ItemListModel model) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.cartDaoAccess().updateTask(model);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void updateCartItem(final ItemListModel model, int qty, int freeItemQuantity) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                model.qty = qty;
                noteDatabase.cartDaoAccess().updateTask(model);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void deleteCartItem(final int id) {
        final LiveData<ItemListModel> task = getCartItem(id);
        if (task != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    noteDatabase.cartDaoAccess().deleteTask(task.getValue());
                    return null;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void deleteCartItem(final ItemListModel model) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.cartDaoAccess().deleteTask(model);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void deleteCartItem1(final int id) {
        noteDatabase.cartDaoAccess().deleteItem(id);
    }

    public ItemListModel getItem(int id) {
        return noteDatabase.cartDaoAccess().getItem(id);
    }

    public List<ItemListModel> getItem(String number) {
        return noteDatabase.cartDaoAccess().getItem(number);
    }

    public LiveData<ItemListModel> getCartItem(int id) {
        return noteDatabase.cartDaoAccess().getCartItem(id);
    }

    public ItemListModel getCartItem1(int id) {
        return noteDatabase.cartDaoAccess().getCartItem1(id);
    }

    public ItemListModel getItemByMrpId(int itemId, int mrpId) {
        return noteDatabase.cartDaoAccess().getItemByMrpId(itemId, mrpId);
    }

    public LiveData<List<ItemListModel>> getCart() {
        return noteDatabase.cartDaoAccess().fetchAllTasks();
    }

    public LiveData<List<ItemListModel>> getCartNonZero() {
        return noteDatabase.cartDaoAccess().getCartNonZero();
    }

    public boolean isItemInCart(int id) {
        return noteDatabase.cartDaoAccess().isItemExist(id);
    }

    public LiveData<Double> getCartValue() {
        return noteDatabase.cartDaoAccess().getCartValue();
    }

    public Double getCartValue1() {
        return noteDatabase.cartDaoAccess().getCartValue1();
    }

    public int getQtyByMultiMrp(int multiMrpId) {
        return noteDatabase.cartDaoAccess().getQtyByMultiMrp(multiMrpId);
    }

    public int getQtyByMultiMrp(int id, int multiMrpId) {
        return noteDatabase.cartDaoAccess().getQtyByMultiMrp(id, multiMrpId);
    }

    public QtyDTO getQtyTotalQtyByMrpId(int id, int multiMrpId) {
        return noteDatabase.cartDaoAccess().getQtyTotalQtyByMrpId(id, multiMrpId);
    }

    public List<ItemListModel> getItemsByMultiMrpId(@NotNull List<Integer> idList) {
        return noteDatabase.cartDaoAccess().getItemsByMultiMrpId(idList);
    }



    public int getCartQtyCount() {
        return noteDatabase.cartDaoAccess().getCartQtyCount();
    }

    public int getCartCount() {
        return noteDatabase.cartDaoAccess().getCartCount();
    }

    public void truncateCart() {
        noteDatabase.cartDaoAccess().truncateCart();
    }

    /*Recent search*/

    public void addToSearch(SearchItemDTO model) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.searchDaoAccess().insertTask(model);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void addToSearch(ArrayList<ItemListModel> list, String query) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!noteDatabase.searchDaoAccess().isItemExist(query)) {
                        noteDatabase.searchDaoAccess().insertTask(new SearchItemDTO(list.get(0).getItemId(),
                                list.get(0).getItemNumber(), list.get(0).itemname, list.get(0).price,
                                list.get(0).getUnitPrice(), list.get(0).getLogoUrl(), list.get(0).marginPoint,
                                list.get(0).dreamPoint, list.get(0).getItemMultiMRPId(), list.get(0).qty, query));
                        int count = noteDatabase.searchDaoAccess().getCount();
                        if (count > 5) {
                            SearchItemDTO model = noteDatabase.searchDaoAccess().fetchTask();
                            noteDatabase.searchDaoAccess().deleteTask(model);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public LiveData<List<SearchItemDTO>> getSearchItem() {
        return noteDatabase.searchDaoAccess().fetchAllTasks();
    }

    public int getSearchCount() {
        return noteDatabase.searchDaoAccess().getCount();
    }

    public void truncateSearch() {
        noteDatabase.searchDaoAccess().truncateTable();
    }


    /* Notify Item*/

    public void insertNotifyItemTask(NotifyModel model) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoNotify().insertTask(model);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public LiveData<NotifyModel> getNotifyTask(int id) {
        return noteDatabase.daoNotify().getTask(id);
    }

    public boolean isNotifyDisable(int id) {
        return noteDatabase.daoNotify().isItemNotify(id);
    }

    public void deleteNotifyTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoNotify().deleteTask();
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /*Language operations*/
    public void insertLangs(String key, String value) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Long i = noteDatabase.daoLangAccess().insertTask(new LangModel(key, value));
                return null;
            }
        }.execute();
    }

    public void insertLangs(ArrayList<LangModel> list) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoLangAccess().insertTask(list);
                return null;
            }
        }.execute();
    }

    public String getData(String key) {
        String value = "";
        try {
            value = noteDatabase.daoLangAccess().getString(key);
            if (TextUtils.isNullOrEmpty(value)) {
                int resId = MyApplication.getInstance().getResources().getIdentifier(key, "string", MyApplication.getInstance().getPackageName());
                value = MyApplication.getInstance().getResources().getString(resId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            int resId = MyApplication.getInstance().getResources().getIdentifier(key, "string", MyApplication.getInstance().getPackageName());
            value = MyApplication.getInstance().getResources().getString(resId);
        }
        return value;
    }

    public String getString(int key) {
        String value = "";
        try {
            String stringKey = MyApplication.getInstance().getResources().getResourceEntryName(key);
            value = noteDatabase.daoLangAccess().getString(stringKey);
            if (TextUtils.isNullOrEmpty(value)) {
                value = MyApplication.getInstance().getResources().getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            value = MyApplication.getInstance().getResources().getString(key);
        }
        return value;
    }


    public void deleteAndUpdateTable(DataSnapshot dataPostSnapshot) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoLangAccess().truncateLangTable();
                for (DataSnapshot postSnapshot : dataPostSnapshot.getChildren()) {
                    String selectedLanguage = SharePrefs.getInstance(MyApplication.getInstance()).getString(SharePrefs.SELECTED_LANGUAGE);
                    if (selectedLanguage.equals(postSnapshot.getKey())) {
                        ArrayList<LangModel> list = new ArrayList<>();
                        for (DataSnapshot langSnapshot : postSnapshot.getChildren()) {
                            list.add(new LangModel(langSnapshot.getKey(), langSnapshot.getValue() + ""));
                        }
                        insertLangs(list);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                System.out.println("db updated");
                Utils.hideProgressDialog();
                try {
                Utils.setLocale(MyApplication.getInstance().activity, LocaleHelper.getLanguage(MyApplication.getInstance().activity));
                LocaleHelper.setLocale(MyApplication.getInstance().activity, LocaleHelper.getLanguage(MyApplication.getInstance().activity));
                MyApplication.getInstance().activity.startActivity(new Intent(MyApplication.getInstance().activity, HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    MyApplication.getInstance().activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public boolean isTableEmpty() {
        return noteDatabase.daoLangAccess().getCount() == 0;
    }

    public void truncateLangTable() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoLangAccess().truncateLangTable();
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    // Community
    public void insertFeed(FeedPostModel model) {
        noteDatabase.daoFeedAccess().insertTask(model);
    }

    public void insertFeed(ArrayList<FeedPostModel> list) {
        noteDatabase.daoFeedAccess().insertTask(list);
    }

    public LiveData<List<FeedPostModel>> fetchAllFeeds() {
        return noteDatabase.daoFeedAccess().fetchAllFeeds();
    }

    public LiveData<List<FeedPostModel>> fetchProfileFeeds(String customerID) {
        return noteDatabase.daoFeedAccess().fetchProfileFeeds(customerID);
    }

    public void updateFeedPoll(String postId, Boolean isPollAnswerGiven, ArrayList<PollModel> pollList) {
        noteDatabase.daoFeedAccess().updateFeedPoll(postId, isPollAnswerGiven, pollList);
    }

    public void updateLike(String postId, int likeStatus, int likeCount) {
        noteDatabase.daoFeedAccess().updateUserLike(postId, likeStatus, likeCount);
    }

    public void followUpdate(int customerID, boolean isFollow) {
        noteDatabase.daoFeedAccess().updateFollowUser(customerID, isFollow);
    }

    public LiveData<List<FeedPostModel>> fetchNotifctinFeedsontop(String customeriD, String postID) {
        return noteDatabase.daoFeedAccess().fetchNotictionFeedsOnTop(customeriD, postID);
    }

    public void updateCommentCount(String postId, int count) {
        noteDatabase.daoFeedAccess().updateCommentCount(postId, count);
    }

    public void deletePost(String postId) {
        noteDatabase.daoFeedAccess().deletePost(postId);
    }

    public void deleteFeed() {
        noteDatabase.daoFeedAccess().deleteFeed();
    }

    public int getCommentCount(@NotNull String postId) {
        return noteDatabase.daoFeedAccess().getCommentCount(postId);
    }
}