package com.vaskka.diary.core.service;

import com.vaskka.diary.core.dal.UserDiaryMapper;
import com.vaskka.diary.core.facade.DiaryAuthManager;
import com.vaskka.diary.core.model.bizobject.Diary;
import com.vaskka.diary.core.model.bizobject.DiaryWrapper;
import com.vaskka.diary.core.model.bizobject.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("defaultDiaryAuthManager")
public class DiaryAuthManagerImpl implements DiaryAuthManager {

    @Resource
    private UserDiaryMapper userDiaryMapper;

    @Override
    public boolean checkDiaryPermission(Long userId, Long diaryId) {
        return userDiaryMapper.findByUserIdDiaryId(userId, diaryId) != null;
    }

    @Override
    public DiaryWrapper filterPermissionDiaryWrapper(User user, DiaryWrapper diaryWrapper) {
        if (user == null) {
            log.info("user not exist, clean diary list");
            diaryWrapper.setDiaryList(new ArrayList<>());
            return diaryWrapper;
        }
        if (diaryWrapper == null) {
            return null;
        }

        if (diaryWrapper.getDiaryList() == null || diaryWrapper.getDiaryList().isEmpty()) {
            return diaryWrapper;
        }

        // filter inner diary list
        List<Diary> permissionDiary = filterPermissionDiaryList(user, diaryWrapper);

        if (permissionDiary.size() != diaryWrapper.getDiaryList().size()) {
            diaryWrapper.setDiaryList(permissionDiary);
        }
        return diaryWrapper;
    }

    @Override
    public List<Diary> filterPermissionDiaryList(User user, DiaryWrapper diaryWrapper) {
        return diaryWrapper.getDiaryList()
                .stream()
                .map(diary -> {
                    if (checkDiaryPermission(Long.parseLong(user.getUid()), Long.parseLong(diary.getDiaryId()))) {
                        return diary;
                    } else {
                        // 无权限
                        return processWhenDiaryPermissionDeny(diary);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Diary processWhenDiaryPermissionDeny(Diary originDiary) {
        // 当前实现：清空日记的内容，并替换：无权限
        originDiary.setSubTitle("");
        originDiary.getDiaryContent().setContent("暂无权限");
        originDiary.getDiaryContent().setSubTitle("");
        return originDiary;
    }
}
