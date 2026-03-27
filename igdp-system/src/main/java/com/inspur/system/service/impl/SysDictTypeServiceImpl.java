package com.inspur.system.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.inspur.common.constant.UserConstants;
import com.inspur.common.core.domain.entity.SysDictData;
import com.inspur.common.core.domain.entity.SysDictType;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DictUtils;
import com.inspur.common.utils.StringUtils;
import com.inspur.system.mapper.SysDictDataMapper;
import com.inspur.system.mapper.SysDictTypeMapper;
import com.inspur.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典 业务层处理
 *
 * @author liyunlong
 */
@Service
public class SysDictTypeServiceImpl extends MPJBaseServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {
    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        loadingDictCache();
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        return list(new LambdaQueryWrapper<SysDictType>()
                .like(StrUtil.isNotEmpty(dictType.getDictName()), SysDictType::getDictName, dictType.getDictName())
                .eq(StrUtil.isNotEmpty(dictType.getStatus()), SysDictType::getStatus, dictType.getStatus())
                .like(StrUtil.isNotEmpty(dictType.getDictType()), SysDictType::getDictType, dictType.getDictType())
                .ge(ObjectUtil.isNotEmpty(dictType.getParams().get("beginTime")), SysDictType::getCreateTime, dictType.getParams().get("beginTime"))
                .le(ObjectUtil.isNotEmpty(dictType.getParams().get("endTime")), SysDictType::getCreateTime, dictType.getParams().get("endTime")));
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeAll() {
        return list();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
//        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
//        if (StringUtils.isNotEmpty(dictDatas)) {
//            return dictDatas;
//        }
        List<SysDictData> dictDatas  = dictDataMapper.selectDictDataByType(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeById(String dictId) {
        return getById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        return getOne(new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getDictType, dictType));
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(String[] dictIds) {
        for (String dictId : dictIds) {
            SysDictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.countDictDataByType(dictType.getDictType()) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            removeById(dictId);
            DictUtils.removeDictCache(dictType.getDictType());
        }
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        SysDictData dictData = new SysDictData();
        dictData.setStatus("0");
        Map<String, List<SysDictData>> dictDataMap = dictDataMapper.selectDictDataList(dictData).stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
        }
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    public boolean insertDictType(SysDictType dict) {
        dict.setCreateTime(LocalDateTime.now());
        boolean result = save(dict);
        if (result) {
            DictUtils.setDictCache(dict.getDictType(), null);
        }
        return result;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDictType(SysDictType dict) {
        SysDictType oldDict = getById(dict.getDictId());
        if (null != oldDict) {
            dictDataMapper.updateDictDataType(oldDict.getDictType(), dict.getDictType());
            dict.setUpdateTime(LocalDateTime.now());
            boolean result = updateById(dict);
            if (result) {
                List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dict.getDictType());
                DictUtils.setDictCache(dict.getDictType(), dictDatas);
            }
            return result;
        }
        return false;

    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public boolean checkDictTypeUnique(SysDictType dict) {
        String dictId = StrUtil.isNotEmpty(dict.getDictId()) ? dict.getDictId() : "1";
        SysDictType dictType = selectDictTypeByType(dict.getDictType());
        if (StringUtils.isNotNull(dictType) && !dictType.getDictId().equals(dictId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
