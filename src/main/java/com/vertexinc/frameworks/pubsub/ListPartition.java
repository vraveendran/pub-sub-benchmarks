package com.vertexinc.frameworks.pubsub;

import java.util.ArrayList;
import java.util.List;

public class ListPartition {

    /**
     * partition a list to specified size
     *
     * @param originList
     * @param size
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> partitionList(List<T> originList, int size) {

        List<List<T>> resultList = new ArrayList<>();
        if (null == originList || 0 == originList.size() || size <= 0) {
            return resultList;
        }
        if (originList.size() <= size) {
            for (T item : originList) {
                List<T> resultItemList = new ArrayList<>();
                resultItemList.add(item);
                resultList.add(resultItemList);
            }
            for (int i = 0; i < (size - originList.size()); i++) {
                resultList.add(new ArrayList<>());
            }
            return resultList;
        }

        for (int i = 0; i < size; i++) {
            resultList.add(new ArrayList<>());
        }
        int count = 0;
        for (T item : originList) {
            int index = count % size;
            resultList.get(index).add(item);
            count++;
        }
        return resultList;
    }

}
