package com.inspur.farmland.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.utils.poi.ExcelUtil;
import com.inspur.farmland.domain.FarmerInfo;
import com.inspur.farmland.service.IFarmerInfoService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 农民管理Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/farmland/farmer")
public class FarmerInfoController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(FarmerInfoController.class);

    @Autowired
    private IFarmerInfoService farmerInfoService;

    /**
     * 获取农民分页列表
     */
    @GetMapping("/page")
    public TableDataInfo page(FarmerInfo farmerInfo) {
        startPage();
        List<FarmerInfo> list = farmerInfoService.selectFarmerInfoList(farmerInfo);
        return getDataTable(list);
    }

    /**
     * 获取农民详情
     */
    @GetMapping("/{farmerId}")
    public AjaxResult getInfo(
            @PathVariable String farmerId) {
        FarmerInfo farmerInfo = farmerInfoService.selectFarmerInfoByFarmerId(farmerId);
        if (farmerInfo == null) {
            return AjaxResult.error("Farmer information does not exist");
        }
        return AjaxResult.success(farmerInfo);
    }

    /**
     * 新增农民
     */
    @PostMapping
    public AjaxResult add(@RequestBody FarmerInfo farmerInfo) {
        // 校验身份证号唯一性
        if (!farmerInfoService.checkIdCardUnique(farmerInfo.getIdCard(), null)) {
            return AjaxResult.error("ID Card number already exists");
        }

        String farmerId = farmerInfoService.insertFarmerInfo(farmerInfo);
        Map<String, Object> result = new HashMap<>();
        result.put("farmerId", farmerId);
        return AjaxResult.success("新增成功", result);
    }

    /**
     * 修改农民
     */
    @PostMapping("/{farmerId}")
    public AjaxResult edit(
            @PathVariable String farmerId,
            @RequestBody FarmerInfo farmerInfo) {
        farmerInfo.setFarmerId(farmerId);

        // 校验农民是否存在
        FarmerInfo existFarmer = farmerInfoService.selectFarmerInfoByFarmerId(farmerId);
        if (existFarmer == null) {
            return AjaxResult.error("Farmer information does not exist");
        }

        // 校验身份证号唯一性（排除自己）
        if (!farmerInfoService.checkIdCardUnique(farmerInfo.getIdCard(), farmerId)) {
            return AjaxResult.error("ID Card number already exists");
        }

        int rows = farmerInfoService.updateFarmerInfo(farmerInfo);
        return toAjax(rows);
    }

    /**
     * 删除农民
     */
    @PostMapping("/{farmerId}/delete")
    public AjaxResult remove(
            @PathVariable String farmerId) {
        int rows = farmerInfoService.deleteFarmerInfoByFarmerId(farmerId);
        return toAjax(rows);
    }

    /**
     * 批量删除农民
     */
    @PostMapping("/batch/delete")
    public AjaxResult removeBatch(@RequestBody Map<String, String[]> params) {
        String[] farmerIds = params.get("farmerIds");
        if (farmerIds == null || farmerIds.length == 0) {
            return AjaxResult.error("Please select farmers to delete");
        }

        Map<String, Integer> result = farmerInfoService.deleteFarmerInfoByIds(farmerIds);
        return AjaxResult.success("删除完成", result);
    }

    /**
     * 获取农民下拉选项
     */
    @GetMapping("/options")
    public AjaxResult options(
            @RequestParam(required = false) String kebeleCode,
            @RequestParam(required = false) String keyword) {
        List<Map<String, Object>> options = farmerInfoService.selectFarmerOptions(kebeleCode, keyword);
        return AjaxResult.success(options);
    }

    /**
     * 下载导入模板
     */
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        OutputStream out = null;
        Workbook workbook = null;
        try {
            // 创建工作簿
            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Farmers");
            
            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            // 创建表头行
            Row headerRow = sheet.createRow(0);
            
            // 定义列名（按照FarmerInfo实体类的字段顺序）
            String[] columns = {
                "Farmer Name",      // farmerName - 必填
                "ID Card",          // idCard - 必填
                "Gender",           // gender - M/F/MALE/FEMALE
                "Birthday",         // birthday - yyyy-MM-dd
                "Phone",            // phone
                "Email",            // email
                "Youth Category",   // youthCategory - 1/0
                "Zone Code",        // zoneCode
                "Zone Name",        // zoneName
                "Woreda Code",      // woredaCode
                "Woreda Name",      // woredaName
                "Kebele Code",      // kebeleCode
                "Kebele Name",      // kebeleName
                "Address",          // address
                "DA ID",            // daId
                "Remark"            // remark
            };
            
            // 写入表头
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256); // 设置列宽
            }
            
            // 添加示例数据行（可选）
            Row exampleRow = sheet.createRow(1);
            String[] exampleData = {
                "John Doe",         // Farmer Name
                "ID123456789",      // ID Card
                "MALE",             // Gender
                "1990-01-15",       // Birthday
                "+251912345678",    // Phone
                "john@example.com", // Email
                "1",                // Youth Category
                "102001000",        // Zone Code
                "East Shewa",       // Zone Name
                "102001001",        // Woreda Code
                "Adama",            // Woreda Name
                "102001001001",     // Kebele Code
                "Kebele 01",        // Kebele Name
                "Main Street 123",  // Address
                "DA001",            // DA ID
                "Sample farmer"     // Remark
            };
            
            for (int i = 0; i < exampleData.length; i++) {
                Cell cell = exampleRow.createCell(i);
                cell.setCellValue(exampleData[i]);
            }
            
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=Farmer_Import_Template.xlsx");
            
            // 写入响应流
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            
        } catch (Exception e) {
            log.error("Failed to generate import template", e);
            try {
                response.reset();
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":500,\"msg\":\"Failed to generate template\"}");
            } catch (Exception ex) {
                log.error("Failed to write error response", ex);
            }
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                log.error("Failed to close resources", e);
            }
        }
    }

    /**
     * 导入农民数据
     */
    @PostMapping("/import")
    public AjaxResult importData(@RequestParam("file") MultipartFile file,
                                 @RequestParam(value = "updateSupport", defaultValue = "false") boolean updateSupport) {
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            
            if (sheet == null) {
                return AjaxResult.error("Excel file is empty");
            }
            
            int successCount = 0;
            int updateCount = 0;
            int failCount = 0;
            StringBuilder failMsg = new StringBuilder();
            
            // 从第2行开始读取（第1行是表头，第2行可能是示例数据）
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;
                
                try {
                    // 读取单元格数据
                    String farmerName = getCellStringValue(row.getCell(0));
                    String idCard = getCellStringValue(row.getCell(1));
                    String gender = getCellStringValue(row.getCell(2));
                    String birthday = getCellStringValue(row.getCell(3));
                    String phone = getCellStringValue(row.getCell(4));
                    String email = getCellStringValue(row.getCell(5));
                    String youthCategory = getCellStringValue(row.getCell(6));
                    String zoneCode = getCellStringValue(row.getCell(7));
                    String zoneName = getCellStringValue(row.getCell(8));
                    String woredaCode = getCellStringValue(row.getCell(9));
                    String woredaName = getCellStringValue(row.getCell(10));
                    String kebeleCode = getCellStringValue(row.getCell(11));
                    String kebeleName = getCellStringValue(row.getCell(12));
                    String address = getCellStringValue(row.getCell(13));
                    String daId = getCellStringValue(row.getCell(14));
                    String remark = getCellStringValue(row.getCell(15));
                    
                    // 跳过空行或示例数据行
                    if (farmerName == null || farmerName.trim().isEmpty()) {
                        continue;
                    }
                    if ("John Doe".equals(farmerName)) {
                        continue; // 跳过示例数据
                    }
                    
                    // 创建FarmerInfo对象
                    FarmerInfo farmer = new FarmerInfo();
                    farmer.setFarmerName(farmerName);
                    farmer.setIdCard(idCard);
                    farmer.setGender(gender);
                    if (birthday != null && !birthday.isEmpty()) {
                        try {
                            farmer.setBirthday(java.sql.Date.valueOf(birthday));
                        } catch (Exception e) {
                            // 忽略日期解析错误
                        }
                    }
                    farmer.setPhone(phone);
                    farmer.setEmail(email);
                    farmer.setYouthCategory(youthCategory);
                    farmer.setZoneCode(zoneCode);
                    farmer.setZoneName(zoneName);
                    farmer.setWoredaCode(woredaCode);
                    farmer.setWoredaName(woredaName);
                    farmer.setKebeleCode(kebeleCode);
                    farmer.setKebeleName(kebeleName);
                    farmer.setAddress(address);
                    farmer.setDaId(daId);
                    farmer.setRemark(remark);
                    
                    // 检查身份证是否已存在
                    FarmerInfo existFarmer = null;
                    if (idCard != null && !idCard.isEmpty()) {
                        existFarmer = farmerInfoService.selectFarmerByIdCard(idCard);
                    }
                    
                    if (existFarmer != null) {
                        if (updateSupport) {
                            farmer.setFarmerId(existFarmer.getFarmerId());
                            farmerInfoService.updateFarmerInfo(farmer);
                            updateCount++;
                        } else {
                            failCount++;
                            failMsg.append("Row ").append(rowNum + 1).append(": ID Card already exists; ");
                        }
                    } else {
                        farmerInfoService.insertFarmerInfo(farmer);
                        successCount++;
                    }
                } catch (Exception e) {
                    failCount++;
                    failMsg.append("Row ").append(rowNum + 1).append(": ").append(e.getMessage()).append("; ");
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("updateCount", updateCount);
            result.put("failCount", failCount);
            if (failMsg.length() > 0) {
                result.put("failMsg", failMsg.toString());
            }
            
            return AjaxResult.success("Import completed", result);
        } catch (Exception e) {
            log.error("Import failed", e);
            return AjaxResult.error("Import failed: " + e.getMessage());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error("Failed to close workbook", e);
                }
            }
        }
    }
    
    /**
     * 获取单元格字符串值
     */
    private String getCellStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    java.util.Date date = cell.getDateCellValue();
                    return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    @GetMapping("/getAllFarmerList")
    public AjaxResult getAllFarmerList() {
        FarmerInfo farmerInfo = new FarmerInfo();
        farmerInfo.setStatus("1");
        List<FarmerInfo> list = farmerInfoService.selectFarmerInfoList(farmerInfo);
        return AjaxResult.success(list);
    }
}
