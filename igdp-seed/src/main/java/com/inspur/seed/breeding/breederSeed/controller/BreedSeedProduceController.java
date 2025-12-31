package com.inspur.seed.breeding.breederSeed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.breeding.breederSeed.domain.dto.BreedSeedProduceDTO;
import com.inspur.seed.breeding.breederSeed.domain.dto.BreedSeedProduceQueryDTO;
import com.inspur.seed.breeding.breederSeed.service.IBreedSeedProduceService;
import com.inspur.seed.breeding.breederSeed.domain.vo.BreedSeedProduceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Breeder Seed 生产Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/seed/breed/produce")
public class BreedSeedProduceController extends BaseController {

    @Autowired
    private IBreedSeedProduceService breedSeedProduceService;

    /**
     * 查询生产数据列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BreedSeedProduceQueryDTO queryDTO) {
        startPage();
        List<BreedSeedProduceVO> list = breedSeedProduceService.getProduceList(queryDTO);
        return getDataTable(list);
    }

    /**
     * 获取生产数据详情
     */
    @GetMapping("/detail/{produceBatchId}")
    public AjaxResult getDetail(@PathVariable("produceBatchId") String produceBatchId) {
        return success(breedSeedProduceService.getProduceById(produceBatchId));
    }

    /**
     * 获取生产批次剩余数量
     */
    @GetMapping("/remaining/{produceBatchId}")
    public AjaxResult getRemaining(@PathVariable("produceBatchId") String produceBatchId) {
        BreedSeedProduceVO vo = breedSeedProduceService.getProduceById(produceBatchId);
        if (vo == null) {
            return error("Production batch not found");
        }
        return success(vo);
    }

    /**
     * 新增生产数据
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BreedSeedProduceDTO dto) {
        BreedSeedProduceVO result = breedSeedProduceService.addProduce(dto);
        return success(result);
    }

    @GetMapping("/delete")
    public AjaxResult delete(@RequestParam("produceBatchId") String produceBatchId) {
    	breedSeedProduceService.delete(produceBatchId);
    	return success();
    }
}
