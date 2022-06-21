package com.example.cng457festivalsdesktopapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class AddConcertController {
    @FXML
    private ComboBox AddConcertFestivalsComboBox;

    @FXML
    private ComboBox AddConcertFestivalRunsComboBox;

    @FXML
    private TextField AddConcertNameTextField;

    @FXML
    private TextField AddConcertDescriptionTextField;

    @FXML
    private TextField AddConcertDateTextField;

    @FXML
    private TextField AddConcertPerformerTextField;

    @FXML
    private TextField AddConcertDurationTextField;

    /**
     * This method used for moving back to main menu.
     * @param e
     * @throws IOException
     */
    public void BackAddConcert(ActionEvent e) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
        s.setTitle("Main");
        s.setScene(scene);
        s.show();

    }

    /**
     * This method runs when add button is clicked and collect necessary data from text field and add the given concert.
     * @param e
     * @throws IOException
     */
    public void AddButtonAddConcert(ActionEvent e) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/addconcert").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json; utf-8");
        connection.setDoInput(true); //Set the DoInput flag to true if you intend to use the URL connection for input
        connection.setDoOutput(true);
        String festivalrunid = AddConcertFestivalRunsComboBox.getValue().toString().split("-")[0];

        JSONObject concert = new JSONObject();
        concert.put("name" , AddConcertNameTextField.getText());
        concert.put("description" ,  AddConcertDescriptionTextField.getText());
        concert.put("duration" , AddConcertDurationTextField.getText());
        concert.put("date" , AddConcertDateTextField.getText());
        concert.put("performer", AddConcertPerformerTextField.getText());


        JSONObject festivalrun = new JSONObject();
        festivalrun.put("festivalrunid" , festivalrunid);
        concert.put("festivalrun" , festivalrun);
        System.out.println(festivalrun.toJSONString());

        try(OutputStream os = connection.getOutputStream()){
            byte[] input = concert.toJSONString().getBytes(StandardCharsets.UTF_8);
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
     * This function initialize the festivals combobox and get festival data from database.
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
                AddConcertFestivalsComboBox.getItems().add(temp.get("festivalid") + "-" +temp.get("festivalname"));
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
                AddConcertFestivalsComboBox.getItems().add(object.get("festivalid") + "-" + object.get("festivalname"));

            }


        }
    }

    /**
     * This method runs when festival combobox is on action and return us the festivalruns of selected festival.
     * @throws ParseException
     * @throws IOException
     */
    public void onAddConcertFestivalsComboBoxChange() throws ParseException, IOException {
        AddConcertFestivalRunsComboBox.getItems().clear(); //for clearing each time the the selected festival is changed
        String festivalid = AddConcertFestivalsComboBox.getValue().toString().split("-")[0];

        String response = "";
        HttpURLConnection connection = (HttpURLConnection)new URL("http://localhost:8080/getallfestivalruns/" + festivalid).openConnection();
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
                AddConcertFestivalRunsComboBox.getItems().add(temp.get("festivalrunid") + "-" + temp.get("name"));
            }
            catch(Exception e){
                String response2 = "";
                HttpURLConnection connection2 = (HttpURLConnection)new URL("http://localhost:8080/getfestivalrun/" + array.get(i)).openConnection();
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
                AddConcertFestivalRunsComboBox.getItems().add(object.get("festivalrunid") + "-" + object.get("name"));
            }
        }


        /*JSONObject temp = (JSONObject) array.get(festivalid);
        AddConcertFestivalsComboBox.getItems().add(temp.get("festivalid") + "-" +temp.get("festivalname"));*/




    }



}
