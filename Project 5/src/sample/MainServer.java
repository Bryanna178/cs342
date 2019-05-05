package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class MainServer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    Boolean suspended = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setScene(ServerPort(primaryStage));
        primaryStage.show();
    }

    // island pic is 380 x 285
    //displays a scene for the port number to be entered
    public Scene ServerPort(Stage primaryStage){
        GridPane portPane = new GridPane();                            //creates pane for Port to be entered

        Label portText = new Label("Please enter a port number: ");
        // text for portText
        portText.setFont(new Font("Cambria",20));
        portText.setTextFill(Color.rgb(255,0,100));
        portText.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,0), CornerRadii.EMPTY, Insets.EMPTY)));

        //in case wrong port number was entered (error message)
        Label error = new Label();
        error.setTextFill(Color.RED);

        TextField portTextField = new TextField();
        Button portButton = new Button("Enter");

        portPane.add(portText, 0,0);
        portPane.add(portTextField, 0, 1);
        portPane.add(portButton,0,2);
        portPane.add(error, 0, 3);

        portButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Server server = new Server(Integer.parseInt(portTextField.getText()));

                    primaryStage.hide();
                    primaryStage.setScene(ServerGUI(server));
                    primaryStage.show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch(IllegalArgumentException e){
                    error.setText("Invalid port number.");
                }
            }
        });

        portPane.setBackground(new Background(makeBackgroundImage("file:src/sample/island.jpg",350)));

        Scene portScene = new Scene(portPane, 600, 350);         //place pane in scene
        portPane.setPadding(new Insets(20));                        //padding between scene and pane
        portPane.setHgap(10);                                          //space between items placed in pane
        portPane.setVgap(10);
        portPane.setAlignment(Pos.CENTER);                             //sets everything inside the pane to the center

        return portScene;
    }

    public Scene ServerGUI(Server s){
        GridPane pane = new GridPane();
        pane.setBackground(new Background(makeBackgroundImage("file:src/sample/sunset.jpg",400)));

        Button onOff = new Button ("Server On/Off");

        Label text1 = new Label("Clients Connected: ");
        text1.setFont(new Font("Cambria",20));
        text1.setTextFill(Color.rgb(0,255,150));

        Label text2 = new Label("Words & Letters Guessed: ");
        text2.setFont(new Font("Cambria",20));
        text2.setTextFill(Color.rgb(0,255,150));

        Label text4 = new Label("Winner: ");
        text4.setFont(new Font("Cambria",20));
        text4.setTextFill(Color.rgb(0,255,150));

        ObservableList<String> clientsConnected = s.clientsConnected;
        ListView<String> l1 = new ListView<>();
        ObservableList<String> played = s.played;
        ListView<String> l2 = new ListView<>();
        ObservableList<String> Winner = s.Winner;
        ListView<String> l3 = new ListView<>();

        pane.add(onOff, 0, 0);
        pane.add(text1,0,1);        //clients connected
        pane.add(l1,0,2);
        pane.add(text2,0,3);        //what clients played
        pane.add(l2,0,4);
        pane.add(text4, 0, 5);      //winner
        pane.add(l3,0,6);

        Thread thread = new Thread(s);
        thread.setDaemon(true);
        thread.start();

        //Have buttons to turn on the server and turn it off
        onOff.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(suspended == false){
                    System.out.println("suspened");
                    thread.suspend();
                    suspended = true;
                }
                else{
                    System.out.println("resumed");
                    thread.resume();
                    suspended = false;
                }
            }
        });

        l1.setItems(clientsConnected);
        l1.setPrefHeight(100);
        l2.setItems(played);
        l2.setPrefHeight(200);
        l3.setItems(Winner);
        l3.setPrefHeight(50);

        pane.setPadding(new Insets(20));
        pane.setHgap(20);
        pane.setVgap(20);
        pane.setAlignment(Pos.CENTER);
        Scene server = new Scene(pane, 400, 570);

        return server;
    }

    // creates a background
    public BackgroundImage makeBackgroundImage(String filename, int wNh){
        Image clientPic = new Image(filename);
        ImageView background = new ImageView(clientPic);
        background.setFitWidth(wNh);
        background.setFitHeight(wNh);
        background.setPreserveRatio(true);
        // creates background
        return new BackgroundImage(clientPic, BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
    }
}
