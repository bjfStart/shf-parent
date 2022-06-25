package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.entity.Dict;
import com.atguigu.mapper.DictMapper;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author feng
 * @create 2022-06-14 18:38
 */
@Service(interfaceClass = DictService.class)
public class DictServiceImpl implements DictService {
    @Autowired
    private DictMapper dictMapper;
    @Override
    public List<Map<String,Object>> findZnodes(Integer parentId) {
        List<Map<String,Object>> zNodes = new ArrayList<>();
        /*if(parentId==0){
            Map<String,Object> responseMap = new HashMap<>();
            responseMap.put("isParent",true);
            responseMap.put("name","全部分类");
            responseMap.put("id",1);
            zNodes.add(responseMap);
        }else{
            List<Dict> dictList = dictMapper.findDictListByParentId(parentId);
            for (Dict dict : dictList) {
                Map<String,Object> map = new HashMap<>();
                Integer count = dictMapper.countIsParent(dict.getId());
                map.put("isParent",count>0?true:false);
                map.put("name",dict.getName());
                map.put("id",dict.getId());
                zNodes.add(map);
            }
        }*/
        /*List<Dict> dictList = dictMapper.findDictListByParentId(parentId);
        for (Dict dict : dictList) {
            Map<String,Object> map = new HashMap<>();
            Integer count = dictMapper.countIsParent(dict.getId());
            map.put("isParent",count>0?true:false);
            map.put("name",dict.getName());
            map.put("id",dict.getId());
            zNodes.add(map);
        }*/

        List<Dict> dictList = dictMapper.findDictListByParentId(parentId);
        zNodes = dictList.stream()
                .map(dict -> {
                    Map<String, Object> responseMap = new HashMap<>();
                    Integer count = dictMapper.countIsParent(dict.getId());
                    responseMap.put("isParent", count > 0 ? true : false);
                    responseMap.put("name", dict.getName());
                    responseMap.put("id", dict.getId());
                    return responseMap;
                })
                .collect(Collectors.toList());

        return zNodes;
    }

    @Override
    public List<Dict> findDictListByParentDictCode(String parentDictCode) {
        return dictMapper.findDictListByParentDictCode(parentDictCode);
    }

    @Override
    public List<Dict> findDictListByParentId(Integer id) {
        return dictMapper.findDictListByParentId(id);
    }
}
