package com.vaskka.diary.core.utils.uid;


import com.vaskka.diary.core.constant.BizPart;

public interface UnionIdProcessor {

    /**
     * 获取全局id，设置为指定的identity位
     * @return long union id
     */
    long getUnionId(BizPart bizPart);

    /**
     * 如何得到机器Id
     * @return long
     */
    long getMachineIdPart();
}
