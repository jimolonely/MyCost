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

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class LifeSearchResultAdapter extends RecyclerView.Adapter<LifeSearchResultAdapter.ItemViewHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    private List<ItemLifeSearchResult> data;


    public LifeSearchResultAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
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

        x.image().loadDrawable(result.getImgUrl(), null, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                holder.imageView.setImageDrawable(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                holder.imageView.setImageDrawable();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView tv_title;

        ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_life_search);
            tv_title = itemView.findViewById(R.id.tv_life_search_title);
        }
    }
}
