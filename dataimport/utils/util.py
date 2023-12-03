import time
import datetime


TIME_FORMAT = "%Y年%m月%d日"


# def str2timestamp(s):
#     return time.mktime(time.strptime(s, TIME_FORMAT))


def str2timestamp(s):
    cd = datetime.datetime.strptime(s, TIME_FORMAT)
    ts = (cd-datetime.datetime(1970,1,1,8)).total_seconds()
    return int(ts * 1000)


def get_start_page(raw_page):
    if raw_page is None or raw_page == '':
        return None

    if raw_page.find('-') == -1:
        if raw_page.startswith('P') or raw_page.startswith('p'):
            return raw_page[1:]
        else:
            return raw_page
    
    start = raw_page.split('-')[0]
    if start.startswith('P') or start.startswith('p'):
        return start[1:]
    else:
        return start


def get_end_page(raw_page):
    if raw_page is None or raw_page == '':
        return None

    if raw_page.find('-') == -1:
        return None
    
    end = raw_page.split('-')[1]
    if end.startswith('P') or end.startswith('p'):
        return end[1:]
    else:
        return end


if __name__ == '__main__':
    # print(str2timestamp_new('1930年9月3日'))
    print(str2timestamp('1860年1月1日'))
    pass
