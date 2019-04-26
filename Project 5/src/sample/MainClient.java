package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainClient extends Application {

    private ArrayList<Thread> threads = new ArrayList<>();
    private VBox cliOpeningOptions = new VBox();            // will store all the text boxes and the labels
    private HBox cliChoices = new HBox();                   // holds the clients choices to shoot
    private HBox gameOptions = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setScene(ClientPort(primaryStage));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception{
        super.stop();

        for (Thread e : threads){
            e.interrupt();
        }
    }

    public Scene ClientPort(Stage secondaryStage){

        Text text1 = new Text("Name");
        Text text2 = new Text("Host Name");
        Text text3 = new Text("Port Number");

        //in case wrong port number was entered (error message)
        Label error = new Label();
        error.setTextFill(Color.RED);

        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        TextField textField3 = new TextField();
        Button btn = new Button("Enter");

        cliOpeningOptions.getChildren().addAll(text1,textField1,text2,textField2,text3,textField3,btn,error);
        cliOpeningOptions.setBackground(new Background(makeBackgroundImage("file:src/sample/mountain.jpg",300)));


        Scene clientScene = new Scene(cliOpeningOptions, 540, 370);      //place pane in scene

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    Client client = new Client(textField1.getText(), textField2.getText(), Integer.parseInt(textField3.getText()));

                    secondaryStage.hide();
                    secondaryStage.setScene(ClientGUI(client));
                    secondaryStage.show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (NumberFormatException e){
                    error.setText("Invalid port number.");
                }
            }
        });
        return clientScene;
    }

    public Scene ClientGUI(Client s) throws IOException {
        GridPane pane = new GridPane();

        //buttons to choose to play again or quit
        Button playAgain = new Button();
        playAgain.setGraphic(makePic("file:src/sample/replay.jpg",80));
        Button quit = new Button();
        quit.setGraphic(makePic("file:src/sample/exit.gif",80));

        Images hm1 = new Images("hm1", "file:src/sample/HM01.JPG");
        Images hm2 = new Images("hm2", "file:src/sample/HM02.JPG");
        Images hm3 = new Images("hm3", "file:src/sample/HM03.JPG");
        Images hm4 = new Images("hm4", "file:src/sample/HM04.JPG");
        Images hm5 = new Images("hm5", "file:src/sample/HM05.JPG");
        Images hm6 = new Images("hm6", "file:src/sample/HM06.JPG");
        Images hm7 = new Images("hm7", "file:src/sample/HM07.JPG");
        Images hm8 = new Images("hm8", "file:src/sample/HM08.JPG");
        Images hm9 = new Images("hm9", "file:src/sample/HM09.JPG");
        Images hm10 = new Images("hm10", "file:src/sample/HM010.JPG");
        Images hm11 = new Images("hm11", "file:src/sample/HM011.JPG");
        Images hm12 = new Images("hm12", "file:src/sample/HM012.JPG");

        gameOptions.getChildren().addAll(playAgain,quit);
        gameOptions.setSpacing(30);

        Label t1 = new Label("Guess a Letter or a Word: ");
        t1.setFont(new Font("Cambria",30));
        t1.setTextFill(Color.rgb(0,255,150));

        Label t2 = new Label("Words & Letters Guessed: ");
        t2.setFont(new Font("Cambria",40));
        t2.setTextFill(Color.rgb(0,100,255));

        ListView<String> list = new ListView<String>();
        ObservableList<String> played = FXCollections.observableArrayList();

        TextField txt = new TextField();

        pane.add(t1,0,0);
        pane.add(txt, 0, 1);
        pane.add(t2, 0, 2);
        pane.add(list,0,3);
        pane.addRow(5,cliChoices);
        pane.addRow(6,gameOptions);

        playAgain.setOnAction((event) -> {
            //will need to handle the rematch stuff
        });

        quit.setOnAction((event) -> {
            System.exit(-1);        // will need to change this
            // and adjust to code... close stuff
        });

        txt.setPrefHeight(30);
        list.setPrefHeight(250);
        list.setItems(played);

        pane.setBackground(new Background(makeBackgroundImage("file:src/sample/space.jpg",400)));

        Scene client = new Scene(pane, 850, 680);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        return client;
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

    // helps make an imageview to set image for buttons
    public ImageView makePic(String filename,int hNw){
        Image pic = new Image(filename);
        ImageView v = new ImageView(pic);
        v.setFitHeight(hNw);
        v.setFitWidth(hNw);
        v.setPreserveRatio(true);
        return v;
    }

    public synchronized void makePlay(String c, ObservableList<String> played, Client s){
        try {
            Client c2 = new Client(s);
            played.clear();
            played.add(c);
            s.setPlayed(c);

            //s.getCliObjOut().writeObject(s.getPlayed());
            s.getCliObjOut().writeObject(s.getPlayed());
            s.getCliObjOut().flush();
            System.out.println("sent " + s.getPlayed());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
