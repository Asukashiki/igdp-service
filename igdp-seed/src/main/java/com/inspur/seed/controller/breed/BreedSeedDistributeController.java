package com.inspur.seed.controller.breed;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.dto.breed.BreedSeedDistributeDTO;
import com.inspur.seed.dto.breed.BreedSeedDistributeQueryDTO;
import com.inspur.seed.service.breed.IBreedSeedDistributeService;
import com.inspur.seed.vo.breed.BreedSeedDistributeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Breeder Seed 分发Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/seed/breed/distribute")
public class BreedSeedDistributeController extends BaseController {

    @Autowired
    private IBreedSeedDistributeService breedSeedDistributeService;

    /**
     * 查询分发数据列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BreedSeedDistributeQueryDTO queryDTO) {
        startPage();
        List<BreedSeedDistributeVO> list = breedSeedDistributeService.getDistributeList(queryDTO);
        return getDataTable(list);
    }

    /**
     * 新增分发数据
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BreedSeedDistributeDTO dto) {
        BreedSeedDistributeVO result = breedSeedDistributeService.addDistribute(dto);
        return success(result);
    }
}
