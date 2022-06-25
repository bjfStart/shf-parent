package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Community;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-14 21:31
 */
public interface CommunityMapper extends BaseMapper<Community> {

    List<Community> findAll();
}
