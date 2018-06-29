package com.jimo.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by jimo on 18-6-29.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonServiceTest {
    @Autowired
    private PersonService personService;

    @Test
    public void getCount() throws Exception {
        Assert.assertEquals(0, personService.getCount());
    }

}