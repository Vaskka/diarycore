package com.vaskka.diary.core.model.viewobject;

import lombok.Data;

import java.util.List;

@Data
public class SearchDiaryRequest extends NeedAuthRequest {

    /**
     * 限制作者的搜索
     */
    private List<String> pickedAuthorId;

    /**
     * 搜索的文字内容
     */
    private String searchText;

}
