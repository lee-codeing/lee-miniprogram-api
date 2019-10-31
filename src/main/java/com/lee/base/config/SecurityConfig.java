package com.lee.base.config;

import com.lee.base.domain.CustomUserDetail;
import com.lee.base.entity.User;
import com.lee.base.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @description Security核心配置
 * @author he.Lee
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserMapper userMapper;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
       return new UserDetailsService(){
           @Override
           public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
               log.info("username:{}",username);
               User user = userMapper.findUserByAccount(username);
               if(user != null){
                   CustomUserDetail customUserDetail = new CustomUserDetail();
                   customUserDetail.setUsername(user.getAccount());
                   customUserDetail.setPassword("{bcrypt}"+bCryptPasswordEncoder.encode(user.getPassword()));
                   List<GrantedAuthority> list = AuthorityUtils.createAuthorityList(user.getRole().getRole());
                   customUserDetail.setAuthorities(list);
                   return customUserDetail;
               }else {//返回空
                   return null;
               }

           }
       };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
