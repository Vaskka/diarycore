package com.vaskka.diary.core.model.viewobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchDiaryRequest extends NeedAuthRequest {

    @Data
    @Builder
    @AllArgsConstructor
    public static class DateRange {

        private String le;

        private String ge;
    }

    /**
     * 限制作者的搜索
     */
    private List<String> pickedAuthorId;

    /**
     * authorId反检索
     */
    private List<String> authorIdNotIn;

    /**
     * 搜索的文字内容
     */
    private String searchText;

    private List<String> multiSearchText;

    private DateRange dateRange;
}
