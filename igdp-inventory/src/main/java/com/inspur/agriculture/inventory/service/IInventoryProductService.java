package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.InventoryProduct;

import java.util.List;

/**
 * 商品服务接口
 */
public interface IInventoryProductService extends IService<InventoryProduct> {

    /**
     * 查询商品列表
     *
     * @param product 查询条件
     * @return 商品列表
     */
    List<InventoryProduct> selectProductList(InventoryProduct product);

    /**
     * 根据ID查询商品
     *
     * @param id 主键ID
     * @return 商品信息
     */
    InventoryProduct selectProductById(Long id);

    /**
     * 查询商品大类列表(parent_id 为空)
     * @return 商品大类列表
     */
    List<InventoryProduct> selectMainCategoryList();

    /**
     * 查询商品分类树(parent_id 关系，两级)
     * @return 分类树
     */
    java.util.List<java.util.Map<String, Object>> selectCategoryTree();

    /**
     * 新增商品
     *
     * @param product 商品信息
     * @return 结果
     */
    boolean createProduct(InventoryProduct product);

    /**
     * 更新商品
     *
     * @param product 商品信息
     * @return 结果
     */
    boolean updateProduct(InventoryProduct product);

    /**
     * 删除商品
     *
     * @param id 主键ID
     * @return 结果
     */
    boolean deleteProduct(Long id);
}
