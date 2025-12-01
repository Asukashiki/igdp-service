package com.inspur.seed.controller.breed;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.dto.breed.BreedSeedProduceDTO;
import com.inspur.seed.dto.breed.BreedSeedProduceQueryDTO;
import com.inspur.seed.service.breed.IBreedSeedProduceService;
import com.inspur.seed.vo.breed.BreedSeedProduceVO;
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
    @GetMapping("/detail/{breedSeedProduceBatchId}")
    public AjaxResult getDetail(@PathVariable("breedSeedProduceBatchId") String breedSeedProduceBatchId) {
        return success(breedSeedProduceService.getProduceById(breedSeedProduceBatchId));
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
    public AjaxResult delete(@RequestParam("breedSeedProduceBatchId") String breedSeedProduceBatchId) {
    	breedSeedProduceService.delete(breedSeedProduceBatchId);
    	return success();
    }
}
