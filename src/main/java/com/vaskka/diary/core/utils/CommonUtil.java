package com.vaskka.diary.core.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

public class CommonUtil {

    public static String getRandom32LengthStr() {
        return DigestUtils.md5Hex(UUID.randomUUID().toString());
    }

}
