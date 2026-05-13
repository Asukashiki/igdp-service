package com.inspur.seed.service.invested;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.invested.BoaZoneReleaseMain;
import com.inspur.seed.dto.invested.BoaZoneReleaseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 鎶曞叆鍝佸垎鍙慡ervice鎺ュ彛
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
public interface IBoaZoneReleaseService extends IService<BoaZoneReleaseMain> {

    /**
     * 鏌ヨOSE鍒嗗彂鍗曞垪琛?
     *
     * @param unionName 鍒嗗彂瀵硅薄鍚嶇О
     * @param inputType 鎶曞叆鍝佺被鍨?
     * @param startTime 寮€濮嬫椂闂?
     * @param endTime 缁撴潫鏃堕棿
     * @return 鍒嗗彂鍗曞垪琛?
     */
    List<BoaZoneReleaseMain> queryReleaseList(String releaseType, String releaseName, String inputType,
                                             LocalDate startTime, LocalDate endTime);

    /**
     * 鏂板鍒嗗彂鍗曪紙涓昏〃+鏄庣粏锛?
     *
     * @param dto 鍒嗗彂鍗旸TO
     * @return 鍒嗗彂鍗旾D鍜岀紪鍙?
     */
    Map<String, String> addRelease(BoaZoneReleaseDTO dto);

    /**
     * 缂栬緫鍒嗗彂鍗曪紙涓昏〃+鏄庣粏锛?
     *
     * @param dto 鍒嗗彂鍗旸TO
     * @return 鏄惁鎴愬姛
     */
    boolean editRelease(BoaZoneReleaseDTO dto);

    /**
     * 鏌ヨ鍒嗗彂鍗曡鎯咃紙鍖呭惈鏄庣粏锛?
     *
     * @param id 鍒嗗彂涓昏〃ID
     * @return 鍒嗗彂鍗曡鎯咃紙鍖呭惈main鍜宒etails锛?
     */
    Map<String, Object> queryReleaseDetail(String id);

    /**
     * 鏍规嵁releaseId鏌ヨ鍒嗗彂鍗曡鎯咃紙鍖呭惈鏄庣粏锛?
     *
     * @param releaseId 鍒嗗彂鍗曠紪鍙?
     * @return 鍒嗗彂鍗曡鎯咃紙鍖呭惈main鍜宒etails锛?
     */
    Map<String, Object> queryReleaseDetailByReleaseId(String releaseId);

    /**
     * 鍒犻櫎鍒嗗彂鍗曪紙涓昏〃+鏄庣粏锛?
     *
     * @param ids 鍒嗗彂涓昏〃ID闆嗗悎
     * @return 鏄惁鎴愬姛
     */
    boolean removeRelease(List<String> ids);

    /**
     * 鏌ヨ鍒嗗彂鍗曞嚭鍏ュ簱鐘舵€?
     *
     * @param releaseIds 鍒嗗彂鍗曠紪鍙烽泦鍚?
     * @return Map<releaseId, stockStatus> 鍑哄叆搴撶姸鎬佹槧灏?
     */
    Map<String, String> queryStockStatus(List<String> releaseIds);

    /**
     * 鏌ヨ鍙敤搴撳瓨
     *
     * @param inputCategory 鎶曞叆鍝佺被鍒?
     * @param organCode 缁勭粐缂栫爜
     * @return 鍙敤搴撳瓨淇℃伅
     */
    Map<String, Object> queryAvailableStock(String inputCategory, String organCode);

    /**
     * 按存储类型（0/1）将本次接口请求参数写入缓存。
     *
     * @param storeType    0 或 1
     * @param bizScene     业务场景标识，如 list、delete、stockStatus
     * @param requestBody  请求参数快照
     */
    void saveRequestSnapshot(int storeType, String bizScene, Map<String, Object> requestBody);
}


