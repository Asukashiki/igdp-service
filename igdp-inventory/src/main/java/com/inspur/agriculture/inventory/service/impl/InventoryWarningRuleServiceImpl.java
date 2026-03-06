package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryWarningRule;
import com.inspur.agriculture.inventory.mapper.InventoryWarningRuleMapper;
import com.inspur.agriculture.inventory.service.IInventoryWarningRuleService;
import org.springframework.stereotype.Service;

@Service
public class InventoryWarningRuleServiceImpl extends ServiceImpl<InventoryWarningRuleMapper, InventoryWarningRule> implements IInventoryWarningRuleService {
}
