package com.jimo.mycost.func.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jimo.mycost.R;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class SelectImgAdapter extends RecyclerView.Adapter<SelectImgAdapter.ImgViewHolder> {


    private LayoutInflater inflater;
    private List<String> path;

    public SelectImgAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.path = new ArrayList<>();
    }

    @Override
    public ImgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImgViewHolder(inflater.inflate(R.layout.item_common_select_img, null));
    }

    @Override
    public void onBindViewHolder(ImgViewHolder holder, int position) {
        x.image().bind(holder.imageView, path.get(position));
        holder.imageView.setOnLongClickListener(v -> {
            path.remove(position);
            SelectImgAdapter.this.notifyDataSetChanged();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return path == null ? 0 : path.size();
    }

    public void setData(List<String> data) {
        this.path = data;
    }

    public List<String> getData() {
        return path;
    }

    public void clear() {
        path.clear();
        this.notifyDataSetChanged();
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ImgViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_img_select_show);
        }
    }
}
