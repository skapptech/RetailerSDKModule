package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.EditProfileActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.StoryBordSharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;

public class ProfileDialogFragment extends DialogFragment {
    private HomeActivity activity;
    private String criticalInfoMissMSG, currentDate;


    public ProfileDialogFragment() {
    }


    public static ProfileDialogFragment newInstance(String msg, String date) {
        ProfileDialogFragment fragment = new ProfileDialogFragment();
        Bundle args = new Bundle();
        args.putString("msg", msg);
        args.putString("date", date);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            criticalInfoMissMSG = getArguments().getString("msg");
            currentDate = getArguments().getString("date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_edit_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitle = view.findViewById(R.id.pd_title);
        TextView tvInfo = view.findViewById(R.id.tvInfo);
        Button okBtn = view.findViewById(R.id.ok_btn);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);

        if (criticalInfoMissMSG.startsWith("Your Critical info")) {
            tvTitle.setText(MyApplication.getInstance().dbHelper.getString(R.string.critical_info_msg));
        } else {
            tvTitle.setText(criticalInfoMissMSG);
        }
        tvInfo.setText(MyApplication.getInstance().dbHelper.getString(R.string.please_update_your_information));

        okBtn.setOnClickListener(v -> {
            dismiss();
            SharePrefs.getInstance(activity).putString(SharePrefs.PROFILE_LAST_DATE_SHOW, currentDate);
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.DOC_EMPTY, false);
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.DOC_DAY_EMPTY, false);
            startActivity(new Intent(activity, EditProfileActivity.class));
            if (!StoryBordSharePrefs.getInstance(activity).getBoolean(StoryBordSharePrefs.HOMEPAGE)) {
                activity.appStoryView();
            }
        });
        cancelBtn.setOnClickListener(v -> {
            dismiss();
            SharePrefs.getInstance(activity).putString(SharePrefs.PROFILE_LAST_DATE_SHOW, currentDate);
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.DOC_EMPTY, false);
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.DOC_DAY_EMPTY, false);
        });

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // update analytic
        MyApplication.getInstance().updateAnalytics("criticalInfoDialog");

        getDialog().setCancelable(false);
    }
}