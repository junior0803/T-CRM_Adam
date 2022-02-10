package com.bts.adamcrm.adapter;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bts.adamcrm.R;
import com.bts.adamcrm.model.Nav;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavAdapter extends RecyclerView.Adapter<NavAdapter.ViewHolder> {

    private List<Nav> navList;

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.text)
        TextView text;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            ButterKnife.bind(this, view);
        }
    }

    public NavAdapter(List<Nav> list) {
        this.navList = list;
    }

    public void updateAdapter(List<Nav> list) {
        this.navList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nav, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Nav nav = this.navList.get(position);
        viewHolder.text.setText(nav.getTitle());
        viewHolder.img.setImageResource(nav.getIcon());
        viewHolder.img.setColorFilter(ContextCompat.getColor(viewHolder.img.getContext(),
                R.color.md_black_1000), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public int getItemCount() {
        return this.navList.size();
    }
}
