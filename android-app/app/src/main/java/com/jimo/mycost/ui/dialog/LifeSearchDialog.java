package com.jimo.mycost.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.adapter.ItemLifeSearchResult;
import com.jimo.mycost.adapter.LifeSearchResultAdapter;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class LifeSearchDialog extends Dialog {

    private Context context;
    private String keyword;//搜索关键字
    private DataCallback callback;

    private int start = 0;
    private final int TYPE_MORE = 0;
    private final int TYPE_REFRESH = 1;

    private RecyclerView recyclerView;
    private EasyRefreshLayout easyRefreshLayout;
    private LifeSearchResultAdapter adapter;

    public interface DataCallback {
        void onDataSelect();
    }

    public LifeSearchDialog(@NonNull Context context, String keyword) {
        super(context);
        this.context = context;
        this.keyword = keyword;
        this.adapter = new LifeSearchResultAdapter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_life_search, null);

        easyRefreshLayout = view.findViewById(R.id.easylayout);
        recyclerView = view.findViewById(R.id.recyclerview);
        TextView tvKey = view.findViewById(R.id.tv_life_search_keyword);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        final String title = "[" + keyword + "]搜索结果:";
        tvKey.setText(title);
//        easyRefreshLayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
        easyRefreshLayout.addEasyEvent(new MyEasyEvent());

//        this.setTitle("[" + keyword + "]搜索结果:");
        // 设置对话框宽度满屏
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
    }

    private class MyEasyEvent implements EasyRefreshLayout.EasyEvent {

        @Override
        public void onLoadMore() {
            loadSearchData(TYPE_MORE);
        }

        @Override
        public void onRefreshing() {
            loadSearchData(TYPE_REFRESH);
        }

    }

    private void loadSearchData(int type) {
        final RequestParams params = new RequestParams(MyConst.DOUBAN_BOOK_API);
        params.addQueryStringParameter("q", keyword);
        if (type == TYPE_REFRESH) {
            //如果是刷新,则从0开始
            start = 0;
            adapter.getData().clear();
        }
        params.addQueryStringParameter("start", start + "");
        x.http().get(params, new Callback.CacheCallback<String>() {
            String cacheResult = null;

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    cacheResult = result;
                }
                JSONObject obj = JSON.parseObject(cacheResult);
                final JSONArray books = obj.getJSONArray("books");
                int len = books.size();
                for (int i = 0; i < len; i++) {
                    JSONObject book = (JSONObject) books.get(i);
//                    Log.i("book", book.toJSONString());
                    adapter.getData().add(new ItemLifeSearchResult(book.getJSONObject("images")
                            .getString("small"), book.getString("title")));
                }
                start += len;

                Log.i("data-in", adapter.getData().size() + "");
                adapter.notifyDataSetChanged();
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.loadMoreComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("search-rr", ex.getMessage());
                JimoUtil.myToast(context, "error:" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }

            @Override
            public boolean onCache(String result) {
                cacheResult = result;
                return true;
            }
        });
    }

}
