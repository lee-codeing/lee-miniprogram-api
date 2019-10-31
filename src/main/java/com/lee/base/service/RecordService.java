package com.lee.base.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lee.base.entity.Record;

/**
 * @version 1.0
 * @auther he.Lee
 * @Description: 记录服务接口
 * @date 2019/10/30 19:00
 */
public interface RecordService {
    /**
     * 添加记录
     *
     * @param jsonObject
     * @return
     */
    public int insertRecord(JSONObject jsonObject);

    /**
     * 更新记录
     *
     * @param jsonObject
     * @return
     */
    public int updateRecord(JSONObject jsonObject);

    /**
     * 查询记录
     *
     * @param id
     * @return
     */
    public Record findRecord(String id);

    /**
     * 分页查询
     *
     * @param jsonObject
     * @return
     */
    public IPage<Record> selectPage(JSONObject jsonObject);

    /**
     * 删除记录
     *
     * @param recordId
     * @return
     */
    public int deleteRecord(String recordId);
}
