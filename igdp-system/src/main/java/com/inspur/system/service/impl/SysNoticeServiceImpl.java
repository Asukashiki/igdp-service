package com.inspur.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.system.domain.SysNotice;
import com.inspur.system.mapper.SysNoticeMapper;
import com.inspur.system.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 公告 服务层实现
 *
 * @author liyunlong
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId) {
        return getById(noticeId.toString());
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice) {
        return this.baseMapper.selectNoticeList(notice);

    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public boolean  insertNotice(SysNotice notice) {
        notice.setCreateTime(LocalDateTime.now());
        return save(notice);
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public boolean updateNotice(SysNotice notice) {
        notice.setUpdateTime(LocalDateTime.now());
        return updateById(notice);
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public boolean deleteNoticeById(String noticeId) {
        return removeById(noticeId);
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNoticeByIds(Long[] noticeIds) {
        if (null != noticeIds && noticeIds.length > 0) {
            for (Long noticeId : noticeIds){
                String id = String.valueOf(noticeId);
                removeById(id);
            }
            return true;
        }
        return false;
    }
}
