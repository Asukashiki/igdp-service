package com.inspur.workorder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderSyncMapper
 * @date 2024/6/22 20:04
 */
@Mapper
public interface WorkOrderSyncMapper {

    /**
     * 根据sql更新同步数据
     *
     * @param sqlStr sql语句
     */
    @Select("${sqlStr}")
    void syncBySql(@Param("sqlStr") String sqlStr);

}
