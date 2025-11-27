package com.inspur.agriculture.input.service.dashboard.impl;

import com.inspur.agriculture.input.domain.inventory.enums.StockStatusEnum;
import com.inspur.agriculture.input.domain.supplier.enums.CertStatusEnum;

import com.inspur.agriculture.input.mapper.dashboard.DashboardMapper;
import com.inspur.agriculture.input.service.dashboard.IDashboardService;
import com.inspur.agriculture.input.vo.dashboard.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 大屏数据Service业务层处理
 *
 * @author igdp
 */
@Service
public class DashboardServiceImpl implements IDashboardService {

    @Autowired
    private DashboardMapper dashboardMapper;

    @Override
    public ComprehensiveDashboardVO getComprehensiveDashboard() {
        ComprehensiveDashboardVO result = new ComprehensiveDashboardVO();

        // 获取总览数据
        result.setOverview(getOverviewData());

        // 获取投入品类型分布
        result.setInputTypeDistribution(getInputTypeDistribution());

        // 获取TOP10供应商
        result.setTopSuppliers(getTopSuppliers(10));

        // 获取TOP10热门投入品
        result.setTopInputs(getTopInputs(10));

        // 获取仓库统计
        result.setWarehouseStats(getWarehouseStats());

        // 获取近7天趋势
        result.setStockTrend7Days(getStockTrend(7));

        // 获取近30天趋势
        result.setStockTrend30Days(getStockTrend(30));

        // 获取预警信息
        result.setWarnings(getWarningList(20));

        return result;
    }

    @Override
    public DashboardOverviewVO getOverviewData() {
        return dashboardMapper.getOverviewData();
    }

    @Override
    public List<SupplierStatsVO> getTopSuppliers(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        List<SupplierStatsVO> list = dashboardMapper.getTopSuppliers(limit);
        // 补充状态描述
        for (SupplierStatsVO vo : list) {
            if (vo.getStatus() != null) {
                vo.setStatusDesc(CertStatusEnum.getDescByCode(vo.getStatus()));
            }
            // 默认质量评级
            if (vo.getQualityRating() == null) {
                vo.setQualityRating("A");
            }
        }
        return list;
    }

    @Override
    public List<InputStatsVO> getTopInputs(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        List<InputStatsVO> list = dashboardMapper.getTopInputs(limit);
        // 补充类型描述和状态描述
        for (InputStatsVO vo : list) {
            if (vo.getType() != null) {
                switch (vo.getType()) {
                    case "0":
                        vo.setTypeDesc("种子");
                        break;
                    case "1":
                        vo.setTypeDesc("化肥");
                        break;
                    case "2":
                        vo.setTypeDesc("农药");
                        break;
                    default:
                        vo.setTypeDesc("其他");
                }
            }
            if (vo.getStockStatus() != null) {
                vo.setStockStatusDesc(StockStatusEnum.getDescByCode(vo.getStockStatus()));
            }
        }
        return list;
    }

    @Override
    public List<WarehouseStatsVO> getWarehouseStats() {
        return dashboardMapper.getWarehouseStats();
    }

    @Override
    public List<StockTrendVO> getStockTrend(Integer days) {
        if (days == null || days <= 0) {
            days = 7;
        }
        if (days > 30) {
            days = 30;
        }
        return dashboardMapper.getStockTrend(days);
    }

    @Override
    public List<InputTypeDistributionVO> getInputTypeDistribution() {
        return dashboardMapper.getInputTypeDistribution();
    }

    @Override
    public List<WarningInfoVO> getWarningList(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 20;
        }
        return dashboardMapper.getWarningList(limit);
    }
}
