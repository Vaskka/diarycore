package com.vaskka.diary.core.service;

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
    public DiaryWrapper findByAuthorAndDate(String authorId, String dateInStr) {
        LogUtil.infof(log, "[daily],find by {} and {}", authorId, dateInStr);
        var author = authorServiceImpl.findById(authorId);
        if (author == null) {
            LogUtil.errorf(log, "[daily],auth not exist,authId:{}", authorId);
            throw new AuthorNotExistException();
        }

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

    @Override
    public Diary findSubDiaryById(String diarySubId) {
        return buildDiary(diaryMapper.findById(Long.parseLong(diarySubId)));
    }

    @Override
    public List<Diary> simpleSearch(SearchCondition searchCondition) {
        var searchResult = diaryContentDAO.simpleSearch(searchCondition.getSearchText());
        return searchResult.stream()
                .filter(o -> searchCondition.getAuthorIdPicker().contains(o.getAuthorId()))
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
        diary.setSubTitle(diaryDO.getSubTitle());
        diary.setDiaryDateTimestamp(diaryDO.getDiaryDateTimestamp());
        diary.setDiaryDateTimeStr(CommonUtil.getDateStr(diary.getDiaryDateTimestamp()));
        diary.setStartPage(diaryDO.getStartPage());
        diary.setEndPage(diaryDO.getEndPage());
        diary.setOriginPic(diaryDO.getOriginPic());
        diary.setDiaryContent(diaryContentDAO.findById(String.valueOf(diaryDO.getId())).orElseThrow(EsException::new));
        diary.setExternParam(diaryDO.getExternParam());
        return diary;
    }

    private Diary buildFromDiaryContent(DiaryContent diaryContent) {
        return buildDiary(diaryMapper.findById(Long.parseLong(diaryContent.getDiaryId())));
    }
}
