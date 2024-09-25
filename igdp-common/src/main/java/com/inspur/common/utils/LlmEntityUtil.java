package com.inspur.common.utils;


import cn.hutool.core.date.DateTime;
import com.inspur.common.domain.BaseLLmEntity;


public class LlmEntityUtil {
    public static void setDefaultValue(BaseLLmEntity entity) {

        String user = LoginHelper.getUsername();
        if (entity.getId() == 0) {
            entity.setCreatedTime(DateTime.now());
            entity.setUpdatedTime(DateTime.now());
            entity.setCreator(user);
            entity.setUpdater(user);
        } else {
            entity.setUpdater(user);
            entity.setUpdatedTime(DateTime.now());
        }


    }

}
