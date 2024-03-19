package com.vaskka.diary.core.service;

import com.vaskka.diary.core.dal.UserDiaryMapper;
import com.vaskka.diary.core.facade.DiaryAuthManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("defaultDiaryAuthManager")
public class DiaryAuthManagerImpl implements DiaryAuthManager {

    @Resource
    private UserDiaryMapper userDiaryMapper;

    @Override
    public boolean checkDiaryPermission(Long userId, Long diaryId) {
        return userDiaryMapper.findByUserIdDiaryId(userId, diaryId) != null;
    }
}
