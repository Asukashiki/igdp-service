package com.inspur.seed.controller.prebasic;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.dto.prebasic.PrebasicSeedProduceDTO;
import com.inspur.seed.dto.prebasic.PrebasicSeedProduceQueryDTO;
import com.inspur.seed.service.prebasic.IPrebasicSeedProduceService;
import com.inspur.seed.vo.prebasic.PrebasicSeedProduceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Pre-basic Seed 生产Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/seed/prebasic/produce")
public class PrebasicSeedProduceController extends BaseController {

    @Autowired
    private IPrebasicSeedProduceService prebasicSeedProduceService;

    /**
     * 查询生产数据列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PrebasicSeedProduceQueryDTO queryDTO) {
        startPage();
        List<PrebasicSeedProduceVO> list = prebasicSeedProduceService.getProduceList(queryDTO);
        return getDataTable(list);
    }

    /**
     * 获取生产数据详情
     */
    @GetMapping("/detail/{produceBatchId}")
    public AjaxResult getDetail(@PathVariable("produceBatchId") String produceBatchId) {
        return success(prebasicSeedProduceService.getProduceById(produceBatchId));
    }

    /**
     * 新增生产数据
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody PrebasicSeedProduceDTO dto) {
        PrebasicSeedProduceVO result = prebasicSeedProduceService.addProduce(dto);
        return success(result);
    }

    /**
     * 作废批次
     */
    @PutMapping("/void/{produceBatchId}")
    public AjaxResult voidProduce(@PathVariable("produceBatchId") String produceBatchId) {
        prebasicSeedProduceService.voidProduce(produceBatchId);
        return success("Batch voided successfully");
    }
}
