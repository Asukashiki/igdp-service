package com.inspur.assets.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.assets.monitor.domain.WebAutoTest;
import com.inspur.common.core.domain.AjaxResult;

import java.util.List;
public interface IWebAutoTestService extends IService<WebAutoTest> {

     List<WebAutoTest> SelectAllRecord();

     AjaxResult deleteRecord(int id);

     AjaxResult insertNewRecord(WebAutoTest webAutoTest);
}
