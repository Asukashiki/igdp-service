package com.inspur.seed.service.invested;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.invested.InputReceiveWoreda;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Woreda接收确认Service接口
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
public interface IInputReceiveWoredaService extends IService<InputReceiveWoreda> {

    /**
     * 查询Woreda接收确认列表
     *
     * @param woredaName   Woreda名称
     * @param receiveStatus 接收状态
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 接收确认列表
     */
    List<InputReceiveWoreda> queryReceiveList(String woredaName, String receiveStatus,
                                               LocalDate startTime, LocalDate endTime);

    /**
     * 查询接收确认详情
     *
     * @param id 主键ID
     * @return 接收确认详情（包含分发明细）
     */
    Map<String, Object> queryReceiveDetail(String id);

    /**
     * 确认接收
     *
     * @param id        主键ID
     * @param confirmBy 确认人
     * @param confirmOrg 确认机构
     * @return 是否成功
     */
    boolean confirmReceive(String id, String confirmBy, String confirmOrg);
}
