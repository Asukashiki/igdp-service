package com.inspur.workorder.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.common.annotation.Log;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.common.utils.poi.ExcelUtil;
import com.inspur.workorder.domain.WorkOrderKnowledgeBase;
import com.inspur.workorder.domain.WorkOrderKnowledgeFile;
import com.inspur.workorder.domain.WorkOrderKnowledgeType;
import com.inspur.workorder.domain.payload.CascadeSelectTool;
import com.inspur.workorder.service.IWorkOrderKnowledgeBaseService;
import com.inspur.workorder.service.IWorkOrderKnowledgeFileService;
import com.inspur.workorder.service.IWorkOrderKnowledgeTypeService;
import com.inspur.workorder.service.IWorkOrderSearchService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author liyunlong
 */
@RestController
@RequestMapping("/work-order/knowledge-base")
public class WorkOrderKnowledgeBaseController extends BaseController{
    @Resource
    private IWorkOrderKnowledgeBaseService workOrderKnowledgeBaseService;
    @Resource
    private IWorkOrderKnowledgeFileService workOrderKnowledgeFileService;
    @Resource
    private IWorkOrderSearchService workOrderSearchService;
    @Resource
    private IWorkOrderKnowledgeTypeService workOrderKnowledgeTypeService;


    /**
     * 知识创建
     *
     * @param workOrderKnowledgeBase 知识库内容
     */
    @PostMapping("/add")
    public AjaxResult creationKnowledge(@RequestBody WorkOrderKnowledgeBase workOrderKnowledgeBase) {
        return workOrderKnowledgeBaseService.addKnowledge(workOrderKnowledgeBase);
    }


    /**
     * 知识检索
     *
     * @param keyword 关键字
     */
    @GetMapping("/search")
    public TableDataInfo<?> queryKnowledge(String keyword) {
        startPage();
        List<WorkOrderKnowledgeBase> list = workOrderKnowledgeBaseService.getListByKeyword(keyword);

        ExecutorService service = Executors.newFixedThreadPool(1);
        service.execute(new Runnable() {
            @Override
            public void run() {
                workOrderSearchService.addSearch(keyword);
            }
        });
        return getDataTable(list);
    }

    /**
     * 查询热门搜索
     * @return
     */
    @GetMapping("/frequency")
    public AjaxResult queryFrequency() {

        return AjaxResult.success(workOrderSearchService.queryFrequency());
    }

    /**
     * 查询单条知识库内容
     */
    @GetMapping("/query/{id}")
    public AjaxResult query(@PathVariable("id") String id) {
        WorkOrderKnowledgeBase workOrderKnowledgeBase = workOrderKnowledgeBaseService.getById(id);
        //todo 获取附件内容并赋值
        List<WorkOrderKnowledgeFile> fileList = workOrderKnowledgeFileService.getFile(id);
        workOrderKnowledgeBase.setFileList(fileList);
        return AjaxResult.success(workOrderKnowledgeBase);
    }



    /**
     * 删除知识库数据
     */
    @PostMapping("/delete")
    public AjaxResult deleteKnowledge(String[] id) {
        return workOrderKnowledgeBaseService.deleteKnowledge(id);
    }

    /**
     * 修改知识库数据
     */
    @PostMapping("/edit")
    public AjaxResult editKnowledge(@RequestBody WorkOrderKnowledgeBase workOrderKnowledgeBase) {

        return workOrderKnowledgeBaseService.updateKnowledge(workOrderKnowledgeBase);
    }

    /**
     * 查询知识库
     */
    @GetMapping("/queryKnowledge")
    public TableDataInfo<?> queryKnowledgeBase(WorkOrderKnowledgeBase workOrderKnowledgeBase) {
        startPage();
        List<WorkOrderKnowledgeBase> getklBase = workOrderKnowledgeBaseService.getKlBaseList(workOrderKnowledgeBase);
        return getDataTable(getklBase);
    }

    /**
     * 下载模版
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
//        ExcelUtil<WorkOrderKnowledgeBase> util = new ExcelUtil<WorkOrderKnowledgeBase>(WorkOrderKnowledgeBase.class);
//        util.importTemplateExcel(response, "知识库数据");
        //workOrderKnowledgeBaseService.importTemplate();
        String nameStr = "知识导入";
        List<String> heads = Arrays.asList("问题描述", "解决方式", "知识分类");
        XSSFWorkbook book = new XSSFWorkbook();
        CascadeSelectTool cascadeSelectTool = new CascadeSelectTool(book)
                .createSheet(nameStr)
                .createHead(heads);
        List<WorkOrderKnowledgeType> list = workOrderKnowledgeTypeService.getTypeList();
        List<String> classificationList = getclassification(list);
        List<String> storeList = CollectionUtil.newArrayList(classificationList);
        String[] storeStr = storeList.toArray(new String[storeList.size()]);
        cascadeSelectTool.setDropDownBox(nameStr, storeStr, 2);

        String fileName = nameStr+".xlsx";
        // 将工作簿写入输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        book.write(outputStream);
        // 设置响应头，告诉浏览器返回的是一个Excel文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        // 将Excel文件写入响应的输出流
        OutputStream outStream = response.getOutputStream();
        outputStream.writeTo(outStream);
        outStream.flush();
        outStream.close();
    }

    public List<String> getclassification(List<WorkOrderKnowledgeType> list){
        List<String> classificationList = new ArrayList<>();
        list.forEach(classification ->{
            classificationList.add(classification.getName());
            if (classification.getChildren().size() != 0){
                List<String> childrenList = getclassification(classification.getChildren());
                childrenList.forEach( children->{
                    classificationList.add(children);
                });
            }
        });
        return classificationList;
    }
    /**
     * 导入数据
     * @param file
     * @param updateSupport
     * @return
     * @throws Exception
     */
    @Log(title = "知识库管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<WorkOrderKnowledgeBase> util = new ExcelUtil<WorkOrderKnowledgeBase>(WorkOrderKnowledgeBase.class);
        List<WorkOrderKnowledgeBase> userList = util.importExcel(file.getInputStream());
        List<WorkOrderKnowledgeType> workOrderKnowledgeTypeList = workOrderKnowledgeTypeService.getKnowledgeType(userList);
        for(int i =0;i<userList.size();i++){
            userList.get(i).setClassification(workOrderKnowledgeTypeList.get(i).getId());
        }
        String operName = getUsername();
        String message = workOrderKnowledgeBaseService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    /**
     * 知识库导出
     * @param response
     * @param workOrderKnowledgeBase
     */
    @Log(title = "知识库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WorkOrderKnowledgeBase workOrderKnowledgeBase) {
        List<WorkOrderKnowledgeBase> list = workOrderKnowledgeBaseService.getKlBaseList(workOrderKnowledgeBase);
        List<WorkOrderKnowledgeBase> workOrderKnowledgeBaseList = new ArrayList<>();
        list.forEach( baseList->{
            String solution = baseList.getSolution().replaceAll("<[^>]*>", "");
            baseList.setSolution(solution);
            workOrderKnowledgeBaseList.add(baseList);
        });

        ExcelUtil<WorkOrderKnowledgeBase> util = new ExcelUtil<WorkOrderKnowledgeBase>(WorkOrderKnowledgeBase.class);
        util.exportExcel(response, workOrderKnowledgeBaseList, "知识库数据");
    }


}
