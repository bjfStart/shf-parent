package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Community;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-14 21:06
 */
public interface CommunityService extends BaseService<Community> {

    List<Community> findAll();
}
