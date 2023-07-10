package com.vaskka.diary.core;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.vaskka.diary.core.model.bizobject.Author;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
class DiaryCoreApplicationTests {

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	@Test
	void contextLoads() {
	}

	@Test
	public void esTestInsertOne() throws IOException {
		Author author = new Author();
		author.setAuthorName("测试authorName"+ System.currentTimeMillis());
		author.setAuthorId(UUID.randomUUID().toString());
		author.setAuthorAvatarUrl("ceyyasysbahsa-" + System.currentTimeMillis());
		IndexResponse response = elasticsearchClient.index(i -> i
				.index("author_demo")
				.id(author.getAuthorId())
				.document(author)
		);

		log.info(response.toString());

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
		String searchText = "ceyyasysbahsa-1689002966864";

		SearchResponse<Author> response = elasticsearchClient.search(s -> s
						.index("author_demo")
						.query(q -> q
								.match(t -> t
										.field("authorAvatarUrl")
										.query(searchText)
								)
						),
				Author.class
		);

		TotalHits total = response.hits().total();
		assert total != null;
		boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

		if (isExactResult) {
			log.info("There are " + total.value() + " results");
		} else {
			log.info("There are more than " + total.value() + " results");
		}

		List<Hit<Author>> hits = response.hits().hits();
		for (Hit<Author> hit: hits) {
			Author product = hit.source();
			assert product != null;
			log.info("Found product " + product + ", score " + hit.score());
		}
	}


}
