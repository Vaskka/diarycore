import time


TIME_FORMAT = "%Y年%m月%d日"


def str2timestamp(s):
    return time.mktime(time.strptime(s, TIME_FORMAT))


def get_start_page(raw_page):
    if raw_page is None or raw_page == '':
        return None

    if raw_page.find('-') == -1:
        return None
    
    start = raw_page.split('-')[0]
    if start.startwith('P') or start.startwith('p'):
        return start[1:]
    else:
        return start


def get_end_page(raw_page):
    if raw_page is None or raw_page == '':
        return None

    if raw_page.find('-') == -1:
        return None
    
    end = raw_page.split('-')[1]
    if end.startwith('P') or end.startwith('p'):
        return end[1:]
    else:
        return end
