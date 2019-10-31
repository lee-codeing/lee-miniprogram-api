package com.lee.base.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.lee.base.dto.LoginUserDTO;
import com.lee.base.dto.UserDTO;
import com.lee.base.entity.User;
import com.lee.base.vo.UserVO;
import com.lee.base.vo.ResponseVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @description 用户业务接口
 * @author he.Lee
 * @date 2019/10/29 14:06
 */
public interface UserService {

    /**
     * @description 添加用户
     */
    void addUser(UserDTO userDTO) throws Exception;

    /**
     * 删除用户
     * @param id
     */
    void deleteUser(Integer id) throws Exception;

    /**
     * @description 修改用户信息
     * @param userDTO
     */
    void updateUser(UserDTO userDTO);


    /**
     * @description 用户登录
     * @return
     */
    ResponseVO login(LoginUserDTO loginUserDTO);

    /**
     * 分页查询用户
     */
    IPage<User> selectPage(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> queryWrapper);

    /**
     * 分页查询返回Map
     */
    IPage<Map<String, Object>> selectMapsPage(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> queryWrapper);

    /**
     * 根据微信小程序OPENID查询用户信息
     * @param wxOpenId
     * @return
     */
    User findUserByWxOpenid(String wxOpenId);

}
