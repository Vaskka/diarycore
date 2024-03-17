package com.vaskka.diary.core.dal;

import com.vaskka.diary.core.model.dataobject.UserIpDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserIpMapper {

    @Insert(value = "INSERT INTO user_ip(id, gmt_create, gmt_modified, ip, ref_user_id) " +
            "VALUES (#{id}, #{gmtCreate, jdbcType=TIMESTAMP}, #{gmtModified, jdbcType=TIMESTAMP}, " +
            "#{ip}, #{refUserId})")
    Integer insertUserIp(UserIpDO userIpDO);

    @Select(value = "SELECT id, gmt_create, gmt_modified, " +
            "ip, ref_user_id " +
            "FROM user_ip ui WHERE ui.ip=#{ip}")
    UserIpDO findByIp(@Param("ip") String ip);

    @Select(value = "SELECT id, gmt_create, gmt_modified, " +
            "ip, ref_user_id " +
            "FROM user_ip ui where ui.ref_user_id=#{ref_user_id}")
    List<UserIpDO> findByRefUserId(@Param("ref_user_id") Long userId);

    @Delete(value = "DELETE FROM user_ip ui WHERE ui.ip=#{ip}")
    Integer deleteByIp(@Param("ip") String ip);
}
