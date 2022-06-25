package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-15 16:01
 */
public interface HouseImageMapper extends BaseMapper<HouseImage> {

    List<HouseImage> findHouseImageList(@Param("id") Long id,@Param("type") int type);
}
