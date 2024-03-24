package com.vaskka.diary.core.service;

import com.vaskka.diary.core.dal.DiaryContentDAO;
import com.vaskka.diary.core.dal.UserDiaryMapper;
import com.vaskka.diary.core.facade.AdminService;
import com.vaskka.diary.core.model.dataobject.UserDiaryDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service(value = "adminServiceImpl")
public class AdminServiceImpl implements AdminService {

    @Resource
    private UserDiaryMapper userDiaryMapper;

    @Resource
    private DiaryContentDAO diaryContentDAO;

    @Override
    @Transactional
    public long updateUserIdListByDiaryIds(List<String> userIdList, List<String> diaryIds) {
        long count = 0;
        for (String diaryId : diaryIds) {
            long realDiaryId = Long.parseLong(diaryId);
            for (String userId : userIdList) {
                long realUserId = Long.parseLong(userId);
                var userDiaryRecord = userDiaryMapper.findByUserIdDiaryId(realUserId, realDiaryId);

                if (userDiaryRecord != null) {
                    userDiaryMapper.deleteUserDiaryById(userDiaryRecord.getId());
                }

                UserDiaryDO userDiaryDO = new UserDiaryDO();
                userDiaryDO.setDiaryId(realDiaryId);
                userDiaryDO.setUserId(realUserId);
                int effect = userDiaryMapper.insertUserDiary(userDiaryDO);
                count += effect;
            }
        }

        // 同步修改es
        diaryContentDAO.updateUserIdListByDiaryId(userIdList, diaryIds);

        return count;
    }
}
