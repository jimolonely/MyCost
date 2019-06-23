package com.jimo.mycost.util;

import com.jimo.mycost.func.life.LifeItemSearchResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 这个与DoubanCrawler的区别是：
 * 本类采用http先把页面下载再解析，然后没解析完一部电影就调用回调方法
 * 而DoubanCrawler是全部下载完了才返回
 */
public class MovieCrawler {
    private static final String BASE_URL = "https://www.douban.com/";

    private String getUrl(String text, int start) {
        return BASE_URL + "search?q=" + text + "&cat=1002&start=" + start;
    }

    private String downloadPage(String url) throws Throwable {
        RequestParams params = new RequestParams(url);
        return x.http().getSync(params, String.class);
    }

    public void searchOneByOne(
            String movie, int start, MyCallback.CommonCallback callback) throws Throwable {
        String url = getUrl(movie, start);
        Document doc = Jsoup.parse(downloadPage(url));
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
    LifeItemSearchResult getItem(String href) throws Throwable {
        Document doc = Jsoup.parse(downloadPage(href));
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
}
