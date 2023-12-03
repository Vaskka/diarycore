package com.vaskka.diary.core.model.bizobject;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class SearchCondition {

    /**
     * 多选器
     * @param <T> 可被选择的内容
     */
    @Data
    public static class MultiPicker<T> {
        private List<T> pickContent;

        /**
         * 是否全选 pickContent为空即为全选
         * @return boolean
         */
        public boolean isAllSelect() {
            return pickContent == null || pickContent.isEmpty();
        }

        /**
         * 元素是否被选择
         * @param t t
         * @return boolean
         */
        public boolean contains(T t) {
            if (isAllSelect()) {
                return true;
            }

            return pickContent.contains(t);
        }

        /**
         * build instance
         * @param content content
         * @return picker
         * @param <T> content type
         */
        public static <T> MultiPicker<T> build(List<T> content) {
            var res = new MultiPicker<T>();
            res.setPickContent(content);
            return res;
        }
    }

    /**
     * 范围选择器
     * @param <T> 类型
     */
    @Data
    @NoArgsConstructor
    public static class RangePicker<T> {
        private T gte;

        private T lte;

        public static <T> RangePicker<T> getInstance(T gte, T lte) {
            var instance = new RangePicker<T>();
            instance.setGte(gte);
            instance.setLte(lte);
            return instance;
        }
    }


    private String searchText;

    /**
     * 多关键字搜索
     */
    private List<String> multiSearchText;

    private MultiPicker<String> authorIdPicker;

    /**
     * authorId反过滤
     */
    private List<String> authorIdNotIn;

    private RangePicker<Long> timestampRange;

    private Integer page;

    private Integer size;
}
