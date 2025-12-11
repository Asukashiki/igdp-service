package com.inspur.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.system.domain.SysNotice;
import com.inspur.system.service.ISysNoticeService;

/**
 * 公告公开接口 - 不需要权限控制
 * 用于首页展示公告列表和详情
 *
 * @author system
 */
@RestController
@RequestMapping("/public/notice")
public class PublicNoticeController extends BaseController {
    
    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 获取公开的通知公告列表（仅状态正常的）
     */
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice) {
        startPage();
        // 只查询状态正常(0)的公告
        notice.setStatus("0");
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息（公开接口）
     */
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId) {
        SysNotice notice = noticeService.selectNoticeById(noticeId);
        // 只返回状态正常的公告
        if (notice != null && "0".equals(notice.getStatus())) {
            return success(notice);
        }
        return error("Notice does not exist or has been closed");
    }
}
