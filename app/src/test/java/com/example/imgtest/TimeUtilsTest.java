package com.example.imgtest;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeUtilsTest {

    @Test
    public void getFitTimeSpan() {
        long time1= System.nanoTime();
        long time2 = time1-(31*1000);
        long min = TimeUtils.getFitTimeSpan(time1,time2);
        Assert.assertTrue(min == 1);
    }
}