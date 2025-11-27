package com.inspur.agriculture.input.controller.feedback;

import com.inspur.agriculture.input.dto.feedback.*;
import com.inspur.agriculture.input.service.feedback.IFeedbackService;
import com.inspur.agriculture.input.vo.feedback.FeedbackStatsVO;
import com.inspur.agriculture.input.vo.feedback.FeedbackVO;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 反馈Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController extends BaseController {

    @Autowired
    private IFeedbackService feedbackService;

    /**
     * 查询反馈列表
     */
    @GetMapping("/list")
    public TableDataInfo<FeedbackVO> list(FeedbackQueryDTO queryDTO) {
        startPage();
        List<FeedbackVO> list = feedbackService.getFeedbackList(queryDTO);
        return getDataTable(list);
    }

    /**
     * 获取反馈详情
     */
    @GetMapping("/{feedbackId}")
    public AjaxResult getInfo(@PathVariable("feedbackId") Long feedbackId) {
        return success(feedbackService.getFeedbackById(feedbackId));
    }

    /**
     * 提交反馈
     */
    @PostMapping
    public AjaxResult add(@Validated @RequestBody FeedbackDTO dto) {
        return toAjax(feedbackService.submitFeedback(dto));
    }

    /**
     * 修改反馈
     */
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody FeedbackDTO dto) {
        return toAjax(feedbackService.updateFeedback(dto));
    }

    /**
     * 删除反馈
     */
    @DeleteMapping("/{feedbackId}")
    public AjaxResult remove(@PathVariable Long feedbackId) {
        return toAjax(feedbackService.deleteFeedback(feedbackId));
    }

    /**
     * 批量删除反馈
     */
    @DeleteMapping("/batch/{feedbackIds}")
    public AjaxResult batchRemove(@PathVariable Long[] feedbackIds) {
        return toAjax(feedbackService.batchDeleteFeedback(feedbackIds));
    }

    /**
     * 分配处理人
     */
    @PostMapping("/{feedbackId}/assign")
    public AjaxResult assign(
            @PathVariable Long feedbackId,
            @RequestParam String handlerId) {
        return toAjax(feedbackService.assignHandler(feedbackId, handlerId));
    }

    /**
     * 处理反馈
     */
    @PostMapping("/handle")
    public AjaxResult handle(@Validated @RequestBody FeedbackHandleDTO dto) {
        return toAjax(feedbackService.handleFeedback(dto));
    }

    /**
     * 关闭反馈
     */
    @PostMapping("/{feedbackId}/close")
    public AjaxResult close(@PathVariable Long feedbackId) {
        return toAjax(feedbackService.closeFeedback(feedbackId));
    }

    /**
     * 评价反馈
     */
    @PostMapping("/evaluate")
    public AjaxResult evaluate(@Validated @RequestBody FeedbackEvaluationDTO dto) {
        return toAjax(feedbackService.evaluateFeedback(dto));
    }

    /**
     * 回复反馈
     */
    @PostMapping("/reply")
    public AjaxResult reply(@Validated @RequestBody FeedbackReplyDTO dto) {
        return toAjax(feedbackService.replyFeedback(dto));
    }

    /**
     * 获取反馈统计数据
     */
    @GetMapping("/stats")
    public AjaxResult getStats() {
        FeedbackStatsVO stats = feedbackService.getFeedbackStats();
        return success(stats);
    }
}
