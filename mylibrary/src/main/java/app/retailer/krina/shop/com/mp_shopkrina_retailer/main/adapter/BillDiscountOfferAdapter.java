package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemBillDiscountBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemScratchCardListBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnApplyOfferClick;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnSelectClick;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.payment.PaymentOptionActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.dialog.OfferInfoFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class BillDiscountOfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final PaymentOptionActivity activity;
    private ArrayList<BillDiscountModel> list;
    private final OnSelectClick onSelectClick;
    private final OnApplyOfferClick onApplyOfferClick;
    private String lang = "";


    public BillDiscountOfferAdapter(PaymentOptionActivity activity, ArrayList<BillDiscountModel> list, OnSelectClick onSelectClick, OnApplyOfferClick onApplyOfferClick) {
        this.activity = activity;
        this.list = list;
        this.onSelectClick = onSelectClick;
        this.onApplyOfferClick = onApplyOfferClick;
        lang = LocaleHelper.getLanguage(activity);
    }

    public void setBillDiscount(ArrayList<BillDiscountModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ScratchCardHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_scratch_card_list, parent, false));
        } else {
            return new BillDiscountHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_bill_discount, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        BillDiscountModel model = list.get(pos);
        if (holder.getItemViewType() == 0) {
            String llApplyColorCode;
            ((ScratchCardHolder) holder).mBinding.rlBill.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(model.getColorCode() != null ? model.getColorCode() : "#4D9654")));
            // set String
            ((ScratchCardHolder) holder).mBinding.tvDes.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.min_ord_value) + model.getBillAmount());
            ((ScratchCardHolder) holder).mBinding.tvMsg.setText(model.getMessage());
            ((ScratchCardHolder) holder).mBinding.tvTime.setText("00:00");
            if (list.get(pos).getImagePath() != null) {
                String path = model.getImagePath();
                if (!model.getImagePath().contains("https"))
                    path = EndPointPref.getInstance(activity).getBaseUrl() + model.getImagePath();
                Glide.with(activity).load(path)
                        .placeholder(R.drawable.logo_sk)
                        .into(((ScratchCardHolder) holder).mBinding.ivImage);
            } else {
                ((ScratchCardHolder) holder).mBinding.ivImage.setImageResource(R.drawable.scratch_card);
            }

            if (model.isScratchBDCode()) {
                ((ScratchCardHolder) holder).mBinding.tvTime.setTag(pos);
                ((ScratchCardHolder) holder).mBinding.tvOffer.setTypeface(((ScratchCardHolder) holder).mBinding.tvOffer.getTypeface(), Typeface.NORMAL);
                ((ScratchCardHolder) holder).mBinding.ivImage.setImageResource(R.drawable.logo_sk);
                if (model.getBillDiscountOfferOn() != null && model.getBillDiscountOfferOn().equalsIgnoreCase("Percentage")) {
                    ((ScratchCardHolder) holder).mBinding.tvOffer.setText(new DecimalFormat("##.##").format(model.getDiscountPercentage()) + "% "+ RetailerSDKApp.getInstance().dbHelper.getString(R.string.off));
                } else if (model.getBillDiscountOfferOn() != null && model.getBillDiscountOfferOn().equalsIgnoreCase("DynamicAmount")) {
                    ((ScratchCardHolder) holder).mBinding.tvOffer.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.flat_rs) + new DecimalFormat("##.##").format(model.getBillDiscountWallet()) + " "+ RetailerSDKApp.getInstance().dbHelper.getString(R.string.off));
                } else {
                    ((ScratchCardHolder) holder).mBinding.tvOffer.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.flat_rs) + new DecimalFormat("##.##").format(convertToAmount(model.getBillDiscountWallet())) + " "+ RetailerSDKApp.getInstance().dbHelper.getString(R.string.off));
                }
            } else {
                ((ScratchCardHolder) holder).mBinding.tvOffer.setTypeface(((ScratchCardHolder) holder).mBinding.tvOffer.getTypeface(), Typeface.BOLD);
                ((ScratchCardHolder) holder).mBinding.tvOffer.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_scratch_win));
            }
            if (model.isSelected) {
                ((ScratchCardHolder) holder).mBinding.rlMain.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_tealish_green_8dp_corner));
                llApplyColorCode = "#EBFBF3";
                ((ScratchCardHolder) holder).mBinding.tvSelect.setText(RetailerSDKApp.getInstance().noteRepository.getString(R.string.text_remove));
                ((ScratchCardHolder) holder).mBinding.tvSelect.setTextColor(ContextCompat.getColor(activity, R.color.red));
                ((ScratchCardHolder) holder).mBinding.tvMsg.setTextColor(ContextCompat.getColor(activity, R.color.black));
                ((ScratchCardHolder) holder).mBinding.tvMsg.setText(RetailerSDKApp.getInstance().noteRepository.getString(R.string.congrats_offer_applied));
            } else {
                if (model.isApplicable) {
                    // tap to apply
                    llApplyColorCode = "#FCF3F0";
                    ((ScratchCardHolder) holder).mBinding.tvSelect.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.tap_to_apply));
                    ((ScratchCardHolder) holder).mBinding.tvSelect.setTextColor(ContextCompat.getColor(activity, R.color.color_gray_txt));
                    ((ScratchCardHolder) holder).mBinding.rlMain.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_grey_8dp_corner));
                    ((ScratchCardHolder) holder).mBinding.tvMsg.setTextColor(ContextCompat.getColor(activity, R.color.black));
                    ((ScratchCardHolder) holder).mBinding.tvMsg.setText(RetailerSDKApp.getInstance().noteRepository.getString(R.string.congrats_offer_unlocked));
                } else {
                    ((ScratchCardHolder) holder).mBinding.rlMain.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_orange_8dp_corner));
                    ((ScratchCardHolder) holder).mBinding.tvMsg.setTextColor(ContextCompat.getColor(activity, R.color.red));
                    llApplyColorCode = "#f5f9ff";
                    if (EndPointPref.getInstance(activity).getBoolean(EndPointPref.showOfferBtn))
                        ((ScratchCardHolder) holder).mBinding.tvSelect.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.add_more));
                    else
                        ((ScratchCardHolder) holder).mBinding.tvSelect.setText("");
                    ((ScratchCardHolder) holder).mBinding.tvSelect.setTextColor(ContextCompat.getColor(activity, R.color.chinese_orange));
                }
            }

            long timestamp = getTimeStamp(model.getEnd());
            long expiryTime = timestamp - new Date().getTime();
            ((ScratchCardHolder) holder).timer = new CountDownTimer(expiryTime, 1000) {
                @Override
                public void onTick(long millis) {
                    long day = TimeUnit.MILLISECONDS.toDays(millis);
                    if (day > 0) {
                        ((ScratchCardHolder) holder).mBinding.tvTime.setText("Expires in " + day + " day");
                    } else {
                        long hour = TimeUnit.MILLISECONDS.toHours(millis);
                        if (hour > 0) {
                            ((ScratchCardHolder) holder).mBinding.tvTime.setText("Expires in " + hour % 24 + " hour");
                        } else {
                            long sec = TimeUnit.MILLISECONDS.toSeconds(millis);
                            long min = TimeUnit.MILLISECONDS.toMinutes(millis);
                            ((ScratchCardHolder) holder).mBinding.tvTime.setText("Expires in " + min % 60 + ":" + sec % 60);
                        }
                    }
                }

                @Override
                public void onFinish() {
                    ((ScratchCardHolder) holder).mBinding.tvTime.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_time_expire));
                }
            }.start();

            int llApplyDynamicColor = Color.parseColor(llApplyColorCode);
            ((ScratchCardHolder) holder).mBinding.llApply.setBackgroundTintList(ColorStateList.valueOf(llApplyDynamicColor));

        } else {
            String llApplyColorCode;
            if (list.get(pos).getApplyType().equalsIgnoreCase("PrimeCustomer")) {
                ((BillDiscountHolder) holder).mBinding.tvPrimeOffer.setVisibility(View.VISIBLE);
            } else {
                ((BillDiscountHolder) holder).mBinding.tvPrimeOffer.setVisibility(View.GONE);
            }

            if (model.getImagePath() != null) {
                String path = model.getImagePath();
                if (!model.getImagePath().contains("https"))
                    path = EndPointPref.getInstance(activity).getBaseUrl() + model.getImagePath();
                Glide.with(activity).load(path)
                        .placeholder(R.drawable.logo_sk)
                        .into(((BillDiscountHolder) holder).mBinding.ivImg);
            } else {
                ((BillDiscountHolder) holder).mBinding.ivImg.setImageResource(R.drawable.logo_sk);
            }

            ((BillDiscountHolder) holder).mBinding.tvOfferDes.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.min_ord_value) + list.get(pos).getBillAmount());
            ((BillDiscountHolder) holder).mBinding.tvSelect.setTag(pos);
            ((BillDiscountHolder) holder).mBinding.rlBillItem.setVisibility(View.GONE);
            if (model.getBillDiscountOfferOn() != null && model.getBillDiscountOfferOn().equalsIgnoreCase("Percentage")) {
                ((BillDiscountHolder) holder).mBinding.tvOffer.setText(new DecimalFormat("##.##").format(model.getDiscountPercentage()) + "% " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.bill_discount));
            } else if (model.getBillDiscountOfferOn() != null && model.getBillDiscountOfferOn().equalsIgnoreCase("FreeItem")) {
                ((BillDiscountHolder) holder).mBinding.rlBillItem.setVisibility(View.VISIBLE);
                ((BillDiscountHolder) holder).mBinding.tvOffer.setText(R.string.free_item_offer);
                BillDiscountFreeItemAdapter discountItemAdapter = new BillDiscountFreeItemAdapter(activity, model.getRetailerBillDiscountFreeItemDcs());
                ((BillDiscountHolder) holder).mBinding.recyclerBillDiscountItem.setAdapter(discountItemAdapter);
                //
                ((BillDiscountHolder) holder).mBinding.tvItemName.setVisibility(View.VISIBLE);
                if (model.isBillDiscountFreebiesItem()) {
                    ((BillDiscountHolder) holder).mBinding.tvItemName.setText("On every purchase of "
                            + model.getOfferMinOrderQty() + " " + model.getOfferitemname() + " Get free");
                } else if (model.isBillDiscountFreebiesValue()) {
                    ((BillDiscountHolder) holder).mBinding.tvItemName.setText("On every purchase worth rupees "
                            + model.getBillAmount() + " Get free");
                } else ((BillDiscountHolder) holder).mBinding.tvItemName.setVisibility(View.GONE);
            } else {
                String msgPostBill = model.getApplyOn().equalsIgnoreCase("PostOffer") ? RetailerSDKApp.getInstance().dbHelper.getString(R.string.postoffer) : "";
                if (model.getWalletType().equalsIgnoreCase("WalletPercentage")) {
                    ((BillDiscountHolder) holder).mBinding.tvOffer.setText(new DecimalFormat("##.##").format(model.getBillDiscountWallet()) + "% "+ RetailerSDKApp.getInstance().dbHelper.getString(R.string.off) + msgPostBill);
                } else {
                    ((BillDiscountHolder) holder).mBinding.tvOffer.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.flat_rs) + new DecimalFormat("##.##").format(convertToAmount(model.getBillDiscountWallet())) + " "+ RetailerSDKApp.getInstance().dbHelper.getString(R.string.off) + msgPostBill);
                }
            }
            ((BillDiscountHolder) holder).mBinding.tvMinQty.setText("( " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.min_ord_value) + new DecimalFormat("##.##").format(model.getBillAmount()) + " )");
            long timestamp = getTimeStamp(model.getEnd());
            long expiryTime = timestamp - new Date().getTime();
            ((BillDiscountHolder) holder).timer = new CountDownTimer(expiryTime, 1000) {
                @Override
                public void onTick(long millis) {
                    long day = TimeUnit.MILLISECONDS.toDays(millis);
                    if (day > 0) {
                        ((BillDiscountHolder) holder).mBinding.tvTime.setText("Expires in " + day + " day");
                    } else {
                        long hour = TimeUnit.MILLISECONDS.toHours(millis);
                        if (hour > 0) {
                            ((BillDiscountHolder) holder).mBinding.tvTime.setText("Expires in " + hour % 24 + " hour");
                        } else {
                            long sec = TimeUnit.MILLISECONDS.toSeconds(millis);
                            long min = TimeUnit.MILLISECONDS.toMinutes(millis);
                            ((BillDiscountHolder) holder).mBinding.tvTime.setText("Expires in " + min % 60 + ":" + sec % 60);
                        }
                    }
                }

                @Override
                public void onFinish() {
                    ((BillDiscountHolder) holder).mBinding.tvTime.setText("Time Expired!");
                }
            }.start();

            ((BillDiscountHolder) holder).mBinding.tvMsg.setText(model.getMessage());
            if (model.isSelected) {
                // offer applied
                llApplyColorCode = "#EBFBF3";
                ((BillDiscountHolder) holder).mBinding.tvSelect.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_remove));
                ((BillDiscountHolder) holder).mBinding.tvSelect.setTextColor(ContextCompat.getColor(activity, R.color.red));
                ((BillDiscountHolder) holder).mBinding.rlMain.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_tealish_green_8dp_corner));
                ((BillDiscountHolder) holder).mBinding.tvMsg.setTextColor(ContextCompat.getColor(activity, R.color.black));
                ((BillDiscountHolder) holder).mBinding.tvMsg.setText(RetailerSDKApp.getInstance().noteRepository.getString(R.string.congrats_offer_applied));
            } else {
                if (model.isApplicable) {
                    // tap to apply
                    llApplyColorCode = "#FCF3F0";
                    ((BillDiscountHolder) holder).mBinding.tvSelect.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.tap_to_apply));
                    ((BillDiscountHolder) holder).mBinding.tvSelect.setTextColor(ContextCompat.getColor(activity, R.color.color_gray_txt));
                    ((BillDiscountHolder) holder).mBinding.rlMain.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_grey_8dp_corner));
                    ((BillDiscountHolder) holder).mBinding.tvMsg.setTextColor(ContextCompat.getColor(activity, R.color.black));
                    ((BillDiscountHolder) holder).mBinding.tvMsg.setText(RetailerSDKApp.getInstance().noteRepository.getString(R.string.congrats_offer_unlocked));
                } else {
                    llApplyColorCode = "#f5f9ff";
                    ((BillDiscountHolder) holder).mBinding.tvSelect.setTextColor(ContextCompat.getColor(activity, R.color.chinese_orange));
                    ((BillDiscountHolder) holder).mBinding.rlMain.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_orange_8dp_corner));
                    ((BillDiscountHolder) holder).mBinding.tvMsg.setTextColor(ContextCompat.getColor(activity, R.color.red));
                    if (EndPointPref.getInstance(activity).getBoolean(EndPointPref.showOfferBtn))
                        ((BillDiscountHolder) holder).mBinding.tvSelect.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.add_more));
                    else
                        ((BillDiscountHolder) holder).mBinding.tvSelect.setText("");
                }
            }
            ((BillDiscountHolder) holder).mBinding.rlBill.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(model.getColorCode() != null ? model.getColorCode() : "#4D9654")));

            int llApplyDynamicColor = Color.parseColor(llApplyColorCode);
            ((BillDiscountHolder) holder).mBinding.llApply.setBackgroundTintList(ColorStateList.valueOf(llApplyDynamicColor));
            if (model.getBillDiscountType().equals("ClearanceStock")) {
                ((BillDiscountHolder) holder).mBinding.tvSelect.setVisibility(View.INVISIBLE);
            } else {
                ((BillDiscountHolder) holder).mBinding.tvSelect.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getOfferOn().equalsIgnoreCase("ScratchBillDiscount") ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class BillDiscountHolder extends RecyclerView.ViewHolder {
        ItemBillDiscountBinding mBinding;
        CountDownTimer timer;

        BillDiscountHolder(ItemBillDiscountBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.ivInfo.setOnClickListener(view -> {
                new OfferInfoFragment().newInstance(0, list.get(getAdapterPosition()))
                        .show(activity.getSupportFragmentManager(), "a");
            });
            mBinding.tvSelect.setOnClickListener(view -> {
                if (!list.get(getAdapterPosition()).isSelected && !list.get(getAdapterPosition()).isApplicable && EndPointPref.getInstance(activity).getBoolean(EndPointPref.showOfferBtn)) {
                    Bundle args = new Bundle();
                    args.putSerializable("OfferModel", list.get(getAdapterPosition()));
                    args.putSerializable("OfferAddMore", true);
                    activity.startActivity(new Intent(activity, HomeActivity.class).putExtras(args));
//                 if (mBinding.tvSelect.getText() == MyApplication.getInstance().dbHelper.getString(R.string.add_more)) {
//                    Bundle args = new Bundle();
//                    args.putSerializable("model", list.get(getAdapterPosition()));
//                    activity.pushFragments(new OfferDetailFragment(), false, true, args);
                } else {
                    boolean isApplied = !list.get(getAdapterPosition()).isSelected;
                    CommonClassForAPI commonClassForAPI = CommonClassForAPI.getInstance(activity);
                    try {
                        if (commonClassForAPI != null) {
                            Utils.showProgressDialog(activity);
                            commonClassForAPI.getApplyDiscountResponse(new DisposableObserver<CheckoutCartResponse>() {
                                @Override
                                public void onNext(@NotNull CheckoutCartResponse response) {
                                    Utils.hideProgressDialog();
                                    if (response.getStatus()) {
                                        try {
                                            if (list.get(getAdapterPosition()).isSelected) {
                                                //  mBinding.rlBill.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_grey));
                                                //   mBinding.tvSelect.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_grey));
                                                mBinding.tvSelect.setTextColor(ContextCompat.getColor(activity, R.color.color_gray_txt));
                                                mBinding.tvSelect.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.tap_to_apply));
                                                int llApplyDynamicColor = Color.parseColor("#FCF3F0");
                                                mBinding.llApply.setBackgroundTintList(ColorStateList.valueOf(llApplyDynamicColor));
                                                mBinding.rlMain.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_grey_8dp_corner));
                                                mBinding.tvMsg.setTextColor(ContextCompat.getColor(activity, R.color.black));
                                                list.get(getAdapterPosition()).isSelected = false;
                                                mBinding.tvMsg.setText(RetailerSDKApp.getInstance().noteRepository.getString(R.string.congrats_offer_unlocked));
                                            } else {
                                                //  mBinding.rlBill.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_orange));
                                                //   mBinding.tvSelect.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_orange));
                                                mBinding.tvSelect.setTextColor(ContextCompat.getColor(activity, R.color.red));
                                                mBinding.tvSelect.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_remove));
                                                int llApplyDynamicColor = Color.parseColor("#EBFBF3");
                                                mBinding.llApply.setBackgroundTintList(ColorStateList.valueOf(llApplyDynamicColor));
                                                mBinding.rlMain.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_tealish_green_8dp_corner));
                                                mBinding.tvMsg.setTextColor(ContextCompat.getColor(activity, R.color.black));
                                                list.get(getAdapterPosition()).isSelected = true;
                                                mBinding.tvMsg.setText(RetailerSDKApp.getInstance().noteRepository.getString(R.string.congrats_offer_applied));

                                            }
                                            onApplyOfferClick.onApplyOfferClick(response);
                                            // update analytic apply promotion
                                            RetailerSDKApp.getInstance().updateAnalyticPromotion(list.get(getAdapterPosition()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Utils.setLongToast(activity, response.getMessage() != null ? response.getMessage() : "error");
                                    }
                                }

                                @Override
                                public void onError(@NotNull Throwable e) {
                                    Utils.hideProgressDialog();
                                }

                                @Override
                                public void onComplete() {
                                }
                            }, SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID), SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID), list.get(getAdapterPosition()).getOfferId(), isApplied, lang, "BillDiscountAdapter");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public class ScratchCardHolder extends RecyclerView.ViewHolder {
        ItemScratchCardListBinding mBinding;
        CountDownTimer timer;

        ScratchCardHolder(ItemScratchCardListBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.ivInfo.setOnClickListener(view -> {
                if (list.get(getAdapterPosition()).isScratchBDCode())
                    new OfferInfoFragment().newInstance(0, list.get(getAdapterPosition()))
                            .show(activity.getSupportFragmentManager(), "a");
                else
                    Utils.setToast(activity, "Scratch the card first");
            });
            mBinding.getRoot().setOnClickListener(view -> {
                if (!list.get(getAdapterPosition()).isScratchBDCode()) {
                    onSelectClick.onSelectClick(getAdapterPosition());
                } else {
                    if (!list.get(getAdapterPosition()).isSelected && !list.get(getAdapterPosition()).isApplicable && EndPointPref.getInstance(activity).getBoolean(EndPointPref.showOfferBtn)) {
                        Bundle args = new Bundle();
                        args.putSerializable("OfferModel", list.get(getAdapterPosition()));
                        args.putSerializable("OfferAddMore", true);
                        activity.startActivity(new Intent(activity, HomeActivity.class).putExtras(args));
                    } else {
                        boolean isApplied = !list.get(getAdapterPosition()).isSelected;
                        CommonClassForAPI commonClassForAPI = CommonClassForAPI.getInstance(activity);
                        try {
                            if (commonClassForAPI != null) {
                                Utils.showProgressDialog(activity);
                                commonClassForAPI.getApplyDiscountResponse(new DisposableObserver<CheckoutCartResponse>() {
                                    @Override
                                    public void onNext(@NotNull CheckoutCartResponse response) {
                                        Utils.hideProgressDialog();
                                        if (response.getStatus()) {
                                            try {
                                                if (list.get(getAdapterPosition()).isSelected) {
                                                    mBinding.tvSelect.setTextColor(ContextCompat.getColor(activity, R.color.chinese_orange));
                                                    mBinding.tvSelect.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.tap_to_apply));
                                                    int llApplyDynamicColor = Color.parseColor("#FCF3F0");
                                                    mBinding.llApply.setBackgroundTintList(ColorStateList.valueOf(llApplyDynamicColor));
                                                    mBinding.rlMain.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_grey_8dp_corner));
                                                    mBinding.tvMsg.setTextColor(ContextCompat.getColor(activity, R.color.black));
                                                    list.get(getAdapterPosition()).isSelected = false;
                                                    mBinding.tvMsg.setText(RetailerSDKApp.getInstance().noteRepository.getString(R.string.congrats_offer_unlocked));
                                                } else {
                                                    mBinding.tvSelect.setTextColor(ContextCompat.getColor(activity, R.color.tealish_green));
                                                    mBinding.tvSelect.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_btn_applied));
                                                    int llApplyDynamicColor = Color.parseColor("#EBFBF3");
                                                    mBinding.llApply.setBackgroundTintList(ColorStateList.valueOf(llApplyDynamicColor));
                                                    mBinding.rlMain.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_tealish_green_8dp_corner));
                                                    mBinding.tvMsg.setTextColor(ContextCompat.getColor(activity, R.color.black));
                                                    list.get(getAdapterPosition()).isSelected = true;
                                                    mBinding.tvMsg.setText(RetailerSDKApp.getInstance().noteRepository.getString(R.string.congrats_offer_applied));

                                                }
                                                onApplyOfferClick.onApplyOfferClick(response);
                                                // update analytic apply promotion
                                                RetailerSDKApp.getInstance().updateAnalyticPromotion(list.get(getAdapterPosition()));
                                            } catch (Exception e) {
                                                e.printStackTrace();

                                            }
                                        } else {
                                            Utils.setToast(activity, response.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onError(@NotNull Throwable e) {
                                        Utils.hideProgressDialog();
                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                }, SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID), SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID), list.get(getAdapterPosition()).getOfferId(), isApplied, lang, "BillDiscountAdapter");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private double convertToAmount(double amount) {
        return amount / 10;
    }

    private static long getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String all = dateStr.replaceAll("\\+0([0-9]){1}\\:00", "+0$100");
        String s = all.replaceAll("T", " ");
        long timestamp = 0;
        try {
            Date date = format.parse(s);
            timestamp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }
}