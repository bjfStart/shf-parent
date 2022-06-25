package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author feng
 * @create 2022-06-17 8:40
 */
public interface UserFollowMapper extends BaseMapper<UserFollow> {
    UserFollow findByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);

    Page<UserFollowVo> findListPage(Long id);
}
