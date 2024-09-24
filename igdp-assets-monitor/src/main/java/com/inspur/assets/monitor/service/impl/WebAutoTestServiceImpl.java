package com.inspur.assets.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.monitor.domain.WebAutoTest;
import com.inspur.assets.monitor.domain.WebAutoTestInput;
import com.inspur.assets.monitor.domain.WebInspectionInfo;
import com.inspur.assets.monitor.mapper.WebAutoTestInputMapper;
import com.inspur.assets.monitor.mapper.WebAutoTestMapper;
import com.inspur.assets.monitor.service.IWebAutoTestService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WebAutoTestServiceImpl extends ServiceImpl<WebAutoTestMapper, WebAutoTest> implements IWebAutoTestService {


    @Override
    public List<WebAutoTest> SelectAllRecord() {

        return list();
    }

    @Override
    public AjaxResult deleteRecord(int id) {
        LambdaQueryWrapper<WebAutoTest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WebAutoTest::getId, id);
        removeById(id);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult insertNewRecord(WebAutoTest param) {

        save(param);
        return AjaxResult.success();
    }
}
