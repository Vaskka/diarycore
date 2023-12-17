package com.vaskka.diary.core.dal;

import com.vaskka.diary.core.model.dataobject.AuthorTypeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthorTypeMapper {

    @Select(value = "SELECT DISTINCT at.author_type_name from author_type at")
    List<String> getAuthorTypeNameDisc();

    @Select(value = "SELECT id, gmt_create, gmt_modified, author_id, author_type_name, extern_param " +
            "FROM author_type at WHERE at.author_type_name=#{authorTypeName}")
    List<AuthorTypeDO> getAuthorTypeByName(@Param("authorTypeName") String authorTypeName);

}
