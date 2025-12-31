package com.inspur.seed.controller.basic;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.basic.BasicSeedProduceResult;
import com.inspur.seed.dto.basic.BasicSeedProduceResultQueryDTO;
import com.inspur.seed.service.basic.IBasicSeedProduceResultService;
import com.inspur.seed.vo.basic.BasicSeedProduceResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Basic Seed 生产结果Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/seed/basic/produce/result")
public class BasicSeedProduceResultController extends BaseController {

    @Autowired
    private IBasicSeedProduceResultService basicSeedProduceResultService;

    /**
     * 提交生产结果
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BasicSeedProduceResult result) {
        return toAjax(basicSeedProduceResultService.submitResult(result));
    }

    /**
     * 查询结果列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BasicSeedProduceResultQueryDTO queryDTO) {
        startPage();
        List<BasicSeedProduceResultVO> list = basicSeedProduceResultService.getResultList(queryDTO);
        return getDataTable(list);
    }

    /**
     * 获取结果详情
     */
    @GetMapping("/{resultId}")
    public AjaxResult getInfo(@PathVariable("resultId") String resultId) {
        return success(basicSeedProduceResultService.getResultById(resultId));
    }
}
