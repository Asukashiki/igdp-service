package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.BreedingMaterial;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 育种材料登记服务接口
 *
 * @author system
 */
public interface IBreedingMaterialService extends IService<BreedingMaterial> {

    /**
     * 新增育种材料登记
     *
     * @param breedingMaterial 育种材料信息
     * @return 材料ID和登记编码
     */
    Map<String, String> addBreedingMaterial(BreedingMaterial breedingMaterial);

    /**
     * 查询育种材料列表
     *
     * @param batchId 育种批次ID
     * @param seedType 种子类别
     * @param receiveDateStart 接收日期起始
     * @param receiveDateEnd 接收日期结束
     * @return 育种材料列表
     */
    List<BreedingMaterial> queryBreedingMaterialList(String batchId, String seedType, LocalDate receiveDateStart, LocalDate receiveDateEnd);

    /**
     * 根据材料ID查询育种材料详情
     *
     * @param materialId 材料ID
     * @return 育种材料详情
     */
    BreedingMaterial queryByMaterialId(String materialId);

    /**
     * 编辑育种材料
     *
     * @param breedingMaterial 育种材料信息
     * @return 更新结果
     */
    boolean editBreedingMaterial(BreedingMaterial breedingMaterial);

    /**
     * 删除育种材料
     *
     * @param materialId 材料ID
     * @return 删除结果
     */
    boolean removeBreedingMaterial(String materialId);
}
