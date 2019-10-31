package com.lee.base.service.impl;

import com.lee.base.entity.Role;
import com.lee.base.mapper.RoleMapper;
import com.lee.base.service.RoleService;
import com.lee.base.vo.ResponseVO;
import com.lee.base.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ResponseVO findAllRoleVO() {
        List<Role> rolePOList = roleMapper.findAll();
        List<RoleVO> roleVOList = new ArrayList<>();
        rolePOList.forEach(rolePO->{
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(rolePO,roleVO);
            roleVOList.add(roleVO);
        });
        return ResponseVO.success(roleVOList);
    }

    @Override
    public Role findById(Integer id) {
        return roleMapper.findById(id);
    }
}
