package com.vaskka.diary.core.service;

import com.google.common.collect.Lists;
import com.vaskka.diary.core.dal.DiaryContentDAO;
import com.vaskka.diary.core.dal.DiaryMapper;
import com.vaskka.diary.core.exceptions.AuthorNotExistException;
import com.vaskka.diary.core.exceptions.EsException;
import com.vaskka.diary.core.facade.AuthorFacade;
import com.vaskka.diary.core.facade.DiaryFacade;
import com.vaskka.diary.core.model.bizobject.*;
import com.vaskka.diary.core.model.dataobject.DiaryDO;
import com.vaskka.diary.core.utils.CommonUtil;
import com.vaskka.diary.core.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service("diaryServiceImpl")
public class DiaryServiceImpl implements DiaryFacade {

    @Resource
    private DiaryMapper diaryMapper;

    @Resource
    private DiaryContentDAO diaryContentDAO;

    @Resource
    private AuthorFacade authorServiceImpl;

    @Override
    public DiaryWrapper findByAuthor(String authorId) {
        LogUtil.infof(log, "[daily],find by {}", authorId);
        var author = authorServiceImpl.findById(authorId);
        if (author == null) {
            LogUtil.errorf(log, "[daily],auth not exist,authId:{}", authorId);
            throw new AuthorNotExistException();
        }

        List<DiaryDO> diaryDOList = diaryMapper.findByAuthorId(Long.parseLong(author.getAuthorId()));
        return buildDiaryWrapper(diaryDOList, author);
    }

    @Override
    public DiaryWrapper findByAuthorPageable(String authorId, Integer page, Integer size) {
        LogUtil.infof(log, "[daily],find by pageable {}", authorId);
        var author = authorServiceImpl.findById(authorId);
        if (author == null) {
            LogUtil.errorf(log, "[daily],auth not exist,authId:{}", authorId);
            throw new AuthorNotExistException();
        }

        List<DiaryDO> diaryDOList = diaryMapper.findByAuthorIdPageable(Long.parseLong(author.getAuthorId()), size * page, size);
        return buildDiaryWrapper(diaryDOList, author);
    }

    @Override
    public DiaryWrapper findByAuthorAndDate(String authorId, String dateInStr) {
        LogUtil.infof(log, "[daily],find by {} and {}", authorId, dateInStr);
        var author = authorServiceImpl.findById(authorId);
        if (author == null) {
            LogUtil.errorf(log, "[daily],auth not exist,authId:{}", authorId);
            throw new AuthorNotExistException();
        }

        if (dateInStr.split("-").length == 2) {
            var timestampList = CommonUtil.getDateStrListFromDateStrMonth(dateInStr);
            // 整月浏览
            List<DiaryDO> diaryDOList = diaryMapper.findByAuthorId(Long.parseLong(author.getAuthorId()))
                    .stream()
                    .filter(diaryDO -> {
                        String indb = CommonUtil.getDateStr(diaryDO.getDiaryDateTimestamp());
                        LogUtil.infof(log, "[compare],whole month,incomming={},indb={}", timestampList, indb);
                        return timestampList.contains(indb);
                    })
                    .collect(Collectors.toList());
            return buildDiaryWrapper(diaryDOList, author);
        } else {
            List<DiaryDO> diaryDOList = diaryMapper.findByAuthorId(Long.parseLong(author.getAuthorId()))
                    .stream()
                    .filter(diaryDO -> {
                        long incomm = CommonUtil.parseStrDate2Timestamp(dateInStr);
                        long indb = diaryDO.getDiaryDateTimestamp();
                        LogUtil.infof(log, "[compare],incomming={},indb={}", incomm, indb);
                        return Objects.equals(indb, incomm);
                    })
                    .collect(Collectors.toList());
            return buildDiaryWrapper(diaryDOList, author);
        }
    }

