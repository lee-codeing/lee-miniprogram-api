package com.lee.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lee.base.config.ServerConfig;
import com.lee.base.dto.LoginUserDTO;
import com.lee.base.dto.UserDTO;
import com.lee.base.entity.User;
import com.lee.base.service.UserService;
import com.lee.base.service.WxAuthService;
import com.lee.base.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @version 1.0
 * @auther he.Lee
 * @Description: wx授权登录处理服务
 * @date 2019/10/29 16:11
 */
@Service
public class WxAuthServiceImpl implements WxAuthService {
    private static final Logger LOG = LoggerFactory.getLogger(WxAuthServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private UserService userServiceImpl;

    @Override
    public JSONObject getWxUserInfo(String code) {
        String jsonObject = restTemplate.getForObject(serverConfig.wxJsUrl + "?appid=" + serverConfig.appId
                + "&secret=" + serverConfig.appSecret
                + "&js_code=" + code
                + "&grant_type=authorization_code", String.class);
        return JSONObject.parseObject(jsonObject);
    }

    @Override
    public String wxAuthToken(String code) {
        String token = "";
        JSONObject jsonObject = getWxUserInfo(code);
        if (jsonObject != null && jsonObject.getIntValue("errcode") == 0){
            User user = userServiceImpl.findUserByWxOpenid(jsonObject.getString("openid"));
            if (user == null){
                UserDTO userDTO = new UserDTO();
                userDTO.setAccount(jsonObject.getString("openid"));
                userDTO.setPassword("123456");
                userDTO.setName("weixin用户");
                userDTO.setWxOpenId(jsonObject.getString("openid"));
                userDTO.setWxSessionKey(jsonObject.getString("session_key"));
                try {
                    userServiceImpl.addUser(userDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            LoginUserDTO loginUserDTO = new LoginUserDTO();
            loginUserDTO.setAccount(jsonObject.getString("openid"));
            loginUserDTO.setPassword("123456");
            ResponseVO responseVO = userServiceImpl.login(loginUserDTO);
            JSONObject jsonObject1 = (JSONObject) JSONObject.toJSON(responseVO.getData());
            token = jsonObject1.getString("accessToken");
            LOG.debug("wx 获取到的token" + token);
        }
        return token;
    }
}
