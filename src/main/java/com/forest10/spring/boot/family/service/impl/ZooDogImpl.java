package com.forest10.spring.boot.family.service.impl;

import com.forest10.spring.boot.family.common.AnnimalTypeEnum;
import com.forest10.spring.boot.family.service.IZooHandle;
import org.springframework.stereotype.Component;

/**
 * @author Forest10
 * @date 2019-05-12 18:09
 */
@Component
class ZooDogImpl implements IZooHandle {


    @Override
    public String quark() {
        return "汪汪";
    }

    @Override
    public AnnimalTypeEnum supportedType() {
        return AnnimalTypeEnum.DOG;
    }
}
