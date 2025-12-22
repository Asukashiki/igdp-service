package com.inspur.seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.RulesInfoDTO;
import com.inspur.seed.domain.entity.RulesInfo;
import com.inspur.seed.domain.vo.RulesInfoVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 规则信息服务接口
 *
 * @author igdp
 * @date 2025-12-18
 */
public interface IRulesInfoService extends IService<RulesInfo> {

    /**
     * 分页查询规则信息列表
     *
     * @param page 分页参数
     * @param dto 查询条件
     * @return 分页结果
     */
    IPage<RulesInfoVO> selectRulesInfoPage(IPage<RulesInfo> page, RulesInfoDTO dto);

    /**
     * 查询规则信息列表
     *
     * @param dto 查询条件
     * @return 列表
     */
    List<RulesInfoVO> selectRulesInfoList(RulesInfoDTO dto);

    /**
     * 查询规则信息详情
     *
     * @param id 数据ID
     * @return 详情
     */
    RulesInfoVO selectRulesInfoById(Integer id);

    /**
     * 新增规则信息
     *
     * @param dto 数据
     * @return 结果
     */
    int insertRulesInfo(RulesInfoDTO dto);

    /**
     * 修改规则信息
     *
     * @param dto 数据
     * @return 结果
     */
    int updateRulesInfo(RulesInfoDTO dto);

    /**
     * 删除规则信息
     *
     * @param ids 需要删除的数据ID数组
     * @return 结果
     */
    int deleteRulesInfoByIds(Integer[] ids);

    /**
     * 根据字典编码和值判断是否满足条件
     *
     * @param dictCode 字典编码
     * @param value 值
     * @return 是否满足条件
     */
    boolean checkRule(String dictCode, BigDecimal value);
}