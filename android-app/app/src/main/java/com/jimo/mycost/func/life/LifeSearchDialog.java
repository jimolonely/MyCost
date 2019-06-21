package com.jimo.mycost.func.life;

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
import com.jimo.mycost.util.DoubanCrawler;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LifeSearchDialog extends Dialog {

    private Context context;
    private String keyword;//搜索关键字
    private String theme;//主题
    private DataCallback callback;
    private LifeAddFragment.SearchDataHandle upCallback;

    private int start = 0;
    private final int TYPE_MORE = 0;
    private final int TYPE_REFRESH = 1;

    private EasyRefreshLayout easyRefreshLayout;
    private LifeSearchResultAdapter adapter;

    public interface DataCallback {
        void onDataSelect();
    }

    public LifeSearchDialog(@NonNull Context context, String keyword, String theme, LifeAddFragment.SearchDataHandle callback) {
        super(context);
        this.context = context;
        this.keyword = keyword;
        this.theme = theme;
        this.adapter = new LifeSearchResultAdapter(context, new MyCallback());
        this.upCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_life_search, null);

        easyRefreshLayout = view.findViewById(R.id.easylayout);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
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

    private class MyCallback implements LifeSearchResultAdapter.OnClickCallback {

        @Override
        public void getData(LifeItemSearchResult result) {
            Log.i("item", result.toString());
            upCallback.getData(result);
            LifeSearchDialog.this.dismiss();
        }
    }

    private void loadSearchData(int type) {
        if (LifeAddFragment.THEME_BOOK.equals(theme)) {
            loadData(type, MyConst.DOUBAN_BOOK_API, result -> {
                JSONObject obj = JSON.parseObject(result);
                final JSONArray books = obj.getJSONArray("books");
                if (books == null) {
                    return;
                }
                int len = books.size();
                for (int i = 0; i < len; i++) {
                    JSONObject book = (JSONObject) books.get(i);
                    final JSONArray tags = book.getJSONArray("tags");
                    StringBuilder types = new StringBuilder("类型:");
                    for (int j = 0; j < tags.size(); j++) {
                        types.append(" ").append(((JSONObject) tags.get(j)).getString("name"));
                    }
                    String creators = (String) book.getJSONArray("author").stream().
                            reduce("人物:", (pre, post) -> pre + " " + post);
                    final JSONObject jr = book.getJSONObject("rating");
                    final String rating = jr.getString("average") + "/" + jr.getString("max")
                            + "/" + jr.getString("numRaters");
                    String remark = book.getString("pages") + "页;出版商:" + book.getString("publisher")
                            + ";\n简介:" + book.getString("summary");
                    adapter.getData().add(new LifeItemSearchResult(
                            book.getJSONObject("images").getString("small"), book.getString("title")
                            , types.toString(), creators, book.getString("pubdate"), remark, rating));
                }
                start += len;
            });
        } else if (LifeAddFragment.THEME_MOVIE.equals(theme)) {
            /*loadData(type, MyConst.DOUBAN_MOVIE_API, result -> {
                JSONObject obj = JSON.parseObject(result);
                final JSONArray movies = obj.getJSONArray("subjects");
                if (movies == null) {
                    return;
                }
                int len = movies.size();
                for (int i = 0; i < len; i++) {
                    JSONObject movie = (JSONObject) movies.get(i);
                    String types = (String) movie.getJSONArray("genres").stream().
                            reduce("类型:", (pre, post) -> pre + " " + post);
                    final JSONArray directors = movie.getJSONArray("directors");
                    StringBuilder creators = new StringBuilder("导演:");
                    for (int j = 0; j < directors.size(); j++) {
                        creators.append(" ").append(((JSONObject) directors.get(j)).getString("name"));
                    }
                    final JSONArray casts = movie.getJSONArray("casts");
                    StringBuilder remark = new StringBuilder("主演:");
                    for (int j = 0; j < casts.size(); j++) {
                        remark.append(" ").append(((JSONObject) casts.get(j)).getString("name"));
                    }
                    final JSONObject jr = movie.getJSONObject("rating");
                    final String rating = jr.getString("average") + "/" + jr.getString("max")
                            + "/" + jr.getString("stars");
                    adapter.getData().add(new LifeItemSearchResult(movie.getJSONObject("images")
                            .getString("small"), movie.getString("title"), types
                            , creators.toString(), movie.getString("year"),
                            remark.toString(), rating
                    ));
                }*/
            try {
                List<LifeItemSearchResult> movies = new DoubanCrawler().execute(keyword, start + "").get();
                adapter.setData(movies);
                start += movies.size();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private interface HandleJSONData {
        void callback(String result);
    }

    private void loadData(int type, String api, HandleJSONData handle) {
        final RequestParams params = new RequestParams(api);
        params.addQueryStringParameter("q", keyword);
        params.addQueryStringParameter("apiKey", MyConst.DOUBAN_KEY);
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
                handle.callback(cacheResult);
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
