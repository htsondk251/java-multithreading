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

    // Mutex for controlling access to foundPlaces
    private static final Object lockObj = new Object();
    private static List<Integer> foundPlaces = new ArrayList<>();

    private static void linearSearch(int threadId, int[] arr, int key) {
        int chunkSize = arr.length / NUM_THREADS;
        int start = threadId * chunkSize;
        int end = (threadId == NUM_THREADS - 1) ? arr.length : start + chunkSize;

        for (int i = start; i < end; ++i) {
            try {
                Thread.sleep(1); // Simulate a heavy task
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (arr[i] == key) {
                synchronized (lockObj) { // Lock when modifying foundPlaces
                    foundPlaces.add(i); // Append the index to foundPlaces\
                }
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

        List<Thread> threads = new ArrayList<>(); // List to hold the threads
        int key = 19; // Element to find

        // Start the threads
        for (int i = 0; i < NUM_THREADS; ++i) {
            int threadId = i;
            Thread thread = new Thread(() -> linearSearch(threadId, arr, key));
            threads.add(thread);
            thread.start();
        }

        // Join the threads with the main thread
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Display the result
        if (foundPlaces.isEmpty()) {
            System.out.println("Element not found in the array.");
        } else {
            System.out.print("Element found at indices: ");
            synchronized (lockObj) { // Lock when reading from foundPlaces
                for (int index : foundPlaces) {
                    System.out.print(index + " ");
                }
            }
            System.out.println();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) / 1000.0 + " seconds");
    }
}

