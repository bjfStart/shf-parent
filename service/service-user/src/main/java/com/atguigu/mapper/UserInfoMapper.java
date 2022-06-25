package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.UserInfo;

/**
 * @author feng
 * @create 2022-06-16 14:04
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    UserInfo getByPhone(String phone);
}
