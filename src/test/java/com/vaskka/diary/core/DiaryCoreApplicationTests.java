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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

@Slf4j
//@SpringBootTest
// @ActiveProfiles("w")
class DiaryCoreApplicationTests {
//
//	@Autowired
//	private ElasticsearchClient elasticsearchClient;
//
//	@Test
//	void contextLoads() {
//	}
//
//	@Test
//	public void esTestInsertDiaryContent() throws IOException {
//		DiaryContent diaryContent_0 = new DiaryContent();
//		diaryContent_0.setDiaryId("3");
//		diaryContent_0.setContent("心为一生之本，培养己心即是培植其本。树木之本固，则枝叶繁茂而荫广远。人皆爱其阴，均来憩于其下，徘徊久之而不忍遽去。心若培养到极好之处，自有靡限之快乐纷至沓来，虽未到蓬莱胜境，而亦栩栩然不啻神仙矣。<br>培养己心之法，先从孝弟做起。尽孝尽弟，即是赤子之心。由此扩而充之，持己接物，只能忠恕，亦可攸往咸宜。人不孝弟，本心已漓其身，不久亦亡矣。<br>天晴微风。<br>来拜者十余人，皆言去冬无雪，已见旱形，粮价又涨，兼之火车由晋运量出境，腊月已多，若再运之不已，势必大涨。吾晋恐不免有饥馁之忧矣。");
//		IndexResponse response_0 = elasticsearchClient.index(i -> i
//				.index("diary_content_idx")
//				.id(diaryContent_0.getDiaryId())
//				.document(diaryContent_0)
//		);
//		log.info(response_0.toString());
//
//		DiaryContent diaryContent_1 = new DiaryContent();
//		diaryContent_1.setDiaryId("4");
//		diaryContent_1.setContent("刘邦治告，其县（清源）之八景：<br>中隐环青（即小峪寺）；西岭香严（在马峪）；白石云松（在白石沟）；米阳晚渡（城东十里汾河）；平泉流碧（即不老泉）；东湖夜月（城中）；青堆烟草（在城南十五里）；陶唐遗迹（在县东南三十里名曰尧城）。<br>清源小峪寺八景<br>茶烟流润；洞角朝阳；角楼挂月（宛作禅林图画内）；虎洞龙蟠；泡泉喷云；乳水九源（以上三景寺外）；寿岭五云；凤冈晴霭。");
//		IndexResponse response_1 = elasticsearchClient.index(i -> i
//				.index("diary_content_idx")
//				.id(diaryContent_1.getDiaryId())
//				.document(diaryContent_1)
//		);
//		log.info(response_1.toString());
//
////		DiaryContent diaryContent_2 = new DiaryContent();
////		diaryContent_2.setDiaryId("1");
////		diaryContent_2.setContent("晴。掌灯抹牌。");
////		IndexResponse response_2 = elasticsearchClient.index(i -> i
////				.index("diary_content_idx")
////				.id(diaryContent_2.getDiaryId())
////				.document(diaryContent_2)
////		);
////		log.info(response_2.toString());
//
//	}
//
//	@Test
//	public void esFindById() throws IOException {
//		GetResponse<Author> response = elasticsearchClient.get(g -> g
//						.index("author_demo")
//						.id("ef8f363e-0032-43af-8163-0352edc180f2"),
//				Author.class
//		);
//		assert response.source() != null;
//		log.info(response.source().toString());
//	}
//
//
//	@Test
//	public void esSearch() throws IOException {
//		String searchText = "宗旨";
//
//		SearchResponse<DiaryContent> response = elasticsearchClient.search(s -> s
//						.index("diary_content_test")
//						.query(q -> q
//								.match(t -> t
//										.field("content")
//										.query(searchText)
//								)
//						),
//				DiaryContent.class
//		);
//
//		TotalHits total = response.hits().total();
//		assert total != null;
//		boolean isExactResult = total.relation() == TotalHitsRelation.Eq;
//
//		if (isExactResult) {
//			log.info("There are " + total.value() + " results");
//		} else {
//			log.info("There are more than " + total.value() + " results");
//		}
//
//		List<Hit<DiaryContent>> hits = response.hits().hits();
//		for (Hit<DiaryContent> hit: hits) {
//			DiaryContent content = hit.source();
//			// log.info("highLight:{}", JSONObject.toJSONString(hit.highlight()));
//			log.info("Found product " + content + ", score " + hit.score());
//		}
//	}
//
//	@Test
//	public void dateTest() {
//		Assertions.assertEquals("1969-12-29 23:36:35.889", CommonUtil.getDateTimeDetailStr(-203004111));
//	}


}
