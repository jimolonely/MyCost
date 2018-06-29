package com.jimo.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * Created by jimo on 18-6-29.
 */
public interface PersonMapper {

    @Select("select count(*) from person")
    int getCount();
}
