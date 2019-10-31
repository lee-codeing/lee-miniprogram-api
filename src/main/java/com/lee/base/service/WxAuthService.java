package com.lee.base.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @version 1.0
 * @auther he.Lee
 * @Description: 微信登录服务类
 * @date 2019/10/29 16:08
 */
public interface WxAuthService {
    /**
     * 查询登录用户信息
     * @param param
     * @return
     */
    public JSONObject getWxUserInfo(String param);

    /**
     * 微信授权登录获取token
     * @param code
     * @return
     */
    public String wxAuthToken(String code);

}
