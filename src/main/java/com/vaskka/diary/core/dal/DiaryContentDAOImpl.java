package com.vaskka.diary.core.dal;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.google.common.collect.Lists;
import com.vaskka.diary.core.exceptions.EsException;
import com.vaskka.diary.core.model.bizobject.DiaryContent;
import com.vaskka.diary.core.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("diaryContentDAO")
public class DiaryContentDAOImpl implements DiaryContentDAO {

    private static final String DIARY_CONTENT_INDEX = "diaryContentIdx";

    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Override
    public void insertDiaryContent(DiaryContent diaryContent) {
        LogUtil.infof(log, "[es],insert,data={}", diaryContent);
        try {
            IndexResponse response = elasticsearchClient.index(i -> i
                    .index(DIARY_CONTENT_INDEX)
                    .id(diaryContent.getDiaryId())
                    .document(diaryContent)
            );

            if (response != null && response.result() != null && Result.Created.equals(response.result())) {
                LogUtil.infof(log, "[es],insert,success");
            } else {
                LogUtil.errorf(log, "[es],insert,fail,diaryId={}", diaryContent.getDiaryId());
            }
        } catch (Exception e) {
            LogUtil.errorf(log, "[es],insert,error,", e);
            throw new EsException(e);
        }
    }

    @Override
    public Optional<DiaryContent> findById(String diaryId) {
        GetResponse<DiaryContent> response = null;
        try {
            response = elasticsearchClient.get(g -> g
                            .index(DIARY_CONTENT_INDEX)
                            .id(diaryId),
                    DiaryContent.class
            );
            if (response != null && response.source() != null) {
                return Optional.of(response.source());
            }

            LogUtil.errorf(log, "[es],findById,notFound,diaryId={}", diaryId);
            return Optional.empty();
        } catch (Exception e) {
            LogUtil.errorf(log, "[es],findById,error,", e);
            return Optional.empty();
        }
    }

    @Override
    public List<DiaryContent> simpleSearch(String searchText) {
        var res = new ArrayList<DiaryContent>();
        SearchResponse<DiaryContent> response;
        try {
            response = elasticsearchClient.search(s -> s
                            .index(DIARY_CONTENT_INDEX)
                            .query(q -> q
                                    .match(t -> t
                                            .field("content")
                                            .query(searchText)
                                    )
                            ),
                    DiaryContent.class
            );

            if (response == null || response.hits() == null || response.hits().hits() == null) {
                LogUtil.errorf(log, "[es],simpleSearch,error,response is null,searchText={}", searchText);
                return Lists.newArrayList();
            }

            List<Hit<DiaryContent>> hits = response.hits().hits();
            for (Hit<DiaryContent> hit: hits) {
                DiaryContent content = hit.source();
                if (content != null) {
                    log.info("[es],simpleSearch,found,score={}", hit.score());
                    res.add(content);
                }
            }
            return res;
        } catch (Exception e) {
            LogUtil.errorf(log, "[es],simpleSearch,error,", e);
            return Lists.newArrayList();
        }

    }
}
