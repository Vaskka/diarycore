package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class User {

    private String uid;

    private String userName;

    private String userType;

    private Map<String, Object> externParam = new HashMap<>();

}
