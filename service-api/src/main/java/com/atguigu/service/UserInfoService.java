package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.UserInfo;

/**
 * @author feng
 * @create 2022-06-16 14:02
 */
public interface UserInfoService extends BaseService<UserInfo> {
    UserInfo getByPhone(String phone);
}
