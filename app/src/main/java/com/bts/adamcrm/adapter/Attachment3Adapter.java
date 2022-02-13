package com.bts.adamcrm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.R;
import com.bts.adamcrm.model.Attachment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Attachment3Adapter extends RecyclerView.Adapter<Attachment3Adapter.ViewHolder> {
    private List<Attachment> attachmentList;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
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

    public Attachment3Adapter(List<Attachment> list) {
        this.attachmentList = list;
    }

    public void updateAdapter(List<Attachment> list) {
        this.attachmentList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_attachment2, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txt_name.setText(this.attachmentList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return this.attachmentList.size();
    }
}
