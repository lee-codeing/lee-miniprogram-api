package com.lee.base.entity;

import com.lee.base.domain.Base;
import lombok.Data;
import java.io.Serializable;

@Data
public class Role extends Base implements Serializable {

    private static final long serialVersionUID = -8478114427891717226L;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色
     */
    private String role;

    /**
     * 角色 -- 用户: 1对多
     */
}
