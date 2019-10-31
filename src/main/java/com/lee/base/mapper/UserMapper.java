package com.lee.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lee.base.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @version 1.0 2019/10/28
 * @auther lee he
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据账号查询用户信息
     * @param account
     * @return
     */
    @Select("SELECT t_u.*, t_ru.role_id  FROM um_t_user t_u LEFT JOIN um_t_role_user t_ru ON t_u.id = t_ru.user_id " +
            " WHERE t_u.account = #{account}")
    @Results({
            @Result(column="role_id", property="role", one=@One(select="com.lee.base.mapper.RoleMapper.findById", fetchType= FetchType.EAGER))
    })
    User findUserByAccount(String account);

    /**
     * 查询全部用户信息
     * @return
     */
    List<User> findAll();

    /**
     * 添加用户表与角色关联表
     */
    @Select("insert into um_t_role_user(role_id, user_id) values(#{roleId}, #{userId})")
    void insertUserRole(int userId, int roleId);

    /**
     * 根据OPENID查询用户信息
     * @param wxOpenId
     * @return
     */
    @Select("SELECT * FROM um_t_user WHERE wx_open_id = #{wxOpenId}")
    User findUserByWxOpenid(String wxOpenId);

}
