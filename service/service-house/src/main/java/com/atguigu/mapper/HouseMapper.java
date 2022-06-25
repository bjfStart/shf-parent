package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * @author feng
 * @create 2022-06-15 10:09
 */
public interface HouseMapper extends BaseMapper<House> {
    /**
     * 根据小区id查询房屋数量
     * @param id
     * @return
     */
    Integer findCountByCommunityId(Long id);

    /**
     * 前台项目搜索房源的分页信息
     * @param houseQueryBo
     * @return
     */
    Page<HouseVo> findListPage(HouseQueryBo houseQueryBo);
}
