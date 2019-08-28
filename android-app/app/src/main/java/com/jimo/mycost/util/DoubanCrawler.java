package com.jimo.mycost.util;

import android.os.AsyncTask;

import com.jimo.mycost.func.life.LifeItemSearchResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 因为要网络请求，所以不能在主线程执行
 * 使用方法：new DoubanCrawler().execute(theme, start + "").get()
 */
public class DoubanCrawler extends AsyncTask<String, Void, List<LifeItemSearchResult>> {

    // https://movie.douban.com/subject_search?search_text=%E8%A5%BF%E6%B8%B8%E8%AE%B0&start=15
    // https://www.douban.com/search?cat=1002&q=%E8%A5%BF%E6%B8%B8%E8%AE%B0

    private static final String BASE_URL = "https://www.douban.com/";

/*    private String encode(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return text;
        }
    }*/

    private String getUrl(String text, int start) {
        return BASE_URL + "search?q=" + text + "&cat=1002&start=" + start;
    }

    public List<LifeItemSearchResult> search(String movie, int start) throws IOException {
        String url = getUrl(movie, start);
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("div.result div.title a");
        List<LifeItemSearchResult> results = new ArrayList<>();
        int i = 0;
        for (Element link : links) {
            String href = link.attr("href");
            System.out.println(href);
            results.add(getItem(href));
            // 只取4个
            if (i++ > 3) {
                break;
            }
        }
        return results;
    }

    public void searchOneByOne(
            String movie, int start, MyCallback.CommonCallback callback) throws IOException {
        String url = getUrl(movie, start);
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("div.result div.title a");
        for (Element link : links) {
            String href = link.attr("href");
            System.out.println(href);
            callback.doSomething(getItem(href));
        }
    }

    /**
     * 每一部电影页面的解析
     */
    LifeItemSearchResult getItem(String href) throws IOException {
        Document doc = Jsoup.connect(href).get();
        System.out.println(doc.html());
        Element e = doc.selectFirst("span[property=v:summary]");
        String desc = getText(e);
        e = doc.selectFirst("img[rel=v:image]");
        String imgUrl = "";
        if (e != null) {
            imgUrl = e.attr("src");
        }
        String title = getText(doc.selectFirst("span[property=v:itemreviewed]"));
        String type = getLists(doc, "span[property=v:genre]");
        String creators = getLists(doc, "a[rel=v:starring]");
        String pubdate = getText(doc.selectFirst("span[property=v:initialReleaseDate]"));
        String rating = getText(doc.selectFirst("strong[property=v:average]"))
                + "/" + getText(doc.selectFirst("span[property=v:votes]"));
        return new LifeItemSearchResult(imgUrl, title,
                type, creators, pubdate, desc, rating);
    }

    private String getText(Element e) {
        if (e != null) {
            return e.text();
        }
        return "";
    }

    private String getLists(Document doc, String css) {
        StringBuilder sb = new StringBuilder();
        Elements actors = doc.select(css);
        if (actors == null) {
            return "";
        }
        for (Element actor : actors) {
            sb.append(actor.text()).append("/");
        }
        return sb.toString();
    }

    @Override
    protected List<LifeItemSearchResult> doInBackground(String... strings) {
        try {
            return search(strings[0], Integer.parseInt(strings[1]));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
