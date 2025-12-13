package com.inspur.agriculture.input.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.domain.AgriInput;
import com.inspur.agriculture.input.service.IAgriInputService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 农业投入品控制器
 *
 * @author igdp
 */
@RestController
@RequestMapping("/agriculture/input")
public class AgriInputController {

    @Autowired
    private IAgriInputService agriInputService;

    // 图片存储配置（建议移到application.yml）
    private static final String UPLOAD_ROOT_PATH = "/usr/local/agriculture/upload/input/";
    private static final String IMAGE_ACCESS_PREFIX = "/agriculture/upload/input/";
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(".jpg", ".png", ".webp");
    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024;

    /**
     * 查询投入品列表（分页）
     */
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(required = false) String inputName,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String agriculturalInputType,
            @RequestParam(required = false) String registerCode,
            @RequestParam(required = false) String inputSku,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        AgriInput agriInput = new AgriInput();
        agriInput.setInputName(inputName);
        agriInput.setType(type);
        agriInput.setAgriculturalInputType(agriculturalInputType);
        agriInput.setRegisterCode(registerCode);
        agriInput.setInputSku(inputSku);
        agriInput.setStatus(status);

        if (keyword != null && !keyword.trim().isEmpty()) {
            agriInput.setInputName(keyword);
            agriInput.setInputSku(keyword);
            agriInput.setRegisterCode(keyword);
            agriInput.setTrademark(keyword);
            agriInput.setAgriculturalInputType(keyword);
        }

        PageHelper.startPage(page, pageSize);
        List<AgriInput> list = agriInputService.selectInputList(agriInput);
        PageInfo<AgriInput> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("page", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());

        return AjaxResult.success(result);
    }

    /**
     * 获取全部投入品列表（不分页）
     */
    @GetMapping("/getAllInputList")
    public AjaxResult getAllInputList() {
        AgriInput agriInput = new AgriInput();
        agriInput.setDelFlag("0");
        agriInput.setStatus("active");
        List<AgriInput> list = agriInputService.selectInputList(agriInput);
        return AjaxResult.success(list);
    }

    /**
     * 根据ID获取投入品详情
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        AgriInput agriInput = agriInputService.selectInputById(id);
        if (agriInput == null) {
            return AjaxResult.error("Agricultural input does not exist");
        }
        return AjaxResult.success(agriInput);
    }

    /**
     * 新增投入品
     */
    @PostMapping
    public AjaxResult add(@RequestBody AgriInput agriInput) {
        if (agriInput.getInputName() == null || agriInput.getInputName().trim().isEmpty()) {
            return AjaxResult.error("Input product name cannot be empty");
        }
        if (agriInput.getType() == null || agriInput.getType().trim().isEmpty()) {
            return AjaxResult.error("Input product type cannot be empty");
        }
        if (agriInput.getInputSku() == null || agriInput.getInputSku().trim().isEmpty()) {
            return AjaxResult.error("Product identification code cannot be empty");
        }

        if (agriInput.getStatus() == null || agriInput.getStatus().trim().isEmpty()) {
            agriInput.setStatus("active");
        }
        if (agriInput.getDelFlag() == null || agriInput.getDelFlag().trim().isEmpty()) {
            agriInput.setDelFlag("0");
        }

        int rows = agriInputService.insertInput(agriInput);
        if (rows > 0) {
            return AjaxResult.success("Add successfully", agriInput);
        }
        return AjaxResult.error("Add failed");
    }

    /**
     * 修改投入品
     */
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") Long id, @RequestBody AgriInput agriInput) {
        AgriInput existInput = agriInputService.selectInputById(id);
        if (existInput == null) {
            return AjaxResult.error("Agricultural input does not exist");
        }

        agriInput.setInputId(id);
        int rows = agriInputService.updateInput(agriInput);
        if (rows > 0) {
            return AjaxResult.success("Modify successfully", agriInput);
        }
        return AjaxResult.error("Modify failed");
    }

    /**
     * 删除投入品（单条）
     */
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") Long id) {
        int rows = agriInputService.deleteInputById(id);
        if (rows > 0) {
            return AjaxResult.success("Delete successfully");
        }
        return AjaxResult.error("Delete failed");
    }

    /**
     * 批量删除投入品
     */
    @DeleteMapping("/batch")
    public AjaxResult removeBatch(@RequestBody Long[] ids) {
        if (ids == null || ids.length == 0) {
            return AjaxResult.error("Please select agricultural inputs to delete");
        }

        int rows = agriInputService.deleteInputByIds(ids);
        if (rows > 0) {
            return AjaxResult.success("Batch delete successfully");
        }
        return AjaxResult.error("Batch delete failed");
    }

    /**
     * 获取投入品统计信息
     */
    @GetMapping("/statistics")
    public AjaxResult getStatistics() {
        Map<String, Object> statistics = agriInputService.getInputStatistics();
        return AjaxResult.success(statistics);
    }

    /**
     * 导出投入品数据
     */
    @GetMapping("/export")
    public AjaxResult export(
            @RequestParam(required = false) String inputName,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword
    ) {
        AgriInput agriInput = new AgriInput();
        agriInput.setInputName(inputName);
        agriInput.setType(type);

        if (keyword != null && !keyword.trim().isEmpty()) {
            agriInput.setInputName(keyword);
            agriInput.setInputSku(keyword);
            agriInput.setRegisterCode(keyword);
            agriInput.setTrademark(keyword);
        }

        List<AgriInput> list = agriInputService.selectInputList(agriInput);
        return AjaxResult.success("Export successfully", list);
    }

    /**
     * 投入品图片上传接口
     */
    @PostMapping("/uploadImage")
    public AjaxResult uploadImage(@RequestParam("file") MultipartFile file) {
        // 空文件校验
        if (file.isEmpty()) {
            return AjaxResult.error("The uploaded image cannot be empty");
        }

        // 文件后缀校验
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.lastIndexOf(".") == -1) {
            return AjaxResult.error("Invalid image file (no suffix)");
        }
        String fileSuffix = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();

        // 图片类型校验
        if (!ALLOWED_IMAGE_TYPES.contains(fileSuffix)) {
            return AjaxResult.error("Only JPG, PNG, WEBP format images are supported!");
        }

        // 大小校验
        if (file.getSize() > MAX_IMAGE_SIZE) {
            return AjaxResult.error("Image size cannot exceed 2MB!");
        }

        // 生成唯一文件名
        String uniqueFileName = UUID.randomUUID().toString() + fileSuffix;
        File saveFile = new File(UPLOAD_ROOT_PATH + uniqueFileName);

        try {
            // 创建目录
            if (!saveFile.getParentFile().exists()) {
                boolean mkdirs = saveFile.getParentFile().mkdirs();
                if (!mkdirs) {
                    return AjaxResult.error("Failed to create image storage directory, please check server permissions!");
                }
            }

            // 写入文件
            file.transferTo(saveFile);

            // 返回可访问的图片URL
            String imageUrl = IMAGE_ACCESS_PREFIX + uniqueFileName;
            return AjaxResult.success(imageUrl);

        } catch (IOException e) {
            return AjaxResult.error("Image upload failed: " + e.getMessage());
        }
    }

}