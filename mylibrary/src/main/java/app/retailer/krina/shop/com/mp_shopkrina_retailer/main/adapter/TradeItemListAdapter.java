package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.TradeOfferItemsBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.productDetails.ProductDetailsActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NotifyModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.MoqAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import pl.droidsonroids.gif.GifImageView;

public class TradeItemListAdapter extends RecyclerView.Adapter<TradeItemListAdapter.ViewHolder> implements OnItemClick {
    private final HomeActivity activity;
    private ArrayList<ItemListModel> list;

    private MoqAdapter adapter;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private GifImageView progressBar;


    public TradeItemListAdapter(HomeActivity activity, ArrayList<ItemListModel> list) {
        this.list = list;
        this.activity = activity;
    }

    public void setItemListCategory(ArrayList<ItemListModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(TradeOfferItemsBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                 viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        try {
            ItemListModel model = list.get(i);
            //set String
            viewHolder.mBinding.tvMrpText.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_mrp));
            viewHolder.mBinding.tvRemainingQtyText.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.remaining_qty) + " ");
            viewHolder.btnAdd.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.add_btn));

            viewHolder.mBinding.btItemNotyfy.setOnClickListener(v -> {
                activity.getNotifyItems(model.getWarehouseId(), model.getItemNumber());
                RetailerSDKApp.getInstance().noteRepository.insertNotifyItemTask(new NotifyModel(model.getItemId()));
                viewHolder.mBinding.btItemNotyfy.setBackground(activity.getResources().getDrawable(R.drawable.background_for_buttons_disble));
                viewHolder.mBinding.btItemNotyfy.setClickable(false);
                viewHolder.mBinding.btItemNotyfy.setEnabled(false);
            });

            Collections.sort(list, new MarginDescComparator());
            // set MOQ
            if (model.moqList.size() > 0) {
                viewHolder.tvMoq.setVisibility(View.GONE);
                viewHolder.tvMultiMoq.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvMoq.setVisibility(View.VISIBLE);
                viewHolder.tvMultiMoq.setVisibility(View.GONE);
            }
            viewHolder.tvMoq.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_moq)
                    + " " + model.getMinOrderQty());
            viewHolder.tvMultiMoq.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_moq)
                    + " " + model.getMinOrderQty());
            //viewHolder.tvItemName.setText(model.getItemname());
            if (!TextUtils.isNullOrEmpty(model.getLogoUrl())) {
                Picasso.get().load(model.getLogoUrl())
                        .placeholder(R.drawable.logo_grey)
                        .error(R.drawable.logo_grey)
                        .into(viewHolder.ivItemImage);
            } else {
                viewHolder.ivItemImage.setImageResource(R.drawable.logo_grey);
            }
            //set value in UI
            setValueInUI(viewHolder, model);

            model.isChecked = true;
            //Minis Btn clicked
            viewHolder.ivMinusBtn.setOnClickListener(view -> {
                //click effect
                Utils.buttonEffect(viewHolder.ivMinusBtn);
                itemAddRemove(viewHolder, model, false);
            });
            //plus Btn clicked
            viewHolder.ivPlusBtn.setOnClickListener(view -> {
                Utils.buttonEffect(viewHolder.ivPlusBtn);
                itemAddRemove(viewHolder, model, true);
            });
            // fav section
            viewHolder.mBinding.favItem.setOnClickListener(v -> addRemoveFav(model, viewHolder));
            //Add Btn clicked
            viewHolder.btnAdd.setOnClickListener(v -> {
                viewHolder.mBinding.visible.setVisibility(View.VISIBLE);
                viewHolder.btnAdd.setVisibility(View.GONE);
                itemAddRemove(viewHolder, model, true);

            });
            //checkout clicked
            viewHolder.relItemDetails.setOnClickListener(v -> {
                viewHolder.relItemDetails.setClickable(false);
                detailsScree(model);
            });
            /*MOQ popup open here*/
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

                tvDSelectQty.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.select_quantities_for));
                tvDMoq.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.moq));
                tvDMrp.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.mrp));
                tvDRs.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.rs));
                tvDMargin.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.margins_d));

                ListView mMoqPriceList = dialogLayout.findViewById(R.id.listview_moq_price);
                item_name.setText(model.itemname);

                AdapterInterface listener = new AdapterInterface() {
                    @Override
                    public void onClick(int pos) {
                        ArrayList<ItemListModel> moq = list.get(i).moqList;
                        list.set(i, list.get(i).moqList.get(pos));
                        list.get(i).moqList = moq;
                        for (int j = 0; j < list.get(i).moqList.size(); j++) {
                            list.get(i).moqList.get(j).isChecked = pos == j;
                        }
                        list.get(i).moqList.get(pos).isChecked = true;
                        notifyDataSetChanged();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 100ms
                                dialog.dismiss();
                                handler.postDelayed(this, 500);
                                handler.removeCallbacks(this);
                            }
                        }, 500);
                    }
                };

                adapter = new MoqAdapter(activity, model.moqList, listener);
                mMoqPriceList.setAdapter(adapter);
                dialog.show();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRemoveFav(ItemListModel model, @NonNull ViewHolder viewHolder) {
        if (RetailerSDKApp.getInstance().noteRepository.isItemWishList(model.getItemId())) {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite);
            RetailerSDKApp.getInstance().noteRepository.deleteTask(model.getItemId());
            Utils.addFav(model.getItemId(), false, activity);
        } else {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red);
            RetailerSDKApp.getInstance().noteRepository.insertTask(model);
            Utils.addFav(model.getItemId(), true, activity);
        }
    }

    //click event
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
        Utils.leftTransaction(activity);
    }

    //Value set
    private void setValueInUI(@NonNull ViewHolder viewHolder, ItemListModel model) {
        // check item in wishList
        if (RetailerSDKApp.getInstance().noteRepository.isItemWishList(model.getItemId())) {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red);
        } else {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite);
        }
        viewHolder.tvItemName.setText(model.itemname);
        String sPRICE = "| <font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(model.getUnitPrice()) + "</font>";
        String sMargin = RetailerSDKApp.getInstance().dbHelper.getString(R.string.moq_margin) + " " +
                (new DecimalFormat("##.##").format(Double.parseDouble(model.marginPoint))) + "%";
        String sMRP = new DecimalFormat("##.##").format(model.price);
        // set values
        viewHolder.tvMrp.setText(sMRP);
        viewHolder.tvMrp.setPaintFlags(viewHolder.tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.tvPrice.setText(Html.fromHtml(sPRICE));
        viewHolder.mBinding.tvMargin.setText(sMargin);
        viewHolder.mBinding.tvOffer.setText("Mrgn" + System.getProperty("line.separator") +
                (new DecimalFormat("##.##").format(Double.parseDouble(model.marginPoint))) + "%");
        viewHolder.tvDreamPoint.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.Dp)
                + " " + model.dreamPoint);
        // set prime item price
        if (model.isPrimeItem) {
            viewHolder.mBinding.liPrime.setVisibility(View.VISIBLE);
            viewHolder.mBinding.tvPPrice.setText(SharePrefs.getInstance(activity).getString(SharePrefs.PRIME_NAME)
                    + " " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.price)
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

        ItemListModel cartModel = RetailerSDKApp.getInstance().noteRepository.getItemByMrpId(model.getItemId(), model.getItemMultiMRPId());

        // set UI for ItemLimit
        if (model.isItemLimit) {
            int totalItemInCart = 0;
            int itemLimitQuantity;
            int totalAvailQty;
            viewHolder.mBinding.availQtyLayout.setVisibility(View.VISIBLE);

            itemLimitQuantity = model.getItemLimitQty();
            if (cartModel != null) {
                totalItemInCart += cartModel.qty;
                totalAvailQty = itemLimitQuantity - totalItemInCart;
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

        boolean isItemFound;
        if (cartModel != null && model.getItemId() == cartModel.getItemId()) {
            isItemFound = true;
            int itemQuantity = cartModel.qty;
            if (itemQuantity > 0) {
                viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem) {
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

            viewHolder.btnAdd.setVisibility(View.VISIBLE);
            viewHolder.mBinding.visible.setVisibility(View.GONE);
        } else {
            viewHolder.btnAdd.setVisibility(View.GONE);
            viewHolder.mBinding.visible.setVisibility(View.VISIBLE);
        }

        if (model.getActive()) {
            viewHolder.mBinding.tvMrpText.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_mrp) + " ");
            viewHolder.mBinding.btItemNotyfy.setVisibility(View.GONE);
            viewHolder.btnAdd.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mBinding.tvMrpText.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_out_of_stock));
            viewHolder.mBinding.tvMrpText.setTextColor(activity.getResources().getColor(R.color.colorAccent));
            viewHolder.mBinding.btItemNotyfy.setVisibility(View.VISIBLE);
            viewHolder.btnAdd.setVisibility(View.GONE);
            viewHolder.mBinding.tvMrp.setVisibility(View.GONE);
            viewHolder.mBinding.tvPrice.setVisibility(View.GONE);
            viewHolder.mBinding.tvMargin.setVisibility(View.GONE);
            viewHolder.mBinding.tvSelectedItemPrice.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_mrp) + " " + sMRP);

            if (RetailerSDKApp.getInstance().noteRepository.isNotifyDisable(model.getItemId())) {
                viewHolder.mBinding.btItemNotyfy.setBackground(activity.getResources().getDrawable(R.drawable.background_for_buttons_disble));
                viewHolder.mBinding.btItemNotyfy.setClickable(false);
                viewHolder.mBinding.btItemNotyfy.setEnabled(false);
            } else {
                viewHolder.mBinding.btItemNotyfy.setBackground(activity.getResources().getDrawable(R.drawable.background_for_buttons));
                viewHolder.mBinding.btItemNotyfy.setClickable(true);
                viewHolder.mBinding.btItemNotyfy.setEnabled(true);
            }
        }
    }

    private void itemAddRemove(@NonNull ViewHolder viewHolder, ItemListModel model, boolean addItem) {
        // progress bar
        progressBar = viewHolder.mBinding.ivProgress;
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);

        try {
            int FreeItemQuantity = 0;
            double FreeWalletPoint = 0;
            boolean addFlag = false;
            //get item limit
            int itemQuantity = Integer.parseInt(viewHolder.tvselectedItemQuantity.getText().toString());

            if (addItem) {
                //plus btn
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
                //minus btn
                if (model.isItemLimit) {
                    if (itemQuantity > 0) {
                        itemQuantity -= model.getMinOrderQty();
                        addFlag = setLimit(viewHolder, model, addFlag, itemQuantity, false);
                    } else {
                        viewHolder.btnAdd.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (itemQuantity > 0) {
                        itemQuantity -= model.getMinOrderQty();
                        viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                        addFlag = true;
                    } else {
                        viewHolder.btnAdd.setVisibility(View.VISIBLE);
                    }
                }
            }
            if (addFlag) {
                double calUnitPrice;
                boolean isPrimeItem;
                if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem) {
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format((itemQuantity * model.getPrimePrice()));
                    viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                    calUnitPrice = model.getPrimePrice();
                    isPrimeItem = true;
                } else {
                    String price = "<font color=#FF4500>&#8377; " + new DecimalFormat("##.##").format(itemQuantity * model.getUnitPrice());
                    viewHolder.tvSelectedItemPrice.setText(Html.fromHtml(price));
                    calUnitPrice = model.getUnitPrice();
                    isPrimeItem = false;
                }
                progressBar.setVisibility(View.VISIBLE);
                activity.addItemInCartItemArrayList(model.getItemId(),
                        itemQuantity, calUnitPrice, model, FreeItemQuantity,
                        FreeWalletPoint, isPrimeItem, this);
            }
            if (itemQuantity > 999) {
                viewHolder.tvselectedItemQuantity.setTextSize(14);
            } else {
                viewHolder.tvselectedItemQuantity.setTextSize(16);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean setLimit(ViewHolder viewHolder, ItemListModel model, boolean addFlag, int itemQuantity, boolean add) {
        int total = RetailerSDKApp.getInstance().noteRepository.getQtyByMultiMrp(model.getItemId(), model.getItemMultiMRPId());
        int availqty;
        int totalItemqty = RetailerSDKApp.getInstance().noteRepository.getQtyByMultiMrp(model.getItemMultiMRPId());
        int itemlimitqty;
        if (add) {
            totalItemqty += model.getMinOrderQty();
        } else {
            totalItemqty -= model.getMinOrderQty();
        }
        itemlimitqty = model.getItemLimitQty();
        if (total > 0) {
            if (add) {
                if (total + itemQuantity > itemlimitqty) {
                    Utils.setToast(activity, RetailerSDKApp.getInstance().dbHelper.getString(R.string.additemToast)
                            + " " + model.getItemLimitQty() + " " +
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.additemToast_2));
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
                availqty = itemlimitqty - totalItemqty;
                if (availqty >= 0) {
                    viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
                }
                viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                addFlag = true;
            }
        } else {
            if (itemQuantity > 0) {
                if (itemQuantity > model.getItemLimitQty()) {
                    Utils.setToast(activity, RetailerSDKApp.getInstance().dbHelper.getString(R.string.additemToast)
                            + " " + model.getItemLimitQty() + " " +
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.additemToast_2));
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
            int total = RetailerSDKApp.getInstance().noteRepository.getQtyByMultiMrp(model.getItemId(), model.getItemMultiMRPId());
            int itemlimitqty;
            itemlimitqty = model.getBillLimitQty();
            if (total > 0) {
                if (total + itemQuantity > itemlimitqty) {
                    Utils.setToast(activity, RetailerSDKApp.getInstance().dbHelper.getString(R.string.bill_limit_text)
                            + " " + model.getBillLimitQty() + " item");
                } else {
                    viewHolder.tvselectedItemQuantity.setText("" + itemQuantity);
                    addFlag = true;
                }
            } else {
                if (itemQuantity > 0) {
                    if (itemQuantity > model.getBillLimitQty()) {
                        Utils.setToast(activity, RetailerSDKApp.getInstance().dbHelper.getString(R.string.bill_limit_text)
                                + " " + model.getBillLimitQty() + " item");
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        TradeOfferItemsBinding mBinding;
        RelativeLayout relItemDetails;
        LinearLayout LLItemMain;
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
        private final TextView tvDreamPoint;
        private final Button btnAdd;

        public ViewHolder(TradeOfferItemsBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            tvSelectedItemPrice = mBinding.tvSelectedItemPrice;
            relItemDetails = mBinding.itemDetails;
            tvselectedItemQuantity = mBinding.tvSelectedItemQuantity;
            ivMinusBtn = mBinding.minusBtn;
            ivPlusBtn = mBinding.plusBtn;
            ivItemImage = mBinding.productImage;
            tvItemName = mBinding.tvItemName;
            tvMrp = mBinding.tvMrp;
            tvPrice = mBinding.tvPrice;
            tvMoq = mBinding.tvMoq;
            tvDreamPoint = mBinding.tvDreamPoint;
            tvMultiMoq = mBinding.multiMoq;
            btnAdd = mBinding.addItemBtn;
            LLItemMain = mBinding.LLItemMain;
        }

        public void bind() {
          //  mBinding.executePendingBindings();
        }
    }

    public class MarginDescComparator implements Comparator<ItemListModel> {

        @SuppressLint("NewApi")
        @Override
        public int compare(ItemListModel lhs, ItemListModel rhs) {
            double i1 = Double.parseDouble(lhs.marginPoint);
            double i2 = Double.parseDouble(rhs.marginPoint);
            return Double.compare(i2, i1);
        }
    }
}