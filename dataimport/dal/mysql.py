# Connect to the database
import pymysql


db_conn = pymysql.connect(host='localhost',
                             user='root',
                             password='d94d8c79',
                             database='diary',
                             charset='utf8mb4',
                             cursorclass=pymysql.cursors.DictCursor)


MOCK_MYSQL = True


def insert_mysql_diary(author_id, title, sub_title, timestamp, start_page, end_page, comment, ext_param):
    if MOCK_MYSQL:
        return
    with db_conn.cursor() as cursor:
        # Create a new record
        sql = "INSERT INTO `diary` (`id`, `gmt_create`, `gmt_modified`, `author_id`, `diary_title`, `sub_title`, `diary_date_timestamp`, `start_page`, `end_page`, `origin_pic`, `comment`,`extern_param`) \
            VALUES (NULL, NULL, NULL, %s, %s, %s, %s, %s, %s, NULL, %s, %s)"
        cursor.execute(sql, (author_id, title, sub_title, timestamp, start_page, end_page, comment, ext_param))

    # connection is not autocommit by default. So you must commit to save
    # your changes.
    db_conn.commit()

    pass


def insert_mysql_author(author_name, author_avatar_url, extern_param):
    if MOCK_MYSQL:
        return
    with db_conn.cursor() as cursor:
        # Create a new record
        sql = "INSERT INTO `author` (`id`, `gmt_create`, `gmt_modified`, `author_name`, `author_avatar_url`, `extern_param`) \
            VALUES (NULL, NULL, NULL, %s, %s, %s)"
        cursor.execute(sql, (author_name, author_avatar_url, extern_param))

    # connection is not autocommit by default. So you must commit to save
    # your changes.
    db_conn.commit()
    pass


def select_by_author_id_title_sub_title(author_id, title, sub_title):
    with db_conn.cursor() as cursor:
        # Create a new record
        sql = "SELECT * FROM `diary` WHERE `author_id` = %s AND `diary_title` = %s AND `sub_title` = %s"
        cursor.execute(sql, (author_id, title, sub_title))
        return cursor.fetchone()


def select_by_author_name(author_name):
     with db_conn.cursor() as cursor:
        # Create a new record
        sql = "SELECT * FROM `author` WHERE `author_name` = %s"
        cursor.execute(sql, (author_name))
        return cursor.fetchone()


def setMysqlMock(mock):
    global MOCK_MYSQL
    MOCK_MYSQL = mock


if __name__ == '__main__':
    
    pass