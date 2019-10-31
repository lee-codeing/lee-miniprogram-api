package com.lee.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lee.base.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @version 1.0 2019/10/28
 * @auther lee he
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT * FROM um_t_role WHERE id = #{id}")
    Role findById(int id);
    List<Role> findAll();

}
