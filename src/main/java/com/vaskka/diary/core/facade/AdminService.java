package com.vaskka.diary.core.facade;

import java.util.List;

public interface AdminService {

    /**
     * 批量修改一批diary 的 user权限
     * @param userIdList userIdList
     * @param diaryIds diaryIds
     * @return effect
     */
    long updateUserIdListByDiaryIds(List<String> userIdList, List<String> diaryIds);

}
