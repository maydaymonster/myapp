package com.ttit.myapp.schedule.mvp.course;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.ttit.myapp.R;
import com.ttit.myapp.schedule.RecyclerBaseAdapter;

import java.util.List;

public class SelectWeekAdapter extends RecyclerBaseAdapter<String> {

    private int selectIndex = 2;

    public SelectWeekAdapter(int itemLayoutId, @NonNull List<String> data) {
        super(itemLayoutId, data);
    }
    @Override
    protected void convert(ViewHolder holder, int position) {
        holder.setText(R.id.tv_text, getData().get(position));
        if (selectIndex == position) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
    }
}
