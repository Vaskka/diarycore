package com.vaskka.diary.core.dal;

import com.vaskka.diary.core.model.dataobject.AuthorDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthorMapper {

    @Insert(value = "INSERT INTO author(id, gmt_create, gmt_modified, author_name, author_avatar_url, extern_param) " +
            "VALUES (#{id}, #{gmtCreate, jdbcType=TIMESTAMP}, #{gmtModified, jdbcType=TIMESTAMP}, " +
            "#{authorName}, #{authorAvatarUrl}, #{externParam})")
    Integer insertAuthor(AuthorDO authorDO);

    @Select(value = "SELECT id, gmt_create, gmt_modified, author_name, author_avatar_url, extern_param " +
            "FROM author au where au.id=#{id}")
    AuthorDO findById(@Param("id") Long id);

    @Select(value = "SELECT id, gmt_create, gmt_modified, author_name, author_avatar_url, extern_param FROM author")
    List<AuthorDO> findAll();
}
