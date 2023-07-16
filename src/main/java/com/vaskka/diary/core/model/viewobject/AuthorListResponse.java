package com.vaskka.diary.core.model.viewobject;

import com.vaskka.diary.core.model.bizobject.Author;
import lombok.Data;

import java.util.List;

@Data
public class AuthorListResponse extends CommonResponse<List<Author>> {

}
