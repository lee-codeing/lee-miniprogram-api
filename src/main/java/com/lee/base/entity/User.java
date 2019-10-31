package com.lee.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lee.base.domain.Base;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "um_t_user")
public class User extends Base implements Serializable {
    private static final long serialVersionUID = -8478114427891717226L;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户 --角色 多对一
     */
    @JsonBackReference
    @TableField(exist=false)
    private Role role;

    /**
     * 微信小程序的OPENID
     */
    private String wxOpenId;

    /**
     * 微信小程序的会话密钥
     */
    private String wxSessionKey;

    /**
     * 微信公众平台唯一KEY
     */
    private String wxUnionId;

}
