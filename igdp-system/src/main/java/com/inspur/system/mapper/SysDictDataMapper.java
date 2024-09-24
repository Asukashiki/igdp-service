package com.inspur.system.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.entity.SysDictData;
import com.inspur.common.utils.LoginHelper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典表 数据层
 *
 * @author liyunlong
 */
public interface SysDictDataMapper extends MPJBaseMapper<SysDictData> {
    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    default List<SysDictData> selectDictDataList(SysDictData dictData) {
        return selectList(new LambdaQueryWrapper<SysDictData>()
                .eq(StrUtil.isNotEmpty(dictData.getDictType()), SysDictData::getDictType, dictData.getDictType())
                .like(StrUtil.isNotEmpty(dictData.getDictLabel()), SysDictData::getDictLabel, dictData.getDictLabel())
                .eq(StrUtil.isNotEmpty(dictData.getStatus()), SysDictData::getStatus, dictData.getStatus()));
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    default List<SysDictData> selectDictDataByType(String dictType) {
        return selectList(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getStatus, Constants.STATUS_VALID)
                .eq(SysDictData::getDictType, dictType));
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    default String selectDictLabel(String dictType, String dictValue) {
        SysDictData sysDictData = selectOne(new LambdaQueryWrapper<SysDictData>()
                .select(SysDictData::getDictLabel)
                .eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getDictValue, dictValue));
        if (null != sysDictData) {
            return sysDictData.getDictLabel();
        }
        return null;
    }


    /**
     * 查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据
     */
    default long countDictDataByType(String dictType){
        return selectCount(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType,dictType));
    }


    /**
     * 同步修改字典类型
     *
     * @param oldDictType 旧字典类型
     * @param newDictType 新旧字典类型
     */
    default void updateDictDataType(String oldDictType, String newDictType){
        SysDictData updateEntity = new SysDictData();
        updateEntity.setDictType(newDictType);
        updateEntity.setUpdateTime(LocalDateTime.now());
        updateEntity.setUpdateBy(LoginHelper.getUserId());
        update(updateEntity, new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getDictType, oldDictType));
    }
}
