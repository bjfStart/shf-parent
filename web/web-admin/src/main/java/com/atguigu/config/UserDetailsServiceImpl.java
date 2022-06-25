package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author feng
 * @create 2022-06-20 9:21
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username表示用户在登录页面输入的用户名，我们就需要校验username是否正确
        //根据用户名查找用户
        Admin admin = adminService.getByUsername(username);
        if(admin == null){
            //根据用户名无法查询到用户，也就说明用户名错误
            throw new UsernameNotFoundException("用户不存在");
        }
        //说明用户名正确，接下来校验密码
        //admin中的密码是数据库的密码，只需要将这个密码交给 Spring security，由Spring security校验
        //在授权时，Spring security要求我们用字符串表示权限，一个字符串（acl_permission中的code字段）
        //获取当前用户的所有code


        List<String> codePermissionList = permissionService.findCodePermissionListByAdminId(admin.getId());

        /*List<SimpleGrantedAuthority> grantedAuthorityList = codePermissionList.stream()
                .filter(codePermission -> !StringUtils.isEmpty(codePermission))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());*/

        //
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        if(permissionService != null && codePermissionList.size() > 0) {
            for (String code : codePermissionList) {
                //每一个code就对应一个SimpleGrantedAuthority
                if (StringUtils.isEmpty(code)) {
                    continue;
                }
                //将对象加入集合
                grantedAuthorityList.add(new SimpleGrantedAuthority(code));
            }
        }
        //校验密码
        return new User(username,admin.getPassword(), grantedAuthorityList);
    }


}
