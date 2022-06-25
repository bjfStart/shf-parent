package com.atguigu.service;

import com.atguigu.entity.Dict;
import com.atguigu.result.Result;

import java.util.List;
import java.util.Map;

/**
 * @author feng
 * @create 2022-06-14 16:33
 */
public interface DictService {
    /**
     * 根据父id查询返回数据列表
     * @param parentId
     * @return
     */
    List<Map<String,Object>> findZnodes(Integer parentId);

    List<Dict> findDictListByParentDictCode(String parentDictCode);

    List<Dict> findDictListByParentId(Integer id);
}
