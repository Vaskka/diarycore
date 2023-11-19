from elasticsearch import Elasticsearch


es = Elasticsearch(
            "https://localhost:9200",
            basic_auth=('elastic', 'HbeJwG5xONdI37mR-3ux'),
            ssl_assert_fingerprint=('31764610aa16c11605084b4a57c215985ee694ca17d0b735ecc3959b5493fc43'),
            verify_certs = False
        )

ES_INDEX = 'diary_content_idx'


MOCK_ES = True


def insert_es(id, diary_id, author_id, content):
    if MOCK_ES:
        return
    return es.index(id=id, index=ES_INDEX, body={
        'diaryId': diary_id,
        'authorId': author_id,
        'content': content,
    })


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
