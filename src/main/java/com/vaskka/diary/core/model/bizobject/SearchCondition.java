package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

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
    }

    private String searchText;


    private MultiPicker<String> authorIdPicker;
}
