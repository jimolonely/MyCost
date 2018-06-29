package com.jimo.service;

import com.jimo.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jimo on 18-6-29.
 */
@Service
public class PersonService {

    private final PersonMapper mapper;

    @Autowired
    public PersonService(PersonMapper mapper) {
        this.mapper = mapper;
    }

    public int getCount() {
        return mapper.getCount();
    }
}
