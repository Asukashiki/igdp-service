package com.inspur.farmland.service;

import com.inspur.farmland.domain.DaInfo;

import java.util.List;
import java.util.Map;

/**
 * DA信息Service接口
 *
 * @author inspur
 */
public interface IDaInfoService {

    /**
     * 分页查询DA列表
     *
     * @param daInfo 查询条件
     * @return DA列表
     */
    List<DaInfo> selectDaInfoList(DaInfo daInfo);

    /**
     * 根据DA编码查询DA详情
     *
     * @param daId DA编码
     * @return DA信息
     */
    DaInfo selectDaInfoByDaId(String daId);

    /**
     * 新增DA
     *
     * @param daInfo DA信息
     * @return DA编码
     */
    String insertDaInfo(DaInfo daInfo);

    /**
     * 修改DA
     *
     * @param daId DA编码
     * @param daInfo DA信息
     * @return 影响行数
     */
    int updateDaInfo(String daId, DaInfo daInfo);

    /**
     * 删除DA
     *
     * @param daId DA编码
     * @return 影响行数
     */
    int deleteDaInfoByDaId(String daId);

    /**
     * 启用/禁用DA账号
     *
     * @param daId DA编码
     * @param accountStatus 账号状态：1-启用 0-禁用
     * @return 影响行数
     */
    int updateAccountStatus(String daId, String accountStatus);

    /**
     * 重置DA密码
     *
     * @param daId DA编码
     * @param newPassword 新密码
     * @return 影响行数
     */
    int resetPassword(String daId, String newPassword);

    /**
     * 获取DA下拉选项
     *
     * @param kebeleCode 村代码
     * @return DA选项列表
     */
    List<Map<String, Object>> selectDaOptions(String kebeleCode);

    /**
     * 检查DA编码是否唯一
     *
     * @param daId DA编码
     * @return true-唯一 false-重复
     */
    boolean checkDaIdUnique(String daId);

    /**
     * 检查身份证号是否唯一
     *
     * @param idCard 身份证号
     * @param daId DA编码（修改时排除自己）
     * @return true-唯一 false-重复
     */
    boolean checkIdCardUnique(String idCard, String daId);

    /**
     * 检查账号是否唯一
     *
     * @param account 账号
     * @param daId DA编码（修改时排除自己）
     * @return true-唯一 false-重复
     */
    boolean checkAccountUnique(String account, String daId);
}
