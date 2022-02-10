package com.bts.adamcrm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.R;
import com.bts.adamcrm.model.InvoiceItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;

public class InvoiceItemAdapter extends RecyclerView.Adapter<InvoiceItemAdapter.ViewHolder> {
    private List<InvoiceItem> InvoiceList;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return (long) i;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_description)
        TextView txt_description;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public InvoiceItemAdapter(List<InvoiceItem> list) {
        this.InvoiceList = list;
    }

    public void updateAdapter(List<InvoiceItem> list) {
        this.InvoiceList = list;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invoice_item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        InvoiceItem invoiceItem = this.InvoiceList.get(i);
        TextView textView = viewHolder.txt_quantity;
        textView.setText(invoiceItem.getQuantity() + "");
        viewHolder.txt_description.setText(invoiceItem.getDescription());
        viewHolder.txt_price.setText(invoiceItem.getPrice());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.InvoiceList.size();
    }
}
