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
import butterknife.Unbinder;
import butterknife.internal.Utils;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {
    private List<String> attachmentList;

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

    public AttachmentAdapter(List<String> list) {
        this.attachmentList = list;
    }

    public void updateAdapter(List<String> list) {
        this.attachmentList = list;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_attachment, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String attachment = this.attachmentList.get(i);
        TextView textView = viewHolder.txt_name;
        textView.setText(attachment);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.attachmentList.size();
    }
}
