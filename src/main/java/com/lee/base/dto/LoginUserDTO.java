package com.lee.base.dto;

import lombok.Data;


/**
 * @description 登录用户传输参数
 * @author he.Lee
 * @date 2019/4/19 14:26
 */
@Data
public class LoginUserDTO {

    /**
     * 用户名
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

}
