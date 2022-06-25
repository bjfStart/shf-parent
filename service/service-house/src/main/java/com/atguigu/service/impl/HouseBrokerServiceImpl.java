package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.HouseBroker;
import com.atguigu.mapper.HouseBrokerMapper;
import com.atguigu.service.HouseBrokerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-15 16:17
 */
@Service(interfaceClass = HouseBrokerService.class)
public class HouseBrokerServiceImpl extends BaseServiceImpl<HouseBroker> implements HouseBrokerService {
    @Autowired
    private HouseBrokerMapper houseBrokerMapper;
    @Override
    protected BaseMapper<HouseBroker> getEntityMapper() {
        return houseBrokerMapper;
    }

    @Override
    public List<HouseBroker> findHouseBrokerListByHouseId(Long id) {
        return houseBrokerMapper.findHouseBrokerListByHouseId(id);
    }

    @Override
    public HouseBroker getByHouseIdAndBrokerId(Long houseId, Long brokerId) {
        return houseBrokerMapper.getByHouseIdAndBrokerId(houseId,brokerId);
    }
}
