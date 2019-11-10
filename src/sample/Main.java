package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/*
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
} */

public class Main extends Application {
    final int WINDOW_SIZE = 10;
    private ScheduledExecutorService scheduledExecutorService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JavaFX Realtime Chart Demo");

        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis(); // we are gonna plot against time
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time/s");
        xAxis.setAnimated(false); // axis animations are removed
        yAxis.setLabel("Value");
        yAxis.setAnimated(false); // axis animations are removed

        //creating the line chart with two axis created above
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Realtime JavaFX Charts");
        lineChart.setAnimated(false); // disable animations

        // setup scene
        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setScene(scene);

        // show the stage
        primaryStage.show();

        // this is used to display time in HH:mm:ss format
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

        // setup a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        // Initialize the array of multiple series
        int numSeries = 3;
        XYChart.Series<String, Number> [] allSeries = new XYChart.Series[numSeries];

        // Initialize k value for series color that needs to be changed
        int k = 1;

        // Loop series to numSeries
        for(int i = 0; i < numSeries; i++) {

            // Initialize the individual series
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            allSeries[i] = series;
            allSeries[i].setName("Data Series" + (i + 1));

            // Check if i is k series that needs to be changed to new color (line)
            if(i == k) {
                // Change color of series k
                for(int l=0; l<1; l++){
//                    for(Node n : lineChart.lookupAll(".series" + l)){
//                        n.setStyle( "-fx-background-color: rgb(202,152,235), white; "
//                        + "-fx-stroke: rgb(202,152,235); ");
//                    }

                    for(Node n : lineChart.lookupAll(".chart-series-area-line.series" + l)){
                        n.setStyle("-fx-stroke: rgb(202,152,235); ");
                    }
                }
            }

            // add series to chart
            lineChart.getData().add(allSeries[i]);

            // put dummy data onto graph per second
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                // get a random integer between 0-10
                Integer random = ThreadLocalRandom.current().nextInt(10);

                // Update the chart
                Platform.runLater(() -> {
                    // get current time
                    Date now = new Date();
                    XYChart.Data<String, Number> dataPoint = new XYChart.Data(simpleDateFormat.format(now), random);


                    // put random number with current time
                    series.getData().add(dataPoint);

                    if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
                });

            }, 0, 1000, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        scheduledExecutorService.shutdownNow();
    }
}
