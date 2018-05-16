package com.jimo.mycost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jimo.mycost.R;

import org.xutils.x;

import java.util.List;

public class SelectImgAdapter extends RecyclerView.Adapter<SelectImgAdapter.ImgViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private List<String> path;

    public SelectImgAdapter(Context context, List<String> path) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.path = path;
    }

    @Override
    public ImgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImgViewHolder(inflater.inflate(R.layout.item_select_img, null));
    }

    @Override
    public void onBindViewHolder(ImgViewHolder holder, int position) {
        x.image().bind(holder.imageView, path.get(position));
    }

    @Override
    public int getItemCount() {
        return path == null ? 0 : path.size();
    }

    public void setData(List<String> data) {
        this.path = data;
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ImgViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_img_select_show);
        }
    }
}
