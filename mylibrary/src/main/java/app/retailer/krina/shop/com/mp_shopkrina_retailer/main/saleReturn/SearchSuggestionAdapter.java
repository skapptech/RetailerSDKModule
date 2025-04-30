package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.saleReturn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.ReturnItemModel;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestionAdapter extends ArrayAdapter<ReturnItemModel> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<ReturnItemModel> items, tempItems, suggestions;

    public SearchSuggestionAdapter(Context context, int resource, int textViewResourceId, ArrayList<ReturnItemModel> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<ReturnItemModel>(items); // this makes the difference.
        suggestions = new ArrayList<ReturnItemModel>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.return_item_suggestion, parent, false);
        }
        ReturnItemModel people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getItemname());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((ReturnItemModel) resultValue).getItemname();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ReturnItemModel people : tempItems) {
                    if (people.getItemname().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<ReturnItemModel> filterList = (ArrayList<ReturnItemModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ReturnItemModel people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
