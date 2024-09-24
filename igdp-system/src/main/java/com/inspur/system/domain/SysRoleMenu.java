package com.inspur.system.domain;




import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和菜单关联 sys_role_menu
 * 
 * @author liyunlong
 */
@TableName("sys_role_menu")
@Setter
@Getter
public class SysRoleMenu
{
    /** 角色ID */
    
    private String roleId;
    
    /** 菜单ID */
    
    private String menuId;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("menuId", getMenuId())
            .toString();
    }
}
