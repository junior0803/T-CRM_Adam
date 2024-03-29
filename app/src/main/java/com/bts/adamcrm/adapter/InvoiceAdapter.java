package com.bts.adamcrm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.R;
import com.bts.adamcrm.model.Invoice;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {
    private List<Invoice> attachmentList;

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        TextView txt_name;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public InvoiceAdapter(List<Invoice> list) {
        this.attachmentList = list;
    }

    public void updateAdapter(List<Invoice> list) {
        this.attachmentList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invoice, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txt_name.setText(this.attachmentList.get(i).getInvoice_no());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.attachmentList.size();
    }
}
