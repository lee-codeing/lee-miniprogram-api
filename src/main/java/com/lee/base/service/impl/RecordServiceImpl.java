package com.lee.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.base.entity.Record;
import com.lee.base.mapper.RecordMapper;
import com.lee.base.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @version 1.0
 * @auther he.Lee
 * @Description: 记录服务实现
 * @date 2019/10/31 9:52
 */
@Service
public class RecordServiceImpl implements RecordService {
    private static final Logger LOG = LoggerFactory.getLogger(RecordServiceImpl.class);
    @Autowired
    private RecordMapper recordMapper;

    @Override
    public int insertRecord(JSONObject jsonObject) {
        int count = 0;
        Record record = new Record();
        record.setRecordTitle(jsonObject.getString("recordTitle"));
        record.setRecordContext(jsonObject.getString("recordContext"));
        record.setOpenId(jsonObject.getString("openId"));
        record.setRecordId(UUID.randomUUID().toString().replaceAll("-", ""));
        record.setRecordCreateTime(System.currentTimeMillis());
        try {
            count = recordMapper.insert(record);
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return count;
        }
        return count;
    }

    @Override
    public int updateRecord(JSONObject jsonObject) {
        int count = 0;
        Record record = new Record();
        Record queryRecord = findRecord(jsonObject.getString("recordId"));
        if (queryRecord != null) {
            record.setRecordContext(jsonObject.getString("recordContext"));
            record.setRecordTitle(jsonObject.getString("recordTitle"));
            if (StringUtils.isNotEmpty(record.getRecordContext()) || StringUtils.isNotEmpty(record.getRecordTitle())) {
                record.setRecordUpdateTime(System.currentTimeMillis());
                record.setRecordId(jsonObject.getString("recordId"));
                count = recordMapper.updateById(record);
            }
        }
        return count;
    }

    @Override
    public Record findRecord(String id) {
        return recordMapper.selectById(id);
    }

    @Override
    public IPage<Record> selectPage(JSONObject jsonParam) {
        QueryWrapper<Record> wrapper = new QueryWrapper();
        Record record = new Record();
        record.setOpenId(jsonParam.getString("openId"));
        record.setRecordStatus(0);
        wrapper.setEntity(record);
        wrapper.orderByDesc("record_create_time");
        Page<Record> page = new Page<>(jsonParam.getIntValue("currentPage")
                > 0 ? jsonParam.getIntValue("currentPage") : 1, jsonParam.getIntValue("pageSize")
                > 0 ? jsonParam.getIntValue("pageSize") : 10);
        page.setSearchCount(false);

        IPage<Record> RecordIPage = recordMapper.selectPage(page, wrapper);
        return RecordIPage;
    }

    @Override
    public int deleteRecord(String recordId) {
        int count = 0;
        Record queryRecord = findRecord(recordId);
        if (queryRecord != null) {
            queryRecord = new Record();
            queryRecord.setRecordId(recordId);
            queryRecord.setRecordStatus(1);
            count = recordMapper.updateById(queryRecord);
        }
        return count;
    }
}
