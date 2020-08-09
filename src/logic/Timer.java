package logic;

import graphics.scoreController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import main.Main;
import templator.Templator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public class Timer {

    long startTime;
    public Label timerDisplay = new Label();
    Templator scoreTemplate = new Templator(new File("resources/scoreNotification.fxml"));

    public Timer() throws IOException, ParseException {
        startTime = System.currentTimeMillis();
        timerDisplay.setPadding(new Insets(0, 685, 450, 0));
        timerDisplay.setFont(new Font("serif", 16));
    }

    public void update() throws IOException, InterruptedException {

        int timeLimit = 60;

        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 1000;
        long remainingTime = timeLimit - elapsedSeconds;
        long secondsDisplay = remainingTime % 60;
        long minutesDisplay = remainingTime / 60;
        String displayText = minutesDisplay + ":" + Math.abs(secondsDisplay);
        timerDisplay.setText(displayText);

        if (remainingTime <= 0) {

            HashMap<String, String> scores = new HashMap<>();
            scores.put("SCORE", Integer.toString(Counter.value));
            File destination = new File("resources/scoreNotification_present.fxml");
            scoreTemplate.template(scores, destination);
            Main.root.getChildren().add(FXMLLoader.load(destination.toURI().toURL()));
            Main.animationTimer.stop();

//            Thread.currentThread().wait();

        }

    }

//    private void createScore() throws IOException {
//
//        if (!scoreBoardCreated) {
//            HashMap<String, String> scores = new HashMap<>();
//            scores.put("SCORE", Integer.toString(Counter.value));
//            File destination = new File("resources/scoreNotification_present.fxml");
//            scoreTemplate.template(scores, destination);
//            scoreBoardDisplay = FXMLLoader.load(destination.toURI().toURL());
//            scoreBoardCreated = true;
//        }
//    }

}
