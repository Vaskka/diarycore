package com.vaskka.diary.core.facade;

public interface DiaryAuthManager {

    boolean checkDiaryPermission(Long userId, Long diaryId);

}
