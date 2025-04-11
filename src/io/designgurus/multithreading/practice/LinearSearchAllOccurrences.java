package io.designgurus.multithreading.practice;

import java.util.*;

/**
 * @version 1.0
 * @description:
 * @author: sonhoangthanh
 * @date: 08/04/2025 18:26
 */
public class LinearSearchAllOccurrences {

    private static final int SIZE = 40_000;
    private static final int NUM_THREADS = 8;

    private static final List<Integer> threadResults = new ArrayList<>();
    public static final Object lock = new Object();

    private static void linearSearch(int threadId, int[] arr, int key) {
        int chunkSize = arr.length / NUM_THREADS;
        int start = threadId * chunkSize;
        int end = (threadId == NUM_THREADS - 1) ? arr.length : start + chunkSize;

        List<Integer> localResults = new ArrayList<>();

        for (int i = start; i < end; ++i) {
            simulateHeavyTask();
            if (arr[i] == key) {
                localResults.add(i);
            }
        }


        if (!localResults.isEmpty()) {
            synchronized (lock) {
                threadResults.addAll(localResults);
            }
        }
    }

    private static void simulateHeavyTask() {
        long sum = 0;
        for (long i = 0; i < 1_000_000L; i++) {
            sum += i;
        }
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        // Khởi tạo mảng ngẫu nhiên
        int[] arr = new int[SIZE];
        Random random = new Random();
        for (int i = 0; i < SIZE; ++i) {
            arr[i] = random.nextInt(100);
        }

        List<Thread> threads = new ArrayList<>();
        int key = 19;

        // Tạo và khởi chạy các thread
        for (int i = 0; i < NUM_THREADS; ++i) {
            int threadId = i;
            Thread thread = new Thread(() -> linearSearch(threadId, arr, key));
            threads.add(thread);
            thread.start();
        }

        // Chờ các thread kết thúc
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Hiển thị kết quả
        if (threadResults.isEmpty()) {
            System.out.println("Element not found in the array.");
        } else {
            System.out.print("Element found at indices: ");
            for (int index : threadResults) {
                System.out.print(index + " ");
            }
            System.out.println();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) / 1000.0 + " seconds");
    }
}
