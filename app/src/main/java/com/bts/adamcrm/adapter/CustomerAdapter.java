package com.bts.adamcrm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.R;
import com.bts.adamcrm.helper.ItemTouchHelperAdapter;
import com.bts.adamcrm.model.Customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> implements Filterable, ItemTouchHelperAdapter {
    private List<Customer> customerList;
    private ArrayList filteredData = null;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public Filter getFilter() {
        return new Filter() {
            /* class xyz.client.t_crm.adapter.CustomerAdapter.AnonymousClass1 */

            /* access modifiers changed from: protected */
            public Filter.FilterResults performFiltering(CharSequence charSequence) {
                Filter.FilterResults filterResults = new Filter.FilterResults();
                ArrayList<Customer> arrayList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.count = customerList.size();
                    filterResults.values = customerList;
                } else {
                    for (Customer customer : customerList) {
                        if (customer.getTask_title().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            arrayList.add(customer);
                        }
                    }
                    filterResults.count = arrayList.size();
                    filterResults.values = arrayList;
                }
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                filteredData = (ArrayList) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override // xyz.client.t_crm.helper.ItemTouchHelperAdapter
    public void onItemDismiss(int i) {
        filteredData.remove(i);
        notifyItemRemoved(i);
    }

    public List<Customer> getCustomerList() {
        return filteredData;
    }

    @Override // xyz.client.t_crm.helper.ItemTouchHelperAdapter
    public boolean onItemMove(int i, int i2) {
        Collections.swap(filteredData, i, i2);
        notifyItemMoved(i, i2);
        return true;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chk_title)
        CheckBox chk_title;
        @BindView(R.id.title)
        TextView title;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public CustomerAdapter(List<Customer> list) {
        customerList = list;
        filteredData = (ArrayList) list;
    }

    public void updateAdapter(List<Customer> list) {
        customerList = list;
        filteredData = (ArrayList) list;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Customer customer = (Customer) filteredData.get(i);
        viewHolder.title.setText(customer.getTask_title().split("\n")[0]);
        viewHolder.chk_title.setChecked(customer.getStatus() == 2);
        if (customer.isSms_sent()) {
            viewHolder.title.setTextColor(viewHolder.chk_title.getContext().getResources().getColor(R.color.colorAccent));
        } else {
            viewHolder.title.setTextColor(viewHolder.chk_title.getContext().getResources().getColor(R.color.md_black_1000));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return filteredData.size();
    }
}
