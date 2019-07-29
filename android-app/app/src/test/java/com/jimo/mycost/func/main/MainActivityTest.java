package com.jimo.mycost.func.main;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Test
    public void syncToCloud() throws IOException {
        new MainActivity().syncToCloud();
    }
}