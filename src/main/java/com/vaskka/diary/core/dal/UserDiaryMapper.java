package com.vaskka.diary.core.dal;

import com.vaskka.diary.core.model.dataobject.UserDiaryDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDiaryMapper {

    @Insert(value = "INSERT INTO user_diary(id, gmt_create, gmt_modified, user_id, diary_id) " +
            "VALUES (#{id}, #{gmtCreate, jdbcType=TIMESTAMP}, #{gmtModified, jdbcType=TIMESTAMP}, " +
            "#{userId}, #{diaryId})")
    Integer insertUserDiary(UserDiaryDO userDiaryDO);

    @Delete(value = "DELETE FROM user_diary ud WHERE ud.id=#{id} LIMIT 1")
    Integer deleteUserIpById(@Param("id") Long id);

    @Select(value = "SELECT id, gmt_create, gmt_modified, user_id, diary_id " +
            "FROM user_diary ud WHERE ud.user_id=#{userId} AND ud.diary_id=#{diaryId}")
    UserDiaryDO findByUserIdDiaryId(@Param("userId") Long userId, @Param("diaryId") Long diaryId);

}
