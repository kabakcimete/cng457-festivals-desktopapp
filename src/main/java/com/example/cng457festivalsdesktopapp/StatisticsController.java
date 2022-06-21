package com.example.cng457festivalsdesktopapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StatisticsController {
    @FXML
    private CheckBox StatisticsPopularFestivalsCB;

    @FXML
    private CheckBox StatisticsLongestConcertsCB;

    @FXML
    private ListView StatisticsPFListView;

    @FXML
    private ListView StatisticsLCListView;
    public void BackStatistics(ActionEvent e) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
        s.setTitle("Main");
        s.setScene(scene);
        s.show();

    }
}
