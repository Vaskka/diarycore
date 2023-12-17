package com.vaskka.diary.core.model.viewobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthorTypeListResponse extends CommonResponse<List<String>> {
}
