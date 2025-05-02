package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FavItemsBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db.QtyDTO;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.FavouriteActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CustomRunnable;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RxBus;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by user on 5/4/2017.
 */
@SuppressLint("SetTextI18n")
public class FavItemListAdapter extends RecyclerView.Adapter<FavItemListAdapter.ViewHolder> implements OnItemClick {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ArrayList<ItemListModel> list;
    private final FavouriteActivity activity;
    private boolean flashOfferFlag = false;
    private CustomRunnable customRunnable;
    private GifImageView progressBar;


    public FavItemListAdapter(FavouriteActivity activity, ArrayList<ItemListModel> list) {
        this.list = list;
        this.activity = activity;
    }

    public void setFavItemList(ArrayList<ItemListModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.fav_items, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ItemListModel model = list.get(i);
        //set String
        viewHolder.mBinding.tvMrpText.setText(MyApplication.getInstance().dbHelper.getString(R.string.item_mrp));
        viewHolder.mBinding.tvRemainingQtyText.setText(MyApplication.getInstance().dbHelper.getString(R.string.remaining_qty) + " ");
        viewHolder.mBinding.addItemBtn.setText(MyApplication.getInstance().dbHelper.getString(R.string.add_btn));
        viewHolder.mBinding.tvItemLeftText.setText(MyApplication.getInstance().dbHelper.getString(R.string.item_left));
        viewHolder.mBinding.tvEndInText.setText(MyApplication.getInstance().dbHelper.getString(R.string.end_in_text));
        viewHolder.mBinding.tvFreeItemNotActiveText.setText(MyApplication.getInstance().dbHelper.getString(R.string.inactive_customer_msg));

        viewHolder.mBinding.btItemNotyfy.setOnClickListener(v -> {
            activity.getNotfayItems(model.getWarehouseId(), model.getItemNumber());
        });

        Drawable vectorDrawable = AppCompatResources.getDrawable(activity, R.drawable.logo_grey);
        viewHolder.mBinding.tvItemName.setText(model.itemname);

        // check item in wishList
        if (MyApplication.getInstance().noteRepository.isItemWishList(model.getItemId())) {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red);
        } else {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite);
        }
        viewHolder.mBinding.tvLeftItems.setBackgroundResource(R.drawable.ic_count_bg);
        viewHolder.mBinding.tvRemaingMinests.setBackgroundResource(R.drawable.ic_count_bg);
        viewHolder.mBinding.tvRemainghours.setBackgroundResource(R.drawable.ic_count_bg);
        viewHolder.mBinding.tvRemaingSecand.setBackgroundResource(R.drawable.ic_count_bg);

        //set offer UI
        if (model.isOffer) {
            /*for inactive customer*/
            if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.CUST_ACTIVE)) {
                viewHolder.mBinding.llInActiveUser.setVisibility(View.VISIBLE);
            }
            viewHolder.mBinding.tvOffer.setVisibility(View.GONE);
            viewHolder.mainOfferViewLL.setVisibility(View.VISIBLE);
            viewHolder.flashOfferViewLL.setVisibility(View.GONE);
            viewHolder.mBinding.favItem.setVisibility(View.GONE);
            viewHolder.tvFlashdealPrice.setVisibility(View.GONE);
            // String[] spItemName = list.get(0).getItemname().split("(?<=\\D)(?=\\d)");
            String spItemName = model.itemname;
            String freeOfferTextBuy = "<font color=#fe4e4e>Buy&nbsp;" + model.getOfferMinimumQty() + "&nbsp;pcs&nbsp;</font>" + "<font color=#000000>&nbsp;" + spItemName + " </font>";
            String freeOfferTextGet = "";

            viewHolder.buyValueText.setText(Html.fromHtml(freeOfferTextBuy));

            switch (model.getOfferType()) {
                case "WalletPoint":
                    // String sfreewalletDP = new DecimalFormat("##.##").format(list.get(0).getOfferWalletPoint());
                    viewHolder.freeItemQutTV.setText("0");
                    viewHolder.mBinding.ivFreeIteam.setBackgroundResource(R.drawable.ic_gift_bg);
                    viewHolder.freeDepePointTV.setText(MyApplication.getInstance().dbHelper.getString(R.string.Dp));
                    String sfreewalletDP = new DecimalFormat("##.##").format(model.getOfferWalletPoint());
                    freeOfferTextGet = "<font color=#fe4e4e>Get&nbsp;" + sfreewalletDP + "&nbsp;Free</font>" + "<font color=#000000>&nbsp;Dream Points</font>";

                    break;
                case "ItemMaster":
                    if (!TextUtils.isNullOrEmpty(model.getOfferFreeItemImage())) {
                        Glide.with(activity).load(model.getOfferFreeItemImage()).placeholder(vectorDrawable).into(viewHolder.mBinding.ivFreeIteam);
                    } else {
                        viewHolder.mBinding.ivFreeIteam.setImageDrawable(vectorDrawable);
                    }
                    viewHolder.freeItemQutTV.setText("0");
                    viewHolder.freeDepePointTV.setText(MyApplication.getInstance().dbHelper.getString(R.string.free));
                    freeOfferTextGet = "<font color=#fe4e4e>Get " + model.getOfferFreeItemQuantity() + "&nbsp;Free </font>" + "<font color=#000000>&nbsp;" + model.getOfferFreeItemName() + " </font>";

                    break;
                case "FlashDeal":
                    boolean isflashDealUsed = false;
                    String jsonFlashString = SharePrefs.getStringSharedPreferences(activity, SharePrefs.ITEM_FLASH_DEAL_USED_JSON);
                    try {
                        if (!jsonFlashString.isEmpty()) {
                            JSONObject jsonFlashUsed = new JSONObject(jsonFlashString);
                            if (jsonFlashUsed.has(String.valueOf(model.getItemId()))) {
                                if (jsonFlashUsed.get(String.valueOf(model.getItemId())).equals("1")) {
                                    isflashDealUsed = true;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!isflashDealUsed) {
                        viewHolder.leftItemsTV.setText(String.valueOf(model.getOfferQtyAvaiable()));
                        viewHolder.mainOfferViewLL.setVisibility(View.GONE);
                        viewHolder.flashOfferViewLL.setVisibility(View.VISIBLE);
                        viewHolder.tvFlashdealPrice.setVisibility(View.VISIBLE);
                        flashOfferFlag = true;
                        //end time
                        //  timeDeference(list, viewHolder);
                        if (model.getFlashDealSpecialPrice() != 0) {
                            viewHolder.mBinding.tvPrice.setPaintFlags(viewHolder.mBinding.tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            viewHolder.tvFlashdealPrice.setText(" | " + new DecimalFormat("##.##").format(model.getFlashDealSpecialPrice()));
                        }
                        long currMillis = System.currentTimeMillis();
                        SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.myFormat, Locale.ENGLISH);
                        sdf1.setTimeZone(TimeZone.getDefault());
                        Date endTime = null;
                        try {
                            endTime = sdf1.parse(model.getOfferEndTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long millse = endTime.getTime() - currMillis;
                        viewHolder.timerExpire(millse);
                    } else {
                        flashOfferFlag = false;
                        viewHolder.mainOfferViewLL.setVisibility(View.GONE);
                        viewHolder.flashOfferViewLL.setVisibility(View.GONE);
                        viewHolder.mBinding.favItem.setVisibility(View.VISIBLE);
                        viewHolder.tvFlashdealPrice.setVisibility(View.GONE);
                        viewHolder.mBinding.tvPrice.setPaintFlags(0);
                    }
                    break;
            }
            viewHolder.GetValueTextView.setText(Html.fromHtml(freeOfferTextGet));
        } else {
            viewHolder.mainOfferViewLL.setVisibility(View.GONE);
            viewHolder.flashOfferViewLL.setVisibility(View.GONE);
            viewHolder.mBinding.favItem.setVisibility(View.VISIBLE);
            viewHolder.tvFlashdealPrice.setVisibility(View.GONE);
            viewHolder.mBinding.tvPrice.setPaintFlags(0);
        }

        //set item images
        if (!TextUtils.isNullOrEmpty(model.getLogoUrl())) {
            Glide.with(activity).load(model.getLogoUrl()).placeholder(vectorDrawable).into(viewHolder.mBinding.productImage);
        } else {
            Glide.with(activity).load(R.drawable.logo_grey).into(viewHolder.mBinding.productImage);
        }

        String sMoq = MyApplication.getInstance().dbHelper.getString(R.string.moqs) + " " + model.getMinOrderQty();
        String sMRP = new DecimalFormat("##.##").format(model.price);
        String sPRICE = " | <font color=#FF4500> &#8377; " + new DecimalFormat("##.##").format(model.getUnitPrice()) + "</font>";
        String sMargin;
        if (model.marginPoint != null)
            sMargin = "Margins: " + new DecimalFormat("##.##").format(Double.parseDouble(model.marginPoint)) + "%";
        else
            sMargin = "Margins: 0%";

        // set values
        viewHolder.mBinding.tvItemName.setText(model.itemname);
        viewHolder.mBinding.tvMrp.setText(sMRP);
        viewHolder.mBinding.tvMrp.setPaintFlags(viewHolder.mBinding.tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.mBinding.tvPrice.setText(Html.fromHtml(sPRICE));
        viewHolder.mBinding.tvMoq.setText(Html.fromHtml(sMoq));
        viewHolder.mBinding.tvMargin.setText(Html.fromHtml(sMargin));
        viewHolder.mBinding.tvDreamPoint.setText(MyApplication.getInstance().dbHelper.getString(R.string.Dp) + " " + +model.dreamPoint);

        if (!TextUtils.isNullOrEmpty(model.getScheme())) {
            viewHolder.mBinding.tvSchemeText.setVisibility(View.VISIBLE);
            viewHolder.mBinding.tvSchemeText.setText("" + model.getScheme());
        } else {
            viewHolder.mBinding.tvSchemeText.setVisibility(View.GONE);
        }

        // set prime item price
        if (model.isPrimeItem) {
            viewHolder.mBinding.liPrime.setVisibility(View.VISIBLE);
            viewHolder.mBinding.tvPPrice.setText(SharePrefs.getInstance(activity).getString(SharePrefs.PRIME_NAME)
                    + ": ₹" + new DecimalFormat("##.##").format(model.getPrimePrice()));
            viewHolder.mBinding.tvPrice.setTextColor(activity.getResources().getColor(R.color.grey));
        } else {
            viewHolder.mBinding.liPrime.setVisibility(View.GONE);
            viewHolder.mBinding.tvPrice.setTextColor(Color.parseColor("#FF4500"));
        }
        if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem) {
            viewHolder.mBinding.tvUnlock.setText("");
            viewHolder.mBinding.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_open, 0, 0, 0);
        } else {
            viewHolder.mBinding.tvUnlock.setText(" " + MyApplication.getInstance().dbHelper.getString(R.string.text_unlock));
            viewHolder.mBinding.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_right_arrow, 0);
        }

        ItemListModel cartModel = MyApplication.getInstance().noteRepository.getItemByMrpId(model.getItemId(), model.getItemMultiMRPId());
        QtyDTO qtyDTO = MyApplication.getInstance().noteRepository.getQtyTotalQtyByMrpId(model.getItemId(), model.getItemMultiMRPId());

        //set UI for ItemLimit
        if (model.isItemLimit) {
            int totalItemInCart = 0;
            int itemlimitQuantity;
            int totalAvailQty;
            viewHolder.mBinding.availQtyLayout.setVisibility(View.VISIBLE);

            itemlimitQuantity = model.getItemLimitQty();
            if (qtyDTO != null) {
                totalItemInCart += qtyDTO.getTotalQuantity();
                totalAvailQty = itemlimitQuantity - totalItemInCart;
                viewHolder.mBinding.availQty.setText(String.valueOf(totalAvailQty));
            } else {
                totalAvailQty = model.getItemLimitQty();
                viewHolder.mBinding.availQty.setText(String.valueOf(totalAvailQty));
            }
        } else {
            viewHolder.mBinding.availQtyLayout.setVisibility(View.GONE);
        }

        boolean isItemFound = false;
        if (cartModel != null) {
            isItemFound = true;
            int itemQuantity = cartModel.qty;
            /*offer section start*/
            if (cartModel.isOffer) {
                if (cartModel.getOfferType().equalsIgnoreCase("WalletPoint")) {
                    double freeWalletPoint = cartModel.getTotalFreeWalletPoint();
                    String sfreewalletDP = new DecimalFormat("##.##").format(freeWalletPoint);
                    if (freeWalletPoint > 0) {
                        viewHolder.freeItemQutTV.setText(sfreewalletDP);
                    }
                } else if (cartModel.getOfferType().equalsIgnoreCase("ItemMaster")) {
                    int freeItemQuantity = Integer.parseInt(cartModel.getOfferFreeItemQuantity());
                    if (freeItemQuantity > 0) {
                        viewHolder.freeItemQutTV.setText("" + freeItemQuantity);
                    }
                } else {
                    int remainingLeft = cartModel.getOfferQtyAvaiable() - itemQuantity;
                    if (remainingLeft > 0) {
                        viewHolder.leftItemsTV.setText("" + remainingLeft);
                    } else {
                        viewHolder.leftItemsTV.setText("0");
                    }
                }
            }
            /*offer section end*/
            if (itemQuantity > 0) {
                viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                if (flashOfferFlag) {
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(itemQuantity * model.getFlashDealSpecialPrice());
                    viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                } else if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem) {
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(itemQuantity * model.getPrimePrice());
                    viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                } else {
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(itemQuantity * model.getUnitPrice());
                    viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                }
            } else {
                isItemFound = false;
            }

        } else {
            isItemFound = false;
        }
        if (!isItemFound) {
            int itemQuantity = 0;
            viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
            String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(itemQuantity * model.getUnitPrice());
            viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
            viewHolder.mBinding.addItemBtn.setVisibility(View.VISIBLE);
            viewHolder.visibleLL.setVisibility(View.GONE);
        } else {
            viewHolder.mBinding.addItemBtn.setVisibility(View.GONE);
            viewHolder.mBinding.visibleLl.setVisibility(View.VISIBLE);
        }

        viewHolder.mBinding.minusBtn.setOnClickListener(view -> itemAddRemove(viewHolder, model, false, false));
        viewHolder.mBinding.plusBtn.setOnClickListener(view -> {
            itemAddRemove(viewHolder, model, true, false);
        });
        //Add Btn clicked
        viewHolder.mBinding.addItemBtn.setOnClickListener(v -> {
            viewHolder.visibleLL.setVisibility(View.VISIBLE);
            viewHolder.mBinding.addItemBtn.setVisibility(View.GONE);
            itemAddRemove(viewHolder, model, true, true);
        });
        viewHolder.mBinding.favItem.setOnClickListener(v -> {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite);
            Utils.addFav(model.getItemId(), false, activity);
            MyApplication.getInstance().noteRepository.deleteTask(list.get(i));
            list.remove(i);
            notifyItemRemoved(i);
            notifyItemRangeChanged(i, list.size());
        });
        viewHolder.mBinding.tvUnlock.setOnClickListener(v -> {
            if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem) {
                activity.startActivity(new Intent(activity, MembershipPlanActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onItemClick(int position, boolean itemAdded) {
        progressBar.setVisibility(View.INVISIBLE);
        if (!itemAdded) {
            notifyDataSetChanged();
        }
    }


    private void itemAddRemove(@NonNull ViewHolder viewHolder, ItemListModel model, boolean addItem, boolean addBtn) {
        // progress bar
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
        progressBar = viewHolder.mBinding.ivProgress;
        try {
            int FreeItemQuantity = 0;
            double FreeWalletPoint = 0;
            boolean addFlag = false;
            boolean flashDealFlag = false;
            flashOfferFlag = false;
            double calUnitPrice;
            boolean isflashDealUsed = false;
            boolean isPrimeItem = false;
            //get item limit
            int itemQuantity = Integer.parseInt(viewHolder.tvselectedItemQuantity.getText().toString());

            if (model.isOffer) {
                try {
                    if (model.getOfferType() != null) {
                        if (model.getOfferType().equalsIgnoreCase("FlashDeal")) {
                            String jsonFlashString = SharePrefs.getStringSharedPreferences(activity, SharePrefs.ITEM_FLASH_DEAL_USED_JSON);
                            if (!jsonFlashString.isEmpty()) {
                                JSONObject jsonObject = new JSONObject(jsonFlashString);
                                if (jsonObject.has(String.valueOf(model.getItemId()))) {
                                    if (jsonObject.get(String.valueOf(model.getItemId())).equals("1")) {
                                        isflashDealUsed = true;
                                    }
                                }
                            }
                            if (model.getFlashDealMaxQtyPersonCanTake() >= itemQuantity + model.getMinOrderQty() && model.getOfferQtyAvaiable() > itemQuantity) {
                                flashDealFlag = false;
                            } else {
                                if (!isflashDealUsed) {
                                    flashDealFlag = true;
                                }
                            }
                        } else {
                            flashDealFlag = false;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (addItem) {
                //plus btn
                if (!flashDealFlag) {
                    itemQuantity += model.getMinOrderQty();
                    //Item Limit code
                    if (model.isItemLimit) {
                        addFlag = setLimit(viewHolder, model, addFlag, itemQuantity, true);
                    } else {
                        if (itemQuantity > 0) {
                            if (model.getBillLimitQty() != 0) {
                                addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity);
                            } else {
                                viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                                addFlag = true;
                            }
                        }
                    }
                } else {
                    if (addBtn) {
                        viewHolder.visibleLL.setVisibility(View.GONE);
                        viewHolder.mBinding.addItemBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(activity, "No items available", Toast.LENGTH_SHORT).show();
                    } else {
                        if (model.getOfferQtyAvaiable() <= itemQuantity) {
                            Toast.makeText(activity, "No items available", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "Only add maximum item  " + model.getFlashDealMaxQtyPersonCanTake(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                //minus btn
                if (model.isItemLimit) {
                    if (itemQuantity > 0) {
                        itemQuantity -= model.getMinOrderQty();
                        addFlag = setLimit(viewHolder, model, addFlag, itemQuantity, false);
                    } else {
                        viewHolder.mBinding.addItemBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (itemQuantity > 0) {
                        itemQuantity -= model.getMinOrderQty();
                        viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                        addFlag = true;
                    } else {
                        viewHolder.mBinding.addItemBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (addFlag) {
                /*offer section*/
                if (model.isOffer) {
                    boolean customerActive = SharePrefs.getInstance(activity).getBoolean(SharePrefs.CUST_ACTIVE);
                    int offerMinimumQty = model.getOfferMinimumQty();
                    switch (model.getOfferType()) {
                        case "WalletPoint":
                            if (customerActive) {
                                if (offerMinimumQty != 0) {
                                    if (model.getOfferWalletPoint() != null) {
                                        //event trigger

                                        if (itemQuantity >= offerMinimumQty) {
                                            FreeWalletPoint = model.getOfferWalletPoint();
                                            int calfreeItemQty = itemQuantity / offerMinimumQty;
                                            FreeWalletPoint *= calfreeItemQty;
                                            String sfreewalletDP = new DecimalFormat("##.##").format(FreeWalletPoint);
                                            if (FreeWalletPoint > 0) {
                                                viewHolder.freeItemQutTV.setText(sfreewalletDP);
                                            }
                                        } else {
                                            viewHolder.freeItemQutTV.setText("0");
                                        }
                                    } else {
                                        Toast.makeText(activity, "Minimum Qty should not be zero", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } /*else {
                                Toast.makeText(activity, R.string.inactive_customer_msg, Toast.LENGTH_SHORT).show();
                            }*/
                            break;
                        case "ItemMaster":
                            if (customerActive) {
                                if (offerMinimumQty != 0) {
                                    if (model.getOfferFreeItemQuantity() != null) {
                                        //event trigger
                                        if (itemQuantity >= offerMinimumQty) {
                                            FreeItemQuantity = Integer.parseInt(model.getOfferFreeItemQuantity());
                                            int calfreeItemQty = itemQuantity / offerMinimumQty;
                                            FreeItemQuantity *= calfreeItemQty;
                                            if (FreeItemQuantity > 0) {
                                                viewHolder.freeItemQutTV.setText(String.valueOf(FreeItemQuantity));
                                            }
                                        } else {
                                            viewHolder.freeItemQutTV.setText("0");
                                        }
                                    }
                                } else {
                                    Toast.makeText(activity, "Minimum Qty should not be zero", Toast.LENGTH_SHORT).show();
                                }
                            } /*else {
                                Toast.makeText(activity, R.string.inactive_customer_msg, Toast.LENGTH_SHORT).show();
                            }*/
                            break;
                        case "FlashDeal":
                            if (!isflashDealUsed) {
                                int remainingLeft = model.getOfferQtyAvaiable() - itemQuantity;
                                if (remainingLeft > 0) {
                                    viewHolder.leftItemsTV.setText("" + remainingLeft);
                                } else {
                                    viewHolder.leftItemsTV.setText("0");
                                }
                                flashOfferFlag = true;
                            } else {
                                flashOfferFlag = false;
                            }
                            break;
                    }
                }
                if (flashOfferFlag) {
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(itemQuantity * model.getFlashDealSpecialPrice());
                    viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                    calUnitPrice = model.getFlashDealSpecialPrice();
                } else if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem) {
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format((itemQuantity * model.getPrimePrice()));
                    viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                    calUnitPrice = model.getPrimePrice();
                    isPrimeItem = true;
                } else {
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(itemQuantity * model.getUnitPrice());
                    viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                    calUnitPrice = model.getUnitPrice();
                }
                progressBar.setVisibility(View.VISIBLE);
                activity.addItemInCartItemArrayList(model.getItemId(), itemQuantity,
                        calUnitPrice, model, FreeItemQuantity, FreeWalletPoint, isPrimeItem, this);
                RxBus.getInstance().sendEvent(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean setLimit(ViewHolder viewHolder, ItemListModel model, boolean addFlag, int itemQuantity, boolean add) {
        QtyDTO qtyDTO = MyApplication.getInstance().noteRepository.getQtyTotalQtyByMrpId(model.getItemId(), model.getItemMultiMRPId());
        int total = qtyDTO.getQuantity();
        int availqty = 0;
        int totalItemqty = qtyDTO.getTotalQuantity();
        int itemlimitqty;
        if (add) {
            totalItemqty += model.getMinOrderQty();

        } else {
            totalItemqty -= model.getMinOrderQty();

        }
        itemlimitqty = model.getItemLimitQty();

        if (total > 0) {
            if (total + itemQuantity > itemlimitqty) {
                Utils.setToast(activity, MyApplication.getInstance().dbHelper.getString(R.string.additemToast) + " " + model.getItemLimitQty() + " " + MyApplication.getInstance().dbHelper.getString(R.string.additemToast_2));
            } else {
                if (model.getBillLimitQty() != 0) {
                    addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity);
                    if (addFlag) {
                        availqty = itemlimitqty - totalItemqty;
                        if (availqty >= 0) {
                            viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
                        }
                    }
                } else {
                    viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                    addFlag = true;
                    availqty = itemlimitqty - totalItemqty;
                    if (availqty >= 0) {
                        viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
                    }
                }
            }
        } else {
            if (itemQuantity > 0) {
                if (itemQuantity > model.getItemLimitQty()) {
                    Utils.setToast(activity, MyApplication.getInstance().dbHelper.getString(R.string.additemToast) + " " + model.getItemLimitQty() + " " + MyApplication.getInstance().dbHelper.getString(R.string.additemToast_2));
                } else {
                    if (model.getBillLimitQty() != 0) {
                        addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity);
                        if (addFlag) {
                            availqty = itemlimitqty - totalItemqty;
                            if (availqty >= 0) {
                                viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
                            }
                        }
                    } else {
                        viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                        addFlag = true;
                        availqty = itemlimitqty - totalItemqty;
                        if (availqty >= 0) {
                            viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
                        }
                    }
                }
            }
        }
        return addFlag;
    }

    private boolean setBillLimit(@NonNull ViewHolder viewHolder, ItemListModel model, boolean addFlag, int itemQuantity) {
        try {
            int total = MyApplication.getInstance().noteRepository.getQtyByMultiMrp(model.getItemId(), model.getItemMultiMRPId());
            int itemlimitqty = model.getBillLimitQty();
            if (total > 0) {
                if (total + itemQuantity > itemlimitqty) {
                    Utils.setToast(activity, MyApplication.getInstance().dbHelper.getString(R.string.bill_limit_text) + " " + model.getBillLimitQty() + " item");
                } else {
                    viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                    addFlag = true;
                }
            } else {
                if (itemQuantity > 0) {
                    if (itemQuantity > model.getBillLimitQty()) {
                        Utils.setToast(activity, MyApplication.getInstance().dbHelper.getString(R.string.bill_limit_text) + " " + model.getBillLimitQty() + " item");
                    } else {
                        viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                        addFlag = true;
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return addFlag;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final FavItemsBinding mBinding;

        private final AppCompatTextView tvselectedItemQuantity;
        private final TextView tvSelectedItemPrice;
        private final TextView leftItemsTV;
        private final TextView buyValueText;
        private final TextView GetValueTextView;
        private final TextView tvFlashdealPrice;
        private final LinearLayout mainOfferViewLL;
        private final LinearLayout flashOfferViewLL;
        private final LinearLayout visibleLL;
        private final TextView freeItemQutTV;
        private final TextView freeDepePointTV;

        public ViewHolder(FavItemsBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

            visibleLL = mBinding.visibleLl;
            tvselectedItemQuantity = mBinding.tvSelectedItemQuantity;
            tvSelectedItemPrice = mBinding.tvSelectedItemPrice;
            freeItemQutTV = mBinding.tvFreeItemQut;
            mainOfferViewLL = mBinding.llMainOfferView;
            freeDepePointTV = mBinding.tvFreeDepePoint;
            flashOfferViewLL = mBinding.llFlashOfferView;
            leftItemsTV = mBinding.tvLeftItems;
            buyValueText = mBinding.tvBuyValue;
            GetValueTextView = mBinding.tvGetValue;

            tvFlashdealPrice = mBinding.tvFlashdealPrice;
            customRunnable = new CustomRunnable(handler, mBinding.tvRemaingSecand, 10000);
        }

        public void timerExpire(long endTime) {
            handler.removeCallbacks(customRunnable);
            customRunnable.holder = mBinding.tvRemaingSecand;
            customRunnable.millisUntilFinished = endTime; //Current time - received time
            handler.postDelayed(customRunnable, 1000);
        }
    }
}