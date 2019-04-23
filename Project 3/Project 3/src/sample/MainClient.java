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

    private TextField guessBox = new TextField();
    private Button sendGuess = new Button("Guess");

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
                    //OutputStream outputStream = client.getSocket().getOutputStream();          //getting output stream from the socket in Client
                    //ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream); //creates object from output stream (client object)
                    //objOutputStream.writeObject(client);                                       //sends client object to the server

                    Thread thread = new Thread(client);

//                    thread.setName("Server Thread");
                    thread.setDaemon(true);                         //sets current thread as a Daemon thread (else its a user thread)
                    thread.start();

                    //add thread to array list called threads
//                    threads.add(thread);

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
//        pane.setBackground(new Background(makeBackgroundImage("file:src/sample/space.jpg",400)));
//        Scene client = new Scene(pane, 850, 680);
//        pane.setPadding(new Insets(20));
//        pane.setHgap(10);
//        pane.setVgap(10);
        //pane.setAlignment(Pos.CENTER);

        //PlayingImages objects
        PlayingImages Rock = new PlayingImages("Rock", "file:src/sample/Rock.jpg");
        PlayingImages Paper = new PlayingImages("Paper", "file:src/sample/Paper.png");
        PlayingImages Scissors = new PlayingImages("Scissors", "file:src/sample/Scissors.png");
        PlayingImages Lizard = new PlayingImages("Lizard", "file:src/sample/Lizard.jpg");
        PlayingImages Spock = new PlayingImages("Spock", "file:src/sample/Spock.jpg");

        //clickable images to choose what to play
        Button rock = new Button();
        rock.setGraphic(new ImageView(Rock.getImage()));
        Button paper = new Button();
        paper.setGraphic(new ImageView(Paper.getImage()));
        Button scissors = new Button();
        scissors.setGraphic(new ImageView(Scissors.getImage()));
        Button lizard = new Button();
        lizard.setGraphic(new ImageView(Lizard.getImage()));
        Button spock = new Button();
        spock.setGraphic(new ImageView(Spock.getImage()));

        //buttons to choose to play again or quit
        Button playAgain = new Button();
        playAgain.setGraphic(makePic("file:src/sample/replay.jpg",80));
        Button quit = new Button();
        quit.setGraphic(makePic("file:src/sample/exit.gif",80));

        gameOptions.getChildren().addAll(playAgain,quit,guessBox,sendGuess);
        gameOptions.setSpacing(30);

        Label t1 = new Label("Points: ");
        // text for the points
        t1.setFont(new Font("Cambria",40));
        t1.setTextFill(Color.rgb(0,100,255));

        Label t2 = new Label("Played: ");
        t2.setFont(new Font("Cambria",40));
        t2.setTextFill(Color.rgb(0,100,255));

        Label t3 = new Label("Pick an Image to play: ");
        t3.setFont(new Font("Cambria",30));
        t3.setTextFill(Color.rgb(0,255,150));

        ObservableList<String> points = s.points;
        ListView<String> list = new ListView<String>();
        ObservableList<String> played = FXCollections.observableArrayList();
        ListView<String> l1 = new ListView<>();

        cliChoices.getChildren().addAll(rock,paper,scissors,lizard,spock);
        cliChoices.setSpacing(30);

        pane.add(t1,0,0);
        pane.add(list, 0, 1);
        pane.add(t2, 0, 2);
        pane.add(l1,0,3);
        pane.add(t3, 0, 4);
//        pane.add(rock, 0, 5);
//        pane.add(paper,1,5);
//        pane.add(scissors, 2,5);
//        pane.add(lizard, 3,5);
//        pane.add(spock,4,5);
        pane.addRow(5,cliChoices);
        pane.addRow(6,gameOptions);

     //   OutputStream outputStream = s.getSocket().getOutputStream();                //gets socket (info to server)
     //   ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream);  //its an object now yay

        rock.setOnAction((event) -> {
            // took out the objOutputStream because there was an error with there being more output streams
//            makePlay("Rock",played,s,objOutputStream);
            makePlay("Rock",played,s);
        });

        paper.setOnAction((event) -> {
//            makePlay("Paper",played,s,objOutputStream);
            makePlay("Paper",played,s);
        });

        scissors.setOnAction((event) -> {
//            makePlay("Scissors",played,s,objOutputStream);
            makePlay("Scissors",played,s);
        });

        lizard.setOnAction((event) -> {
//            makePlay("Lizard",played,s,objOutputStream);
            makePlay("Lizzzrd",played,s);
        });

        spock.setOnAction((event) -> {
//            makePlay("Spock",played,s,objOutputStream);
            makePlay("Spock",played,s);
        });

        playAgain.setOnAction((event) -> {
            //will need to handle the rematch stuff
        });

        quit.setOnAction((event) -> {
            System.exit(-1);        // will need to change this
                                        // and adjust to code... close stuff
        });

        sendGuess.setOnAction((event) -> {
            makeGuess(guessBox.getText(),played,s);
        });

        list.setItems(points);
        list.setPrefHeight(50);
        l1.setItems(played);
        l1.setPrefHeight(50);

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
            played.clear();
            played.add(c);
            //s.setPlayed(c);

            s.getSndObj().setMsg(c);
            s.getCliObjOut().writeObject(s.getSndObj());
            s.getCliObjOut().flush();
            System.out.println("sent " + s.getSndObj().getMsg());

//            s.getCliObjOut().writeObject(s.getPlayed());
//            s.getCliObjOut().flush();
//            System.out.println("sent " + s.getPlayed());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeGuess(String s, ObservableList<String> played, Client c){
        try{
            // tell the client what guesses were made was made
            played.add(s);

            c.getSndObj().setMsg(s);
            c.getCliObjOut().writeObject(c.getSndObj());
            c.getCliObjOut().flush();
            System.out.println("sent " + c.getSndObj().getMsg());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
