package com.vaskka.diary.core.model.viewobject;

import com.vaskka.diary.core.model.bizobject.Diary;
import com.vaskka.diary.core.model.bizobject.SearchSummaryResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class SearchDiaryResponse extends CommonResponse<SearchSummaryResult> {
}
