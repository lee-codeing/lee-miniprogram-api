package com.lee.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.base.config.OAuth2Config;
import com.lee.base.config.ServerConfig;
import com.lee.base.domain.Token;
import com.lee.base.dto.LoginUserDTO;
import com.lee.base.dto.UserDTO;
import com.lee.base.entity.User;
import com.lee.base.enums.ResponseEnum;
import com.lee.base.enums.UrlEnum;
import com.lee.base.mapper.UserMapper;
import com.lee.base.utils.BeanUtils;
import com.lee.base.utils.RedisUtil;
import com.lee.base.vo.LoginUserVO;
import com.lee.base.service.RoleService;
import com.lee.base.service.UserService;
import com.lee.base.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserDTO userDTO)  {
        User userPO = new User();
        BeanUtils.copyPropertiesIgnoreNull(userDTO,userPO);
        userMapper.insert(userPO);
        int id = userPO.getId();
        userMapper.insertUserRole(id, 1);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Integer id)  {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDTO userDTO) {

    }

    @Override
    public ResponseVO login(LoginUserDTO loginUserDTO) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("client_id", OAuth2Config.CLIENT_ID);
        paramMap.add("client_secret", OAuth2Config.CLIENT_SECRET);
        paramMap.add("username", loginUserDTO.getAccount());
        paramMap.add("password", loginUserDTO.getPassword());
        paramMap.add("grant_type", OAuth2Config.GRANT_TYPE[0]);
        Token token = null;
        try {
            //因为oauth2本身自带的登录接口是"/oauth/token"，并且返回的数据类型不能按我们想要的去返回
            //但是我的业务需求是，登录接口是"user/login"，由于我没研究过要怎么去修改oauth2内部的endpoint配置
            //所以这里我用restTemplate(HTTP客户端)进行一次转发到oauth2内部的登录接口，比较简单粗暴
            token = restTemplate.postForObject(serverConfig.getUrl() + UrlEnum.LOGIN_URL.getUrl(), paramMap, Token.class);
            LoginUserVO loginUserVO = redisUtil.get(token.getValue(), LoginUserVO.class);
            if(loginUserVO != null){
                //登录的时候，判断该用户是否已经登录过了
                //如果redis里面已经存在该用户已经登录过了的信息
                //我这边要刷新一遍token信息，不然，它会返回上一次还未过时的token信息给你
                //不便于做单点维护
                token = oauthRefreshToken(loginUserVO.getRefreshToken());
                redisUtil.deleteCache(loginUserVO.getAccessToken());
            }
        } catch (Exception e) {
            try {

                e.printStackTrace();
                //此处应该用自定义异常去返回，在这里我就不去具体实现了
                //throw new Exception("username or password error");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        //这里我拿到了登录成功后返回的token信息之后，我再进行一层封装，最后返回给前端的其实是LoginUserVO
        LoginUserVO loginUserVO = new LoginUserVO();
        User userPO = userMapper.findUserByAccount(loginUserDTO.getAccount());
        BeanUtils.copyPropertiesIgnoreNull(userPO, loginUserVO);
        loginUserVO.setPassword(userPO.getPassword());
        loginUserVO.setAccessToken(token.getValue());
        loginUserVO.setAccessTokenExpiresIn(token.getExpiresIn());
        loginUserVO.setAccessTokenExpiration(token.getExpiration());
        loginUserVO.setExpired(token.isExpired());
        loginUserVO.setScope(token.getScope());
        loginUserVO.setTokenType(token.getTokenType());
        loginUserVO.setRefreshToken(token.getRefreshToken().getValue());
        loginUserVO.setRefreshTokenExpiration(token.getRefreshToken().getExpiration());
        //存储登录的用户
        redisUtil.set(loginUserVO.getAccessToken(),loginUserVO,TimeUnit.HOURS.toSeconds(1));
        return ResponseVO.success(loginUserVO);
    }

    @Override
    public IPage<User> selectPage(IPage<User> page, Wrapper<User> queryWrapper) {
        QueryWrapper<User> wrapper = new QueryWrapper();

        page = new Page<>(1,2);

        IPage<User> userIPage = userMapper.selectPage(page, wrapper);
        return userIPage;
        //IPage<Map<String, Object>> mapIPage = userMapper.selectMapsPage(page, wrapper);
    }

    @Override
    public IPage<Map<String, Object>> selectMapsPage(IPage<User> page, Wrapper<User> queryWrapper) {
        QueryWrapper<User> wrapper = new QueryWrapper();

        page = new Page<>(1,2);

        IPage<Map<String, Object>> mapIPage = userMapper.selectMapsPage(page, wrapper);
        return mapIPage;
    }

    @Override
    public User findUserByWxOpenid(String wxOpenId) {
        return userMapper.findUserByWxOpenid(wxOpenId);
    }

    /**
     * @description oauth2客户端刷新token
     * @param refreshToken
     * @date 2019/03/05 14:27:22
     * @author he.Lee
     * @return
     */
    private Token oauthRefreshToken(String refreshToken) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("client_id", OAuth2Config.CLIENT_ID);
        paramMap.add("client_secret", OAuth2Config.CLIENT_SECRET);
        paramMap.add("refresh_token", refreshToken);
        paramMap.add("grant_type", OAuth2Config.GRANT_TYPE[1]);
        Token token = null;
        try {
            token = restTemplate.postForObject(serverConfig.getUrl() + UrlEnum.LOGIN_URL.getUrl(), paramMap, Token.class);
        } catch (RestClientException e) {
            try {
                //此处应该用自定义异常去返回，在这里我就不去具体实现了
                throw new Exception(ResponseEnum.REFRESH_TOKEN_INVALID.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return token;
    }


}
