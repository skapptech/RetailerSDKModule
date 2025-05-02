package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedPostModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.FavItemsDetails;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NotifyModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DataConverterImage;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DataConverterPoll;

@Database(entities = {FavItemsDetails.class, ItemListModel.class, SearchItemDTO.class, NotifyModel.class, LangModel.class, FeedPostModel.class}, version  = 1, exportSchema = false)
@TypeConverters({DataConverterPoll.class, DataConverterImage.class})
public abstract class NoteDatabase extends RoomDatabase {

    public abstract WishListDaoAccess daoAccess();

    public abstract CartDaoAccess cartDaoAccess();

    public abstract SearchDaoAccess searchDaoAccess();

    public abstract DaoNotify daoNotify();

    public abstract LangDaoAccess daoLangAccess();

    public abstract FeedDao daoFeedAccess();
}