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

import javafx.scene.chart.XYChart;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Class CompletableTest.
 */
public class CompletableTest {

    EnergyUsage energyUsage = new EnergyUsage();

    @Test
    public void simple() throws InterruptedException {
        CompletableFuture<List<String>> stringCompletableFuture = CompletableFuture.supplyAsync(new Supplier<List<String>>() {
            @Override
            public List<String> get() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return Arrays.asList("42", "43", "44");
            }
        });

        System.out.println("CF Defined");

        stringCompletableFuture.thenAccept(f -> f.stream().forEach(System.out::println));

        Thread.sleep(2000);
    }

    @Test
    public void setEnergyUsage() throws InterruptedException {
        CompletableFuture<List<XYChart.Series>> completableFuture = CompletableFuture.supplyAsync(energyUsage::getWeekSeries);

        final MyCOnsumer<List<XYChart.Series>> myCOnsumer = new MyCOnsumer<>();
        //completableFuture.thenAccept(myCOnsumer);
        completableFuture.thenAccept(f -> f.stream().forEach(System.out::println));

        Thread.sleep(4000);
    }

    private class MyCOnsumer<T> implements Consumer<T> {
        @Override
        public void accept(final T serieses) {
            System.out.println("serieses = " + serieses);
        }

        @Override
        public Consumer<T> andThen(final Consumer<? super T> after) {
            return null;
        }
    }

}