    @Override
    public List<String> findDiaryBetween(String authorId) {
        List<String> res = new ArrayList<>();
        String start = CommonUtil.getDateStr(diaryMapper.findDateFirst(Long.parseLong(authorId))).split("-")[0];
        String end = CommonUtil.getDateStr(diaryMapper.findDateLatest(Long.parseLong(authorId))).split("-")[0];
        for (int i = Integer.parseInt(start); i <= Integer.parseInt(end); i++) {
            res.add(String.valueOf(i));
        }

        return res;
    }

    @Override
    public List<String> findDateTimeByAuthor(String authorId) {
        return diaryMapper.findByAuthorId(Long.valueOf(authorId))
                .stream()
                .map(DO -> CommonUtil.getDateStr(DO.getDiaryDateTimestamp()))
                .collect(Collectors.toList());
    }

    @Override
    public Diary findSubDiaryById(String diarySubId) {
        return buildDiary(diaryMapper.findById(Long.parseLong(diarySubId)));
    }

    @Override
    public List<Diary> simpleSearch(SearchCondition searchCondition) {
        var searchResult = diaryContentDAO.simpleSearch(searchCondition.getSearchText());
        return searchResult.stream()
                .filter(o -> searchCondition.getAuthorIdPicker().contains(o.getAuthorId()))
                .filter(o -> diaryMapper.findById(Long.parseLong(o.getDiaryId())) != null)
                .map(this::buildFromDiaryContent)
                .collect(Collectors.toList());
    }

    private DiaryWrapper buildDiaryWrapper(List<DiaryDO> diaryDOList, Author author) {
        DiaryWrapper diaryWrapper = new DiaryWrapper();
        diaryWrapper.setDiaryTitle(author.getAuthorName() + "日记");
        diaryWrapper.setAuthor(author);
        diaryWrapper.setDiaryList(diaryDOList.stream()
                .map(this::buildDiary)
                .collect(Collectors.toList()));
        return diaryWrapper;
    }

    private Diary buildDiary(DiaryDO diaryDO) {
        Diary diary = new Diary();
        diary.setAuthorId(String.valueOf(diaryDO.getAuthorId()));
        diary.setAuthorName(authorServiceImpl.findById(String.valueOf(diaryDO.getAuthorId())).getAuthorName());
        diary.setDiaryId(String.valueOf(diaryDO.getId()));
        diary.setTitle(diaryDO.getDiaryTitle());
        diary.setSubTitle(diaryDO.getSubTitle());
        diary.setDiaryDateTimestamp(diaryDO.getDiaryDateTimestamp());
        diary.setDiaryDateTimeStr(CommonUtil.getDateStr(diary.getDiaryDateTimestamp()));
        diary.setStartPage(diaryDO.getStartPage());
        diary.setEndPage(diaryDO.getEndPage());
        diary.setOriginPic(diaryDO.getOriginPic());
        diary.setPreDateStr(findPreDateStr(diaryDO));
        diary.setNextDateStr(findNextDateStr(diaryDO));
        diary.setDiaryContent(diaryContentDAO.findById(String.valueOf(diaryDO.getId())).orElseThrow(EsException::new));
        diary.setExternParam(diaryDO.getExternParam());
        return diary;
    }

    private String findPreDateStr(DiaryDO diaryDO) {
        var pre = diaryMapper.findPreTimestamp(diaryDO.getDiaryDateTimestamp());
        if (pre == null) {
            return null;
        }

        return CommonUtil.getDateStr(pre);
    }

    private String findNextDateStr(DiaryDO diaryDO) {
        var next = diaryMapper.findNextTimestamp(diaryDO.getDiaryDateTimestamp());
        if (next == null) {
            return null;
        }
        return CommonUtil.getDateStr(next);
    }


    private Diary buildFromDiaryContent(DiaryContent diaryContent) {
        return buildDiary(diaryMapper.findById(Long.parseLong(diaryContent.getDiaryId())));
    }
}
