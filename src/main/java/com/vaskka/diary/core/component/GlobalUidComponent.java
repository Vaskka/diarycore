package com.vaskka.diary.core.component;

import com.vaskka.diary.core.utils.uid.AbstractUnionIdProcessor;
import org.springframework.stereotype.Component;

@Component("uidComponent")
public class GlobalUidComponent extends AbstractUnionIdProcessor {

    @Override
    public long getMachineIdPart() {
        return 0;
    }

}
