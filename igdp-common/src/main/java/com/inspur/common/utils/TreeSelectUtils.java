package com.inspur.common.utils;

import com.inspur.common.core.domain.SelectEntity;
import com.inspur.common.core.domain.TreeSelect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName TreeSelectUtils
 * @date 2024/7/4 17:15
 */
public class TreeSelectUtils {
    public static List<SelectEntity> buildTreeBySelectEntity(List<SelectEntity> selectEntityList) {
        if (null != selectEntityList && !selectEntityList.isEmpty()) {
            List<SelectEntity> handleList = new ArrayList<>();
            List<String> tempList = selectEntityList.stream().map(SelectEntity::getValue).collect(Collectors.toList());
            for (SelectEntity entity : selectEntityList) {
                // 如果是顶级节点, 遍历该父节点的所有子节点
                if (!tempList.contains(entity.getParentValue())) {
                    recursionFn(selectEntityList, entity);
                    handleList.add(entity);
                }
            }
            if (handleList.isEmpty()) {
                handleList = selectEntityList;
            }
            return handleList;
        }
        return null;
    }


    /**
     * 递归列表
     */
    public static void recursionFn(List<SelectEntity> list, SelectEntity t) {
        // 得到子节点列表
        List<SelectEntity> childList = getChildListForSelectEntity(list, t);
        t.setChildren(childList);
        for (SelectEntity tChild : childList) {
            if (hasChildForSelectEntity(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    public static List<SelectEntity> getChildListForSelectEntity(List<SelectEntity> list, SelectEntity t) {
        List<SelectEntity> tlist = new ArrayList<>();
        for (SelectEntity n : list) {
            if (StringUtils.isNotNull(n.getParentValue()) && n.getParentValue().equals(t.getValue())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    public static boolean hasChildForSelectEntity(List<SelectEntity> list, SelectEntity t) {
        return !getChildListForSelectEntity(list, t).isEmpty();
    }
}
