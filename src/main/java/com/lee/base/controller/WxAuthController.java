package com.lee.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.lee.base.service.WxAuthService;
import com.lee.base.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @auther he.Lee
 * @Description: 微信登录信息处理
 * @date 2019/10/29 15:55
 */
@RestController
@RequestMapping("/wx")
public class WxAuthController {

    @Autowired
    private WxAuthService wxAuthServiceImpl;

    /**
     * 根据CODE, APP_ID, AppSecret 查询用户基本信息
     */
    @PostMapping("/wxAuth")
    public ResponseVO wxAuth(@RequestBody JSONObject codeJson){
        String token = wxAuthServiceImpl.wxAuthToken(codeJson.getString("code"));
        return ResponseVO.success(token);
    }

    /**
     * 校验微信的授权TOKEN
     * @return
     */
    @PostMapping("/wxAuthCheck")
    public ResponseVO wxAuthCheck(HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getName();
        return ResponseVO.success();
    }

}
