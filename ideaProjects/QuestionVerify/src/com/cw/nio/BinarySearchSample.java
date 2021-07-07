package com.cw.nio;

import java.util.Arrays;
import java.util.Vector;

// 二分查找

public class BinarySearchSample {

    public static void search() {
        int []numArray = {-1, 0, 3, 5, 9, 12, 20};
        Vector<Integer> nums = new Vector<>();
        for (int j = 0; j < numArray.length; j++) {
            nums.add(numArray[j]);
        }
        int target = 9;

        System.out.println("find index: " + search(nums, target));

    }


    private static int search(Vector<Integer> nums, int target) {
        int start = 0;
        int end = nums.size() - 1;


        while (start <= end
        && start < nums.size()
        && end < nums.size()) {
            int middle = (start + end) / 2;
            if (target == nums.get(middle)) {
                return middle;
            } else if (target < nums.get(middle)) {
                end = middle - 1;
            } else {  // target > nums.get(middle)
                start = middle + 1;
            }
        }

        return -1;
    }
}
