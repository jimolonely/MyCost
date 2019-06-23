package com.jimo.mycost.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class MovieCrawlerTest {

    private MovieCrawler crawler = new MovieCrawler();

    @Test
    public void searchOneByOne() throws Throwable {
        crawler.searchOneByOne("西游记", 0, System.out::println);
    }
}