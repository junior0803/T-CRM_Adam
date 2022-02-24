package com.bts.adamcrm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.R;
import com.bts.adamcrm.model.Category;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter2 extends RecyclerView.Adapter<CategoryAdapter2.ViewHolder> {
    private List<Category> InvoiceList;

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txt_title;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public CategoryAdapter2(List<Category> list) {
        this.InvoiceList = list;
    }

    public void updateAdapter(List<Category> list) {
        this.InvoiceList = list;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category_2, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txt_title.setText(this.InvoiceList.get(i).getName());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.InvoiceList.size();
    }
}