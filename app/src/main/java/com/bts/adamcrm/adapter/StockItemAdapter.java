package com.bts.adamcrm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.R;
import com.bts.adamcrm.model.Customer;
import com.bts.adamcrm.model.StockItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockItemAdapter extends RecyclerView.Adapter<StockItemAdapter.ViewHolder> implements Filterable {
    private List<StockItem> StockList;
    private List<StockItem> filteredData = null;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return (long) i;
    }

    public Filter getFilter() {
        return new Filter() {
            public Filter.FilterResults performFiltering(CharSequence charSequence) {
                Filter.FilterResults filterResults = new Filter.FilterResults();
                ArrayList arrayList = new ArrayList();
                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.count = StockItemAdapter.this.StockList.size();
                    filterResults.values = StockItemAdapter.this.StockList;
                } else {
                    for (StockItem stockItem : StockItemAdapter.this.StockList) {
                        if (stockItem.getDescription().toLowerCase().contains(charSequence.toString().toLowerCase())
                                || stockItem.getPno().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            arrayList.add(stockItem);
                        }
                    }
                    filterResults.count = arrayList.size();
                    filterResults.values = arrayList;
                }
                return filterResults;
            }

            public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                StockItemAdapter.this.filteredData = (ArrayList) filterResults.values;
                StockItemAdapter.this.notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_description)
        TextView txt_description;
        @BindView(R.id.txt_minimum_quantity)
        TextView txt_minimum_quantity;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public StockItemAdapter(List<StockItem> list) {
        this.StockList = list;
        this.filteredData = list;
    }

    public void updateAdapter(List<StockItem> list) {
        this.StockList = list;
        this.filteredData = list;
        notifyDataSetChanged();
    }

    public List<StockItem> getStockItemList() {
        return filteredData;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_stock_item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        StockItem stockItem = this.filteredData.get(i);
        TextView textView = viewHolder.txt_quantity;
        textView.setText(stockItem.getQuantity() + "");
        TextView textView2 = viewHolder.txt_minimum_quantity;
        textView2.setText(stockItem.getMinimum_quantity() + "");
        viewHolder.txt_description.setText(stockItem.getDescription());
        TextView textView3 = viewHolder.txt_price;
        textView3.setText(stockItem.getPno() + "");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.filteredData.size();
    }
}
