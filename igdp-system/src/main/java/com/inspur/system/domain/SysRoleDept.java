package com.inspur.system.domain;




import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和部门关联 sys_role_dept
 * 
 * @author liyunlong
 */
@TableName("sys_role_dept")
@Setter
@Getter
@ToString
public class SysRoleDept
{
    /** 角色ID */
    
    private String roleId;
    
    /** 部门ID */
    
    private String deptId;

}
