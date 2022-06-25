package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.bo.LoginBo;
import com.atguigu.entity.bo.RegisterBo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author feng
 * @create 2022-06-16 11:50
 */
@Controller
@RequestMapping("/userInfo")
public class UserInfoController {
    @Reference
    private UserInfoService userInfoService;
    @ResponseBody
    @PostMapping("/register")
    public Result register(@RequestBody RegisterBo registerBo,HttpSession session){
        //1.确认验证码是否与请求的发送的验证码匹配
        //1.1如果匹配进行下一步，如果不匹配，将数据回显和报错信息
        String code = (String)session.getAttribute("CODE");
        if(code==null||!code.equals(registerBo.getCode())  ){
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        //2.校验手机号是否重复
        //2.1 将传来的手机号与在后端中查询，看是否有匹配的，如果有匹配，说明已注册，将数据回显和报错信息
        UserInfo result  = userInfoService.getByPhone(registerBo.getPhone());
        if(result!=null){
            return Result.build(null,ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        //3.将密码进行加密，设置状态为1（0为锁定，1为正常）然后添加到user-info表中
        UserInfo userInfo = new UserInfo();
        //将registerBo的属性全部复制到userInfo中
        BeanUtils.copyProperties(registerBo,userInfo);
        userInfo.setStatus(1);
        userInfo.setPassword(MD5.encrypt(userInfo.getPassword()));
        userInfoService.insert(userInfo);
        return Result.ok();
    }

    @GetMapping("/sendCode/{phone}")
    @ResponseBody
    public Result sendCode(@PathVariable("phone")String phone, HttpSession session){
        //模拟
        String code = "1111";
        session.setAttribute("CODE",code);
        //验证码发送成功
        return Result.ok();
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestBody LoginBo loginBo,HttpSession session){
        //1.查询手机号与数据库中有匹配的，若匹配的返回数据
        UserInfo userInfo = userInfoService.getByPhone(loginBo.getPhone());
        if(userInfo==null){
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        }
        //查询账号是否被锁定
        if(userInfo.getStatus() == 0){
            return Result.build(null,ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }
        //2.将密码加密后和查询的数据进行匹配
        if(!userInfo.getPassword().equals(MD5.encrypt(loginBo.getPassword()))){
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }
        //将数据回显
        session.setAttribute("USER",userInfo);

        Map responseMapping = new HashMap<>();
        responseMapping.put("nickName",userInfo.getNickName());
        responseMapping.put("phone",userInfo.getPhone());

        return Result.ok(responseMapping);
    }

    @GetMapping("/logout")
    @ResponseBody
    public Result logout(HttpSession session){
        session.removeAttribute("userInfo");
        return Result.ok();
    }
}
