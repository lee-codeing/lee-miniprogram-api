package com.lee.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lee.base.entity.Record;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 1.0
 * @auther he.Lee
 * @Description: 记录持久层
 * @date 2019/10/30 18:44
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}
