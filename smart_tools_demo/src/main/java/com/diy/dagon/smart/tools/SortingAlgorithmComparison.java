package com.diy.dagon.smart.tools;

import java.util.Arrays;
import java.util.Random;

public class SortingAlgorithmComparison {

    public static void main(String[] args) {
        // 测试不同大小的数组
        int[] sizes = {1000, 10000, 100000};
        
        for (int size : sizes) {
            int[] array1 = generateRandomArray(size);
            int[] array2 = array1.clone();  // 创建相同数组的副本
            
            System.out.println("\n测试数组大小: " + size);
            
            // 测试冒泡排序
            long startTime = System.currentTimeMillis();
            bubbleSort(array1);
            long endTime = System.currentTimeMillis();
            System.out.println("冒泡排序耗时: " + (endTime - startTime) + " ms");
            
            // 测试快速排序
            startTime = System.currentTimeMillis();
            quickSort(array2, 0, array2.length - 1);
            endTime = System.currentTimeMillis();
            System.out.println("快速排序耗时: " + (endTime - startTime) + " ms");
            
            // 验证排序结果是否正确
            System.out.println("排序结果正确性检查：" + 
                (isSorted(array1) && isSorted(array2) && Arrays.equals(array1, array2)));
        }
    }
    
    // 生成随机数组
    private static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(1000000);
        }
        return array;
    }
    
    // 冒泡排序
    private static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 交换元素
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
    
    // 快速排序
    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    
    // 快速排序的分区方法
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                // 交换元素
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        
        // 将基准值放到正确的位置
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        
        return i + 1;
    }
    
    // 检查数组是否已排序
    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
} 