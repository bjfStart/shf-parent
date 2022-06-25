package com.atguigu.mapper;

import com.atguigu.entity.Dict;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-14 18:39
 */
public interface DictMapper {
    /**
     * 根据父节点的Id查询Dict列表
     * @param parentId
     * @return
     */
    List<Dict> findDictListByParentId(Integer parentId);

    /**
     * 将当前id作为父节点的id，查询子节点的数量
     * @param id
     * @return
     */
    Integer countIsParent(Long id);

    /**
     * 根据父节点的dictCode查询 Dict列表
     * @param parentDictCode
     * @return
     */
    List<Dict> findDictListByParentDictCode(String parentDictCode);
}
