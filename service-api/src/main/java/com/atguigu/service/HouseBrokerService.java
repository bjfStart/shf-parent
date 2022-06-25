package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseBroker;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-15 16:16
 */
public interface HouseBrokerService extends BaseService<HouseBroker> {
    List<HouseBroker> findHouseBrokerListByHouseId(Long id);

    HouseBroker getByHouseIdAndBrokerId(Long houseId, Long brokerId);
}
