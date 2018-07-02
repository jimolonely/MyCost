package com.jimo.controller;

import com.jimo.dto.Result;
import com.jimo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jimo on 18-7-2.
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @RequestMapping("/test")
    public Result test() {
        return new Result(personService.getCount());
    }
}
