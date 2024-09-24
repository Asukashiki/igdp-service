package com.inspur.doc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysFileInfo
 * @date 2024/5/5 9:55
 */
@TableName("sys_file_info")
@Setter
@Getter
public class SysFileInfo extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 文件服务返回的数据id
     */
    private String dataId;

    /**
     * 重命名后的文件名称
     * */
    private String fileName;

    private String originalFileName;

    private String filePath;

    private String fileType;

    /**
     * 文件服务类型
     * minio、fastDb
     */
    private String serverType;

    public SysFileInfo(String dataId, String fileName, String originalFileName, String filePath, String fileType, String serverType) {
        this.setDataId(dataId);
        this.setOriginalFileName(originalFileName);
        this.setFileName(fileName);
        this.setFilePath(filePath);
        this.setFileType(fileType);
        this.setServerType(serverType);
    }
}
