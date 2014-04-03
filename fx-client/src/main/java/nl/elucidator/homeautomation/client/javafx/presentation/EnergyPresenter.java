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

package nl.elucidator.homeautomation.client.javafx.presentation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nl.elucidator.homeautomation.client.javafx.boundry.EnergyUsage;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * @author adam-bien.com
 */
public class EnergyPresenter implements Initializable {

    @FXML
    Label currentPower;
    @FXML
    BarChart<String, Number> dayUsage;
    @FXML
    Button updateGraph;
    @Inject
    EnergyUsage energyUsage;

    private Timeline currentPowerTimeLine;

    public EnergyPresenter() {

    }

    private void createCurrentPowerTimeLine() {
        currentPowerTimeLine = new Timeline(new KeyFrame(Duration.seconds(0), actionEvent -> currentPower.setText(getLatestPower())), new KeyFrame(Duration.seconds(10)));
        currentPowerTimeLine.setCycleCount(Animation.INDEFINITE);
        currentPowerTimeLine.play();
    }

    private String getLatestPower() {

        final Client clientBuilder = ClientBuilder.newBuilder().build();
        final WebTarget target = clientBuilder.target("http://localhost:8080/homeautomation/rest/client/power/average/5");
        final Response response = target.request(MediaType.TEXT_PLAIN).get();
        final String responseData = response.readEntity(String.class);
        response.close();
        Double value = Double.parseDouble(responseData);
        return String.format("%1$.2f", value);
    }

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        fillChart();
        createCurrentPowerTimeLine();
    }

    public void updateGraph() {
        dayUsage.getData().clear();
        fillChart();
    }

    @SuppressWarnings("unchecked")
    private void fillChart() {
        CompletableFuture<List<XYChart.Series>> completableFuture = CompletableFuture.supplyAsync(energyUsage::getWeekSeries);
        completableFuture.thenAccept(f -> Platform.runLater(() -> dayUsage.getData().addAll(f.toArray(new XYChart.Series[f.size()]))));
    }
}
