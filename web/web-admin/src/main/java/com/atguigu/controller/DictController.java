package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author feng
 * @create 2022-06-14 16:30
 */
@Controller
@RequestMapping("/dict")
public class DictController {
    @Reference
    private DictService dictService;

    @GetMapping("/findZnodes")
    @ResponseBody
    public Result findZnodes(@RequestParam(value = "id", defaultValue = "0") Integer id) {
        System.out.println(id);
        List<Map<String,Object>> znodes = dictService.findZnodes(id);
        return Result.ok(znodes);
    }

    @GetMapping("/findDictListByParentId/{id}")
    @ResponseBody
    public Result findDictListByParentId(@PathVariable("id") Integer id){
        List<Dict> znodes = dictService.findDictListByParentId(id);
        return Result.ok(znodes);
    }
}
