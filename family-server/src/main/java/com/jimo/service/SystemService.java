package com.jimo.service;

import com.jimo.dto.Result;
import com.jimo.mapper.SystemMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by jimo on 18-7-3.
 */
@Service
public class SystemService {

    private final SystemMapper systemMapper;

    @Autowired
    public SystemService(SystemMapper systemMapper) {
        this.systemMapper = systemMapper;
    }


    public Result login(String userName, String md5Password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        final byte[] digest = md5.digest(md5Password.getBytes("utf-8"));
        StringBuilder buffer = new StringBuilder();
        // 把每一个byte 做一个与运算 0xff;
        for (byte b : digest) {
            // 与运算
            int number = b & 0xff;// 加盐
            String str = Integer.toHexString(number);
            if (str.length() == 1) {
                buffer.append("0");
            }
            buffer.append(str);
        }
        // 标准的md5加密后的结果
        final String ps = buffer.toString();
        //compare　to database
        final String psFromDB = systemMapper.getPassword(userName);
        if (ps.equals(psFromDB)) {
            return new Result(getToken(userName));
        }
        return new Result(false, "用户名或密码错误");
    }

    private final int TOKEN_EXPIRED_TIME = 1000 * 60 * 30;//30min
    private final String base64EncodedSecretKey = "base64EncodedSecretKey";//私钥

    /**
     * 取得ＪＷＴ　ｔｏｋｅｎ
     *
     * @param userName
     * @return
     */
    private String getToken(String userName) {
        return Jwts.builder().setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRED_TIME))
                .setSubject(userName).claim("roles", "user").setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey).compact();
    }
}
