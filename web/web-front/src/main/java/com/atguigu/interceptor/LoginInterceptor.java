package com.atguigu.interceptor;

import com.alibaba.fastjson.JSON;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.util.WebUtil;
import com.mysql.cj.Session;
import com.qiniu.util.Json;
import okhttp3.Interceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author feng
 * @create 2022-06-17 9:53
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否登录
        HttpSession session = request.getSession();
        if(session.getAttribute("USER")==null){
            //未登录
            WebUtil.writeJSON(response, Result.build("未登录", ResultCodeEnum.LOGIN_AUTH));
//            response.getWriter().write(JSON.toJSONString(Result.build(null,ResultCodeEnum.LOGIN_AUTH)));
            return false;
        }
        return true;
    }
}

