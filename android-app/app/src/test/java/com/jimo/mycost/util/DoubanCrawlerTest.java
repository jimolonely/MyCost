package com.jimo.mycost.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DoubanCrawlerTest {

    private DoubanCrawler crawler = new DoubanCrawler();

    @Test
    public void search() throws IOException {
        crawler.search("西游记", 0);
    }

    @Test
    public void getItem() throws IOException {
        crawler.getItem("https://www.douban.com/link2/?url=https%3A%2F%2Fmovie.douban.com%2Fsubject%2F2156663%2F&query=%E8%A5%BF%E6%B8%B8%E8%AE%B0&cat_id=1002&type=search&pos=0");
    }
}