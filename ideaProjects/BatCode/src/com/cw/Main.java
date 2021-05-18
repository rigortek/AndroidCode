package com.cw;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
	// write your code here
        int[]nums = new int[] {11, 15, 2, 7};
        int target = 9;

//        int[] resumt = twoSum1(nums, target);
        int[] resumt = twoSum2(nums, target);

        for (int i = 0; i < resumt.length; i++) {
            System.out.println("find " + resumt[i]);
        }
    }


//    04 TwoSum暴力解法
//    算法复杂度O(n平方)，因为需要2个循环嵌套
    public static int[] twoSum1(int[] nums, int target) {
        int[] indexArray = new int[2];

        for (int i = 0; i < nums.length; ++i) {
            int another = target - nums[i];

            for (int j = i + 1; j < nums.length; ++j) {
                if (nums[j] == another) {
                    indexArray[0] = i;
                    indexArray[1] = j;
                }
            }
        }

        return indexArray;
    }

//    05 TwoSum优化解法
//
    public static int[] twoSum2(int[] nums, int target) {
        int[] indexArray = new int[2];
        HashMap<Integer, Integer> tmp = new HashMap<>();

        for (int i = 0; i < nums.length; ++i) {
            int another = target - nums[i];
            if (tmp.containsKey(another)) {
                indexArray[0] = tmp.get(another);
                indexArray[1] = i;
            } else {
                tmp.put(nums[i], i);
            }
        }

        return indexArray;
    }
}
