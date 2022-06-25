package com.atguigu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author feng
 * @create 2022-06-20 8:50
 */
@Configuration
@EnableWebSecurity //@EnableWebSecurity注解开启spring security的默认行为
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启方法级别的权限认证
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("lucy")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("");
    }*/

    /**
     * 必须指定加密方式，上下的加密方式必须一致
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 默认spring security不允许 iframe 嵌套显示，
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        //必须调用父类的方法，否则就不需要认证即可访问
//        super.configure(http);
//        //允许iframe嵌套显示
//        http.headers().frameOptions().disable();

        //允许iframe嵌套显示
        http.headers().frameOptions().disable();
        http
                .authorizeRequests()
                .antMatchers("/static/**","/login").permitAll()  //如果不登录可以访问的路径
                .anyRequest().authenticated()    // 除了上述内容之外，其它内容都需要登录后才能访问
                .and()
                .formLogin()
                .loginPage("/login")    //设置登录页面
                .defaultSuccessUrl("/") //登录认证成功后默认转跳的路径，意思时admin登录后也跳转到/user
                .and()
                .logout()
                .logoutUrl("/logout")   //退出登陆的路径，指定spring security拦截的注销url,退出功能是security提供的
                .logoutSuccessUrl("/login");//用户退出后要被重定向的url
        //关闭跨域请求伪造，防止跨域
        http.csrf().disable();

        //指定自定义的访问拒绝处理器
        http.exceptionHandling().accessDeniedHandler(new AtguiguAccessDeniedHandler());

    }


}
