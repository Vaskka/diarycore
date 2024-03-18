package com.vaskka.diary.core.dal;

import com.vaskka.diary.core.model.dataobject.UserSessionDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserSessionMapper {
    @Insert(value = "INSERT INTO user_session(id, gmt_create, gmt_modified, user_id, auth_token, expire_timestamp) " +
            "VALUES (#{id}, #{gmtCreate, jdbcType=TIMESTAMP}, #{gmtModified, jdbcType=TIMESTAMP}, " +
            "#{userId}, #{authToken}, #{expireTimestamp})")
    Integer insertUserIp(UserSessionDO userSessionDO);

    @Select(value = "select id, gmt_create, gmt_modified, user_id, auth_token, expire_timestamp " +
            "from user_session ust " +
            "where ust.auth_token=#{authToken} and ust.expire_timestamp>#{timestampGte}")
    List<UserSessionDO> findByAuthTokenAndExpireTimestampGte(@Param("authToken") String authToken, @Param("timestampGte") Long timestampGte);
}
