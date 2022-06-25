package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author feng
 * @create 2022-06-15 23:59
 * 实体Bean的多种类型
 * 1. POJO: Plain Old Java Object,简单的Java对象，它是用在与数据库交互（与表对应）
 * 2. VO: Value Object,值对象，它是用在与前端交互（与页面对应），vo中的数据是用来显示的页面上的
 * 3. BO: Business Object,业务对象，它是用在与业务逻辑交互（与业务逻辑对应），用来封装浏览器传递过来的数据
 * 4. DTO: Data Transfer Object,数据传输对象，它的使用场景一般是两个不同的系统之间的数据传输（远程调用）
 */
@RequestMapping("/house")
@Controller
public class HouseController {
    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseUserService houseUserService;
    @Reference
    private UserFollowService userFollowService;
    @ResponseBody
    @PostMapping("/list/{pageNum}/{pageSize}")
    public Result list(@PathVariable("pageNum") Integer pageNum,
                       @PathVariable("pageSize") Integer pageSize,
                       @RequestBody HouseQueryBo houseQueryBo){
        //1.调用业务层的方法搜索房源的分页数据
        PageInfo<HouseVo> houseVoPageInfo =  houseService.findListPage(pageNum,pageSize,houseQueryBo);
        return Result.ok(houseVoPageInfo);
    }

    @GetMapping("/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id")Long id,HttpSession session){

        //查询当前的房源信息
        House house = houseService.getById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("house",house);
        //查询当前房源所属的小区信息：要查询小区信息，必须得直到小区id
        Community community = communityService.getById(house.getCommunityId());
        data.put("community",community);
        //查询当前房源得经纪人列表
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(id);
        data.put("houseBrokerList",houseBrokerList);
        //查询当前房源的图片列表
        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(id, 1);
        data.put("houseImage1List",houseImage1List);
        //查询房东的列表
        List<HouseUser> houseUserList = houseUserService.findHouseUserListByHouseId(id);
        data.put("houseUserList",houseUserList);
        boolean isFollow = false;

        //查询用户是否已经关注过该房源
        //获取session的用户登录信息，判断用户是否登录
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        if(userInfo!=null){
            //如果用户已经登录，则判断用户是否关注过该房源
            //根据houseId和userId查询UserFollow,如果查询到，则说明用户已经关注过该房源
            UserFollow userFollow = userFollowService.findByUserIdAndHouseId(userInfo.getId(),id);
            System.out.println("userFollow = " + userFollow);
            if(userFollow!=null && userFollow.getIsDeleted()==0){
                isFollow = true;
            }
        }

        data.put("isFollow",isFollow);
        return Result.ok(data);
    }
}
