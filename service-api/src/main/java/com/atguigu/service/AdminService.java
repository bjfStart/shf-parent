package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Admin;

import java.util.List;


/**
 * @author feng
 * @create 2022-06-11 15:46
 */
public interface AdminService extends BaseService<Admin> {
    List<Admin> findAll();

    Admin getByUsername(String username);
}
