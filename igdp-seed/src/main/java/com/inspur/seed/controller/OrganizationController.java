package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.Organization;
import com.inspur.seed.service.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seed/organization")
public class OrganizationController extends BaseController {

    @Autowired
    private IOrganizationService organizationService;

    /** 查询组织列表 */
    @GetMapping("/list")
    public TableDataInfo list(Organization organization) {
        startPage();
        return getDataTable(organizationService.selectOrganizationList(organization));
    }

    /** 查询组织详情 */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(organizationService.selectOrganizationById(id));
    }

    /** 新增组织 */
    @PostMapping
    public AjaxResult add(@RequestBody Organization organization) {
        return toAjax(organizationService.createOrganization(organization));
    }

    /** 修改组织 */
    @PutMapping
    public AjaxResult edit(@RequestBody Organization organization) {
        return toAjax(organizationService.updateOrganization(organization));
    }

    /** 删除组织 */
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(organizationService.deleteOrganization(id));
    }
}
