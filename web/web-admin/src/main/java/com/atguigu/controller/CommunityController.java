package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** ctrl+Alt+M 提取代码为方法
 * @author feng
 * @create 2022-06-14 20:54
 */
@RequestMapping("/community")
@Controller
public class CommunityController extends BaseController {
    private static final String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";
    private static final String LIST_ACTION = "redirect:/community";
    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;
    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        if (filters.get("pageNum")!=null && "".equals(filters.get("pageNum"))) {
            filters.put("pageNum",1);
        }
        if (filters.get("pageSize")!=null && "".equals(filters.get("pageSize"))) {
            filters.put("pageSize",5);
        }
        PageInfo<Community> page = communityService.findPage(filters);
        model.addAttribute("page",page);
        List<Dict> dictList = dictService.findDictListByParentDictCode("beijing");
        if(!filters.containsKey("areaId")){
            filters.put("areaId",0);
        }
        if(!filters.containsKey("plateId")){
            filters.put("plateId",0);
        }
        model.addAttribute("areaList",dictList);
        model.addAttribute("filters",filters);
        return PAGE_INDEX;
    }

    @RequestMapping("/create")
    public String create(Model model){
        saveAreaListToModel(model);
        return PAGE_CREATE;
    }

    private void saveAreaListToModel(Model model) {
        List<Dict> dictList = dictService.findDictListByParentDictCode("beijing");
        model.addAttribute("areaList", dictList);
    }

    @PostMapping("/save")
    public String save(Community community,Model model){
        communityService.insert(community);
        return successPage(model,"小区信息添加成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        Community community = communityService.getById(id);
        model.addAttribute("community",community);
        saveAreaListToModel(model);
        return PAGE_EDIT;
    }

    @RequestMapping("/update")
    public String update(Community community,Model model){
        communityService.update(community);
        return successPage(model,"修改小区信息成功");
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id,Model model){
        try {
            communityService.delete(id);
        } catch (Exception e) {
            return errorPage(model,e.getMessage());
        }
        return LIST_ACTION;
    }
}
