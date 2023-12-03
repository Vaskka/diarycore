import io
import os
import re

import pymysql
from PIL import Image
from fitz_new import fitz

from dal import mysql

db_conn = pymysql.connect(host='localhost',
                             user='root',
                             password='d94d8c79',
                             database='diary',
                             charset='utf8mb4',
                             cursorclass=pymysql.cursors.DictCursor)


def get_file_name(path):
    m = re.search('^.*/(.*)$', path)
    return m.group(1).replace('.pdf', '')
    pass


def pdf_to_images(pdf_path, output_folder):
    # 打开PDF文件
    pdf_document = fitz.open(pdf_path)

    # 遍历每一页
    for page_number in range(pdf_document.page_count):
        # 获取页面
        page = pdf_document[page_number]

        # 获取页面的图像
        image_list = page.get_images(full=True)

        # 遍历图像列表
        for img_index, img in enumerate(image_list):
            xref = img[0]
            base_image = pdf_document.extract_image(xref)
            image_bytes = base_image["image"]

            # 使用Pillow打开图像
            image = Image.open(io.BytesIO(image_bytes))

            # 保存图像
            image.save(f"{output_folder}/{get_file_name(pdf_path)}_page_{page_number + 1}.png")

    # 关闭PDF文件
    pdf_document.close()


def set_author_page(author_id):
    mysql.update_origin_pic(author_id)
    pass


if __name__ == '__main__':
    d = './pdf/'
    for filepath, dirnames, filenames in os.walk(d):
        for filename in filenames:
            path = d + os.sep + filename
            output_folder = "pdfimg"
            pdf_to_images(path, output_folder)
            print('process pdf done,', path)

    # for author in mysql.select_all_author():
    #     set_author_page(author_id=author['id'])
    # 使用示例
    # pdf_path = "pdf/D0074+《聂耳日记》，大象出版社，2004年.pdf"
    # output_folder = "pdfimg"
    # pdf_to_images(pdf_path, output_folder)
