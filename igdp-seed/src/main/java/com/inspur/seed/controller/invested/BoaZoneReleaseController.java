package com.inspur.seed.controller.invested;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.invested.BoaZoneReleaseMain;
import com.inspur.seed.dto.invested.BoaZoneReleaseDTO;
import com.inspur.seed.service.invested.IBoaZoneReleaseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * BOA to Zone release management controller.
 */
@RestController
@RequestMapping("/invested/release/boa-zone")
public class BoaZoneReleaseController extends BaseController {

    @Resource
    private IBoaZoneReleaseService releaseService;

    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false, defaultValue = "0") Integer storeType,
            @RequestParam(required = true) String releaseType,
            @RequestParam(required = false) String releaseName,
            @RequestParam(required = false) String inputType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        int type = resolveStoreType(storeType);
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("releaseType", releaseType);
        snapshot.put("releaseName", releaseName);
        snapshot.put("inputType", inputType);
        snapshot.put("startTime", startTime != null ? startTime.toString() : null);
        snapshot.put("endTime", endTime != null ? endTime.toString() : null);
        releaseService.saveRequestSnapshot(type, "list", snapshot);
        startPage();
        List<BoaZoneReleaseMain> list = releaseService.queryReleaseList(releaseType, releaseName, inputType, startTime, endTime);
        return getDataTable(list);
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody BoaZoneReleaseDTO dto) {
        try {
            Map<String, String> result = releaseService.addRelease(dto);
            return AjaxResult.success("BOA to Zone release added successfully", result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody BoaZoneReleaseDTO dto) {
        try {
            releaseService.editRelease(dto);
            return AjaxResult.success("BOA to Zone release edited successfully");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    public AjaxResult getDetail(@PathVariable String id) {
        try {
            Map<String, Object> detail = releaseService.queryReleaseDetail(id);
            return AjaxResult.success("Query success", detail);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/detailByReleaseId/{releaseId}")
    public AjaxResult getDetailByReleaseId(@PathVariable String releaseId) {
        try {
            Map<String, Object> detail = releaseService.queryReleaseDetailByReleaseId(releaseId);
            return AjaxResult.success("Query success", detail);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{ids}")
    public AjaxResult delete(
            @RequestParam(required = false, defaultValue = "0") Integer storeType,
            @PathVariable String ids) {
        try {
            int type = resolveStoreType(storeType);
            Map<String, Object> snapshot = new LinkedHashMap<>();
            snapshot.put("ids", ids);
            releaseService.saveRequestSnapshot(type, "delete", snapshot);
            List<String> idList = Arrays.asList(ids.split(","));
            releaseService.removeRelease(idList);
            return AjaxResult.success("BOA to Zone release deleted successfully");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/stockStatus")
    public AjaxResult getStockStatus(
            @RequestParam(required = false, defaultValue = "0") Integer storeType,
            @RequestParam String releaseIds) {
        try {
            int type = resolveStoreType(storeType);
            Map<String, Object> snapshot = new LinkedHashMap<>();
            snapshot.put("releaseIds", releaseIds);
            releaseService.saveRequestSnapshot(type, "stockStatus", snapshot);
            List<String> idList = Arrays.asList(releaseIds.split(","));
            Map<String, String> statusMap = releaseService.queryStockStatus(idList);
            return AjaxResult.success("Query success", statusMap);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/availableStock")
    public AjaxResult getAvailableStock(
            @RequestParam String inputType,
            @RequestParam String inputCategory,
            @RequestParam String organCode) {
        try {
            Map<String, Object> result = releaseService.queryAvailableStock(inputCategory, organCode);
            return AjaxResult.success("Query success", result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    private static int resolveStoreType(Integer storeType) {
        return (storeType != null && storeType == 1) ? 1 : 0;
    }
}