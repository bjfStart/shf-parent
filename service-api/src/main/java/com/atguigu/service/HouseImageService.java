package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-15 15:59
 */
public interface HouseImageService extends BaseService<HouseImage> {

    List<HouseImage> findHouseImageList(@Param("id") Long id,@Param("type") int type);
}
