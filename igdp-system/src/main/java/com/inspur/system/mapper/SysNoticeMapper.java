package com.inspur.system.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.inspur.system.domain.SysNotice;

import java.util.List;

/**
 * 通知公告表 数据层
 *
 * @author liyunlong
 */
public interface SysNoticeMapper extends BaseMapper<SysNotice>
{

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
     default List<SysNotice> selectNoticeList(SysNotice notice){
         LambdaQueryWrapper<SysNotice> queryWrapper = new LambdaQueryWrapper<>();
         queryWrapper.like(StrUtil.isNotEmpty(notice.getNoticeTitle()),SysNotice::getNoticeTitle,notice.getNoticeTitle());
         queryWrapper.eq(StrUtil.isNotEmpty(notice.getNoticeType()),SysNotice::getNoticeType,notice.getNoticeType());
         queryWrapper.eq(StrUtil.isNotEmpty(notice.getCreateBy()),SysNotice::getCreateBy,notice.getCreateBy());
         return selectList(queryWrapper);
     }

}
