package com.inspur.seed.service.impl;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inspur.seed.domain.AgronomicTrait;
import com.inspur.seed.domain.AgronomicTraitAudit;
import com.inspur.seed.mapper.AgronomicTraitAuditMapper;
import com.inspur.seed.domain.entity.AgronomicTraitRecord;
import com.inspur.seed.mapper.AgronomicTraitRecordMapper;
import cn.hutool.core.util.IdUtil;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.mapper.AgronomicTraitMapper;
import com.inspur.seed.service.IAgronomicTraitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static cn.dev33.satoken.SaManager.log;

/**
 * 农艺性状数据Service实现类
 *
 * 注意: photoUrl字段用于存储照片URL。照片上传功能应在前端实现，
 * 前端需要先上传照片到文件服务器，然后将返回的URL保存到photoUrl字段中。
 *
 * @author inspur
 */
@Service
public class AgronomicTraitServiceImpl implements IAgronomicTraitService {

    @Autowired
    private AgronomicTraitMapper agronomicTraitMapper;

    @Autowired
    private AgronomicTraitAuditMapper traitAuditMapper;
    @Autowired
    private AgronomicTraitRecordMapper recordMapper;
    @Override
    public List<AgronomicTrait> selectAgronomicTraitList(AgronomicTrait agronomicTrait) {
        return agronomicTraitMapper.selectAgronomicTraitList(agronomicTrait);
    }

    @Override
    public AgronomicTrait selectAgronomicTraitById(String traitId) {
        return agronomicTraitMapper.selectAgronomicTraitById(traitId);
    }

    @Override
    public String insertAgronomicTrait(AgronomicTrait agronomicTrait) {
        // 生成主键
        String traitId = IdUtil.simpleUUID();
        agronomicTrait.setTraitId(traitId);

        // 设置创建信息
        agronomicTrait.setCreateTime(LocalDateTime.now());
        agronomicTrait.setCreateBy(SecurityUtils.getUsername());

        agronomicTraitMapper.insert(agronomicTrait);
        return traitId;
    }

    @Override
    public int updateAgronomicTrait(AgronomicTrait agronomicTrait) {
        // 设置更新信息
        agronomicTrait.setUpdateTime(LocalDateTime.now());
        agronomicTrait.setUpdateBy(SecurityUtils.getUsername());

        return agronomicTraitMapper.updateById(agronomicTrait);
    }

    @Override
    public int deleteAgronomicTraitByIds(String[] traitIds) {
        int count = 0;
        for (String traitId : traitIds) {
            // 使用deleteById方法，让@TableLogic自动处理逻辑删除
            boolean success = agronomicTraitMapper.deleteById(traitId) > 0;
            if (success) {
                count++;
            }
        }
        return count;
    }


}
