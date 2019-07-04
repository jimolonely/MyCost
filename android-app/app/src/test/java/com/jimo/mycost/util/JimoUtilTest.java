package com.jimo.mycost.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jimo on 17-11-26.
 */
public class JimoUtilTest {
    @Test
    public void addTwoTime() throws Exception {
        String time = JimoUtil.addTwoTime("00:00", "00:00");
        assertEquals("00:00:00", time);
    }

    @Test
    public void addTwoTime2() throws Exception {
        String time = JimoUtil.addTwoTime("10:10", "22:08");
        assertEquals("00:32:18", time);
    }

    @Test
    public void addTwoTim3() throws Exception {
        String time = JimoUtil.addTwoTime("41:15", "20:45");
        assertEquals("01:02:00", time);
    }

    @Test
    public void testRangeTime() throws Exception {
        assertEquals("2018-04-01", JimoUtil.getFirstDayOfMonth(4));
        assertEquals("2018-05-31", JimoUtil.getLastDayOfMonth(5));
    }

    @Test
    public void getChoiceDateRange() throws Exception {
        System.out.println(JimoUtil.getChoiceDateRange("周", 2, 0));
        System.out.println(JimoUtil.getChoiceDateRange("月", 1, 0));
        System.out.println(JimoUtil.getChoiceDateRange("年", 1, 0));
    }

    @Test
    public void testFileCopy() {
        JimoUtil.fileCopy("/home/jimo/图片/me.jpg", "/home/jimo/workspace/hehe", "me.jpg");
    }

    @Test
    public void getDistOfDate() {
        int i1 = JimoUtil.getDistOfDate("2019-07-01", "2019-07-01");
        assertEquals(1, i1);
        int i2 = JimoUtil.getDistOfDate("2019-07-01", "2019-07-03");
        assertEquals(3, i2);
    }
}