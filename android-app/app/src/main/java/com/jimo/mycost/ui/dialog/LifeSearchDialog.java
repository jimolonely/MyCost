package com.jimo.mycost.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.adapter.ItemLifeSearchResult;
import com.jimo.mycost.adapter.LifeSearchResultAdapter;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class LifeSearchDialog extends Dialog {

    private Context context;
    private String keyword;//搜索关键字
    private DataCallback callback;

    private RecyclerView recyclerView;
    private LifeSearchResultAdapter adapter;

    public interface DataCallback {
        void onDataSelect();
    }

    public LifeSearchDialog(@NonNull Context context, String keyword) {
        super(context);
        this.context = context;
        this.keyword = keyword;
        this.adapter = new LifeSearchResultAdapter(context, loadSearchData());
    }

    /**
     * 从豆瓣加载数据
     *
     * @return
     */
    private List<ItemLifeSearchResult> loadSearchData() {
        final RequestParams params = new RequestParams(MyConst.DOUBAN_MOVIE_API);
        params.addQueryStringParameter("q", keyword);
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_life_search, null);

        final EasyRefreshLayout easyRefreshLayout = view.findViewById(R.id.easylayout);
        recyclerView = view.findViewById(R.id.recyclerview);

        easyRefreshLayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
        easyRefreshLayout.addEasyEvent(new MyEasyEvent());

        this.setTitle("[" + keyword + "]搜索结果:");
        this.setContentView(view);
    }

    private class MyEasyEvent implements EasyRefreshLayout.EasyEvent {

        @Override
        public void onLoadMore() {

        }

        @Override
        public void onRefreshing() {
        }
    }


}
