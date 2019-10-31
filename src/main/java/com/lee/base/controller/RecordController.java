package com.lee.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lee.base.entity.Record;
import com.lee.base.enums.ResponseEnum;
import com.lee.base.service.RecordService;
import com.lee.base.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @auther he.Lee
 * @Description: 记录控制层
 * @date 2019/10/30 17:42
 */

@RestController
@RequestMapping("/record")
public class RecordController {
    private static final Logger LOG = LoggerFactory.getLogger(RecordController.class);

    @Autowired
    private RecordService recordServiceImpl;

    /**
     * 记录列表
     *
     * @param currentPage
     * @return
     */
    @GetMapping("/records")
    public ResponseVO recordList(@RequestParam int currentPage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("currentPage", currentPage);
        jsonObject.put("openId", authentication.getName());
        IPage<Record> recordIPage = recordServiceImpl.selectPage(jsonObject);
        return ResponseVO.success(recordIPage);
    }

    /**
     * 记录添加
     *
     * @param jsonParam
     * @return
     */
    @PostMapping("/record")
    public ResponseVO recordAdd(@RequestBody String jsonParam) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JSONObject jsonObject = JSONObject.parseObject(jsonParam);
        jsonObject.put("openId", authentication.getName());
        int count = recordServiceImpl.insertRecord(jsonObject);
        if (count <= 0) {
            return ResponseVO.error(ResponseEnum.RECORD_ADD_ERROR);
        }
        return ResponseVO.success();
    }

    /**
     * 记录修改
     *
     * @param jsonParam
     * @return
     */
    @PutMapping("/record/{recordId}")
    public ResponseVO recordUpdate(@RequestBody String jsonParam, @PathVariable String recordId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JSONObject jsonObject = JSONObject.parseObject(jsonParam);
        jsonObject.put("openId", authentication.getName());
        jsonObject.put("recordId", recordId);
        int count = recordServiceImpl.updateRecord(jsonObject);
        if (count <= 0) {
            return ResponseVO.error(ResponseEnum.RECORD_UPDATE_ERROR);
        }
        return ResponseVO.success();
    }

    /**
     * 记录删除
     *
     * @param recordId
     * @return
     */
    @DeleteMapping("/record/{recordId}")
    public ResponseVO recordDelete(@PathVariable String recordId) {
        int count = recordServiceImpl.deleteRecord(recordId);
        if (count <= 0) {
            return ResponseVO.error(ResponseEnum.RECORD_DELETE_ERROR);
        }
        return ResponseVO.success();
    }
}
