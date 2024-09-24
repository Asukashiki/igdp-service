package com.inspur.system.domain;




import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户和岗位关联 sys_user_post
 * 
 * @author liyunlong
 */
@TableName("sys_user_post")
@Data
public class SysUserPost
{
    /** 用户ID */
    
    private String userId;
    
    /** 岗位ID */
    
    private String postId;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("postId", getPostId())
            .toString();
    }
}
