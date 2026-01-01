package com.inspur.seed.controller.basic;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.dto.basic.BasicSeedProduceDTO;
import com.inspur.seed.dto.basic.BasicSeedProduceQueryDTO;
import com.inspur.seed.service.basic.IBasicSeedProduceService;
import com.inspur.seed.vo.basic.BasicSeedProduceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Basic Seed 生产Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/seed/basic/produce")
public class BasicSeedProduceController extends BaseController {

    @Autowired
    private IBasicSeedProduceService basicSeedProduceService;

    /**
     * 查询生产数据列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BasicSeedProduceQueryDTO queryDTO) {
        startPage();
        List<BasicSeedProduceVO> list = basicSeedProduceService.getProduceList(queryDTO);
        return getDataTable(list);
    }

    /**
     * 获取生产数据详情
     */
    @GetMapping("/detail/{produceBatchId}")
    public AjaxResult getDetail(@PathVariable("produceBatchId") String produceBatchId) {
        return success(basicSeedProduceService.getProduceById(produceBatchId));
    }

    /**
     * 新增生产数据
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BasicSeedProduceDTO dto) {
        BasicSeedProduceVO result = basicSeedProduceService.addProduce(dto);
        return success(result);
    }

    /**
     * 作废批次
     */
    @PutMapping("/void/{produceBatchId}")
    public AjaxResult voidProduce(@PathVariable("produceBatchId") String produceBatchId) {
        basicSeedProduceService.voidProduce(produceBatchId);
        return success("Batch voided successfully");
    }
}
