from dal import es, mysql
import requests

HOSTS = 'http://127.0.0.1:8081'

PRE_URL = HOSTS + '/v1/api/admin'


def update_userid_by_diary_ids(user_id_list, diary_id_list):
    resp = requests.post(PRE_URL + '/update/diary/permission', json={
        'diaryIds': diary_id_list,
        'newUserIdList': user_id_list
    })
    print('update_userid_by_diary_ids:', resp.text)
    pass


def init_all():
    all_diary_id_db = mysql.select_all_diary_id()
    all_diary_id = []

    for diary_id_entry in all_diary_id_db:
        all_diary_id.append(str(diary_id_entry['id']))

    all_userid_in_db = mysql.select_all_user_id()
    all_userid = []

    for userid_entry in all_userid_in_db:
        all_userid.append(str(userid_entry['id']))

    # do update
    update_userid_by_diary_ids(all_userid, all_diary_id)
    pass


def main():
    init_all()
    pass


if __name__ == '__main__':
    main()
