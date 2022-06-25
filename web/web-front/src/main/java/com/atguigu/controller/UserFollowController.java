package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author feng
 * @create 2022-06-16 17:02
 */
@RequestMapping("/userFollow")
@Controller
public class UserFollowController {
    @Reference
    private UserFollowService userFollowService;

    @GetMapping("/auth/follow/{houseId}")
    @ResponseBody
    public Result follow(@PathVariable("houseId")Long houseId, HttpSession session){
        //1.判断用户之前是否已经添加过这条数据的关注：根据用户id和房源id查询处UserFollow
        //1.1 获取当前登录的用户
        UserInfo userInfo = (UserInfo)session.getAttribute("USER");
/*
        //1.2 根据登录用户的id和houseId查询UseFollow
        UserFollow userFollow = userFollowService.findByUserIdAndHouseId(userInfo.getId(),houseId);

        //2.如果用户之前已经添加过关注，那么我们只需要更新这条数据的is_deleted为0
        if(userFollow!=null){
            userFollow.setIsDeleted(0);
            userFollowService.update(userFollow);
            return Result.ok();
        }
        //3.如果用户之前没有添加过关注，需要添加一条数据
        userFollow = new UserFollow();
        userFollow.setUserId(userInfo.getId());
        userFollow.setHouseId(houseId);
        userFollowService.insert(userFollow);*/
        userFollowService.addUserFollow(userInfo.getId(),houseId);
        return Result.ok();
    }

    @GetMapping("/auth/list/{pageNum}/{pageSize}")
    @ResponseBody
    public Result list(@PathVariable("pageNum")Integer pageNum,@PathVariable("pageSize")Integer pageSize,HttpSession session){
        //1.获取session当前登录的用户信息
        UserInfo userInfo = (UserInfo)session.getAttribute("USER");
        //2.根据用户id分页查询关注列表的分页数据
        PageInfo<UserFollowVo> pageInfo =  userFollowService.findListPage(pageNum,pageSize,userInfo.getId());
        return Result.ok(pageInfo);
    }
    @ResponseBody
    @GetMapping("/auth/cancelFollow/{id}")
    public Result list(@PathVariable("id")Long id){
        //根据id逻辑删除数据
        UserFollow userFollow = new UserFollow();
        userFollow.setId(id);
        userFollow.setIsDeleted(1);
        //调用业务层修改数据
        userFollowService.update(userFollow);
        return Result.ok();
    }
}
