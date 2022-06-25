package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.mapper.UserFollowMapper;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author feng
 * @create 2022-06-17 8:39
 */
@Service(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Override
    protected BaseMapper<UserFollow> getEntityMapper() {
        return userFollowMapper;
    }

    @Override
    public UserFollow findByUserIdAndHouseId(Long userId, Long houseId) {
        return userFollowMapper.findByUserIdAndHouseId(userId,houseId);
    }

    @Override
    public PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long id) {
        PageHelper.startPage(pageNum,pageSize);
        Page<UserFollowVo> userFollowVoList =  userFollowMapper.findListPage(id);
        return new PageInfo(userFollowVoList);
    }
    @Override
    public void addUserFollow(Long userId, Long houseId) {
        //1.根据userId和houseId查询UserFollow：不考虑is_deleted的情况
        UserFollow userFollow = userFollowMapper.findByUserIdAndHouseId(userId,houseId);
        //2.判断userFollow是否为空
        if(userFollow == null) {
            //2.1 说明用户从未关注过，插入一条新的关注记录
            userFollow.setUserId(userId);
            userFollow.setHouseId(houseId);
            super.insert(userFollow);
        }else{
            //2.2说明用户关注过，判断是否已经被删除is_deleted=1,设置is_deleted=0
            if(userFollow.getIsDeleted() == 1) {
                userFollow.setIsDeleted(0);
                super.update(userFollow);
            }
        }
    }
}
