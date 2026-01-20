package com.inspur.seed.breeding.breederSeed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.breeding.breederSeed.domain.entity.BreedSeedProduceResult;
import com.inspur.seed.breeding.breederSeed.service.IBreedSeedProduceResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.breeding.breederSeed.domain.dto.BreedSeedProduceResultQueryDTO;
import com.inspur.seed.breeding.breederSeed.domain.vo.BreedSeedProduceResultVO;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Seed Production Result Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/seed/breed/produce/result")
public class BreedSeedProduceResultController extends BaseController {

    @Autowired
    private IBreedSeedProduceResultService breedSeedProduceResultService;

    /**
     * Submit production result
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BreedSeedProduceResult result) {
        return toAjax(breedSeedProduceResultService.submitResult(result));
    }

    /**
     * Query Result List
     */
    @GetMapping("/list")
    public TableDataInfo list(BreedSeedProduceResultQueryDTO queryDTO) {
        startPage();
        List<BreedSeedProduceResultVO> list = breedSeedProduceResultService.getResultList(queryDTO);
        return getDataTable(list);
    }

    /**
     * Get Result Detail
     */
    @GetMapping(value = "/{resultId}")
    public AjaxResult getInfo(@PathVariable("resultId") String resultId) {
        return success(breedSeedProduceResultService.getResultById(resultId));
    }

    /**
     * Delete Result
     */
    @DeleteMapping("/{resultIds}")
    public AjaxResult remove(@PathVariable String[] resultIds) {
    	// Logic to revert production status? For now just delete record.
    	// Ideally we should check status and revert produceStatus to On-Going if needed
        return toAjax(breedSeedProduceResultService.removeBatchByIds(java.util.Arrays.asList(resultIds)));
    }
}
