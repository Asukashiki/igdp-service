package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryProduct;
import com.inspur.agriculture.inventory.mapper.InventoryProductMapper;
import com.inspur.agriculture.inventory.service.IInventoryProductService;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Product service implementation.
 */
@Service
public class InventoryProductServiceImpl extends ServiceImpl<InventoryProductMapper, InventoryProduct>
        implements IInventoryProductService {

    @Override
    public List<InventoryProduct> selectProductList(InventoryProduct product) {
        return baseMapper.selectProductList(product);
    }

    @Override
    public InventoryProduct selectProductById(Long id) {
        return baseMapper.selectProductById(id);
    }

    @Override
    public List<InventoryProduct> selectMainCategoryList() {
        return baseMapper.selectMainCategoryList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createProduct(InventoryProduct product) {
        validateProduct(product, false);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        product.setCreateBy(getCurrentUsername());
        product.setUpdateBy(getCurrentUsername());
        return this.save(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProduct(InventoryProduct product) {
        if (product == null || product.getId() == null) {
            throw new ServiceException("Product ID cannot be null.");
        }
        InventoryProduct exists = this.getById(product.getId());
        if (exists == null) {
            throw new ServiceException("Product not found.");
        }
        validateProduct(product, true);
        product.setUpdateTime(LocalDateTime.now());
        product.setUpdateBy(getCurrentUsername());
        return this.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(Long id) {
        if (id == null) {
            throw new ServiceException("Product ID cannot be null.");
        }
        InventoryProduct exists = this.getById(id);
        if (exists == null) {
            throw new ServiceException("Product not found.");
        }
        return this.removeById(id);
    }

    private void validateProduct(InventoryProduct product, boolean isUpdate) {
        if (product == null) {
            throw new ServiceException("Product information cannot be null.");
        }
        if (isBlank(product.getProductCode())) {
            throw new ServiceException("Product code cannot be empty.");
        }
        if (isBlank(product.getProductName())) {
            throw new ServiceException("Product name cannot be empty.");
        }
        if (isBlank(product.getMainCategory())) {
            throw new ServiceException("Main category cannot be empty.");
        }
        if (product.getParentId() == null && !isBlank(product.getSubCategory())) {
            throw new ServiceException("自定义商品大类时商品小类必须为空");
        }
        if (isBlank(product.getUnit())) {
            throw new ServiceException("Unit cannot be empty.");
        }
        if (product.getPrice() == null) {
            product.setPrice(BigDecimal.ZERO);
        }
        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("Reference price cannot be less than 0.");
        }

        LambdaQueryWrapper<InventoryProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryProduct::getProductCode, product.getProductCode());
        if (isUpdate && product.getId() != null) {
            wrapper.ne(InventoryProduct::getId, product.getId());
        }
        long count = this.count(wrapper);
        if (count > 0) {
            throw new ServiceException("Product code already exists.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String getCurrentUsername() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception ex) {
            return "system";
        }
    }
}
