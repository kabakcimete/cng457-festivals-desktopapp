package com.example.cng457festivalsdesktopapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private RadioButton RadioAddFestivalRun;

    @FXML
    private RadioButton RadioAddConcert;

    @FXML
    private RadioButton RadioStatistics;

    /**
     * Our main menu, moves the necessary screen
     * @param e
     * @throws IOException
     */
    public void OnclickContinue(ActionEvent e) throws IOException {
        if (RadioAddFestivalRun.isSelected()) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AddFestivalRun-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
            s.setTitle("Add Festival Run");
            s.setScene(scene);
            s.show();
        }
        if (RadioAddConcert.isSelected()) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AddConcert.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
            s.setTitle("Add Concert");
            s.setScene(scene);
            s.show();
        }
        if (RadioStatistics.isSelected()) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Statistics-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
            s.setTitle("Statistics");
            s.setScene(scene);
            s.show();
        }
    }
}

