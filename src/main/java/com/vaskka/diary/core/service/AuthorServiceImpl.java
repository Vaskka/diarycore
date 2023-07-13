package com.vaskka.diary.core.service;

import com.vaskka.diary.core.constant.BizPart;
import com.vaskka.diary.core.dal.AuthorMapper;
import com.vaskka.diary.core.facade.AuthorFacade;
import com.vaskka.diary.core.model.bizobject.Author;
import com.vaskka.diary.core.model.dataobject.AuthorDO;
import com.vaskka.diary.core.utils.LogUtil;
import com.vaskka.diary.core.utils.uid.UnionIdProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("authorServiceImpl")
public class AuthorServiceImpl implements AuthorFacade {

    @Resource
    private AuthorMapper authorMapper;

    @Resource
    private UnionIdProcessor uidComponent;

    @Override
    public List<Author> findAll() {
        return authorMapper.findAll()
                .stream()
                .map(this::buildAuthor)
                .collect(Collectors.toList());
    }

    @Override
    public Author findById(String authorId) {
        return buildAuthor(authorMapper.findById(Long.parseLong(authorId)));
    }

    @Override
    public void addAuthor(Author author) {
        Integer effect = authorMapper.insertAuthor(buildAuthorDO(author));
        LogUtil.infof(log, "[author],insert,effect:{}", effect);
    }

    private Author buildAuthor(AuthorDO authorDO) {
        Author author = new Author();
        author.setAuthorId(String.valueOf(authorDO.getId()));
        author.setAuthorName(authorDO.getAuthorName());
        author.setAuthorAvatarUrl(authorDO.getAuthorAvatarUrl());
        author.setExternParam(authorDO.getExternParam());
        return author;
    }

    private AuthorDO buildAuthorDO(Author author) {
        AuthorDO authorDO = new AuthorDO();
        var now = LocalDateTime.now();
        authorDO.setId(uidComponent.getUnionId(BizPart.AUTHOR));
        authorDO.setGmtCreate(now);
        authorDO.setGmtModified(now);
        authorDO.setAuthorName(author.getAuthorName());
        authorDO.setAuthorAvatarUrl(author.getAuthorAvatarUrl());
        authorDO.setExternParam(author.getExternParam());
        return authorDO;
    }
}
