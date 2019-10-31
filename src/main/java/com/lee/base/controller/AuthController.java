package com.lee.base.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lee.base.dto.LoginUserDTO;
import com.lee.base.dto.UserDTO;
import com.lee.base.entity.User;
import com.lee.base.service.RoleService;
import com.lee.base.service.UserService;
import com.lee.base.utils.AssertUtils;
import com.lee.base.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @description 用户权限管理
 * @author he.Lee
 * @date 2019/4/19 13:58
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/auth/")
public class AuthController {

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisTokenStore redisTokenStore;

    /**
     * @description 添加用户
     * @param userDTO
     * @return
     */
    @PostMapping("user")
    public ResponseVO add(@Valid @RequestBody UserDTO userDTO) throws Exception {
        userServiceImpl.addUser(userDTO);
        return ResponseVO.success();
    }

    /**
     * @description 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("user/{id}")
    public ResponseVO deleteUser(@PathVariable("id")Integer id) throws Exception {
        userServiceImpl.deleteUser(id);
        return ResponseVO.success();
    }

    /**
     * @descripiton 修改用户
     * @param userDTO
     * @return
     */
    @PutMapping("user")
    public ResponseVO updateUser(@Valid @RequestBody UserDTO userDTO){
        userServiceImpl.updateUser(userDTO);
        return ResponseVO.success();
    }

    /**
     * @description 获取用户列表
     * @return
     */
    @GetMapping("user")
    public ResponseVO findAllUser(){
//        IPage<User> userIPage = userServiceImpl.selectPage(null, null);
        IPage<Map<String, Object>> userIPage = userServiceImpl.selectMapsPage(null, null);
        return ResponseVO.success(userIPage);
    }

    /**
     * @description 用户登录
     * @param loginUserDTO
     * @return
     */
    @PostMapping("user/login")
    public ResponseVO login(LoginUserDTO loginUserDTO){
        return userServiceImpl.login(loginUserDTO);
    }


    /**
     * @description 用户注销
     * @param authorization
     * @return
     */
    @GetMapping("user/logout")
    public ResponseVO logout(@RequestHeader("Authorization") String authorization){
        redisTokenStore.removeAccessToken(AssertUtils.extracteToken(authorization));
        return ResponseVO.success();
    }

    /**
     * @description 获取所有角色列表
     * @return
     */
    @GetMapping("role")
    public ResponseVO findAllRole(){
        return roleService.findAllRoleVO();
    }


}
