package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemPlaceRecyclerBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;

public class PlacesAutoCompleteAdapter extends RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder> implements Filterable {
    private ArrayList<PlaceAutocomplete> mResultList = new ArrayList<>();

    private final Context mContext;
    private final CharacterStyle STYLE_BOLD;
    private final CharacterStyle STYLE_NORMAL;
    private final PlacesClient placesClient;
    private ClickListener clickListener;
    private final Boolean isCity;


    public PlacesAutoCompleteAdapter(Context context, Boolean IsCity) {
        mContext = context;
        isCity = IsCity;
        STYLE_BOLD = new StyleSpan(Typeface.BOLD);
        STYLE_NORMAL = new StyleSpan(Typeface.NORMAL);
        placesClient = Places.createClient(context);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void click(Place place, CharSequence address, CharSequence area);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    mResultList = getPredictions(constraint);
                    if (mResultList != null) {
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetChanged();
                }
            }
        };
    }


    private ArrayList<PlaceAutocomplete> getPredictions(CharSequence constraint) {
        TypeFilter typeFilter = null;
//        RectangularBounds bounds = null;
        if (isCity) {
            typeFilter = TypeFilter.CITIES;
        } else {
//            // Create a RectangularBounds object.
//            if (MyApplication.getInstance().latLngBounds != null)
//                bounds = RectangularBounds.newInstance(
//                        new LatLng(MyApplication.getInstance().latLngBounds.southwest.latitude, MyApplication.getInstance().latLngBounds.southwest.longitude),
//                        new LatLng(MyApplication.getInstance().latLngBounds.northeast.latitude, MyApplication.getInstance().latLngBounds.northeast.longitude));
        }

        ArrayList<PlaceAutocomplete> resultList = new ArrayList<>();
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
//                .setLocationBias(bounds)
                .setCountry("IN")
                .setTypeFilter(typeFilter)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build();

        Task<FindAutocompletePredictionsResponse> autocompletePredictions = placesClient.findAutocompletePredictions(request);
        try {
            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        if (autocompletePredictions.isSuccessful()) {
            FindAutocompletePredictionsResponse findAutocompletePredictionsResponse = autocompletePredictions.getResult();
            if (findAutocompletePredictionsResponse != null)
                for (AutocompletePrediction prediction : findAutocompletePredictionsResponse.getAutocompletePredictions()) {
                    resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),
                            prediction.getPrimaryText(STYLE_NORMAL).toString(),
                            prediction.getFullText(STYLE_BOLD).toString()));
                }
        }
        return resultList;
    }

    @NonNull
    @Override
    public PredictionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PredictionHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext())
                , R.layout.item_place_recycler, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionHolder holder, final int i) {
        holder.binding.tvAddress.setText(mResultList.get(i).address);
        holder.binding.tvArea.setText(mResultList.get(i).area);
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }


    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
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
            PlaceAutocomplete item = mResultList.get(getAdapterPosition());
            if (v.getId() == R.id.place_item_view) {

                String placeId = String.valueOf(item.placeId);

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.VIEWPORT);
                FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(request).addOnSuccessListener(response -> {
                    System.out.println(response);
                    System.out.println(response.getPlace());
                    System.out.println(response.getPlace().getName());
                    System.out.println(response.getPlace().getAddress());
                    System.out.println(response.getPlace().getViewport());
                    //
                    clickListener.click(response.getPlace(), item.address, item.area);
                }).addOnFailureListener(exception -> {
                    if (exception instanceof ApiException) {
                        Toast.makeText(mContext, exception.getMessage() + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public static class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence address, area;

        PlaceAutocomplete(CharSequence placeId, CharSequence area, CharSequence address) {
            this.placeId = placeId;
            this.area = area;
            this.address = address;
        }

        @Override
        public String toString() {
            return area.toString();
        }
    }
}