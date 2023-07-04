package com.vaskka.diary.core.model.dataobject;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthStorageDO implements Serializable {

    private String authToken;

    private String userId;


}
