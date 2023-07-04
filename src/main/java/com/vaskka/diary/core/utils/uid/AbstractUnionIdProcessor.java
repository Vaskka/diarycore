package com.vaskka.diary.core.utils.uid;


import com.vaskka.diary.core.constant.BizPart;

public abstract class AbstractUnionIdProcessor implements UnionIdProcessor {

    /**
     * dpitech union id generator
     */
    private final GlobalIdCore core = new GlobalIdCore(getMachineIdPart());

    /**
     * 获取全局id，设置为指定的identity位
     * @return long union id
     */
    @Override
    public long getUnionId(BizPart bizPart) {
        return core.getUnionId(bizPart.getBizCode());
    }

}