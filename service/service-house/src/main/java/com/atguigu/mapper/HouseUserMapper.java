package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseUser;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-15 16:40
 */
public interface HouseUserMapper extends BaseMapper<HouseUser> {
    List<HouseUser> findHouseUserListByHouseId(Long id);
}
