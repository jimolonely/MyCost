package com.jimo.mycost.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jimo.mycost.R;
import com.jimo.mycost.util.FuckUtil;

import java.util.ArrayList;
import java.util.List;

public class LifeSearchResultAdapter extends RecyclerView.Adapter<LifeSearchResultAdapter.ItemViewHolder> {

    private final LayoutInflater inflater;
    private List<ItemLifeSearchResult> data;
    private OnClickCallback callback;

    public interface OnClickCallback {
        void getData(ItemLifeSearchResult result);
    }

    public LifeSearchResultAdapter(Context context, OnClickCallback callback) {
        inflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
        this.callback = callback;
    }

    public List<ItemLifeSearchResult> getData() {
        return this.data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_life_search, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final ItemLifeSearchResult result = data.get(position);
        holder.tv_title.setText(result.getTitle());
        holder.tv_actor.setText(result.getCreators());
        holder.tv_sumary.setText(result.getRemark());

        FuckUtil.loadImg(result.getImgUrl(), (drawable) -> holder.imageView.setImageDrawable((Drawable) drawable));

        holder.imageView.setOnClickListener(v -> callback.getData(result));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView tv_title;
        private TextView tv_sumary;
        private TextView tv_actor;

        ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_life_search);
            tv_title = itemView.findViewById(R.id.tv_life_search_title);
            tv_sumary = itemView.findViewById(R.id.tv_life_search_summary);
            tv_actor = itemView.findViewById(R.id.tv_life_search_actor);
        }
    }
}
