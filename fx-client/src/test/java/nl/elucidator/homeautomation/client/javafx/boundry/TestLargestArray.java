/*
 * Copyright (C) 2012 Pieter van der Meer (pieter(at)elucidator.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.elucidator.homeautomation.client.javafx.boundry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/*
 Problem:  Given a list of random numbers, both positive and negative, find the
 largest sum of a sublist.

 Sample output - Late 2013 8-core MacBook Pro

 Finding largest Array value Naively
 Total execution time: 25599
 Largest Sum: 3327
 Number of searches 3126250
 Finding largest Array value Naively via Futures
 Total execution time: 7297
 Largest Sum: 3327
 Number of searches 3126250
 Finding largest Array value
 Total execution time: 6389
 Largest Sum: 3327
 Number of searches 764466
 Finding largest Array value via Futures
 Total execution time: 1805
 Largest Sum: 3327
 Number of searches 764466
 */
public class TestLargestArray {

    public static void main(String[] args) throws Exception {

        List<Integer> random
                = Collections.unmodifiableList(
                new Random()
                        .ints(-100, 101)
                        .limit(2500)
                        .boxed()
                        .collect(Collectors.toList())
        );

        rest();
        findLargestNaive(random);
        rest();
        findLargestFutureNaive(random);
        rest();
        findLargest(random);
        rest();
        findLargestFuture(random);
    }

    private static long sum(List<Integer> list) {
        return list.stream()
                .map((i) -> (long) i)
                .reduce(0L, (accumulator, _item)
                        -> Math.addExact(accumulator, _item));
    }

    private static void findLargestNaive(List<Integer> list) {
        System.out.println("Finding largest Array value Naively");
        long starttime = System.currentTimeMillis();

        long resultValue = list.get(0); // seed value
        long numTries = 0;

        for (int start = 0; start < list.size(); start++) {
            for (int end = start; end < list.size(); end++) {
                List<Integer> tester = list.subList(start, end + 1);
                long testresult = sum(tester);
                numTries++;
                if (testresult > resultValue) {
                    resultValue = testresult;
                }
            }
        }
        System.out.println("Total execution time: " + (System.currentTimeMillis() - starttime));
        System.out.println("Largest Sum: " + resultValue);
        System.out.println("Number of searches " + numTries);
    }

    private static void findLargestFutureNaive(List<Integer> list) throws Exception {
        System.out.println("Finding largest Array value Naively via Futures");
        long starttime = System.currentTimeMillis();

        ForkJoinPool pool = ForkJoinPool.commonPool();
        List<Future> futures = new ArrayList<>();
        AtomicLong resultValue = new AtomicLong(list.get(0));  // seed value
        LongAdder numTries = new LongAdder();

        for (int start = 0; start < list.size(); start++) {
            final int s = start;
            futures.add(pool.submit(() -> {
                for (int end = s; end < list.size(); end++) {
                    List<Integer> tester = list.subList(s, end + 1);
                    long testresult = sum(tester);
                    numTries.increment();
                    resultValue.accumulateAndGet(testresult, (x, y) -> {
                        return (x > y) ? x : y;
                    });
                }
            }));
        }
        for (Future f : futures) {
            f.get();
        }
        System.out.println("Total execution time: " + (System.currentTimeMillis() - starttime));
        System.out.println("Largest Sum: " + resultValue);
        System.out.println("Number of searches " + numTries);
    }

    private static void findLargest(List<Integer> list) {
        System.out.println("Finding largest Array value");
        long starttime = System.currentTimeMillis();

        long resultValue = list.stream().max(Integer::compare).get(); // seed value
        long numTries = 0;

        for (int start = 0; start < list.size(); start++) {
            if (list.get(start) > 0) {
                for (int end = start; end < list.size(); end++) {
                    if (list.get(end) > 0) {
                        List<Integer> tester = list.subList(start, end + 1); // sublist uses exclusive end
                        long testresult = sum(tester);
                        numTries++;
                        if (testresult > resultValue) {
                            resultValue = testresult;
                        }
                    }
                }
            }
        }
        System.out.println("Total execution time: " + (System.currentTimeMillis() - starttime));
        System.out.println("Largest Sum: " + resultValue);
        System.out.println("Number of searches " + numTries);
    }

    private static void findLargestFuture(List<Integer> list) throws Exception {
        System.out.println("Finding largest Array value via Futures");
        long starttime = System.currentTimeMillis();

        List<Future<Long>> futures = new ArrayList<>();
        LongAdder numTries = new LongAdder();

        for (int start = 0; start < list.size(); start++) {
            final int s = start;
            if (list.get(s) > 0) {
                futures.add(CompletableFuture.supplyAsync(() -> {
                    long maxresult = list.get(s); //seed
                    for (int end = s; end < list.size(); end++) {
                        long testvalue = list.get(end);
                        if (testvalue > 0) {
                            List<Integer> tester = list.subList(s, end + 1);
                            long testresult = sum(tester);
                            numTries.increment();
                            if (maxresult < testresult) {
                                maxresult = testresult;
                            }
                        } else if (testvalue > maxresult) {
                            maxresult = testvalue;
                        }
                    }
                    return maxresult;
                }).exceptionally(ex -> {
                    if (ex.getCause() instanceof ArithmeticException) {
                        return Long.MAX_VALUE;
                    } else {
                        throw (RuntimeException) ex;
                    }
                }));
            }
        }
        long result = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).max(Long::compare)
                        // if all values are negative, get the biggest one
                .orElseGet(() -> (long) list.stream().max(Integer::compare).get());
        System.out.println("Total execution time: " + (System.currentTimeMillis() - starttime));
        System.out.println("Largest Sum: " + result);
        System.out.println("Number of searches " + numTries);
    }

    private static void rest() throws Exception {
        System.gc();
        Thread.sleep(5000);
    }
}
