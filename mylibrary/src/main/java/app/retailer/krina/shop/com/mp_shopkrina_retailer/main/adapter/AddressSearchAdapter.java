package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemPlaceRecyclerBinding;

public class AddressSearchAdapter extends RecyclerView.Adapter<AddressSearchAdapter.PredictionHolder> {
    private JsonArray mResultList = new JsonArray();

    private Context mContext;
    private ClickListener clickListener;
    private Boolean isCity;


    public AddressSearchAdapter(Context context, Boolean IsCity) {
        mContext = context;
        isCity = IsCity;
    }

    public AddressSearchAdapter(@Nullable Context context, @NotNull JsonArray array) {
        mContext = context;
        mResultList = array;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void click(String place, CharSequence address, CharSequence area);
    }

    @NonNull
    @Override
    public PredictionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PredictionHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext())
                , R.layout.item_place_recycler, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionHolder holder, final int i) {
        holder.binding.tvAddress.setText(mResultList.get(i).getAsJsonObject().get("description").getAsString());
        holder.binding.tvArea.setText(mResultList.get(i).getAsJsonObject().get("structured_formatting").getAsJsonObject().get("main_text").getAsString());
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }


    public class PredictionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemPlaceRecyclerBinding binding;

        PredictionHolder(ItemPlaceRecyclerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.placeItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.place_item_view) {
                try {
                    JsonObject jsonObject = mResultList.get(getAdapterPosition()).getAsJsonObject();
                    clickListener.click(jsonObject.get("place_id").getAsString(), jsonObject.get("description").getAsString(), jsonObject.get("structured_formatting").getAsJsonObject().get("main_text").getAsString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}