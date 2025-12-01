package com.inspur.seed.service.ose.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.oauth.PubOrgan;
import com.inspur.seed.domain.oauth.PubOrganTree;
import com.inspur.seed.domain.ose.OseInfo;
import com.inspur.seed.dto.ose.OseInfoDTO;
import com.inspur.seed.dto.ose.OseInfoQueryDTO;
import com.inspur.seed.mapper.oauth.PubOrganMapper;
import com.inspur.seed.mapper.oauth.PubOrganTreeMapper;
import com.inspur.seed.mapper.ose.OseInfoMapper;
import com.inspur.seed.service.ose.IOseInfoService;
import com.inspur.seed.vo.ose.OseInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * OSE基础信息Service实现
 *
 * @author igdp
 */
@Service
public class OseInfoServiceImpl implements IOseInfoService {

    @Autowired
    private OseInfoMapper oseInfoMapper;

    @Autowired
    private PubOrganMapper pubOrganMapper;

    @Autowired
    private PubOrganTreeMapper pubOrganTreeMapper;

    @Override
    public List<OseInfoVO> getOseList(OseInfoQueryDTO queryDTO) {
        return oseInfoMapper.selectOseList(queryDTO);
    }

    @Override
    public OseInfoVO getOseById(String oseId) {
        return oseInfoMapper.selectOseById(oseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OseInfoVO addOse(OseInfoDTO dto) {
        // 验证OSE编码唯一性
        QueryWrapper<OseInfo> codeWrapper = new QueryWrapper<>();
        codeWrapper.eq("ose_code", dto.getOseCode());
        if (oseInfoMapper.selectCount(codeWrapper) > 0) {
            throw new ServiceException("OSE行政编码已存在");
        }

        // 验证OSE名称唯一性
        QueryWrapper<OseInfo> nameWrapper = new QueryWrapper<>();
        nameWrapper.eq("ose_name", dto.getOseName());
        if (oseInfoMapper.selectCount(nameWrapper) > 0) {
            throw new ServiceException("OSE名称已存在");
        }

        // 验证联系电话唯一性
        QueryWrapper<OseInfo> phoneWrapper = new QueryWrapper<>();
        phoneWrapper.eq("contact_number", dto.getContactNumber());
        if (oseInfoMapper.selectCount(phoneWrapper) > 0) {
            throw new ServiceException("联系电话已存在");
        }

        OseInfo oseInfo = new OseInfo();
        BeanUtils.copyProperties(dto, oseInfo);

        // 生成UUID作为主键
        oseInfo.setOseId(IdUtils.fastSimpleUUID());

        // 设置默认状态为启用
        oseInfo.setOseStatus("ENABLED");

        // 设置创建时间
        Date now = new Date();
        oseInfo.setCreateTime(now);
        oseInfo.setUpdateTime(now);

        // TODO: 从region_info表自动带出行政区划名称
        // oseInfo.setRegionName(...);

        oseInfoMapper.insert(oseInfo);

        PubOrgan organ = new PubOrgan();
        organ.setId(oseInfo.getOseId());
        organ.setName(oseInfo.getOseName());
        organ.setCode(oseInfo.getOseCode());
        //TODO: 暂定数据,后续需优化读取正确的区划
        organ.setRegionCode("001000");
        organ.setRegionName("Addis Ababa");
        organ.setType("0");
        organ.setOrganLevel("3");
        organ.setAppCode("inputSupply");
        organ.setOrganType("1");
        organ.setShortName(oseInfo.getOseName());
        organ.setSortOrder(1);
        organ.setCreator("kether");
        organ.setCreateTime(LocalDateTime.now());
        organ.setUpdateTime(LocalDateTime.now());
        pubOrganMapper.insert(organ);

        PubOrganTree organTree = new PubOrganTree();
        organTree.setOrgCode(oseInfo.getOseCode());
        organTree.setParentCode("251201110301");
        organTree.setViewCode("ORG_VERTICAL_VIEW");
        organTree.setIsLeaf("0");
        organTree.setSortOrder(1);
        organTree.setCreator("kether");
        organTree.setCreateTime(LocalDate.from(LocalDateTime.now()));
        organTree.setStatus("1");
        pubOrganTreeMapper.insert(organTree);
        return getOseById(oseInfo.getOseId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OseInfoVO updateOse(String oseId, OseInfoDTO dto) {
        // 验证OSE是否存在
        OseInfo existOse = oseInfoMapper.selectById(oseId);
        if (existOse == null) {
            throw new ServiceException("OSE信息不存在");
        }

        // 验证OSE编码唯一性(排除自身)
        QueryWrapper<OseInfo> codeWrapper = new QueryWrapper<>();
        codeWrapper.eq("ose_code", dto.getOseCode());
        codeWrapper.ne("ose_id", oseId);
        if (oseInfoMapper.selectCount(codeWrapper) > 0) {
            throw new ServiceException("OSE行政编码已存在");
        }

        // 验证OSE名称唯一性(排除自身)
        QueryWrapper<OseInfo> nameWrapper = new QueryWrapper<>();
        nameWrapper.eq("ose_name", dto.getOseName());
        nameWrapper.ne("ose_id", oseId);
        if (oseInfoMapper.selectCount(nameWrapper) > 0) {
            throw new ServiceException("OSE名称已存在");
        }

        // 验证联系电话唯一性(排除自身)
        QueryWrapper<OseInfo> phoneWrapper = new QueryWrapper<>();
        phoneWrapper.eq("contact_number", dto.getContactNumber());
        phoneWrapper.ne("ose_id", oseId);
        if (oseInfoMapper.selectCount(phoneWrapper) > 0) {
            throw new ServiceException("联系电话已存在");
        }

        // 更新OSE信息
        OseInfo oseInfo = new OseInfo();
        BeanUtils.copyProperties(dto, oseInfo);
        oseInfo.setOseId(oseId);
        oseInfo.setUpdateTime(new Date());

        // TODO: 从region_info表自动带出行政区划名称
        // oseInfo.setRegionName(...);

        oseInfoMapper.updateById(oseInfo);

        return getOseById(oseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteOse(String oseId) {
        // 验证OSE是否存在
        OseInfo existOse = oseInfoMapper.selectById(oseId);
        if (existOse == null) {
            throw new ServiceException("OSE信息不存在");
        }

        // TODO: 可以添加级联删除验证，如果有分发记录关联则不允许删除
        // 或者实现逻辑删除而非物理删除

        return oseInfoMapper.deleteById(oseId);
    }
}
