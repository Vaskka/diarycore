package com.vaskka.diary.core.model.viewobject;

import com.vaskka.diary.core.model.bizobject.Diary;
import lombok.Data;

import java.util.List;


@Data
public class SearchDiaryResponse extends CommonResponse<List<Diary>> {
}
