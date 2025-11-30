package com.inspur.seed.controller.ose;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.dto.ose.OseReceiveConfirmDTO;
import com.inspur.seed.dto.ose.OseReceiveConfirmQueryDTO;
import com.inspur.seed.service.ose.IOseReceiveConfirmService;
import com.inspur.seed.vo.ose.OseReceiveConfirmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OSE接收确认Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/seed/ose/receive")
public class OseReceiveConfirmController extends BaseController {

    @Autowired
    private IOseReceiveConfirmService oseReceiveConfirmService;

    /**
     * 查询接收确认列表
     */
    @GetMapping("/confirm/list")
    public TableDataInfo list(OseReceiveConfirmQueryDTO queryDTO) {
        startPage();
        List<OseReceiveConfirmVO> list = oseReceiveConfirmService.getReceiveConfirmList(queryDTO);
        return getDataTable(list);
    }

    /**
     * 确认接收
     */
    @PutMapping("/confirm/{receiveConfirmId}")
    public AjaxResult confirm(
            @PathVariable("receiveConfirmId") String receiveConfirmId,
            @Validated @RequestBody OseReceiveConfirmDTO dto) {
        OseReceiveConfirmVO result = oseReceiveConfirmService.confirmReceive(receiveConfirmId, dto);
        return success(result);
    }
}
