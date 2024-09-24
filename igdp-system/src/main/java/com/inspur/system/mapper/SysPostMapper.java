package com.inspur.system.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.system.domain.SysPost;
import com.inspur.system.domain.SysUserPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author liyunlong
 */
@Mapper
public interface SysPostMapper extends MPJBaseMapper<SysPost> {
    /**
     * 查询岗位数据集合
     *
     * @param post 岗位信息
     * @return 岗位数据集合
     */
    default List<SysPost> selectPostList(SysPost post) {
        LambdaQueryWrapper<SysPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(post.getStatus()), SysPost::getStatus, post.getStatus());
        queryWrapper.like(StrUtil.isNotEmpty(post.getPostCode()), SysPost::getPostCode, post.getPostCode());
        queryWrapper.like(StrUtil.isNotEmpty(post.getPostName()), SysPost::getPostName, post.getPostName());
        return selectList(queryWrapper);
    }


    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    default List<String> selectPostListByUserId(String userId) {
        MPJLambdaWrapper<SysPost> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.select(SysPost::getPostId);
        queryWrapper.leftJoin(SysUserPost.class, SysUserPost::getPostId, SysPost::getPostId);
        queryWrapper.eq(SysUserPost::getUserId, userId);
        return selectJoinList(String.class, queryWrapper);
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    default List<SysPost> selectPostsByUserName(String userName) {
        MPJLambdaWrapper<SysPost> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.select(SysPost::getPostId, SysPost::getPostName, SysPost::getPostCode);
        queryWrapper.leftJoin(SysUserPost.class, SysUserPost::getPostId, SysPost::getPostId);
        queryWrapper.leftJoin(SysUser.class, SysUser::getUserId, SysUserPost::getUserId);
        queryWrapper.eq(SysUser::getUserName, userName);
        return selectJoinList(SysPost.class, queryWrapper);
    }


}
