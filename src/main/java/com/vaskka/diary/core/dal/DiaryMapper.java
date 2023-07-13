package com.vaskka.diary.core.dal;

import com.vaskka.diary.core.model.dataobject.DiaryDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiaryMapper {

    @Insert(value = "INSERT INTO diary(id, gmt_create, gmt_modified, author_id, diary_title, sub_title, diary_date_timestamp, start_page, end_page, origin_pic, extern_param) " +
            "VALUES (#{id}, #{gmtCreate, jdbcType=TIMESTAMP}, #{gmtModified, jdbcType=TIMESTAMP}, " +
            "#{authorId}, #{diaryTitle}, #{subTitle}, #{diary_date_timestamp}, #{startPage}, #{endPage}, #{originPic}, #{externParam})")
    Integer insertDiary(DiaryDO diaryDO);

    @Select(value = "SELECT id, gmt_create, gmt_modified, author_id, diary_title, sub_title, diary_date_timestamp, start_page, end_page, origin_pic, extern_param " +
            "FROM diary di where di.id=#{id}")
    DiaryDO findById(@Param("id") Long id);

    @Select(value = "SELECT id, gmt_create, gmt_modified, author_id, diary_title, sub_title, diary_date_timestamp, start_page, end_page, origin_pic, extern_param " +
            "FROM diary di where di.author_id=#{authorId}")
    List<DiaryDO> findByAuthorId(@Param("authorId") Long authorId);
}
