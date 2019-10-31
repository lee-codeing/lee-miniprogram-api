package com.lee.base.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @auther he.Lee
 * @Description: TODO
 * @date 2019/10/30 18:28
 */
@Data
@TableName(value = "sxsj_record")
public class Record implements Serializable {

    @TableId(value = "record_id")
    private String recordId;
    private String recordContext;
    private Long recordCreateTime;
    private Long recordUpdateTime;
    private int recordStatus;
    private String recordTitle;
    private String openId;
}
