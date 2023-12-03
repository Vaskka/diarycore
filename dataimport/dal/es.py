from elasticsearch import Elasticsearch


es = Elasticsearch(
            "https://localhost:9200",
            basic_auth=('elastic', 'HbeJwG5xONdI37mR-3ux'),
            ssl_assert_fingerprint=('31764610aa16c11605084b4a57c215985ee694ca17d0b735ecc3959b5493fc43'),
            verify_certs=False
        )

ES_INDEX = 'prod_diary_idx'


MOCK_ES = True


def insert_es(id, diary_id, author_id, timestamp, title, sub_title, content, comment):
    if MOCK_ES:
        return
    return es.index(id=id, index=ES_INDEX, body={
        'diaryId': diary_id,
        'authorId': author_id,
        'timestamp': timestamp,
        'title': title,
        'subTitle': sub_title,
        'content': content,
        'comment': comment,
    })


def create_index():
    es.indices.create(index=ES_INDEX, mappings={
        "dynamic": False,
        "properties": {
            "diaryId": {
                "type": "keyword"
            },
            "authorId": {
                "type": "keyword"
            },
            "timestamp": {
                "type": "date"
            },
            "title": {
                "type": "text",
            },
            "subTitle": {
                "type": "text"
            },
            "content": {
                "type": "text"
            },
            "comment": {
                "type": "text"
            }
        }
    })
    pass


def exists_index():
    return es.indices.exists(index=ES_INDEX)


def exists(id):
    return es.exists(index=ES_INDEX, id=id)


def search_es(search_content):

    body = {
        'query': {
            'match': {
                'content': search_content
            }
        },
        'size': 20  
    }

    return es.search(index=ES_INDEX, body=body)


def del_es(id):
    if MOCK_ES:
        return
    return es.delete(index=ES_INDEX, id=id)


def del_all():
    if MOCK_ES:
        return
    return es.indices.delete(index=ES_INDEX)


def all():
    body = {
        'query': {
            'match_all': {}
        },
    }

    return es.search(index=ES_INDEX, body=body)


def setEsMock(mock):
    global MOCK_ES
    MOCK_ES = mock


if __name__ == '__main__':
    setEsMock(False)
    print(exists_index())
#     new_mapping = {
#         "mappings": {
#             "properties": {
#                 "timestamp": {"type": "date"}
#             }
#         }
#     }
#     new_index_name = "prod_diary_idx"
#     es.indices.create(index=new_index_name, body=new_mapping)
#
#     # 使用reindex API复制数据到新索引
#     reindex_body = {
#         "source": {"index": ES_INDEX},
#         "dest": {"index": new_index_name}
#     }
#     es.reindex(body=reindex_body, wait_for_completion=True)

    pass
