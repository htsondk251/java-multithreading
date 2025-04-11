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
    private static final int NUM_THREADS = 2;

    // Mutex for controlling access to foundPlaces
    private static final Object findLock = new Object();
    public static final Object countLock = new Object();

    private static List<Integer> foundPlaces = new ArrayList<>();
    private static int count = 0;

    private static void linearSearch(int threadId, int[] arr, int key) {
        int chunkSize = arr.length / NUM_THREADS;
        int start = threadId * chunkSize;
        int end = Math.min(start + chunkSize, arr.length-1);

        List<Integer> localIndices = new ArrayList<>();
        int localCount = 0;

        for (int i = start; i < end; i++) {
//            simulateHeavyTask();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (arr[i] == key) {
                localIndices.add(i);
                localCount++;
            }
        }

        if (localCount != 0) {
            synchronized (findLock) {
                foundPlaces.addAll(localIndices);
            }
            synchronized (countLock) {
                count += localCount;
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

        // Create an array and fill it with random numbers between 0 and 99
        int[] arr = new int[SIZE];
        Random random = new Random();
        for (int i = 0; i < SIZE; ++i) {
            arr[i] = random.nextInt(100);
        }
        int key = 19; // Element to find

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; ++i) {
            int threadId = i;
            threads[i] = new Thread(() -> linearSearch(threadId, arr, key));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //print the result
        if (foundPlaces.size() != 0) {
            System.out.println("found in; " + Arrays.toString(foundPlaces.toArray()));
            System.out.println("total: " + count);
        } else {
            System.out.println("not found");
        }




        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) / 1000.0 + " seconds");
    }
}

