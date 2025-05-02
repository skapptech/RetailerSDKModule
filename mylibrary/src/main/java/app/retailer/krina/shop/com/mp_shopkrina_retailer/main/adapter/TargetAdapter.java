package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.TargetAdapterBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.SubCategoryTargetModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.TargetCustomerDC;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.TargetModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.ViewHolder> {
    private final ArrayList<TargetModel> msgList;
    private final SubCategoryTargetModel subCategoryTargetModel;
    private final Context context;
    private boolean isBrandListOpen = false;
    private boolean isItemListOpen = false;
    private AchievedTargetItemAdapter adapter;
    private AchievedTargetItemAdapter adapter2;
    private int length = 0;
    private String finalString = "";


    public TargetAdapter(Context context, ArrayList<TargetModel> msgList, SubCategoryTargetModel subCategoryTargetModel, int length, String finalString) {
        this.msgList = msgList;
        this.context = context;
        this.subCategoryTargetModel = subCategoryTargetModel;
        this.length = length;
        this.finalString = finalString;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate
                (LayoutInflater.from(parent.getContext()), R.layout.target_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //set Strings
        holder.mBinding.tvViewMore.setText(" " + MyApplication.getInstance().dbHelper.getData("text_view_more"));
        holder.mBinding.tvTargetDescription.setText(MyApplication.getInstance().dbHelper.getData("text_click_here_to_check_rest_brands"));

        if (msgList.size() == 1 || (position == msgList.size() - 1)) {
            holder.mBinding.view.setVisibility(View.INVISIBLE);
        } else {
            holder.mBinding.view.setVisibility(View.VISIBLE);
        }
        holder.mBinding.tvDesc.setText(msgList.get(position).getName());
        if (msgList.get(position).getType() == 1 || (msgList.get(position).getType() == 3)) {
            holder.mBinding.tvTargetDescription.setVisibility(View.VISIBLE);
            holder.mBinding.imBrandsArrow.setVisibility(View.VISIBLE);
        } else {
            holder.mBinding.tvTargetDescription.setVisibility(View.GONE);
            holder.mBinding.imBrandsArrow.setVisibility(View.GONE);
        }

        holder.mBinding.rlMain.setOnClickListener(v -> {
            if (msgList.get(position).getType() == 1) {
                if (isBrandListOpen) {
                    isBrandListOpen = false;
                    holder.mBinding.recyclerItemBrand.animate().translationY(-10);
                    holder.mBinding.recyclerItemBrand.setVisibility(View.GONE);
                    holder.mBinding.imBrandsArrow.setRotation(90);

                } else {
                    isBrandListOpen = true;
                    holder.mBinding.imBrandsArrow.setRotation(270);
                    holder.mBinding.recyclerItemBrand.animate().translationY(10);
                    holder.mBinding.recyclerItemBrand.setVisibility(View.VISIBLE);
                }
                showBrandItems(holder);
            } else if (msgList.get(position).getType() == 3) {
                if (isItemListOpen) {
                    isItemListOpen = false;
                    holder.mBinding.recyclerItemBrand.animate().translationY(-10);
                    holder.mBinding.recyclerItemBrand.setVisibility(View.GONE);
                    holder.mBinding.imBrandsArrow.setRotation(90);
                } else {
                    isItemListOpen = true;
                    holder.mBinding.imBrandsArrow.setRotation(270);
                    holder.mBinding.recyclerItemBrand.animate().translationY(10);
                    holder.mBinding.recyclerItemBrand.setVisibility(View.VISIBLE);
                }
                showProductItems(holder);
            }
        });
        if (msgList.get(position).getType() == 2) {
            if (subCategoryTargetModel.getNoOfLineItem() == subCategoryTargetModel.getRequiredNoOfLineItem() || subCategoryTargetModel.getNoOfLineItem() > subCategoryTargetModel.getRequiredNoOfLineItem()) {
                holder.mBinding.tvTaskNumber.setBackground(context.getResources().getDrawable(R.drawable.ic_check_green));
                holder.mBinding.tvTaskNumber.setText("");
            } else {
                holder.mBinding.tvTaskNumber.setText("T" + (position + 1));
                holder.mBinding.tvTaskNumber.setBackground(context.getResources().getDrawable(R.drawable.circle_target));
            }
        }
        if (msgList.get(position).getType() == 0) {
            if (length > 3) {
                holder.mBinding.tvViewMore.setVisibility(View.VISIBLE);
            } else {
                holder.mBinding.tvViewMore.setVisibility(View.GONE);
            }
            if (subCategoryTargetModel.getTarget() == subCategoryTargetModel.getCurrentMonthSales() || subCategoryTargetModel.getTarget() < subCategoryTargetModel.getCurrentMonthSales()) {
                holder.mBinding.tvTaskNumber.setBackground(context.getResources().getDrawable(R.drawable.ic_check_green));
                holder.mBinding.tvTaskNumber.setText("");
            } else {
                holder.mBinding.tvTaskNumber.setText("T" + (position + 1));
                holder.mBinding.tvTaskNumber.setBackground(context.getResources().getDrawable(R.drawable.circle_target));
            }
        }
        holder.mBinding.tvViewMore.setOnClickListener(v -> showDialog());
        if (msgList.get(position).getType() == 1) {
            if (subCategoryTargetModel.getmTargetCustomerBrandDcs() != null && subCategoryTargetModel.getmTargetCustomerBrandDcs().size() > 0) {
                boolean isEqual = true;

                for (int i = 0; i < subCategoryTargetModel.getmTargetCustomerBrandDcs().size(); i++) {
                    if (subCategoryTargetModel.getmTargetCustomerBrandDcs().get(i).getTarget() ==
                            subCategoryTargetModel.getmTargetCustomerBrandDcs().get(i).getCurrentTarget()
                            || subCategoryTargetModel.getmTargetCustomerBrandDcs().get(i).getTarget() <
                            subCategoryTargetModel.getmTargetCustomerBrandDcs().get(i).getCurrentTarget()) {

                        isEqual = true;
                    } else {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    holder.mBinding.tvTaskNumber.setText("");
                    holder.mBinding.tvTaskNumber.setBackground((context.getResources().getDrawable(R.drawable.ic_check_green)));
                } else {
                    holder.mBinding.tvTaskNumber.setText("T" + (position + 1));
                    holder.mBinding.tvTaskNumber.setBackground((context.getResources().getDrawable(R.drawable.circle_target)));
                }
            }
        }
        if (msgList.get(position).getType() == 3) {
            if (subCategoryTargetModel.getmTargetCustomerItemDcs() != null && subCategoryTargetModel.getmTargetCustomerItemDcs().size() > 0) {
                boolean isEqual = true;

                for (int i = 0
                     ; i < subCategoryTargetModel.getmTargetCustomerItemDcs().size(); i++) {
                    if (subCategoryTargetModel.getmTargetCustomerItemDcs().get(i).getTarget() ==
                            subCategoryTargetModel.getmTargetCustomerItemDcs().get(i).getCurrentTarget() ||
                            subCategoryTargetModel.getmTargetCustomerItemDcs().get(i).getTarget() <
                                    subCategoryTargetModel.getmTargetCustomerItemDcs().get(i).getCurrentTarget()) {
                        isEqual = true;
                    } else {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    holder.mBinding.tvTaskNumber.setText("");
                    holder.mBinding.tvTaskNumber.setBackground((context.getResources().getDrawable(R.drawable.ic_check_green)));
                } else {
                    holder.mBinding.tvTaskNumber.setText("T" + (position + 1));
                    holder.mBinding.tvTaskNumber.setBackground((context.getResources().getDrawable(R.drawable.circle_target)));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }


    public void showDialog() {
        Dialog customDialog = new Dialog(context, R.style.CustomDialog);
        final View mView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_info, null);
        customDialog.setContentView(mView);
        customDialog.setCancelable(false);
        TextView okBtn = mView.findViewById(R.id.ok_btn);
        TextView pdTitle = mView.findViewById(R.id.pd_title);
        TextView alert = mView.findViewById(R.id.tv_alert);
        alert.setVisibility(View.GONE);
        okBtn.setText(MyApplication.getInstance().dbHelper.getData("ok"));
        pdTitle.setText("You have To Buy " + finalString + " worth RS. " + new DecimalFormat("##.##").format(subCategoryTargetModel.getTarget()) + "/-");
        TextView cancelBtn = mView.findViewById(R.id.cancel_btn);
        okBtn.setOnClickListener(v -> {
            customDialog.dismiss();
        });
        cancelBtn.setOnClickListener(v -> {
            customDialog.dismiss();
        });
        customDialog.show();
    }

    public void showBrandItems(ViewHolder holder) {
        ArrayList<TargetCustomerDC> targetCustomerDCList = new ArrayList<>();
        for (int i = 0; i < subCategoryTargetModel.getmTargetCustomerBrandDcs().size(); i++) {
            targetCustomerDCList.add(new TargetCustomerDC(
                    subCategoryTargetModel.getmTargetCustomerBrandDcs().get(i).getId(),
                    "",
                    subCategoryTargetModel.getmTargetCustomerBrandDcs().get(i).getTarget(),
                    subCategoryTargetModel.getmTargetCustomerBrandDcs().get(i).getCurrentTarget(),
                    subCategoryTargetModel.getmTargetCustomerBrandDcs().get(i).getBrandName(),
                    true
            ));

        }
        adapter = new AchievedTargetItemAdapter(context, targetCustomerDCList);
        holder.mBinding.recyclerItemBrand.setLayoutManager(new LinearLayoutManager(context));
        holder.mBinding.recyclerItemBrand.setAdapter(adapter);
    }

    public void showProductItems(ViewHolder holder) {
        ArrayList<TargetCustomerDC> targetCustomerDCList = new ArrayList<>();
        for (int i = 0; i < subCategoryTargetModel.getmTargetCustomerItemDcs().size(); i++) {
            targetCustomerDCList.add(new TargetCustomerDC(
                    subCategoryTargetModel.getmTargetCustomerItemDcs().get(i).getId(),
                    subCategoryTargetModel.getmTargetCustomerItemDcs().get(i).getItemName(),
                    subCategoryTargetModel.getmTargetCustomerItemDcs().get(i).getTarget(),
                    subCategoryTargetModel.getmTargetCustomerItemDcs().get(i).getCurrentTarget(),
                    "",
                    false
            ));
        }
        adapter2 = new AchievedTargetItemAdapter(context, targetCustomerDCList);
        holder.mBinding.recyclerItemBrand.setLayoutManager(new LinearLayoutManager(context));
        holder.mBinding.recyclerItemBrand.setAdapter(adapter2);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TargetAdapterBinding mBinding;

        public ViewHolder(TargetAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}