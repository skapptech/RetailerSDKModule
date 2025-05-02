package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemGamesBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.GamesWebActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GameModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {
    private final ArrayList<GameModel> list;
    private final Activity activity;
    private final OnButtonClick onButtonClick;


    public GameListAdapter(Activity activity, ArrayList<GameModel> list, OnButtonClick onButtonClick) {
        this.activity = activity;
        this.list = list;
        this.onButtonClick = onButtonClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_games, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameModel model = list.get(position);
        Picasso.get().load(model.getGameLogo()).into(holder.mBinding.ivImage);
        holder.mBinding.tvHelix.setText(model.getGameName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemGamesBinding mBinding;

        public ViewHolder(ItemGamesBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.getRoot().setOnClickListener(v -> {
                if (!list.get(getAdapterPosition()).getWalletPointOnPlay()) {
                    if (list.get(getAdapterPosition()).getGameUrl() != null && list.get(getAdapterPosition()).getGameUrl().length() > 0) {
                        RetailerSDKApp.getInstance().updateAnalytics(list.get(getAdapterPosition()).getGameName());
                        activity.startActivity(new Intent(activity, GamesWebActivity.class)
                                .putExtra("title", list.get(getAdapterPosition()).getGameName())
                                .putExtra("url", list.get(getAdapterPosition()).getGameUrl()));
                    } else {
                        if (list.get(getAdapterPosition()).getGameName().equalsIgnoreCase("solitare")) {
                            RetailerSDKApp.getInstance().updateAnalytics(list.get(getAdapterPosition()).getGameName());
                            Intent intent = new Intent();
                            intent.setClassName("app.retailer.krina.shop.com.mp_shopkrina_retailer" , "com.sk.solitare.SolitaireActivity");
                            try {
                                activity.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (list.get(getAdapterPosition()).getGameName().equalsIgnoreCase("block builder")) {
                            RetailerSDKApp.getInstance().updateAnalytics(list.get(getAdapterPosition()).getGameName());
                            Intent intent = new Intent();
                            intent.setClassName("app.retailer.krina.shop.com.mp_shopkrina_retailer" , "com.sk.blocks.activities.MainActivity");
                            try {
                                activity.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    onButtonClick.onButtonClick(getAdapterPosition(), true);
                }
            });
        }
    }
}