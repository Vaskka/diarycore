package com.vaskka.diary.core.dal;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.UpdateAction;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.google.common.collect.Lists;
import com.vaskka.diary.core.exceptions.EsException;
import com.vaskka.diary.core.model.bizobject.DiaryContent;
import com.vaskka.diary.core.model.bizobject.EsSearchResult;
import com.vaskka.diary.core.model.bizobject.SearchCondition;
import com.vaskka.diary.core.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("diaryContentDAO")
public class DiaryContentDAOImpl implements DiaryContentDAO {

    private static final String DIARY_CONTENT_INDEX = "prod_diary_idx";

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

            return processEsResult(res, response);
        } catch (Exception e) {
            LogUtil.errorf(log, "[es],simpleSearch,error,", e);
            return Lists.newArrayList();
        }

    }

    @Override
    public EsSearchResult searchPageable(String searchText, Integer size, Integer page) {
        var res = new EsSearchResult();
        res.setData(new ArrayList<>());
        res.setTotalPage(0);
        res.setTotalRecordCount(0L);
        res.setSizeOfPage(page);
        SearchResponse<DiaryContent> response;
        try {
            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index(DIARY_CONTENT_INDEX)
                    .query(q -> q.
                            matchPhrase(t -> t
                                .field("content")
                                .query(searchText)))
                    .aggregations("countPerYear", a -> a
                            .dateHistogram(d -> d
                                .field("timestamp")
                                .calendarInterval(CalendarInterval.Year).format("yyyy")))
                    .from(size * page)
                    .size(size)
                    .build();
            response = elasticsearchClient.search(searchRequest, DiaryContent.class);

            if (response == null || response.hits() == null || response.hits().hits() == null) {
                LogUtil.errorf(log, "[es],search pageable,error,response is null,searchText={}", searchText);
                return res;
            }

            return processEsResult(res, response);
        } catch (Exception e) {
            LogUtil.errorf(log, "[es],search pageable,error,", e);
            return res;
        }
    }

    @Override
    public EsSearchResult searchV3(SearchCondition searchCondition) {
        LogUtil.infof(log, "[searchV3],DAO,condition:{}", searchCondition);
        var res = new EsSearchResult();
        res.setData(new ArrayList<>());
        res.setTotalPage(0);
        res.setTotalRecordCount(0L);
        res.setSizeOfPage(searchCondition.getSize());
        if (searchCondition.getUser() == null) {
            // 用户不存在不走查询
            LogUtil.infof(log, "[searchV3],user not exist.");
            return res;
        }

        SearchResponse<DiaryContent> response;
        try {
            // 搜索关键词query
            List<Query> searchTextList = new ArrayList<>();
            for (var searchText : searchCondition.getMultiSearchText()) {
                var byContentTextQuery = MatchQuery.of(m -> m
                        .field("content")
                        .query(searchText)
                )._toQuery();
                searchTextList.add(byContentTextQuery);
            }

            // 时间限定
            Query byDateRange = null;
            if (searchCondition.getTimestampRange() != null) {
                var dateRangeBuilder = new RangeQuery.Builder()
                        .field("timestamp");
                if (searchCondition.getTimestampRange().getGte() != null) {
                    dateRangeBuilder.gte(JsonData.of(searchCondition.getTimestampRange().getGte()));
                }
                if (searchCondition.getTimestampRange().getLte() != null) {
                    dateRangeBuilder.lte(JsonData.of(searchCondition.getTimestampRange().getLte()));
                }
                byDateRange = RangeQuery.of(r -> dateRangeBuilder)._toQuery();
            }

            // 作者限定
            List<Query> authorQueryList = new ArrayList<>();
            if (searchCondition.getAuthorIdPicker() != null && !searchCondition.getAuthorIdPicker().isAllSelect()) {
                for (var authorId : searchCondition.getAuthorIdPicker().getPickContent()) {
                    var byAuthorId = MatchQuery.of(m -> m
                            .field("authorId")
                            .query(authorId)
                    )._toQuery();
                    authorQueryList.add(byAuthorId);
                }
            }

            // authorId反选
            Query authorIdNotInQuery = null;
            if (searchCondition.getAuthorIdNotIn() != null) {
                authorIdNotInQuery = TermsQuery.of(m -> m
                        .field("authorId")
                        .terms(t -> t
                                .value(searchCondition.getAuthorIdNotIn()
                                        .stream()
                                        .map(FieldValue::of)
                                        .collect(Collectors.toList())))
                )._toQuery();
            }

            // user 权限限定
            Query userAuthQuery = TermsQuery.of(m -> m
                    .field("userIdList")
                    .terms(t -> t
                            .value(Lists.newArrayList(FieldValue.of(searchCondition.getUser().getUid()))))
            )._toQuery();

            // 整合最后的query
            List<Query> finalQuery = new ArrayList<>(searchTextList);
            if (byDateRange != null) {
                finalQuery.add(byDateRange);
            }
            finalQuery.addAll(authorQueryList);

            // 整合反选query
            List<Query> finalQueryNotIn = new ArrayList<>();
            if (authorIdNotInQuery != null) {
                finalQueryNotIn.add(authorIdNotInQuery);
            }

            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index(DIARY_CONTENT_INDEX)
                    .query(q -> q
                            .bool(b -> b
                                    .must(finalQuery)
                                    .mustNot(finalQueryNotIn)
                                    .should(userAuthQuery)
                            )
                    )
                    .aggregations("countPerYear", a -> a
                            .dateHistogram(d -> d
                                    .field("timestamp")
                                    .calendarInterval(CalendarInterval.Year)
                                    .format("yyyy")))
                    .aggregations("countPerMonth", a -> a
                            .dateHistogram(d -> d
                                    .field("timestamp")
                                    .calendarInterval(CalendarInterval.Month)
                                    .format("yyyy-MM")))
                    .aggregations("authorCount", a -> a
                            .terms(s -> s
                                    .field("authorId")))
                    .from(searchCondition.getSize() * searchCondition.getPage())
                    .size(searchCondition.getSize())
                    .build();

            response = elasticsearchClient.search(searchRequest, DiaryContent.class);

            if (response == null || response.hits() == null || response.hits().hits() == null) {
                LogUtil.errorf(log, "[es],searchV3,error,response is null,search={}", searchCondition);
                return res;
            }

            return processEsResult(res, response);
        } catch (Exception e) {
            LogUtil.errorf(log, "[es],search pageable,error,", e);
            return res;
        }
    }

    @Override
    public void updateUserIdListByDiaryId(List<String> userIdList, List<String> diaryIds) {
        try {
            var diaryContentDOForUpdate = new DiaryContent();
            diaryContentDOForUpdate.setUserIdList(userIdList);
            var bulkResponse = elasticsearchClient.bulk(b -> b
                    .index(DIARY_CONTENT_INDEX)
                    .operations(diaryIds.stream()
                            .map(diaryId -> BulkOperation.of(
                                bulkOpBuilder -> bulkOpBuilder
                                        .update(u -> u
                                                .id(diaryId)
                                                .index(DIARY_CONTENT_INDEX)
                                                .action(UpdateAction.of(uab -> uab
                                                        .doc(diaryContentDOForUpdate))))
                            ))
                            .collect(Collectors.toList())
                    )
            );
            LogUtil.infof(log, "[updateUserIdListByDiaryId],esResp:{}", bulkResponse);
        } catch (Exception e) {
            LogUtil.errorf(log, "[updateUserIdListByDiaryId] ex", e);
            throw new RuntimeException(e);
        }
    }

    private List<DiaryContent> processEsResult(List<DiaryContent> res, SearchResponse<DiaryContent> response) {

        List<Hit<DiaryContent>> hits = response.hits().hits();
        for (Hit<DiaryContent> hit: hits) {
            DiaryContent content = hit.source();
            if (content != null) {
                // log.info("[es],simpleSearch,found,score={}", hit.score());
                res.add(content);
            }
        }
        return res;
    }

    private EsSearchResult processEsResult(EsSearchResult res, SearchResponse<DiaryContent> response) {
        List<Hit<DiaryContent>> hits = response.hits().hits();
        for (Hit<DiaryContent> hit: hits) {
            DiaryContent content = hit.source();
            if (content != null) {
                // log.info("[es],simpleSearch,found,score={}", hit.score());
                res.getData().add(content);
            }
        }
        // 获取总文档数
        assert response.hits().total() != null;
        long totalHits = response.hits().total().value();

        // 计算总页数
        int totalPages = (int) Math.ceil((double) totalHits / hits.size());

        res.setTotalPage(totalPages);
        res.setTotalRecordCount(totalHits);

        // 统计聚合年结果
        List<DateHistogramBucket> bucketsDate = response.aggregations()
                .get("countPerYear")
                .dateHistogram()
                .buckets()
                .array();
        List<EsSearchResult.DateWithCountResult> aggDateWithCount = new ArrayList<>();
        for (DateHistogramBucket bucket: bucketsDate) {
            EsSearchResult.DateWithCountResult dateWithCountResult = new EsSearchResult.DateWithCountResult();
            dateWithCountResult.setCount(bucket.docCount());
            dateWithCountResult.setDate(bucket.keyAsString());
            aggDateWithCount.add(dateWithCountResult);
        }
        res.setAggDateWithCount(aggDateWithCount);

        // 总计聚合月结果
        List<DateHistogramBucket> bucketsDateMonth = response.aggregations()
                .get("countPerMonth")
                .dateHistogram()
                .buckets()
                .array();
        List<EsSearchResult.DateWithCountResult> aggMonthWithCount = new ArrayList<>();
        for (DateHistogramBucket bucket: bucketsDateMonth) {
            EsSearchResult.DateWithCountResult dateWithCountResult = new EsSearchResult.DateWithCountResult();
            dateWithCountResult.setCount(bucket.docCount());
            dateWithCountResult.setDate(bucket.keyAsString());
            aggMonthWithCount.add(dateWithCountResult);
        }
        res.setAggDateWithCountMonth(aggMonthWithCount);

        // 聚合作者
        var bucketsAuthor = response.aggregations()
                .get("authorCount")
                .sterms()
                .buckets()
                .array();
        Map<String, Long> authorCount = new HashMap<>();
        for (var bucket: bucketsAuthor) {
            authorCount.put(bucket.key().stringValue(), bucket.docCount());
        }
        res.setAggAuthorCount(authorCount);
        return res;
    }
}
