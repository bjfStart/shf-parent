package com.atguigu.service;

import com.atguigu.base.BaseService;

import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

/**
 * @author feng
 * @create 2022-06-17 8:38
 */
public interface UserFollowService extends BaseService<UserFollow> {
    UserFollow findByUserIdAndHouseId(Long id, Long houseId);

    PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long id);

    void addUserFollow(Long userId, Long houseId);
}
