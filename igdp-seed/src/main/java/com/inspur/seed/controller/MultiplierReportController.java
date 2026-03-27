package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.MultiplierReport;
import com.inspur.seed.domain.Organization;
import com.inspur.seed.mapper.OrganizationMapper;
import com.inspur.seed.service.IMultiplierReportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Multiplier Report Controller
 */
@RestController
@RequestMapping("/seed/multiplier-report")
public class MultiplierReportController extends BaseController {

    @Autowired
    private IMultiplierReportService multiplierReportService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @GetMapping("/list")
    public TableDataInfo list(MultiplierReport query) {
        startPage();
        List<MultiplierReport> list = multiplierReportService.selectList(query);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(multiplierReportService.selectById(id));
    }

    @PostMapping
    public AjaxResult add(@RequestBody MultiplierReport report) {
        return toAjax(multiplierReportService.create(report));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody MultiplierReport report) {
        return toAjax(multiplierReportService.edit(report));
    }

    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(multiplierReportService.remove(id));
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, MultiplierReport query) throws IOException {
        List<MultiplierReport> list = multiplierReportService.selectList(query);

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Multiplier Report");

        // ===== 样式 =====
        // 分组标题样式（第一行）
        CellStyle groupStyle = wb.createCellStyle();
        Font groupFont = wb.createFont();
        groupFont.setBold(true);
        groupFont.setFontHeightInPoints((short) 12);
        groupFont.setColor(IndexedColors.WHITE.getIndex());
        groupStyle.setFont(groupFont);
        groupStyle.setAlignment(HorizontalAlignment.CENTER);
        groupStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        groupStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        groupStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        groupStyle.setBorderBottom(BorderStyle.THIN);
        groupStyle.setBorderTop(BorderStyle.THIN);
        groupStyle.setBorderLeft(BorderStyle.THIN);
        groupStyle.setBorderRight(BorderStyle.THIN);

        // 列标题样式（第二行）
        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 10);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setWrapText(true);

        // 数据样式
        CellStyle dataStyle = wb.createCellStyle();
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        // 日期样式
        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.cloneStyleFrom(dataStyle);
        dateStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy-MM-dd"));

        // 数字样式
        CellStyle numStyle = wb.createCellStyle();
        numStyle.cloneStyleFrom(dataStyle);
        numStyle.setDataFormat(wb.createDataFormat().getFormat("0.00"));

        // ===== 第一行：分组标题 =====
        Row groupRow = sheet.createRow(0);
        groupRow.setHeightInPoints(28);
        String[] groups = {"Basic Information", "Basic Information", "Basic Information", "Basic Information",
                "Basic Information", "Basic Information", "Basic Information", "Basic Information",
                "Production Information", "Production Information", "Production Information", "Production Information", "Production Information",
                "Quality Information", "Quality Information", "Quality Information"};
        for (int i = 0; i < groups.length; i++) {
            Cell cell = groupRow.createCell(i);
            cell.setCellValue(groups[i]);
            cell.setCellStyle(groupStyle);
        }
        // 合并单元格：Basic(0-7), Production(8-12), Quality(13-15)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 12));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 15));

        // ===== 第二行：列标题 =====
        Row headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(32);
        String[] headers = {
                "Report Date", "Multiplier", "Distribution ID", "Certificate ID",
                "Seed Class Received", "Farm", "Crop Type", "Variety Name",
                "Area Planted (ha)", "Planting Date", "Harvest Date", "Produced Seed Qty (qt)", "Rejected Qty (qt)",
                "Germination Rate (%)", "Moisture Content (%)", "Remarks"
        };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // ===== 构建组织ID→名称映射 =====
        Map<String, String> orgNameMap = new HashMap<>();
        Set<String> orgIds = list.stream()
                .flatMap(r -> java.util.stream.Stream.of(r.getMultiplierId(), r.getFarmId()))
                .filter(id -> id != null && !id.isEmpty())
                .collect(Collectors.toSet());
        if (!orgIds.isEmpty()) {
            for (String orgId : orgIds) {
                try {
                    Organization org = organizationMapper.selectById(orgId);
                    if (org != null) {
                        orgNameMap.put(orgId, org.getOrgName());
                    }
                } catch (Exception ignored) {}
            }
        }

        // ===== 数据行 =====
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < list.size(); i++) {
            MultiplierReport r = list.get(i);
            Row row = sheet.createRow(i + 2);

            String multiplierName = orgNameMap.getOrDefault(r.getMultiplierId(), r.getMultiplierId());
            String farmName = orgNameMap.getOrDefault(r.getFarmId(), r.getFarmId());

            setCellValue(row, 0, r.getReportDate() != null ? sdf.format(r.getReportDate()) : "", dataStyle);
            setCellValue(row, 1, multiplierName, dataStyle);
            setCellValue(row, 2, r.getDistributionId(), dataStyle);
            setCellValue(row, 3, r.getCertificateId(), dataStyle);
            setCellValue(row, 4, r.getSeedClassReceived(), dataStyle);
            setCellValue(row, 5, farmName, dataStyle);
            setCellValue(row, 6, r.getCropType(), dataStyle);
            setCellValue(row, 7, r.getVarietyName(), dataStyle);
            setCellNumValue(row, 8, r.getAreaPlantedHa(), numStyle);
            setCellValue(row, 9, r.getPlantingDate() != null ? sdf.format(r.getPlantingDate()) : "", dataStyle);
            setCellValue(row, 10, r.getHarvestDate() != null ? sdf.format(r.getHarvestDate()) : "", dataStyle);
            setCellNumValue(row, 11, r.getProducedSeedQuantity(), numStyle);
            setCellNumValue(row, 12, r.getRejectedQuantity(), numStyle);
            setCellNumValue(row, 13, r.getGerminationRate(), numStyle);
            setCellNumValue(row, 14, r.getMoistureContent(), numStyle);
            setCellValue(row, 15, r.getRemark(), dataStyle);
        }

        // ===== 设置列宽 =====
        int[] widths = {14, 20, 20, 20, 18, 15, 15, 25, 15, 14, 14, 22, 18, 18, 18, 30};
        for (int i = 0; i < widths.length; i++) {
            sheet.setColumnWidth(i, widths[i] * 256);
        }

        // ===== 输出 =====
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=Multiplier_Report_Data.xlsx");
        wb.write(response.getOutputStream());
        wb.close();
    }

    private void setCellValue(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    private void setCellNumValue(Row row, int col, java.math.BigDecimal value, CellStyle style) {
        Cell cell = row.createCell(col);
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        }
        cell.setCellStyle(style);
    }
}
