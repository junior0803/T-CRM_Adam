package com.bts.adamcrm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bts.adamcrm.R;
import com.bts.adamcrm.model.Attachment2;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;

public class AttachAdapter extends RecyclerView.Adapter<AttachAdapter.ViewHolder> {
    private List<Attachment2> attachment2List;

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_name)
        TextView txt_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public AttachAdapter(List<Attachment2> list){
        attachment2List = list;
    }

    public void updateAdapter(List<Attachment2> list){
        attachment2List = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attach2, parent, false));
    }


    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_name.setText(attachment2List.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return attachment2List.size();
    }
}
