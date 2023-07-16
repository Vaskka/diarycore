package com.vaskka.diary.core.model.viewobject;

import lombok.Data;

@Data
public class FindDiaryByAuthorRequest extends NeedAuthRequest {

    private String authorId;

}
