package com.vaskka.diary.core.model.viewobject;

import com.vaskka.diary.core.model.bizobject.SearchResultPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchDiaryPageableResponse extends CommonResponse<SearchResultPageable> {
}
