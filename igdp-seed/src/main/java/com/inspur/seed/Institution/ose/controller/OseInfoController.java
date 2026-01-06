package com.inspur.seed.Institution.ose.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.Institution.ose.domain.dto.OseInfoDTO;
import com.inspur.seed.Institution.ose.domain.dto.OseInfoQueryDTO;
import com.inspur.seed.Institution.ose.service.IOseInfoService;
import com.inspur.seed.Institution.ose.domain.vo.OseInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OSE基础信息Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/seed/ose")
public class OseInfoController extends BaseController {

    @Autowired
    private IOseInfoService oseInfoService;

    /**
     * 查询OSE列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OseInfoQueryDTO queryDTO) {
        startPage();
        List<OseInfoVO> list = oseInfoService.getOseList(queryDTO);
        return getDataTable(list);
    }

    /**
     * 获取OSE详情
     */
    @GetMapping("/detail/{oseId}")
    public AjaxResult getDetail(@PathVariable("oseId") String oseId) {
        return success(oseInfoService.getOseById(oseId));
    }

    /**
     * 新增OSE信息
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody OseInfoDTO dto) {
        OseInfoVO result = oseInfoService.addOse(dto);
        return success(result);
    }

    /**
     * 更新OSE信息
     */
    @PutMapping("/update/{oseId}")
    public AjaxResult update(
            @PathVariable("oseId") String oseId,
            @Validated @RequestBody OseInfoDTO dto) {
        OseInfoVO result = oseInfoService.updateOse(oseId, dto);
        return success(result);
    }

    /**
     * 删除OSE信息
     */
    @DeleteMapping("/delete/{oseId}")
    public AjaxResult delete(@PathVariable("oseId") String oseId) {
        return toAjax(oseInfoService.deleteOse(oseId));
    }
}
