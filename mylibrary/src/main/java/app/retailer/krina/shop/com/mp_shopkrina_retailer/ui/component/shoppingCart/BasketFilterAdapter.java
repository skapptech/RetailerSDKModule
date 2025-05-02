package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.CategoryOrderItemsBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.productDetails.ProductDetailsActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.CategoryItemOrderInfo;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.MoqAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CustomRunnable;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RxBus;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;

public class BasketFilterAdapter extends RecyclerView.Adapter<BasketFilterAdapter.ViewHolder> implements OnItemClick {
    private final ShoppingCartActivity activity;
    private ArrayList<ItemListModel> list;

    private final Handler handler = new Handler();
    private MoqAdapter adapter;
    private final Drawable vectorDrawable;
    private boolean flashofferFlag = false;
    private CustomRunnable customRunnable;


    public BasketFilterAdapter(ShoppingCartActivity activity, ArrayList<ItemListModel> list) {
        this.activity = activity;
        this.list = list;
        vectorDrawable = AppCompatResources.getDrawable(activity, R.drawable.logo_grey);
    }

    public void setItemListCategory(ArrayList<ItemListModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(CategoryOrderItemsBinding.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                viewGroup,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            ItemListModel model = list.get(i);
            //set String
            viewHolder.mBinding.tvMrpText.setText(MyApplication.getInstance().dbHelper.getString(R.string.item_mrp));
            viewHolder.mBinding.tvRemainingQtyText.setText(MyApplication.getInstance().dbHelper.getString(R.string.remaining_qty) + " ");
            viewHolder.mBinding.tvItemLeftText.setText(MyApplication.getInstance().dbHelper.getString(R.string.item_left));
            viewHolder.mBinding.tvEndInText.setText(MyApplication.getInstance().dbHelper.getString(R.string.end_in_text));
            viewHolder.mBinding.tvFreeItemNotActiveText.setText(MyApplication.getInstance().dbHelper.getString(R.string.inactive_customer_msg));
            viewHolder.btnAdd.setText(MyApplication.getInstance().dbHelper.getString(R.string.add_btn));

            // set MOQ
            if (model.moqList.size() > 0) {
                viewHolder.tvMoq.setVisibility(View.VISIBLE);
                viewHolder.tvMultiMoq.setVisibility(View.GONE);
                viewHolder.tvMoq.setText(MyApplication.getInstance().dbHelper.getString(R.string.item_moq) + " " + model.getMinOrderQty());
            } else {
                viewHolder.tvMoq.setVisibility(View.GONE);
                viewHolder.tvMultiMoq.setVisibility(View.VISIBLE);
            }

            viewHolder.tvMultiMoq.setText(MyApplication.getInstance().dbHelper.getString(R.string.item_moq) + " " + model.getMinOrderQty());

            if (!TextUtils.isNullOrEmpty(model.getScheme())) {
                viewHolder.mBinding.tvSchemeText.setVisibility(View.VISIBLE);
                viewHolder.mBinding.tvSchemeText.setText("" + model.getScheme());
            } else {
                viewHolder.mBinding.tvSchemeText.setVisibility(View.GONE);
            }

            viewHolder.tvItemName.setText(model.itemname);
            viewHolder.leftItemsTV.setBackgroundResource(R.drawable.ic_count_bg);
            viewHolder.remaingSecTV.setBackgroundResource(R.drawable.ic_count_bg);
            // set value on UI
            setValueInUI(i, viewHolder, model);
            model.isChecked = true;
            // Minis Btn clicked
            viewHolder.ivMinusBtn.setOnClickListener(view -> {
                // click effect
                Utils.buttonEffect(viewHolder.ivMinusBtn);
                itemAddRemove(viewHolder, model, false, false);
            });
            // plus Btn clicked
            viewHolder.ivPlusBtn.setOnClickListener(view -> {
                Utils.buttonEffect(viewHolder.ivPlusBtn);
                itemAddRemove(viewHolder, model, true, false);
            });
            // fav section
            viewHolder.mBinding.favItem.setOnClickListener(v -> {
                addRemoveFav(model, viewHolder);
            });
            // Add Btn clicked
            viewHolder.btnAdd.setOnClickListener(v -> {
                viewHolder.mBinding.visible.setVisibility(View.VISIBLE);
                viewHolder.btnAdd.setVisibility(View.GONE);
                itemAddRemove(viewHolder, model, true, true);
            });
            // checkout clicked
            viewHolder.LLItemMain.setOnClickListener(v -> {
                viewHolder.LLItemMain.setClickable(false);
                detailsScree(model);
            });

            // MOQ popup open here
            viewHolder.tvMultiMoq.setOnClickListener(v -> {
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

                item_name.setText(model.itemname);
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
                        // Do something after 100ms
                        dialog.dismiss();
                    }, 400);
                };
                adapter = new MoqAdapter(activity, model.moqList, listener);
                mMoqPriceList.setAdapter(adapter);
                dialog.show();
            });
        } catch (Exception e) {
            e.printStackTrace();
            activity.startActivity(new Intent(activity, HomeActivity.class));
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    private void detailsScree(ItemListModel model) {
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
        args.putSerializable("ITEM_LIST", model);
        intent.putExtras(args);
        activity.startActivity(intent);
    }

    private void addRemoveFav(ItemListModel model, @NonNull ViewHolder viewHolder) {
        if (MyApplication.getInstance().noteRepository.isItemWishList(model.getItemId())) {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite);
            MyApplication.getInstance().noteRepository.deleteTask(model);
            Utils.addFav(model.getItemId(), false, activity);
        } else {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red);
            MyApplication.getInstance().noteRepository.insertTask(model);
            Utils.addFav(model.getItemId(), true, activity);
        }
    }

    private void setValueInUI(int i, @NonNull ViewHolder viewHolder, ItemListModel model) {
        /*Fav section*/
        flashofferFlag = false;
        try {
            // check item in wishList
            if (MyApplication.getInstance().noteRepository.isItemWishList(model.getItemId())) {
                viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red);
            } else {
                viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite);
            }

            //set offer UI
            if (model.isOffer) {
                viewHolder.tvOffer.setVisibility(View.GONE);
                viewHolder.mainOfferViewLL.setVisibility(View.VISIBLE);
                viewHolder.flashOfferViewLL.setVisibility(View.GONE);
                viewHolder.mBinding.favItem.setVisibility(View.GONE);
                viewHolder.tvFlashdealPrice.setVisibility(View.GONE);
                String spItemName = model.itemname;
                String freeOfferTextBuy = "<font color=#fe4e4e>Buy&nbsp;" + model.getOfferMinimumQty() + "&nbsp;pcs&nbsp;</font>" + "<font color=#000000>&nbsp;" + spItemName + " </font>";
                String freeOfferTextGet = "";

                viewHolder.buyValueText.setText(Html.fromHtml(freeOfferTextBuy));

                switch (model.getOfferType()) {
                    case "WalletPoint":
                        // String sfreewalletDP = new DecimalFormat("##.##").format(model.getOfferWalletPoint());
                        viewHolder.freeItemQutTV.setText("0");
                        viewHolder.freeItemIV.setBackgroundResource(R.drawable.ic_gift_bg);
                        viewHolder.freeDepePointTV.setText(MyApplication.getInstance().dbHelper.getString(R.string.Dp));
                        String sfreewalletDP = new DecimalFormat("##.##").format(model.getOfferWalletPoint());
                        freeOfferTextGet = "<font color=#fe4e4e>Get&nbsp;" + sfreewalletDP + "&nbsp;Free</font>" + "<font color=#000000>&nbsp;Dream Points</font>";
                        break;
                    case "ItemMaster":
                        // viewHolder.freeItemQutTV.setText(String.valueOf(model.getOfferFreeItemQuantity()));
                        if (!TextUtils.isNullOrEmpty(model.getOfferFreeItemImage())) {
                            Glide.with(activity).load(model.getOfferFreeItemImage()).placeholder(vectorDrawable).into(viewHolder.freeItemIV);
                        } else {
                            viewHolder.freeItemIV.setImageDrawable(vectorDrawable);
                        }
                        viewHolder.freeItemQutTV.setText("0");
                        viewHolder.freeDepePointTV.setText(MyApplication.getInstance().dbHelper.getString(R.string.free));
                        // String[] spItemFreeItemName = model.getOfferFreeItemName().split("(?<=\\D)(?=\\d)");
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
                            flashofferFlag = true;
                            //end time
                            //  timeDeference(moqPojoArrayList, viewHolder);
                            if (model.getFlashDealSpecialPrice() != 0) {
                                viewHolder.tvPrice.setPaintFlags(viewHolder.tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                viewHolder.tvFlashdealPrice.setText(" | " + new DecimalFormat("##.##").format(model.getFlashDealSpecialPrice()));
                            }
                            long currMillis = System.currentTimeMillis();
                            SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.myFormat, Locale.getDefault());
                            sdf1.setTimeZone(TimeZone.getDefault());
                            Date endTime = sdf1.parse(model.getOfferEndTime());
                            long millse = endTime.getTime() - currMillis;
                            viewHolder.timerExpire(millse);
                        } else {
                            flashofferFlag = false;
                            viewHolder.mainOfferViewLL.setVisibility(View.GONE);
                            viewHolder.flashOfferViewLL.setVisibility(View.GONE);
                            viewHolder.mBinding.favItem.setVisibility(View.VISIBLE);
                            viewHolder.tvFlashdealPrice.setVisibility(View.GONE);
                            viewHolder.tvPrice.setPaintFlags(0);
                        }
                        break;
                }
                viewHolder.GetValueTextView.setText(Html.fromHtml(freeOfferTextGet));

            } else {
                viewHolder.mainOfferViewLL.setVisibility(View.GONE);
                viewHolder.flashOfferViewLL.setVisibility(View.GONE);
                viewHolder.mBinding.favItem.setVisibility(View.VISIBLE);
                viewHolder.tvFlashdealPrice.setVisibility(View.GONE);
                viewHolder.tvPrice.setPaintFlags(0);
            }
            if (!TextUtils.isNullOrEmpty(model.getLogoUrl())) {
                Glide.with(activity).load(model.getLogoUrl()).placeholder(vectorDrawable).into(viewHolder.ivItemImage);
            } else {
                viewHolder.ivItemImage.setImageDrawable(vectorDrawable);
            }
            String sPRICE = "| <font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(model.getUnitPrice()) + "</font>";
            String sMargin = MyApplication.getInstance().dbHelper.getString(R.string.moq_margin) + " " + new DecimalFormat("##.##").format(Double.parseDouble(model.marginPoint)) + "%";
            String sMRP = new DecimalFormat("##.##").format(model.price);
            //set values
            viewHolder.tvMrp.setText(sMRP);
            viewHolder.tvMrp.setPaintFlags(viewHolder.tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tvPrice.setText(Html.fromHtml(sPRICE));
            viewHolder.tvMargin.setText(sMargin);
            viewHolder.tvDreamPoint.setText(MyApplication.getInstance().dbHelper.getString(R.string.Dp) + " " + model.dreamPoint);
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
                viewHolder.mBinding.tvUnlock.setText("Unlock");
                viewHolder.mBinding.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_right_arrow, 0);
            }

            ItemListModel cartModel = MyApplication.getInstance().noteRepository.getItemByMrpId(model.getItemId(), model.getItemMultiMRPId());

            boolean isItemFound;
            if (cartModel != null && model.getItemId() == cartModel.getItemId()) {
                isItemFound = true;
                int itemQuantity = cartModel.qty;
                // offer section start
                if (model.isOffer) {
                    if (model.getOfferType().equalsIgnoreCase("WalletPoint")) {
                        double freeWalletPoint = model.getTotalFreeWalletPoint();
                        String sfreewalletDP = new DecimalFormat("##.##").format(freeWalletPoint);
                        if (freeWalletPoint > 0) {
                            viewHolder.freeItemQutTV.setText(sfreewalletDP);
                        }
                    } else if (model.getOfferType().equalsIgnoreCase("ItemMaster")) {
                        int freeItemQuantity = Integer.parseInt(model.getOfferFreeItemQuantity());
                        if (freeItemQuantity > 0) {
                            viewHolder.freeItemQutTV.setText("" + freeItemQuantity);
                        }
                    } else {
                        int remainingLeft = model.getOfferQtyAvaiable() - itemQuantity;
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
            if (!isItemFound) {
                int itemQuantity = 0;
                viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                Logger.logD("Adapter", "ItemQ3::" + itemQuantity);
                String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(itemQuantity * model.getUnitPrice());
                viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                viewHolder.btnAdd.setVisibility(View.VISIBLE);
                viewHolder.mBinding.visible.setVisibility(View.GONE);
            } else {
                viewHolder.btnAdd.setVisibility(View.GONE);
                viewHolder.mBinding.visible.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void itemAddRemove(@NonNull ViewHolder viewHolder, ItemListModel model, boolean addItem, boolean addBtn) {
        try {
            int FreeItemQuantity = 0;
            int total = 0;
            int totalItemqty = 0;
            int itemlimitqty = 0;
            double FreeWalletPoint = 0;
            boolean addFlag = false;
            boolean flashDealFlag = false;
            flashofferFlag = false;
            double calUnitPrice = 0;
            boolean isflashDealUsed = false;
            boolean isPrimeItem = false;
            int availqty = 0;

            //get item limit
            int itemQuantity = Integer.parseInt(viewHolder.tvselectedItemQuantity.getText().toString());
            itemlimitqty = model.getItemLimitQty();

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
                            if (model.getFlashDealMaxQtyPersonCanTake() > itemQuantity && model.getOfferQtyAvaiable() > itemQuantity) {
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
                        addFlag = setlimit(viewHolder, model, addFlag, itemQuantity);

                    } else {
                        if (model.getBillLimitQty() != 0) {
                            if (model.getBillLimitQty() >= itemQuantity) {
                                viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                                addFlag = true;
                                availqty = itemlimitqty - totalItemqty;
                                if (availqty >= 0) {
                                    viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
                                }
                            } else {
                                Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.you_can_not_add_more_than) + " " + model.getBillLimitQty() + " " + MyApplication.getInstance().dbHelper.getString(R.string.item_t), Toast.LENGTH_SHORT).show();
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
                    if (addBtn) {
                        viewHolder.mBinding.visible.setVisibility(View.GONE);
                        viewHolder.btnAdd.setVisibility(View.VISIBLE);
                        Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.no_item_available), Toast.LENGTH_SHORT).show();
                    } else {
                        if (model.getOfferQtyAvaiable() <= itemQuantity) {
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
                    availqty = availqty + model.getMinOrderQty();
                    if (availqty >= 0) {
                        viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
                    }
                    viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                    addFlag = true;
                } else {
                    viewHolder.btnAdd.setVisibility(View.VISIBLE);
                }
            }

            if (addFlag) {
                /*offer section*/
                if (model.isOffer) {
                    int offerMinimumQty = model.getOfferMinimumQty();
                    boolean customerActive = SharePrefs.getInstance(activity).getBoolean(SharePrefs.CUST_ACTIVE);
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
                                        Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.mini_qty_should_not_be_zero), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.inactive_customer_msg), Toast.LENGTH_SHORT).show();
                            }
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
                                    Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.mini_qty_should_not_be_zero), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.inactive_customer_msg), Toast.LENGTH_SHORT).show();
                            }
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
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format((itemQuantity * model.getFlashDealSpecialPrice()));
                    viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                    calUnitPrice = model.getFlashDealSpecialPrice();
                } else if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem) {
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format((itemQuantity * model.getPrimePrice()));
                    viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                    calUnitPrice = model.getPrimePrice();
                    isPrimeItem = true;
                } else {
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format((itemQuantity * model.getUnitPrice()));
                    viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                    calUnitPrice = model.getUnitPrice();
                }
                // add to cart
                activity.addItemInCartItemArrayList(model.getItemId(), itemQuantity, calUnitPrice, model,
                        FreeItemQuantity, FreeWalletPoint, isPrimeItem, true, this, true);
                RxBus.getInstance().sendEvent(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean setlimit(ViewHolder viewHolder, ItemListModel model, boolean addFlag, int itemQuantity) {
        int total = MyApplication.getInstance().noteRepository.getQtyByMultiMrp(model.getItemId(), model.getItemMultiMRPId());
        int availqty = 0;
        int totalItemqty = MyApplication.getInstance().noteRepository.getQtyByMultiMrp(model.getItemMultiMRPId());
        int itemlimitqty = 0;
        totalItemqty += model.getMinOrderQty();
        itemlimitqty = model.getItemLimitQty();
//        if (total > 0) {
        if (total + itemQuantity > itemlimitqty) {
            Utils.setToast(activity, MyApplication.getInstance().dbHelper.getString(R.string.additemToast) + " " + model.getItemLimitQty() + " " + MyApplication.getInstance().dbHelper.getString(R.string.additemToast_2));
        } else {
            if (model.getBillLimitQty() != 0) {
                if (model.getBillLimitQty() >= itemQuantity) {
                    viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                    addFlag = true;
                    availqty = itemlimitqty - totalItemqty;
                    if (availqty >= 0) {
                        viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
                    }
                } else {
                    Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.you_can_not_add_more_than) + " " + model.getBillLimitQty() + " " + MyApplication.getInstance().dbHelper.getString(R.string.item_t), Toast.LENGTH_SHORT).show();
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
//        } else {
//            if (itemQuantity > 0) {
//                if (itemQuantity > model.getItemlimitQty()) {
//                    Utils.setToast(activity, MyApplication.getInstance().dbHelper.getString(R.string.additemToast) + " " + model.getItemlimitQty() + " " + MyApplication.getInstance().dbHelper.getString(R.string.additemToast_2));
//                } else {
//                    if (model.getBillLimitQty() != 0) {
//                        if (model.getBillLimitQty() >= itemQuantity) {
//                            viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
//                            addFlag = true;
//                            availqty = itemlimitqty - totalItemqty;
//                            if (availqty >= 0) {
//                                viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
//                            }
//                        } else {
//                            Toast.makeText(activity, MyApplication.getInstance().dbHelper.getString(R.string.you_can_not_add_more_than) + " " + model.getBillLimitQty() + " " + MyApplication.getInstance().dbHelper.getString(R.string.item_t), Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
//                        addFlag = true;
//                        availqty = itemlimitqty - totalItemqty;
//                        if (availqty >= 0) {
//                            viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
//                        }
//                    }
//                }
//            }
//        }
        return addFlag;
    }


    @Override
    public void onItemClick(int position, boolean itemAdded) {
        if (!itemAdded) {
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CategoryOrderItemsBinding mBinding;
        private final ImageView ivItemImage;
        private final TextView tvItemName;
        private final TextView tvSelectedItemPrice;
        private final TextView tvselectedItemQuantity;
        private final ImageView ivMinusBtn;
        private final ImageView ivPlusBtn;

        private final TextView tvMrp;
        private final TextView tvPrice;
        private final TextView tvMoq;
        private final TextView tvMultiMoq;
        private final TextView tvMargin;
        private final TextView tvDreamPoint;
        private final TextView tvOffer;
        private final TextView freeItemQutTV;
        private final TextView freeDepePointTV;
        private final Button btnAdd;
        private final TextView leftItemsTV;
        private final TextView buyValueText;
        private final TextView GetValueTextView;
        private final TextView tvFlashdealPrice;
        private final TextView remaingSecTV;
        private final ImageView freeItemIV;
        private final LinearLayout mainOfferViewLL;
        private final LinearLayout flashOfferViewLL;
        RelativeLayout relItemDetails;
        LinearLayout LLItemMain;

        public ViewHolder(CategoryOrderItemsBinding mbinding) {
            super(mbinding.getRoot());
            this.mBinding = mbinding;

            tvSelectedItemPrice = mBinding.tvSelectedItemPrice;
            relItemDetails = mBinding.itemDetails;
            LLItemMain = mBinding.LLItemMain;
            tvselectedItemQuantity = mBinding.tvSelectedItemQuantity;
            ivMinusBtn = mBinding.minusBtn;
            ivPlusBtn = mBinding.plusBtn;
            ivItemImage = mBinding.productImage;
            tvItemName = mBinding.tvItemName;
            tvMrp = mBinding.tvMrp;
            tvPrice = mBinding.tvPrice;
            tvMoq = mBinding.tvMoq;
            tvMargin = mBinding.tvMargin;
            tvDreamPoint = mBinding.tvDreamPoint;
            tvMultiMoq = mBinding.tvMultiMoq;
            tvOffer = mBinding.tvOffer;
            btnAdd = mBinding.addItemBtn;

            freeItemIV = mBinding.ivFreeIteam;
            freeItemQutTV = mBinding.tvFreeItemQut;
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

//        public void bind(CategoryItemOrderInfo obj) {
//            mBinding.executePendingBindings();
//        }

        public void timerExpire(long endTime) {
            handler.removeCallbacks(customRunnable);
            customRunnable.holder = remaingSecTV;
            customRunnable.millisUntilFinished = endTime; //Current time - received time
            handler.postDelayed(customRunnable, 1000);
        }
    }
}