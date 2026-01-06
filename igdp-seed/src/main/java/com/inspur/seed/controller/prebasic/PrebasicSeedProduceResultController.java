package com.inspur.seed.controller.prebasic;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.prebasic.PrebasicSeedProduceResult;
import com.inspur.seed.dto.prebasic.PrebasicSeedProduceResultQueryDTO;
import com.inspur.seed.service.prebasic.IPrebasicSeedProduceResultService;
import com.inspur.seed.vo.prebasic.PrebasicSeedProduceResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Pre-basic Seed 生产结果Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/seed/prebasic/produce/result")
public class PrebasicSeedProduceResultController extends BaseController {

    @Autowired
    private IPrebasicSeedProduceResultService prebasicSeedProduceResultService;

    /**
     * 提交生产结果
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody PrebasicSeedProduceResult result) {
        return toAjax(prebasicSeedProduceResultService.submitResult(result));
    }

    /**
     * 查询结果列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PrebasicSeedProduceResultQueryDTO queryDTO) {
        startPage();
        List<PrebasicSeedProduceResultVO> list = prebasicSeedProduceResultService.getResultList(queryDTO);
        return getDataTable(list);
    }

    /**
     * 获取结果详情
     */
    @GetMapping("/{resultId}")
    public AjaxResult getInfo(@PathVariable("resultId") String resultId) {
        return success(prebasicSeedProduceResultService.getResultById(resultId));
    }
}
