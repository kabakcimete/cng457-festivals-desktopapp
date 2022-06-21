package com.example.cng457festivalsdesktopapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class StatisticsController implements Runnable{
    @FXML
    private CheckBox StatisticsPopularFestivalsCB;

    @FXML
    private CheckBox StatisticsLongestConcertsCB;

    @FXML
    private ListView StatisticsPFListView;

    @FXML
    private ListView StatisticsLCListView;

    /**
     * This method is terminated when back button clicked and move main menu.
     * @param e
     * @throws IOException
     */
    public void BackStatistics(ActionEvent e) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
        s.setTitle("Main");
        s.setScene(scene);
        s.show();

    }


    /**
     * This method is terminated when show button is clicked and calls the necessary methods accordingly.
     * @throws IOException
     * @throws ParseException
     */

    public void ShowStatistics()  throws IOException, ParseException{
        StatisticsPFListView.getItems().clear();
        StatisticsLCListView.getItems().clear();
        if (StatisticsPopularFestivalsCB.isSelected() && StatisticsLongestConcertsCB.isSelected()) { //both checked

            run();
        }
        else if (!StatisticsPopularFestivalsCB.isSelected() && StatisticsLongestConcertsCB.isSelected()) { //longest concerts checked
            LongestConcertsShow();
        }
        else if (StatisticsPopularFestivalsCB.isSelected() && !StatisticsLongestConcertsCB.isSelected()) { //popular festivals checked
            PopularFestivalsShow();
        }
        else { //both not checked
            sayWarning();
        }
    }

    /**
     * Just a pop-up warning in case no option is checked.
     */

    public void sayWarning(){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Nothing is checked!");
        a.setContentText("Nothing is checked!");
        a.show();
    }

    /**
     * This method gets the Populer festivals and write them on the ListView
     * @throws IOException
     * @throws ParseException
     */

    public void PopularFestivalsShow() throws IOException, ParseException {
        String response = "";
        HttpURLConnection connection = (HttpURLConnection)new URL("http://localhost:8080/popularfestivals").openConnection();
        connection.setRequestMethod("GET");
        int responsecode = connection.getResponseCode();
        if(responsecode == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
            }
            scanner.close();
        }

        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(response);
        for(int i=0; i<array.size(); i++){
            try {
                JSONObject temp = (JSONObject) array.get(i);
                StatisticsPFListView.getItems().add(temp.get("festivalid") + "-" +temp.get("festivalname"));
            }
            catch(Exception e){
                String response2 = "";
                HttpURLConnection connection2 = (HttpURLConnection)new URL("http://localhost:8080/getfestival/" + array.get(i)).openConnection();
                connection2.setRequestMethod("GET");
                int responsecode2 = connection2.getResponseCode();
                if(responsecode2 == 200){
                    Scanner scanner = new Scanner(connection2.getInputStream());
                    while(scanner.hasNextLine()){
                        response2 += scanner.nextLine();
                    }
                    scanner.close();
                }
                JSONObject object = (JSONObject) parser.parse(response2);
                StatisticsPFListView.getItems().add(object.get("festivalid") + "-" + object.get("festivalname"));

            }


        }


    }


    /**
     * This method gets the longest concerts and write them on the ListView
     * @throws IOException
     * @throws ParseException
     */
    public void LongestConcertsShow() throws IOException, ParseException {
        String response = "";
        HttpURLConnection connection = (HttpURLConnection)new URL("http://localhost:8080/longestconcerts").openConnection();
        connection.setRequestMethod("GET");
        int responsecode = connection.getResponseCode();
        if(responsecode == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
            }
            scanner.close();
        }

        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(response);
        for(int i=0; i<array.size(); i++){
            try {
                JSONObject temp = (JSONObject) array.get(i);
                StatisticsLCListView.getItems().add(temp.get("eventid") + "-" +temp.get("name"));
            }
            catch(Exception e){
                String response2 = "";
                HttpURLConnection connection2 = (HttpURLConnection)new URL("http://localhost:8080/longestconcerts").openConnection();
                connection2.setRequestMethod("GET");
                int responsecode2 = connection2.getResponseCode();
                if(responsecode2 == 200){
                    Scanner scanner = new Scanner(connection2.getInputStream());
                    while(scanner.hasNextLine()){
                        response2 += scanner.nextLine();
                    }
                    scanner.close();
                }
                JSONObject object = (JSONObject) parser.parse(response2);
                StatisticsLCListView.getItems().add(object.get("eventid") + "-" + object.get("name"));

            }


        }


    }

    /**
     * Thread fuction; when both longest concert and populer festivals are selected execute them at the same time.
     */

    @Override
    public void run() {
        try {
            LongestConcertsShow();
            PopularFestivalsShow();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}

