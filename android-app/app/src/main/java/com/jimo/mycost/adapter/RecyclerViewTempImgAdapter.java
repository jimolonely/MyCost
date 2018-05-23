package com.jimo.mycost.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jimo.mycost.R;
import com.jimo.mycost.util.FuckUtil;

import java.util.List;

public class RecyclerViewTempImgAdapter extends RecyclerView.Adapter<RecyclerViewTempImgAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<RecyclerViewTempImgItem> items;
    private Context context;

    public RecyclerViewTempImgAdapter(Context context, List<RecyclerViewTempImgItem> items) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_temp_img, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecyclerViewTempImgItem item = items.get(position);
        FuckUtil.loadImg(item.getImgPath(), (drawable) -> holder.imageView.setImageDrawable((Drawable) drawable));
        holder.imageView.setOnClickListener((v) -> {
            //TODO 点击图片放大
            Log.i("imgpath", item.getImgPath());
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
