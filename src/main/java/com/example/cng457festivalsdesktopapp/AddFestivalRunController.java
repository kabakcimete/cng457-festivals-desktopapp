package com.example.cng457festivalsdesktopapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AddFestivalRunController {
    @FXML
    private ComboBox FestivalsComboBox;

    @FXML
    private TextField FestivalRunNameTextField;

    @FXML
    private TextField FestivalRunDurationTextField;

    @FXML
    private TextField FestivalRunDateTextField;

    /**
     * This method helps us to move back to main menu.
     * @param e
     * @throws IOException
     */
    public void BackAddFestivalRun(ActionEvent e) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
            s.setTitle("Main");
            s.setScene(scene);
            s.show();

    }

    /**
     * When Add button is clicked this method collect the data from textfields and add festivals to database
     * @param e
     * @throws IOException
     */
    public void AddButtonAddFestivalRun(ActionEvent e) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/addfestivalrun").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json; utf-8");
        connection.setDoInput(true); //Set the DoInput flag to true if you intend to use the URL connection for input
        connection.setDoOutput(true);
        String festivalid = FestivalsComboBox.getValue().toString().split("-")[0];

        JSONObject festivalrun = new JSONObject();
        festivalrun.put("name" , FestivalRunNameTextField.getText());
        festivalrun.put("duration" , FestivalRunDurationTextField.getText());
        festivalrun.put("date" , FestivalRunDateTextField.getText());


        JSONObject festival = new JSONObject();
        festival.put("festivalid" , festivalid);
        festivalrun.put("festival" , festival);
        System.out.println(festivalrun.toJSONString());

        try(OutputStream os = connection.getOutputStream()){
            byte[] input = festivalrun.toJSONString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        catch (IOException ev){
            ev.printStackTrace();
        }
        String response = "";
        int responsecode = connection.getResponseCode();
        if(responsecode == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
            }
            scanner.close();
        }

        System.out.println(response);


    }

    /**
     * This method initialize the festivals combobox.
     * @throws IOException
     * @throws ParseException
     */
    public void initialize() throws IOException, ParseException {
        String response = "";
        HttpURLConnection connection = (HttpURLConnection)new URL("http://localhost:8080/getallfestivals").openConnection();
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
                FestivalsComboBox.getItems().add(temp.get("festivalid") + "-" +temp.get("festivalname"));
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
                FestivalsComboBox.getItems().add(object.get("festivalid") + "-" + object.get("festivalname"));

            }


        }

    }
}
