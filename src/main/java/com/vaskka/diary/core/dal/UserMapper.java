package com.vaskka.diary.core.dal;

import com.vaskka.diary.core.model.dataobject.UserDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select(value = "SELECT id, gmt_create, gmt_modified, " +
            "user_name, ips, user_type, psw, extern_param " +
            "FROM user u where u.id=#{id}")
    UserDO findById(@Param("id") Long id);

    @Select(value = "SELECT id, gmt_create, gmt_modified, " +
            "user_name, ips, user_type, psw, extern_param " +
            "FROM user u where u.user_name=#{userName}")
    UserDO findByUserName(@Param("userName") String userName);

    @Insert(value = "INSERT INTO user(id, gmt_create, gmt_modified, user_name, ips, user_type, psw, extern_param) " +
            "VALUES (#{id}, #{gmtCreate, jdbcType=TIMESTAMP}, #{gmtModified, jdbcType=TIMESTAMP}, " +
            "#{userName}, #{ips}, #{userType}, #{psw}, #{externParam})")
    Integer insertUser(UserDO userDO);

    @Update(value = "UPDATE user SET user_name=#{userName} " +
            "WHERE id=#{id}")
    Integer updateUserName(@Param("id") Long id, @Param("userName") String userName);
}
