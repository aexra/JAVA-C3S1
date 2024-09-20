package org.dstu.lab1fx.task7;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MediaPlayerAppController {
    @FXML private Label currentTime;
    @FXML private Label totalTime;
    @FXML private MediaView mediaView;
    @FXML private VBox toolBar;
    @FXML private Slider volumeSlider;
    @FXML private ProgressBar videoDurationBar;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private static final Duration SEEK_STEP = new Duration(3000);
    @FXML private void processFileOpening() {
        try {
            String filePath = getFileSource();
            URI uri = Paths.get(filePath).toAbsolutePath().toUri();
            mediaPlayer = new MediaPlayer(new Media(uri.toString()));
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.currentTimeProperty().addListener((observableValue, duration, t1) -> {
                videoDurationBar.setProgress(t1.toSeconds() / mediaPlayer.getTotalDuration().toSeconds());
            });
            mediaPlayer.setOnReady(() -> {
                mediaPlayer.currentTimeProperty().addListener((observableValue, duration, t1) -> {
                    currentTime.setText(getFormattedTime(t1));
                });
                totalTime.setText(getFormattedTime(mediaPlayer.getStopTime()));
            });
            volumeSlider.valueProperty().addListener((observableValue, number, t1) -> {
                mediaPlayer.setVolume(t1.doubleValue() / 100);
            });
            toolBar.setDisable(false);

            pause();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getFormattedTime(Duration time) {
        int hours = (int) time.toSeconds() / 3600;
        int minutes = (int) (time.toSeconds() % 3600) / 60;
        int seconds = (int) time.toSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    private String getFileSource() throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Открыть файл");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video File(mp4)", "*.mp4"));
        fc.setInitialDirectory(Path.of(System.getProperty("user.dir") + "\\src\\main\\resources\\org\\dstu\\lab1fx\\task7\\videos").toFile());
        File file;
        if ((file = fc.showOpenDialog(null)) != null) {
            System.out.println("Файл успешно открыт: " + file.getAbsolutePath());
            return file.getPath();
        }
        else throw new IOException();
    }
    @FXML private void exitApp(ActionEvent event) { Platform.exit(); }
    @FXML private void skipToStart(ActionEvent event) {
        mediaPlayer.seek(mediaPlayer.getStartTime());
    }
    @FXML private void pause() {
        if (isPlaying) {
            mediaPlayer.pause();
        }
        else {
            mediaPlayer.play();
        }
        isPlaying = !isPlaying;
    }
    @FXML private void stop() {
        mediaPlayer.stop();
        isPlaying = false;
    }
    @FXML private void skipToEnd(ActionEvent event) {
        mediaPlayer.seek(mediaPlayer.getStopTime());
        isPlaying = false;
    }
    @FXML private void changeVolume(MouseEvent event) {
        mediaPlayer.setVolume(volumeSlider.getValue() / 100);
    }
    @FXML private void fastForwardMode(ScrollEvent scrollEvent) {
        if (scrollEvent.getDeltaY() > 0) {
            mediaPlayer.seek(Duration.millis(mediaPlayer.getCurrentTime().toMillis() + SEEK_STEP.toMillis()));
        }
        else mediaPlayer.seek(Duration.millis(mediaPlayer.getCurrentTime().toMillis() - SEEK_STEP.toMillis()));
    }
}
