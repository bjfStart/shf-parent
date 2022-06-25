package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseBroker;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-15 16:19
 */
public interface HouseBrokerMapper extends BaseMapper<HouseBroker> {
    List<HouseBroker> findHouseBrokerListByHouseId(Long id);

    HouseBroker getByHouseIdAndBrokerId(@Param("houseId") Long houseId,@Param("brokerId") Long brokerId);
}
