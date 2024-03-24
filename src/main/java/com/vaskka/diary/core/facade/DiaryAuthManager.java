package com.vaskka.diary.core.facade;

import com.vaskka.diary.core.model.bizobject.Diary;
import com.vaskka.diary.core.model.bizobject.DiaryWrapper;
import com.vaskka.diary.core.model.bizobject.User;

import java.util.List;

public interface DiaryAuthManager {

    boolean checkDiaryPermission(Long userId, Long diaryId);

    DiaryWrapper filterPermissionDiaryWrapper(User user, DiaryWrapper diaryWrapper);

    List<Diary> filterPermissionDiaryList(User user, DiaryWrapper diaryWrapper);
}
