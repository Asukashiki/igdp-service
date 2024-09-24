package com.inspur.data.treating.controller;

import com.inspur.common.constant.HttpStatus;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.core.domain.entity.UnifyStatisticsItemValue;
import com.inspur.data.treating.service.IUnifyStatisticsItemValueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName UnifyStatisticsController
 * @date 2024/7/22 10:13
 */
@RestController
@RequestMapping("/unify-statistics")
public class UnifyStatisticsController extends BaseController {
    @Resource
    private IUnifyStatisticsItemValueService unifyStatisticsItemValueService;


    /**
     * 根据条件获取统计内容列表
     * 指标code或者指标id不能为空
     */
    @GetMapping("/value-list")
    public AjaxResult getUnifyStatisticsItemValueLit(UnifyStatisticsItemValue query) {
        if (StringUtils.isEmpty(query.getItemId()) && StringUtils.isEmpty(query.getItemCode())) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "指标编码或者ID不能为空");
        }
        List<UnifyStatisticsItemValue> valueList = unifyStatisticsItemValueService.getList(query);
        return AjaxResult.success(valueList);
    }

}
