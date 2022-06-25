package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;

import com.atguigu.entity.Community;
import com.atguigu.mapper.CommunityMapper;
import com.atguigu.mapper.HouseMapper;
import com.atguigu.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-14 21:30
 */
@Service(interfaceClass = CommunityService.class)
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private HouseMapper houseMapper;
    @Override
    protected BaseMapper<Community> getEntityMapper() {
        return  communityMapper;
    }

    @Override
    public void delete(Long id) {
        //判断：如果该小区下有房源，则不能删除
        Integer count = houseMapper.findCountByCommunityId(id);
        if(count>0){
            //说明小区下有房源,不能删除
            throw new RuntimeException("该小区下有房源，不能删除");
        }
        //该小区下没有房源，可以删除
        super.delete(id);
    }

    @Override
    public List<Community> findAll() {
        return communityMapper.findAll();
    }
}
