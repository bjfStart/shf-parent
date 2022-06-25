package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Admin;

import java.util.List;


/**
 * @author feng
 * @create 2022-06-11 15:47
 */
public interface AdminMapper extends BaseMapper<Admin> {

    Admin getByUsername(String username);

    Admin getByPhone(String phone);

    List<Admin> findAll();


}
