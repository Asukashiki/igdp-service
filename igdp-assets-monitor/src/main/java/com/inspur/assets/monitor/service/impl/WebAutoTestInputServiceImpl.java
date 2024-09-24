package com.inspur.assets.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.monitor.domain.AssetsAlertCur;
import com.inspur.assets.monitor.domain.WebAutoTestInput;
import com.inspur.assets.monitor.domain.WebInspectionInfo;
import com.inspur.assets.monitor.mapper.WebAutoTestInputMapper;
import com.inspur.assets.monitor.mapper.WebInspectionInfoMapper;
import com.inspur.assets.monitor.service.IWebAutoTestInputService;
import com.inspur.assets.monitor.service.IWebInspectionSubmenuService;
import com.inspur.common.core.domain.AjaxResult;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import java.util.List;
@Service
public class WebAutoTestInputServiceImpl extends ServiceImpl<WebAutoTestInputMapper, WebAutoTestInput> implements IWebAutoTestInputService {
    @Resource
    WebAutoTestInputMapper webAutoTestInputMapper;

    @Resource
    IWebInspectionSubmenuService webInspectionSubmenuService;

    @Override
    public List<WebAutoTestInput> selectWebAutoTestInput(){
        QueryWrapper<WebAutoTestInput> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("task_number","inspection_element_name","id","element_location","inspection_success_element","exist_parent_menu_number","shot_screen","parent_button_id");
        return webAutoTestInputMapper.selectList(queryWrapper);
    }

    @Override
    public AjaxResult insertWebAutoTestInput(WebAutoTestInput[] params){
        Map<String, Integer> nameToIdMap = new HashMap<>();
        for (WebAutoTestInput param : params) {
            try{
                System.out.println("new insert is "+param.getId());
                webAutoTestInputMapper.insert(param);
                nameToIdMap.put(param.getInspectionElementName(),param.getId());
            }catch (Exception e){
                System.out.println("插入失败原因 "+e.getMessage());
                return AjaxResult.error("插入数据失败");
            }
        };
        for(WebAutoTestInput param:params){
            Integer map = nameToIdMap.get(param.getParentButtonName());
            param.setParentButtonId(map);
        }

       return AjaxResult.success();
    }

    @Override
    public AjaxResult updateWebAutoTestInput(WebAutoTestInput[] params){
        for(WebAutoTestInput param:params){
            try{
                webAutoTestInputMapper.updateById(param);
            }catch (Exception e){
                return AjaxResult.error("update failed");
            }
        }

        return AjaxResult.success();
    }

    @Override
    public AjaxResult deleteWebAutoTestInput(int[] param){
        for(int id :param){
            try{
                LambdaQueryWrapper<WebAutoTestInput>queryWrapper=new LambdaQueryWrapper<>();
                queryWrapper.eq(WebAutoTestInput::getId,id);
                webAutoTestInputMapper.delete(queryWrapper);
            }catch(Exception e){
                return AjaxResult.error("delete failed");
            }
        }
//        webInspectionSubmenuService.deleteOneElementAllOrderWebInspectionSubmenu(param);
        return AjaxResult.success();
    }

    @Override
    public Boolean deleteOneTaskAllWebAutoTestInput(int param){
        LambdaQueryWrapper<WebAutoTestInput> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WebAutoTestInput::getTaskNumber,param);
            webAutoTestInputMapper.delete(queryWrapper);
            return true;

    }


    @Override
    public List<WebAutoTestInput> queryTargetTestInput(int param){
        LambdaQueryWrapper<WebAutoTestInput> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(WebAutoTestInput::getTaskNumber,param);
        return webAutoTestInputMapper.selectList(queryWrapper);
    }
}
