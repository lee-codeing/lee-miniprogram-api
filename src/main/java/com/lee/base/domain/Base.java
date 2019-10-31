package com.lee.base.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;

/**
 * @description:
 * @date 2019/02/19 18:01:22
 * @author he.lee
 */
@Data
public class Base implements Serializable {

    private static final long serialVersionUID = -7519418012137093264L;

    @TableId(value = "id",type = IdType.AUTO)
    protected Integer id;

    /**
     * 添加时间
     */
    protected Long createdTime;


    /**
     * 描述
     */
    protected String description;

}
