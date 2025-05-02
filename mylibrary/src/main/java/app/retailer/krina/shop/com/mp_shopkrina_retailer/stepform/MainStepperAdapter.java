package app.retailer.krina.shop.com.mp_shopkrina_retailer.stepform;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderStatusModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;

public class MainStepperAdapter extends VerticalStepperAdapter {
    private ArrayList<ReturnOrderStatusModel> list;


    public MainStepperAdapter(Context context, ArrayList<ReturnOrderStatusModel> list) {
        super(context);
        this.list = list;
    }

    @NonNull
    @Override
    public CharSequence getTitle(int position) {
        return list.get(position).getStatus();
    }

    @Nullable
    @Override
    public CharSequence getSummary(int position) {
//        return list.get(position).getMessage();
        return Utils.getDateTimeFormate(list.get(position).getDate());
    }

    @Nullable
    @Override
    public CharSequence getDate(int position) {
//        return Utils.getDateTimeFormate(list.get(position).getDate());
        return "";
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Void getItem(int position) {
        return null;
    }

    @NonNull
    @Override
    public View onCreateContentView(Context context, int position) {
        return new MainItemView(context);
    }
}