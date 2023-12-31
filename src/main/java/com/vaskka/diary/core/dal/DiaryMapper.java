package com.vaskka.diary.core.dal;

import com.vaskka.diary.core.model.dataobject.DiaryDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiaryMapper {

    @Insert(value = "INSERT INTO diary(id, gmt_create, gmt_modified, author_id, diary_title, sub_title, diary_date_timestamp, start_page, end_page, origin_pic, comment, extern_param) " +
            "VALUES (#{id}, #{gmtCreate, jdbcType=TIMESTAMP}, #{gmtModified, jdbcType=TIMESTAMP}, " +
            "#{authorId}, #{diaryTitle}, #{subTitle}, #{diary_date_timestamp}, #{startPage}, #{endPage}, #{originPic}, #{comment}, #{externParam})")
    Integer insertDiary(DiaryDO diaryDO);

    @Select(value = "SELECT id, gmt_create, gmt_modified, author_id, diary_title, sub_title, diary_date_timestamp, start_page, end_page, origin_pic, comment, extern_param " +
            "FROM diary di " +
            "WHERE di.id=#{id}")
    DiaryDO findById(@Param("id") Long id);

    @Select(value = "SELECT id, gmt_create, gmt_modified, author_id, diary_title, sub_title, diary_date_timestamp, start_page, end_page, origin_pic, comment, extern_param " +
            "FROM diary di " +
            "WHERE di.author_id=#{authorId} " +
            "ORDER BY diary_date_timestamp")
    List<DiaryDO> findByAuthorId(@Param("authorId") Long authorId);

    @Select(value = "SELECT id, gmt_create, gmt_modified, author_id, diary_title, sub_title, diary_date_timestamp, start_page, end_page, origin_pic, comment, extern_param " +
            "FROM diary di " +
            "WHERE di.author_id=#{authorId} " +
            "ORDER BY diary_date_timestamp LIMIT #{size} OFFSET #{offset} ")
    List<DiaryDO> findByAuthorIdPageable(@Param("authorId") Long authorId, @Param("offset") Integer offset, @Param("size") Integer size);


    @Select(value = "SELECT diary_date_timestamp " +
            "FROM diary di " +
            "WHERE di.author_id=#{authorId} " +
            "ORDER BY diary_date_timestamp LIMIT 1")
    Long findDateFirst(@Param("authorId") String authorId);

    @Select(value = "SELECT diary_date_timestamp " +
            "FROM diary di " +
            "WHERE di.author_id=#{authorId} " +
            "ORDER BY diary_date_timestamp DESC LIMIT 1")
    Long findDateLatest(@Param("authorId") String authorId);

    @Select(value = "SELECT diary_date_timestamp " +
            "FROM diary di " +
            "WHERE di.diary_date_timestamp < #{timestamp} " +
            "ORDER BY diary_date_timestamp DESC LIMIT 1")
    Long findPreTimestamp(@Param("timestamp") Long timestamp);

    @Select(value = "SELECT diary_date_timestamp " +
            "FROM diary di " +
            "WHERE di.diary_date_timestamp > #{timestamp} " +
            "ORDER BY diary_date_timestamp LIMIT 1")
    Long findNextTimestamp(@Param("timestamp") Long timestamp);
}
