package com.inspur.seed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.BreedingSeedCertificationDTO;
import com.inspur.seed.domain.entity.BreedingSeedCertification;
import com.inspur.seed.domain.vo.BreedingSeedCertificationVO;
import com.inspur.seed.service.IBreedingSeedCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 繁殖种子认证申请Controller
 *
 * @author igdp
 * @date 2025-11-29
 */
@RestController
@RequestMapping("/seed/breeding/certification")
public class BreedingSeedCertificationController {

    @Autowired
    private IBreedingSeedCertificationService breedingSeedCertificationService;

    /**
     * 分页查询繁殖种子认证申请列表
     */
    @PostMapping("/page")
    public AjaxResult page(@RequestBody BreedingSeedCertificationDTO dto,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<BreedingSeedCertification> page = new Page<>(pageNum, pageSize);
        IPage<BreedingSeedCertificationVO> result = breedingSeedCertificationService.selectBreedingSeedCertificationPage(page, dto);
        return AjaxResult.success(result);
    }

    /**
     * 查询繁殖种子认证申请列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody BreedingSeedCertificationDTO dto) {
        List<BreedingSeedCertificationVO> list = breedingSeedCertificationService.selectBreedingSeedCertificationList(dto);
        return AjaxResult.success(list);
    }

    /**
     * 获取繁殖种子认证申请详细信息
     */
    @GetMapping("/detail/{dataId}")
    public AjaxResult detail(@PathVariable String dataId) {
        BreedingSeedCertificationVO vo = breedingSeedCertificationService.selectBreedingSeedCertificationById(dataId);
        return AjaxResult.success(vo);
    }

    /**
     * 新增繁殖种子认证申请
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BreedingSeedCertificationDTO dto) {
        return AjaxResult.success(breedingSeedCertificationService.insertBreedingSeedCertification(dto));
    }

    /**
     * 修改繁殖种子认证申请
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody BreedingSeedCertificationDTO dto) {
        return AjaxResult.success(breedingSeedCertificationService.updateBreedingSeedCertification(dto));
    }

    /**
     * 删除繁殖种子认证申请
     */
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody String[] dataIds) {
        return AjaxResult.success(breedingSeedCertificationService.deleteBreedingSeedCertificationByIds(dataIds));
    }
}
