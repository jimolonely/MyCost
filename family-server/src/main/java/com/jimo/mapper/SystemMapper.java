package com.jimo.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by jimo on 18-7-3.
 */
public interface SystemMapper {

    @Select("select password from user where user_name=#{userName}")
    String getPassword(@Param("userName") String userName);
}
