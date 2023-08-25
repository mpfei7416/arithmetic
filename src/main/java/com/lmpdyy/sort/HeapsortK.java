package com.lmpdyy.sort;

import java.util.PriorityQueue;

/**
 *  一个几乎有序的数组， 选择一个算法排序， 数组中的元素移动的位置不超过K， k 十一个很小的数
 *
 *  小根堆求解出每次的最小值，放入新的数组中，一次求出最小值
 */
public class HeapsortK {

    /**
     * PriorityQueue  优先级队列，就是小根堆
     * 扩容：
     *      一倍扩容
     *      扩容一次为时间复杂度为 log N
     *
     *
     * @param args
     */
    public static void main(String[] args) {

        int[] arr = {2, 4, 2, 6, 9, 5, 3};
        sortedArrDistanceLessK(arr, 8);
        for (int i : arr) {
            System.out.println(i);
        }
    }


    public static void sortedArrDistanceLessK(int[] arr, int k) {
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int index = 0;
        for (; index < Math.min(arr.length, k); index++) {
            heap.add(arr[index]);
        }
        int i =0;
        for (; index < arr.length; i++, index++) {
            heap.add(arr[index]);
            arr[i] = heap.poll();
        }

        while (!heap.isEmpty()) {
            arr[i++] = heap.poll();
        }
    }


}
