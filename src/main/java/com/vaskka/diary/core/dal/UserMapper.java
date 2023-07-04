package com.vaskka.diary.core.dal;

import com.vaskka.diary.core.model.dataobject.UserDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select(value = "SELECT id, gmt_create, gmt_modified, " +
            "user_name, avatar_url, psw, extern_param " +
            "FROM user u where u.id=#{id}")
    UserDO findById(@Param("id") Long id);

    @Insert(value = "INSERT INTO user(id, gmt_create, gmt_modified, user_name, avatar_url, psw, extern_param) " +
            "VALUES (#{id}, #{gmtCreate, jdbcType=TIMESTAMP}, #{gmtModified, jdbcType=TIMESTAMP}, " +
            "#{userName}, #{avatarUrl}, #{psw}, #{externParam})")
    Integer insertUser(UserDO userDO);

    @Update(value = "UPDATE user SET user_name=#{userName}, avatar_url=#{avatarUrl} " +
            "WHERE id=#{id}")
    Integer updateUserInfo(@Param("id") Long id, @Param("userName") String userName, @Param("avatarUrl") String avatarUrl);
}
