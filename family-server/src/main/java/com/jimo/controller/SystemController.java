package com.jimo.controller;

import com.jimo.dto.Result;
import com.jimo.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jimo on 18-7-3.
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    private final SystemService systemService;

    @Autowired
    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @PostMapping("/login")
    public Result login(String userName, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return systemService.login(userName, password);
    }
}
