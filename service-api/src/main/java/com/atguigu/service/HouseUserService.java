package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseUser;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-15 16:38
 */
public interface HouseUserService extends BaseService<HouseUser> {
    List<HouseUser> findHouseUserListByHouseId(Long id);
}
