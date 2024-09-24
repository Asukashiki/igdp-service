package com.inspur.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.inspur.common.constant.UserConstants;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import com.inspur.system.domain.SysPost;
import com.inspur.system.mapper.SysPostMapper;
import com.inspur.system.mapper.SysUserPostMapper;
import com.inspur.system.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 岗位信息 服务层处理
 *
 * @author liyunlong
 */
@Service
public class SysPostServiceImpl extends MPJBaseServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    @Autowired
    private SysUserPostMapper userPostMapper;

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return this.baseMapper.selectPostList(post);
    }

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    @Override
    public List<SysPost> selectPostAll() {
        return list();
    }

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    @Override
    public SysPost selectPostById(String postId) {
        return getById(postId);
    }

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    @Override
    public List<String> selectPostListByUserId(String userId) {
        return this.baseMapper.selectPostListByUserId(userId);
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostNameUnique(SysPost post) {
        String postId = StringUtils.isNull(post.getPostId()) ? "-1" : post.getPostId();
        SysPost info = getOne(new LambdaQueryWrapper<SysPost>().eq(SysPost::getPostName, post.getPostName()));
        if (StringUtils.isNotNull(info) && !info.getPostId().equals(postId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostCodeUnique(SysPost post) {
        String postId = StringUtils.isNull(post.getPostId()) ? "-1" : post.getPostId();
        SysPost info = getOne(new LambdaQueryWrapper<SysPost>().eq(SysPost::getPostCode, post.getPostCode()));
        if (StringUtils.isNotNull(info) && !info.getPostId().equals(postId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public long countUserPostById(String postId) {
        return userPostMapper.countUserPostById(postId);
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public boolean deletePostById(String postId) {
        return removeById(postId);
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePostByIds(String[] postIds) {
        if (null != postIds && postIds.length > 0) {
            for (String postId : postIds) {
                SysPost post = selectPostById(postId);
                if (countUserPostById(postId) > 0) {
                    throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
                }
            }
            return removeByIds(Arrays.asList(postIds));
        }
        return false;
    }

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean insertPost(SysPost post) {
        post.setCreateTime(LocalDateTime.now());
        return save(post);
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean updatePost(SysPost post) {
        post.setUpdateTime(LocalDateTime.now());
        return updateById(post);
    }
}
