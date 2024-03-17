package com.vaskka.diary.core.utils;

import java.util.List;

public class IpsUtil {
    public static List<String> getIps(String ipsLine) {
        return List.of(ipsLine.split(","));
    }
}
