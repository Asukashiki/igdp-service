package com.inspur.seed.controller.invested;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.invested.InputReleaseFarmerMain;
import com.inspur.seed.domain.invested.InputReleaseMain;
import com.inspur.seed.service.invested.IInputReleaseFarmerService;
import com.inspur.seed.service.invested.IInputReleaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分发单管理Controller（用于入库单关联）
 * 供入库管理模块调用，获取分发单下拉列表和详情
 *
 * @author igdp-seed
 * @date 2025-12-11
 */
@RestController
@RequestMapping("/invested/distribution")
public class InputReleaseForInboundController extends BaseController {

    @Resource
    private IInputReleaseService releaseService;

    @Resource
    private IInputReleaseFarmerService releaseFarmerService;

    /**
     * 获取分发单下拉列表（OSE→Union 和 Union→Woreda）
     * 用于入库单的关联单号下拉选择
     *
     * @return 分发单列表（包含 OSE_TO_UNION 和 UNION_TO_WOREDA 类型）
     */
    @GetMapping("/list")
    public AjaxResult getDistributionList() {
        try {
            // 查询 OSE→Union 分发单
            List<InputReleaseMain> oseToUnionList = releaseService.queryReleaseList(
                    "OSE_TO_UNION", null, null, null, null);

            // 查询 Union→Woreda 分发单
            List<InputReleaseMain> unionToWoredaList = releaseService.queryReleaseList(
                    "UNION_TO_WOREDA", null, null, null, null);
            //查询 Woreda->farmer的分发单
            List<InputReleaseFarmerMain> coopTofarmer = releaseFarmerService.queryReleaseList(null,null,null,null,null,null,null,"0");
            // 合并两个列表
            List<Map<String, Object>> resultList = new ArrayList<>();

            // 添加 OSE→Union 分发单
            for (InputReleaseMain release : oseToUnionList) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", release.getId());
                item.put("releaseId", release.getReleaseId());
                item.put("releaseName", release.getReleaseName());
                item.put("releaseType", release.getReleaseType());
                item.put("releaseOrg", release.getReleaseOrg());
                item.put("releaseDate", release.getReleaseDate());
                resultList.add(item);
            }

            // 添加 Union→Woreda 分发单
            for (InputReleaseMain release : unionToWoredaList) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", release.getId());
                item.put("releaseId", release.getReleaseId());
                item.put("releaseName", release.getReleaseName());
                item.put("releaseType", release.getReleaseType());
                item.put("releaseOrg", release.getReleaseOrg());
                item.put("releaseDate", release.getReleaseDate());
                resultList.add(item);
            }

            for (InputReleaseFarmerMain farmerMain: coopTofarmer){
                Map<String, Object> item = new HashMap<>();
                item.put("id", farmerMain.getId());
                item.put("releaseId", farmerMain.getReleaseId());
                item.put("releaseName", farmerMain.getFarmerName());
                item.put("releaseType", null);
                item.put("releaseOrg", farmerMain.getReleaseOrg());
                item.put("releaseDate", farmerMain.getReleaseDate());
                resultList.add(item);
            }

            return AjaxResult.success("查询成功", resultList);
        } catch (Exception e) {
            logger.error("获取分发单列表失败", e);
            return AjaxResult.error("获取分发单列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取分发单详情（包含明细行项）
     * 用于选择分发单后自动填充入库单明细
     *
     * @param id 分发单主表ID
     * @return 分发单详情（main + details）
     */
    @GetMapping("/detail/{id}")
    public AjaxResult getDistributionDetail(@PathVariable String id) {
        try {
            Map<String, Object> detail = releaseService.queryReleaseDetail(id);
            return AjaxResult.success("查询成功", detail);
        } catch (Exception e) {
            logger.error("获取分发单详情失败", e);
            return AjaxResult.error("获取分发单详情失败: " + e.getMessage());
        }
    }
}
