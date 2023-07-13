package com.vaskka.diary.core;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.vaskka.diary.core.model.bizobject.Author;
import com.vaskka.diary.core.model.bizobject.DiaryContent;
import com.vaskka.diary.core.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@Slf4j
@SpringBootTest
class DiaryCoreApplicationTests {

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	@Test
	void contextLoads() {
	}

	@Test
	public void esTestInsertDiaryContent() throws IOException {
		DiaryContent diaryContent_0 = new DiaryContent();
		diaryContent_0.setDiaryId(CommonUtil.getRandom32LengthStr());
		diaryContent_0.setContent("来拜者十八人，午餐者三人。<br>凡来拜者，皆言年月不佳，世困民穷，四民均失其业，不知今岁又将何如也。");
		IndexResponse response_0 = elasticsearchClient.index(i -> i
				.index("diary_content_test")
				.id(diaryContent_0.getDiaryId())
				.document(diaryContent_0)
		);
		log.info(response_0.toString());

		DiaryContent diaryContent_1 = new DiaryContent();
		diaryContent_1.setDiaryId(CommonUtil.getRandom32LengthStr());
		diaryContent_1.setContent("人心不正，莫甚于斯时。至学堂之学生，尤不正之至者矣，学生所学，一以西人之学为宗旨，无父无君，皆习为固然，故入革命党者十居八九，时局不甚可畏哉！");
		IndexResponse response_1 = elasticsearchClient.index(i -> i
				.index("diary_content_test")
				.id(diaryContent_1.getDiaryId())
				.document(diaryContent_1)
		);
		log.info(response_1.toString());

	}

	@Test
	public void esFindById() throws IOException {
		GetResponse<Author> response = elasticsearchClient.get(g -> g
						.index("author_demo")
						.id("ef8f363e-0032-43af-8163-0352edc180f2"),
				Author.class
		);
		assert response.source() != null;
		log.info(response.source().toString());
	}


	@Test
	public void esSearch() throws IOException {
		String searchText = "宗旨";

		SearchResponse<DiaryContent> response = elasticsearchClient.search(s -> s
						.index("diary_content_test")
						.query(q -> q
								.match(t -> t
										.field("content")
										.query(searchText)
								)
						),
				DiaryContent.class
		);

		TotalHits total = response.hits().total();
		assert total != null;
		boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

		if (isExactResult) {
			log.info("There are " + total.value() + " results");
		} else {
			log.info("There are more than " + total.value() + " results");
		}

		List<Hit<DiaryContent>> hits = response.hits().hits();
		for (Hit<DiaryContent> hit: hits) {
			DiaryContent content = hit.source();
			// log.info("highLight:{}", JSONObject.toJSONString(hit.highlight()));
			log.info("Found product " + content + ", score " + hit.score());
		}
	}


}
