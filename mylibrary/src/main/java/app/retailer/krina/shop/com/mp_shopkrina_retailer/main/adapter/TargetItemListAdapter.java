package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.CategoryOrderItemsBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db.QtyDTO;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.productDetails.ProductDetailsActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.TargetOrderListActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NotifyModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.CategoryItemOrderInfo;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.MoqAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CustomRunnable;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import pl.droidsonroids.gif.GifImageView;

public class TargetItemListAdapter extends RecyclerView.Adapter<TargetItemListAdapter.ViewHolder> implements OnItemClick {
    private final Handler handler = new Handler();
    private final TargetOrderListActivity activity;

    private List<ItemListModel> list;
    private MoqAdapter adapter;
    private boolean flashofferFlag = false;
    private CustomRunnable customRunnable;
    private GifImageView progressBar;


    public TargetItemListAdapter(TargetOrderListActivity activity, List<ItemListModel> list) {
        this.activity = activity;
        this.list = list;
        notifyDataSetChanged();
    }

    public void setItemListCategory(ArrayList<ItemListModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.category_order_items, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        ItemListModel model = list.get(i);

        // set String
        viewHolder.mBinding.tvRemainingQtyText.setText(MyApplication.getInstance().dbHelper.getString(R.string.remaining_qty) + " ");
        viewHolder.mBinding.tvItemLeftText.setText(MyApplication.getInstance().dbHelper.getString(R.string.item_left));
        viewHolder.mBinding.tvEndInText.setText(MyApplication.getInstance().dbHelper.getString(R.string.end_in_text));
        viewHolder.mBinding.tvFreeItemNotActiveText.setText(MyApplication.getInstance().dbHelper.getString(R.string.inactive_customer_msg));
        viewHolder.mBinding.addItemBtn.setText(MyApplication.getInstance().dbHelper.getString(R.string.add_btn));

        viewHolder.mBinding.btItemNotyfy.setOnClickListener(v -> {
            activity.getNotifyItems(model.getWarehouseId(), model.getItemNumber());
            MyApplication.getInstance().noteRepository.insertNotifyItemTask(new NotifyModel(model.getItemId()));
            viewHolder.mBinding.btItemNotyfy.setBackground(activity.getResources().getDrawable(R.drawable.background_for_buttons_disble));
            viewHolder.mBinding.btItemNotyfy.setClickable(false);
            viewHolder.mBinding.btItemNotyfy.setEnabled(false);
        });

        // set MOQ
        if (model.moqList != null && model.moqList.size() > 0) {
            viewHolder.mBinding.tvMoq.setVisibility(View.GONE);
            viewHolder.mBinding.tvMultiMoq.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mBinding.tvMoq.setVisibility(View.VISIBLE);
            viewHolder.mBinding.tvMultiMoq.setVisibility(View.GONE);
        }

        viewHolder.mBinding.tvMoq.setText(MyApplication.getInstance().dbHelper.getString(R.string.item_moq)
                + " " + model.getMinOrderQty());
        viewHolder.mBinding.tvMultiMoq.setText(MyApplication.getInstance().dbHelper.getString(R.string.item_moq)
                + " " + model.getMinOrderQty());
        if (!TextUtils.isNullOrEmpty(model.getScheme())) {
            viewHolder.mBinding.tvSchemeText.setVisibility(View.VISIBLE);
            viewHolder.mBinding.tvSchemeText.setText("" + model.getScheme());
        } else {
            viewHolder.mBinding.tvSchemeText.setVisibility(View.GONE);
        }
        viewHolder.leftItemsTV.setBackgroundResource(R.drawable.ic_count_bg);
        viewHolder.remaingSecTV.setBackgroundResource(R.drawable.ic_count_bg);
        //set  value on UI
        setValueInUI(viewHolder, model);
        model.isChecked = true;
        // Minus Btn clicked
        viewHolder.mBinding.minusBtn.setOnClickListener(view -> {
            //click effect
            Utils.buttonEffect(viewHolder.mBinding.minusBtn);
            itemAddRemove(viewHolder, model, false, false);
        });
        //plus Btn clicked
        viewHolder.mBinding.plusBtn.setOnClickListener(view -> {
            Utils.buttonEffect(viewHolder.mBinding.plusBtn);
            itemAddRemove(viewHolder, model, true, false);
        });
        // fav section
        viewHolder.mBinding.favItem.setOnClickListener(v -> {
            if (MyApplication.getInstance().noteRepository.isItemWishList(model.getItemId())) {
                viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite);
                MyApplication.getInstance().noteRepository.deleteTask(model);
                Utils.addFav(model.getItemId(), false, activity);
            } else {
                viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red);
                MyApplication.getInstance().noteRepository.insertTask(model);
                Utils.addFav(model.getItemId(), true, activity);
                MyApplication.getInstance().analyticAddWishList(model);
            }
        });
        // Add Btn clicked
        viewHolder.mBinding.addItemBtn.setOnClickListener(v -> {
            viewHolder.mBinding.visible.setVisibility(View.VISIBLE);
            viewHolder.mBinding.addItemBtn.setVisibility(View.GONE);
            itemAddRemove(viewHolder, model, true, true);
            // analytics for add to cart
            MyApplication.getInstance().updateAnalyticsCart(FirebaseAnalytics.Event.ADD_TO_CART, model);
        });
        // checkout clicked
        viewHolder.mBinding.LLItemMain.setOnClickListener(v -> {
            viewHolder.mBinding.LLItemMain.setClickable(false);
            detailsScree(model, viewHolder);
        });
        // MOQ popup open here
        viewHolder.mBinding.tvMultiMoq.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(activity);
            View dialogLayout = LayoutInflater.from(activity).inflate(R.layout.moq_price_popup, null);
            dialog.setContentView(dialogLayout);

            TextView item_name = dialogLayout.findViewById(R.id.itemName);
            TextView tvDSelectQty = dialogLayout.findViewById(R.id.tvDSelectQty);
            TextView tvDMoq = dialogLayout.findViewById(R.id.tvDMoq);
            TextView tvDMrp = dialogLayout.findViewById(R.id.tvDMrp);
            TextView tvDRs = dialogLayout.findViewById(R.id.tvDRs);
            TextView tvDMargin = dialogLayout.findViewById(R.id.tvDMargin);
            ImageView ivClose = dialogLayout.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(v1 -> dialog.dismiss());

            tvDSelectQty.setText(MyApplication.getInstance().dbHelper.getString(R.string.select_quantities_for));
            tvDMoq.setText(MyApplication.getInstance().dbHelper.getString(R.string.moq));
            tvDMrp.setText(MyApplication.getInstance().dbHelper.getString(R.string.mrp));
            tvDRs.setText(MyApplication.getInstance().dbHelper.getString(R.string.rs));
            tvDMargin.setText(MyApplication.getInstance().dbHelper.getString(R.string.margins_d));

            item_name.setText(list.get(i).itemname);
            ListView mMoqPriceList = dialogLayout.findViewById(R.id.listview_moq_price);

            AdapterInterface listener = pos -> {
                ArrayList<ItemListModel> moq = list.get(i).moqList;
                list.set(i, list.get(i).moqList.get(pos));
                list.get(i).moqList = moq;
                for (int j = 0; j < list.get(i).moqList.size(); j++) {
                    list.get(i).moqList.get(j).isChecked = pos == j;
                }
                list.get(i).moqList.get(pos).isChecked = true;
                notifyDataSetChanged();

                handler.postDelayed(() -> {
                    //Do something after 100ms
                    dialog.dismiss();
                }, 300);
            };
            adapter = new MoqAdapter(activity, model.moqList, listener);
            mMoqPriceList.setAdapter(adapter);
            dialog.show();
        });
        viewHolder.mBinding.tvUnlock.setOnClickListener(v -> {
            if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER) && list.get(i).isPrimeItem) {
                activity.startActivity(new Intent(activity, MembershipPlanActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    private void detailsScree(ItemListModel model, ViewHolder viewHolder) {
        Bundle args = new Bundle();
        Intent intent = new Intent(activity, ProductDetailsActivity.class);
        intent.putExtra("PRODUCT_IMAGE", model.getLogoUrl());
        intent.putExtra("PRODUCT_NAME", model.itemname);
        intent.putExtra("PRODUCT_PRICE", model.getUnitPrice());
        intent.putExtra("PRODUCT_MOQ", model.getMinOrderQty());
        intent.putExtra("PRODUCT_ITEM_ID", model.getItemId());
        intent.putExtra("PRODUCT_DP", model.dreamPoint);
        intent.putExtra("WAREHOUSE_ID", model.getWarehouseId());
        intent.putExtra("COMPANY_ID", model.getCompanyId());
        intent.putExtra("PRICE", model.price);
        intent.putExtra("MARGIN_POINT", model.marginPoint);
        intent.putExtra("NUMBER", model.getItemNumber());
        intent.putExtra("ItemMultiMRPId", model.getItemMultiMRPId());
        intent.putExtra("remainingqty", viewHolder.mBinding.availQty.getText());
        args.putSerializable("ITEM_LIST", model);
        intent.putExtras(args);
        activity.startActivity(intent);
        Utils.leftTransaction(activity);
    }

    private void setValueInUI(@NonNull ViewHolder viewHolder, ItemListModel model) {
        /*Fav section*/
        flashofferFlag = false;

        // check item in wishList
        if (MyApplication.getInstance().noteRepository.isItemWishList(model.getItemId())) {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red);
        } else {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite);
        }
        try {
            viewHolder.tvItemName.setText(model.itemname);
            //set offer UI
            if (model.isOffer) {
                /*for inactive customer*/
                if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.CUST_ACTIVE)) {
                    viewHolder.mBinding.llInActiveUser.setVisibility(View.VISIBLE);
                }
                viewHolder.tvOffer.setVisibility(View.GONE);
                viewHolder.mainOfferViewLL.setVisibility(View.VISIBLE);
                viewHolder.flashOfferViewLL.setVisibility(View.GONE);
                viewHolder.mBinding.favItem.setVisibility(View.GONE);
                viewHolder.tvFlashdealPrice.setVisibility(View.GONE);
                String spItemName = model.itemname;
                String freeOfferTextBuy = "<font color=#fe4e4e>" + MyApplication.getInstance().dbHelper.getString(R.string.buy) + "&nbsp;" + model.getOfferMinimumQty() + "&nbsp;" + MyApplication.getInstance().dbHelper.getString(R.string.pcs) + "&nbsp;</font>" + "<font color=#000000>&nbsp;" + spItemName + " </font>";
                String freeOfferTextGet = "";

                viewHolder.buyValueText.setText(Html.fromHtml(freeOfferTextBuy));
                if (model.getOfferType() != null) {
                    switch (model.getOfferType()) {
                        case "WalletPoint":
                            viewHolder.mBinding.tvFreeItemQut.setText("0");
                            viewHolder.freeItemIV.setBackgroundResource(R.drawable.ic_gift_bg);
                            viewHolder.freeDepePointTV.setText(MyApplication.getInstance().dbHelper.getString(R.string.Dp));
                            String sfreewalletDP = new DecimalFormat("##.##").format(model.getOfferWalletPoint());
                            freeOfferTextGet = "<font color=#fe4e4e>" + MyApplication.getInstance().dbHelper.getString(R.string.get) + "&nbsp;" + sfreewalletDP + "&nbsp;" + MyApplication.getInstance().dbHelper.getString(R.string.free) + "</font>" + "<font color=#000000>&nbsp;" + MyApplication.getInstance().dbHelper.getString(R.string.dream_points) + "</font>";

                            break;
                        case "ItemMaster":
                            if (!TextUtils.isNullOrEmpty(model.getOfferFreeItemImage())) {
                                Picasso.get().load(model.getOfferFreeItemImage())
                                        .placeholder(R.drawable.logo_grey)
                                        .error(R.drawable.logo_grey)
                                        .into(viewHolder.mBinding.ivFreeIteam);
                            } else {
                                viewHolder.mBinding.ivFreeIteam.setImageResource(R.drawable.logo_grey);
                            }
                            viewHolder.mBinding.tvFreeItemQut.setText("0");
                            viewHolder.freeDepePointTV.setText(MyApplication.getInstance().dbHelper.getString(R.string.free));
                            freeOfferTextGet = "<font color=#fe4e4e>" + MyApplication.getInstance().dbHelper.getString(R.string.get) + "&nbsp;" + model.getOfferFreeItemQuantity() + "&nbsp;" + MyApplication.getInstance().dbHelper.getString(R.string.free) + " </font>" + "<font color=#000000>&nbsp;" + model.getOfferFreeItemName() + " </font>";
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!isflashDealUsed) {
                                viewHolder.leftItemsTV.setText(String.valueOf(model.getOfferQtyAvaiable()));
                                viewHolder.mainOfferViewLL.setVisibility(View.GONE);
                                viewHolder.flashOfferViewLL.setVisibility(View.VISIBLE);
                                viewHolder.mBinding.tvFlashdealPrice.setVisibility(View.VISIBLE);
                                flashofferFlag = true;
                                //end time
                                //  timeDeference(moqPojoArrayList, viewHolder);
                                if (model.getFlashDealSpecialPrice() != 0) {
                                    viewHolder.mBinding.tvPrice.setPaintFlags(viewHolder.mBinding.tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    viewHolder.mBinding.tvFlashdealPrice.setText(" | " + new DecimalFormat("##.##").format(model.getFlashDealSpecialPrice()));
                                }
                                SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.myFormat, Locale.ENGLISH);
                                sdf1.setTimeZone(TimeZone.getDefault());
                                Date currentTime = sdf1.parse(model.getCurrentStartTime());
                                long currentEpoch = currentTime.getTime();
                                Date endTime = sdf1.parse(model.getOfferEndTime());
                                long millse = endTime.getTime() - currentEpoch;
                                viewHolder.timerExpire(millse);
                            } else {
                                flashofferFlag = false;
                                viewHolder.mainOfferViewLL.setVisibility(View.GONE);
                                viewHolder.flashOfferViewLL.setVisibility(View.GONE);
                                viewHolder.mBinding.favItem.setVisibility(View.VISIBLE);
                                viewHolder.mBinding.tvFlashdealPrice.setVisibility(View.GONE);
                                viewHolder.mBinding.tvPrice.setPaintFlags(0);
                            }
                            break;
                    }
                }
                viewHolder.GetValueTextView.setText(Html.fromHtml(freeOfferTextGet));

            } else {
                viewHolder.mainOfferViewLL.setVisibility(View.GONE);
                viewHolder.flashOfferViewLL.setVisibility(View.GONE);
                viewHolder.mBinding.favItem.setVisibility(View.VISIBLE);
                viewHolder.tvFlashdealPrice.setVisibility(View.GONE);
                viewHolder.mBinding.tvPrice.setPaintFlags(0);
                viewHolder.mBinding.llInActiveUser.setVisibility(View.GONE);
            }
            if (!TextUtils.isNullOrEmpty(model.getLogoUrl())) {
                Picasso.get().load(model.getLogoUrl())
                        .placeholder(R.drawable.logo_grey)
                        .error(R.drawable.logo_grey)
                        .into(viewHolder.ivItemImage);
            } else {
                viewHolder.ivItemImage.setImageResource(R.drawable.logo_grey);
            }
            String sPRICE = "| ₹" + new DecimalFormat("##.##").format(model.getUnitPrice());
            String sMargin = MyApplication.getInstance().dbHelper.getString(R.string.moq_margin) + " " + new DecimalFormat("##.##").format(Double.parseDouble(model.marginPoint != null ? model.marginPoint : "0")) + "%";
            String sMRP = new DecimalFormat("##.##").format(model.price);
            //set values
            viewHolder.mBinding.tvMrp.setText(sMRP);
            viewHolder.mBinding.tvMrp.setPaintFlags(viewHolder.mBinding.tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.mBinding.tvPrice.setText(sPRICE);
            viewHolder.mBinding.tvMargin.setText(sMargin);
            // set prime item price
            if (model.isPrimeItem) {
                viewHolder.mBinding.liPrime.setVisibility(View.VISIBLE);
                viewHolder.mBinding.tvPPrice.setText(SharePrefs.getInstance(activity).getString(SharePrefs.PRIME_NAME)
                        + " " + MyApplication.getInstance().dbHelper.getString(R.string.price)
                        + ": ₹" + new DecimalFormat("##.##").format(model.getPrimePrice()));
                viewHolder.mBinding.tvPrice.setTextColor(activity.getResources().getColor(R.color.back_arrow_grey));
            } else {
                viewHolder.mBinding.liPrime.setVisibility(View.GONE);
                viewHolder.mBinding.tvPrice.setTextColor(activity.getResources().getColor(R.color.colorAccent));
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

            // set UI for ItemLimit
            if (model.isItemLimit) {
                int totalItemInCart = 0;
                int itemlimitQuantity = 0;
                if (model.getActive()) {
                    viewHolder.mBinding.availQtyLayout.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mBinding.availQtyLayout.setVisibility(View.GONE);
                }
                itemlimitQuantity = model.getItemLimitQty();
                int totalAvailQty = 0;
                if (qtyDTO != null) {
                    totalItemInCart += qtyDTO.getTotalQuantity();
                    totalAvailQty = itemlimitQuantity - totalItemInCart;
                } else {
                    totalAvailQty = model.getItemLimitQty();
                }
                if (totalAvailQty > 0) {
                    viewHolder.mBinding.availQty.setText("" + totalAvailQty);
                } else {
                    viewHolder.mBinding.availQty.setText("0");
                }
            } else {
                viewHolder.mBinding.availQtyLayout.setVisibility(View.GONE);
            }
            viewHolder.tvDreamPoint.setText(MyApplication.getInstance().dbHelper.getString(R.string.Dp) + " " + model.dreamPoint);

            boolean isItemFound;
            if (cartModel != null && model.getItemId() == cartModel.getItemId()) {
                isItemFound = true;
                int itemQuantity = cartModel.qty;
                /*offer section start*/
                if (cartModel.isOffer) {
                    if (cartModel.getOfferType().equalsIgnoreCase("WalletPoint")) {
                        double freeWalletPoint = cartModel.getTotalFreeWalletPoint();
                        String sfreewalletDP = new DecimalFormat("##.##").format(freeWalletPoint);
                        if (freeWalletPoint > 0) {
                            viewHolder.mBinding.tvFreeItemQut.setText(sfreewalletDP);
                        }
                    } else if (cartModel.getOfferType().equalsIgnoreCase("ItemMaster")) {
                        int freeItemQuantity = cartModel.getTotalFreeItemQty();
                        if (freeItemQuantity > 0) {
                            viewHolder.mBinding.tvFreeItemQut.setText("" + freeItemQuantity);
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
                // offer section end
                if (itemQuantity > 0) {
                    viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                    if (flashofferFlag) {
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

            // item notify code
            if (model.getActive()) {
                viewHolder.mBinding.tvMrpText.setText(MyApplication.getInstance().dbHelper.getString(R.string.item_mrp) + " ");
                viewHolder.mBinding.tvMrpText.setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
                viewHolder.mBinding.btItemNotyfy.setVisibility(View.GONE);
                viewHolder.mBinding.addItemBtn.setVisibility(View.VISIBLE);
                viewHolder.mBinding.tvMrp.setVisibility(View.VISIBLE);
                viewHolder.mBinding.tvPrice.setVisibility(View.VISIBLE);
                viewHolder.mBinding.tvSelectedItemPrice.setVisibility(View.VISIBLE);
                viewHolder.mBinding.tvMargin.setVisibility(View.VISIBLE);
                viewHolder.mBinding.favItem.setVisibility(View.VISIBLE);
                if (!isItemFound) {
                    int itemQuantity = 0;
                    viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(itemQuantity * model.getUnitPrice());
                    viewHolder.mBinding.tvSelectedItemPrice.setText(Html.fromHtml(price));
                    viewHolder.mBinding.addItemBtn.setVisibility(View.VISIBLE);
                    viewHolder.mBinding.visible.setVisibility(View.GONE);
                } else {
                    viewHolder.mBinding.addItemBtn.setVisibility(View.GONE);
                    viewHolder.mBinding.visible.setVisibility(View.VISIBLE);
                }
            } else {
                viewHolder.mBinding.tvMrpText.setText(MyApplication.getInstance().dbHelper.getString(R.string.text_out_of_stock));
                viewHolder.mBinding.tvMrpText.setTextColor(activity.getResources().getColor(R.color.colorAccent));
                viewHolder.mBinding.btItemNotyfy.setVisibility(View.VISIBLE);
                viewHolder.mBinding.addItemBtn.setVisibility(View.GONE);
                viewHolder.mBinding.tvMrp.setVisibility(View.GONE);
                viewHolder.mBinding.tvPrice.setVisibility(View.GONE);
                viewHolder.mBinding.tvSelectedItemPrice.setVisibility(View.GONE);
                viewHolder.mBinding.tvMargin.setVisibility(View.GONE);
                viewHolder.mBinding.favItem.setVisibility(View.GONE);

                if (MyApplication.getInstance().noteRepository.isNotifyDisable(model.getItemId())) {
                    viewHolder.mBinding.btItemNotyfy.setBackground(activity.getResources().getDrawable(R.drawable.background_for_buttons_disble));
                    viewHolder.mBinding.btItemNotyfy.setClickable(false);
                    viewHolder.mBinding.btItemNotyfy.setEnabled(false);
                } else {
                    viewHolder.mBinding.btItemNotyfy.setBackground(activity.getResources().getDrawable(R.drawable.background_for_buttons));
                    viewHolder.mBinding.btItemNotyfy.setClickable(true);
                    viewHolder.mBinding.btItemNotyfy.setEnabled(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void itemAddRemove(@NonNull ViewHolder viewHolder, ItemListModel model, boolean addItem, boolean addBtn) {
        // progress bar
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
        progressBar = viewHolder.mBinding.ivProgress;
        try {
            if (new Utils(activity).isNetworkAvailable()) {
                int FreeItemQuantity = 0;
                double FreeWalletPoint = 0;
                boolean addFlag = false;
                boolean flashDealFlag = false;
                flashofferFlag = false;
                double calUnitPrice = 0;
                boolean isflashDealUsed = false;
                boolean isPrimeItem = false;

                // get item limit
                int itemQuantity = Integer.parseInt(viewHolder.tvselectedItemQuantity.getText().toString());

                //get cart data
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
                                if (model.getFlashDealMaxQtyPersonCanTake() >= itemQuantity + model.getMinOrderQty() && model.getOfferQtyAvaiable() >= itemQuantity + model.getMinOrderQty()) {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (addItem) {
                    // plus btn
                    if (!flashDealFlag) {
                        itemQuantity += model.getMinOrderQty();
                        //Item Limit code
                        if (model.isItemLimit) {
                            addFlag = setItemLimit(viewHolder, model, addFlag, itemQuantity, true);
                        } else {
                            if (itemQuantity > 0) {
                                if (model.getBillLimitQty() != 0) {
                                    //bill limit
                                    addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity);
                                } else {
                                    viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                                    addFlag = true;
                                }
                            }
                        }
                    } else {
                        if (addBtn) {
                            viewHolder.mBinding.visible.setVisibility(View.VISIBLE);
                            viewHolder.mBinding.addItemBtn.setVisibility(View.GONE);
                            Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.no_item_available), Toast.LENGTH_SHORT).show();
                        } else {
                            if (model.getOfferQtyAvaiable() <= itemQuantity + model.getMinOrderQty()) {
                                Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.no_item_available), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.only_add_maximum_item) + " " + model.getFlashDealMaxQtyPersonCanTake(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    // minus btn
                    if (itemQuantity > 0) {
                        itemQuantity -= model.getMinOrderQty();
                        if (model.isItemLimit) {
                            addFlag = setItemLimit(viewHolder, model, addFlag, itemQuantity, false);
                        } else {
                            viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                            addFlag = true;
                        }
                    } else {
                        viewHolder.mBinding.addItemBtn.setVisibility(View.VISIBLE);
                        // analytics for remove from cart
                        MyApplication.getInstance().updateAnalyticsCart(FirebaseAnalytics.Event.REMOVE_FROM_CART, model);
                    }
                }
                if (addFlag) {
                    progressBar.setVisibility(View.VISIBLE);
                    // offer section
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
                                                    viewHolder.mBinding.tvFreeItemQut.setText(sfreewalletDP);
                                                }
                                            } else {
                                                viewHolder.mBinding.tvFreeItemQut.setText("0");
                                            }
                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.mini_qty_should_not_be_zero), Toast.LENGTH_SHORT).show();
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
                                                    viewHolder.mBinding.tvFreeItemQut.setText(String.valueOf(FreeItemQuantity));
                                                }
                                            } else {
                                                viewHolder.mBinding.tvFreeItemQut.setText("0");
                                            }
                                        }
                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.mini_qty_should_not_be_zero), Toast.LENGTH_SHORT).show();
                                    }
                                }/* else {
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
                                    flashofferFlag = true;
                                } else {
                                    flashofferFlag = false;
                                }
                                break;
                        }
                    }
                    if (flashofferFlag) {
                        String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(itemQuantity * model.getFlashDealSpecialPrice());
                        viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                        calUnitPrice = model.getFlashDealSpecialPrice();
                    } else if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem) {
                        String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format((itemQuantity * model.getPrimePrice()));
                        viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                        calUnitPrice = model.getPrimePrice();
                        isPrimeItem = true;
                    } else {
                        String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format((itemQuantity * model.getUnitPrice()));
                        viewHolder.mBinding.tvSelectedItemPrice.setText(Html.fromHtml(price));
                        calUnitPrice = model.getUnitPrice();
                    }

                    activity.addItemInCartItemArrayList(model.getItemId(),
                            itemQuantity, calUnitPrice, model,
                            FreeItemQuantity, FreeWalletPoint, isPrimeItem, this);
                }
            } else {
                Utils.setToast(activity, MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean setItemLimit(@NonNull ViewHolder viewHolder, ItemListModel model, boolean addFlag, int itemQuantity, boolean add) {
        try {
            QtyDTO qtyDTO = MyApplication.getInstance().noteRepository.getQtyTotalQtyByMrpId(model.getItemId(), model.getItemMultiMRPId());
            int total = qtyDTO.getQuantity();
            int availqty = 0;
            int totalItemqty = qtyDTO.getTotalQuantity();
            int itemlimitqty = 0;
            if (add) {
                totalItemqty += model.getMinOrderQty();
            } else {
                totalItemqty -= model.getMinOrderQty();
            }
            itemlimitqty = model.getItemLimitQty();
//            if (total > 0) {
            if (add) {
                if (itemQuantity + total > itemlimitqty) {
                    Utils.setToast(activity, MyApplication.getInstance().dbHelper.getString(R.string.additemToast)
                            + " " + model.getItemLimitQty() + " " +
                            MyApplication.getInstance().dbHelper.getString(R.string.additemToast_2));
                } else {
                    if (model.getBillLimitQty() != 0) {
                        addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity);
                    } else {
                        viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                        addFlag = true;
                    }

                    if (addFlag) {
                        availqty = itemlimitqty - totalItemqty;
                        if (availqty >= 0) {
                            viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
                        }
                    }
                }
            } else {
                availqty = itemlimitqty - totalItemqty;
                if (availqty >= 0) {
                    viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
                }
                viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                addFlag = true;
            }
//            } else {
//                if (itemQuantity > 0) {
//                    if (itemQuantity > model.getItemlimitQty()) {
//                        Utils.setToast(activity, MyApplication.getInstance().dbHelper.getString(R.string.additemToast)
//                                + " " + model.getItemlimitQty() + " " + MyApplication.getInstance().dbHelper.getString(R.string.additemToast_2));
//                    } else {
//                        if (model.getBillLimitQty() != 0) {
//                            addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity);
//                        } else {
//                            viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
//                            addFlag = true;
//                        }
//                        if (addFlag) {
//                            availqty = itemlimitqty - itemQuantity;
//                            if (availqty >= 0) {
//                                viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
//                            }
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addFlag;
    }

    private boolean setBillLimit(@NonNull ViewHolder viewHolder, ItemListModel model, boolean addFlag, int itemQuantity) {
        try {
            int total = MyApplication.getInstance().noteRepository.getQtyByMultiMrp(model.getItemId(), model.getItemMultiMRPId());
            int itemlimitqty = 0;
            itemlimitqty = model.getBillLimitQty();
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


    @Override
    public void onItemClick(int position, boolean itemAdded) {
        progressBar.setVisibility(View.INVISIBLE);
        if (!itemAdded)
            notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CategoryOrderItemsBinding mBinding;

        private final ImageView ivItemImage;
        private final TextView tvItemName;
        private final TextView tvSelectedItemPrice;
        private final TextView tvselectedItemQuantity;

        private final TextView tvDreamPoint;
        private final TextView tvOffer;
        private final TextView freeDepePointTV;
        private final TextView leftItemsTV;
        private final TextView buyValueText;
        private final TextView GetValueTextView;
        private final TextView tvFlashdealPrice;
        private final TextView remaingSecTV;
        private final ImageView freeItemIV;
        private final LinearLayout mainOfferViewLL;
        private final LinearLayout flashOfferViewLL;

        public ViewHolder(CategoryOrderItemsBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            tvSelectedItemPrice = mBinding.tvSelectedItemPrice;
            tvselectedItemQuantity = mBinding.tvSelectedItemQuantity;
            ivItemImage = mBinding.productImage;
            tvItemName = mBinding.tvItemName;
            tvDreamPoint = mBinding.tvDreamPoint;
            tvOffer = mBinding.tvOffer;
            freeItemIV = mBinding.ivFreeIteam;
            mainOfferViewLL = mBinding.liOfferView;
            freeDepePointTV = mBinding.tvFreeDepePoint;
            flashOfferViewLL = mBinding.llFlashOfferView;
            leftItemsTV = mBinding.tvLeftItems;
            buyValueText = mBinding.tvBuyValue;
            GetValueTextView = mBinding.tvGetValue;
            remaingSecTV = mBinding.tvRemaingSecand;
            tvFlashdealPrice = mBinding.tvFlashdealPrice;
            customRunnable = new CustomRunnable(handler, remaingSecTV, 10000);
        }

        public void bind(CategoryItemOrderInfo obj) {
            mBinding.executePendingBindings();
        }

        void timerExpire(long endTime) {
            handler.removeCallbacks(customRunnable);
            customRunnable.holder = remaingSecTV;
            customRunnable.millisUntilFinished = endTime; //Current time - received time
            handler.postDelayed(customRunnable, 1000);
        }
    }
}