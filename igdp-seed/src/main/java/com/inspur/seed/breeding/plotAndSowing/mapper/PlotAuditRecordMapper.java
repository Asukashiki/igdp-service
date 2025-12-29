package com.inspur.seed.breeding.plotAndSowing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.breeding.plotAndSowing.domain.entity.PlotAuditRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 地块审核记录Mapper接口
 *
 * @author inspur
 */
@Mapper
public interface PlotAuditRecordMapper extends BaseMapper<PlotAuditRecord> {

    /**
     * 查询地块审核历史
     *
     * @param plotId 地块ID
     * @return 审核记录列表
     */
    List<PlotAuditRecord> selectAuditHistory(String plotId);
}
