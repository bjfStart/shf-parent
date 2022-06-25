package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.mapper.HouseMapper;
import com.atguigu.service.HouseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author feng
 * @create 2022-06-15 10:08
 */
@Service(interfaceClass = HouseService.class)
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {
    @Autowired
    private HouseMapper houseMapper;
    @Override
    protected BaseMapper<House> getEntityMapper() {
        return houseMapper;
    }

    @Override
    public PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryBo houseQueryBo) {
        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        //调用持久层方法查询分页数据，并封装成PageInfo对象
        Page<HouseVo> houseVoPageInfo = houseMapper.findListPage(houseQueryBo);
        return new PageInfo(houseVoPageInfo);
    }
}
