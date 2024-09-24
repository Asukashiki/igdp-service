package com.inspur.assets.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.assets.monitor.domain.WebAutoTaskSummary;
import com.inspur.assets.monitor.domain.WebAutoTest;

import java.util.List;

public interface IWebAutoTaskSummaryService extends IService<WebAutoTaskSummary> {
    List<WebAutoTaskSummary> SelectAllContent();

    List<WebAutoTaskSummary> QueryTargetContent(WebAutoTaskSummary param);

    List<WebAutoTaskSummary>QueryTargetById(Integer id);
}
