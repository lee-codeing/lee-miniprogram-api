package com.lee.base.service;


import com.lee.base.entity.Role;
import com.lee.base.vo.ResponseVO;

/**
 * @description 角色管理接口
 * @author he.Lee
 * @date 2019/10/29 11:05
 */
public interface RoleService {

    /**
     * @description 获取角色列表
     * @return
     */
    ResponseVO findAllRoleVO();

    /**
     * @description 根据角色id获取角色
     */
    Role findById(Integer id);
}
