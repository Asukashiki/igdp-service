package com.inspur.seed.mapper.breed;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.breed.BreedSeedDistributeDetail;
import com.inspur.seed.vo.breed.BreedSeedDistributeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Breeder Seed 分发明细表Mapper接口
 *
 * @author igdp
 */
public interface BreedSeedDistributeDetailMapper extends BaseMapper<BreedSeedDistributeDetail> {

    /**
     * 根据分发ID查询明细列表
     *
     * @param distributeId 分发ID
     * @return 明细列表
     */
    List<BreedSeedDistributeVO.DistributeDetailVO> selectDetailsByDistributeId(@Param("distributeId") String distributeId);

    /**
     * 批量插入分发明细
     *
     * @param detailList 明细列表
     * @return 插入行数
     */
    int batchInsert(@Param("list") List<BreedSeedDistributeDetail> detailList);
}
