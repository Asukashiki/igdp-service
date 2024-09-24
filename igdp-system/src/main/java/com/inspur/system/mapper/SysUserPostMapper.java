package com.inspur.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.inspur.system.domain.SysUserPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.List;

/**
 * 用户与岗位关联表 数据层
 *
 * @author liyunlong
 */
@Mapper
public interface SysUserPostMapper extends MPJBaseMapper<SysUserPost> {
    /**
     * 通过用户ID删除用户和岗位关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    default int deleteUserPostByUserId(String userId) {
        return delete(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getUserId, userId));
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    default long countUserPostById(String postId){
        return selectCount(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getPostId,postId));
    }

    /**
     * 批量删除用户和岗位关联
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    default int deleteUserPost(String[] userIds){
        return delete(new LambdaQueryWrapper<SysUserPost>().in(SysUserPost::getUserId, Arrays.asList(userIds)));
    }

    /**
     * 批量新增用户岗位信息
     *
     * @param userPostList 用户岗位列表
     * @return 结果
     */
    int batchUserPost(List<SysUserPost> userPostList);
}
