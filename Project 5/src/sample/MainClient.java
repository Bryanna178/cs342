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
    private Button sendGuess = new Button("Guess!");

    private SendingObj guiSendobj = new SendingObj();
    private ArrayList<Images> imagesArray = new ArrayList<>();
    private Button gameStatus = new Button();
    private TextField wordStatus = new TextField();

    private Button update = new Button("update");

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

                    //needed to get connection
                    Thread thread = new Thread(client);
                    thread.setDaemon(true);                         //sets current thread as a Daemon thread (else its a user thread)
                    thread.start();

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
        this.guiSendobj.setName(s.getName());        // sets name of the client obj thats being passed around
        GridPane pane = new GridPane();

        //buttons to choose to play again or quit
        Button playAgain = new Button();
        playAgain.setGraphic(makePic(new Images("replay","file:src/sample/replay.jpg").getImage(),80));
        Button quit = new Button();
        quit.setGraphic(makePic(new Images("quit","file:src/sample/exit.gif").getImage(),80));

        Images hm1 = new Images("hm1", "file:src/sample/HM01.JPG");
        Images hm2 = new Images("hm2", "file:src/sample/HM02.JPG");
        Images hm3 = new Images("hm3", "file:src/sample/HM03.JPG");
        Images hm4 = new Images("hm4", "file:src/sample/HM04.JPG");
        Images hm5 = new Images("hm5", "file:src/sample/HM05.JPG");
        Images hm6 = new Images("hm6", "file:src/sample/HM06.JPG");
        Images hm7 = new Images("hm7", "file:src/sample/HM07.JPG");
        Images hm8 = new Images("hm8", "file:src/sample/HM08.JPG");
        Images hm9 = new Images("hm9", "file:src/sample/HM09.JPG");
        Images hm10 = new Images("hm10", "file:src/sample/HM10.JPG");
        Images hm11 = new Images("hm11", "file:src/sample/HM11.JPG");
        Images hm12 = new Images("hm12", "file:src/sample/HM12.JPG");

        imagesArray.add(hm1);
        imagesArray.add(hm2);
        imagesArray.add(hm3);
        imagesArray.add(hm4);
        imagesArray.add(hm5);
        imagesArray.add(hm6);
        imagesArray.add(hm7);
        imagesArray.add(hm8);
        imagesArray.add(hm9);
        imagesArray.add(hm10);
        imagesArray.add(hm11);
        imagesArray.add(hm12);

        gameOptions.getChildren().addAll(playAgain,quit);
        gameOptions.setSpacing(30);
        gameStatus.setGraphic(makePic(imagesArray.get(0).getImage(),300));

        Label t1 = new Label("Guess a Letter or a Word: ");
        t1.setFont(new Font("Cambria",30));
        t1.setTextFill(Color.rgb(0,255,150));

        Label t2 = new Label("Words & Letters Guessed: ");
        t2.setFont(new Font("Cambria",40));
        t2.setTextFill(Color.rgb(0,100,255));

        Label t3 = new Label("Guessed so far");
        t3.setFont(new Font("Cambria",40));
        t3.setTextFill(Color.rgb(255,0,100));

        ListView<String> list = new ListView<String>();
        ObservableList<String> played = FXCollections.observableArrayList();

        TextField guessBox = new TextField();

        pane.add(t1,0,0);
        pane.add(guessBox, 0, 1);
        pane.add(sendGuess,1,1);
        pane.add(update,4,1);               // move around...
        pane.add(t2, 0, 2);
        pane.add(list,0,3);
        gameStatus.setRotate(90.0); //********add to rotate button for hangman to stay straight.
        pane.add(gameStatus,5,3);
        pane.add(t3,0,4);
        pane.add(wordStatus,0,5);
        pane.addRow(6,gameOptions);

        playAgain.setOnAction((event) -> {
            //will need to handle the rematch stuff
            //need to call getRandWord() to get a new word for a new game
            // need to tell server somehow that a client wants to replay?
            // only start game when there are 4 players in server
        });

        quit.setOnAction((event) -> {
            System.exit(-1);        // will need to change this
            // and adjust to code... close stuff
        });

        update.setOnAction((event) ->{
            requestUpdate(s);
            getUpdate(s,played);
        });

        sendGuess.setOnAction((event) ->{
            makeGuess(guessBox.getText(),played,s);
            try {
                SendingObj data = (SendingObj) s.getCliInput().readObject();
                this.guiSendobj = data;
                System.out.println("from server on gui " + data.getMsg() + " strikes "+ data.getStrikes());
                if(data.getMsg().equals("nope")){
                    played.add("NOT ALL PLAYERS CONNECTED PLEASE WAIT FOR ALL PLAYERS");
                }
                else if(data.getStrikes() <= 12){
                    // not a good guess so set a strike and display picture
                    if(data.getMsg().equals("try again")) {
                        System.out.println("TRY AGAIN");
                        setHangmanImage(data.getStrikes(), imagesArray);
                    }

                    // good guess, get position(s) of guessed letter
                    else if(data.getMsg().equals("good guess")){
                        System.out.println("good guess");
                        wordStatus.clear();
                        wordStatus.setText(data.getGuessedSoFar().toString());
                        data.getPosOfGuess().clear();               // once the positions have been used clear it for the next adds
                    }

                    // player lost game
                    else if(data.getMsg().equals("LOSER")){
                        System.out.println(data.getMsg());
                    }

                    // play won game
                    else if(data.getMsg().equals("YOU WON")){
                        wordStatus.clear();
                        wordStatus.setText(data.getGuessedSoFar().toString());
                        System.out.println(data.getMsg());
                    }
                }
                else{
                    // done at this point can no longer play...
                    sendGuess.setDisable(true);         // disable sending button till there is a winner
                    System.out.println("LOSER WAIT");
                    // still update the loser players stuff
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        // will get what the server sends when the client is created...         allows us to get the length of the word being used
        try{
            guiSendobj = (SendingObj)s.getCliInput().readObject();
            played.add("Total letters in word: "+ guiSendobj.getWordLen());
            wordStatus.clear();
            wordStatus.setText(guiSendobj.getGuessedSoFar().toString());       // COME BACK HEREEEEE
        }catch(Exception e){
            e.printStackTrace();
        }

        guessBox.setPrefHeight(30);
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
    public ImageView makePic(Image pic,int hNw){
        ImageView v = new ImageView(pic);
        v.setFitHeight(hNw);
        v.setFitWidth(hNw);
        v.setPreserveRatio(true);
        return v;
    }

    public synchronized void makeGuess(String s, ObservableList<String> played, Client c){      // made synched**
        try{
            // tell the client what guesses were made was made
            played.add(s);

            // this allows for the gui sending obj to update and display
            this.guiSendobj.setMsg(s);
            c.getCliObjOut().writeObject(this.guiSendobj);
            c.getCliObjOut().flush();
            System.out.println("sent " + this.guiSendobj.getMsg() + " with "+ this.guiSendobj.getStrikes() +" strikes********");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // takes in the message from the server and displays image of where the player is at
    public void setHangmanImage(int strikeNum, ArrayList<Images> picArray){
        gameStatus.setGraphic(makePic(picArray.get(strikeNum).getImage(),250));
    }

    public synchronized void requestUpdate(Client c){               // made synched**
        try{
//            this.guiSendobj.setMsg("update");
            SendingObj update = new SendingObj();
            update.setMsg("update");
            c.getCliObjOut().writeObject(update);
            c.getCliObjOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void getUpdate(Client c, ObservableList<String> played){                   // made synched**
        try{
            SendingObj data = (SendingObj) c.getCliInput().readObject();
            System.out.println("got update*********" + data.getMsg());
            for(char s: data.getGuessedSoFar()){
                System.out.print(s);
            }
            wordStatus.clear();
            wordStatus.setText(data.getGuessedSoFar().toString());

            // updates what everyone in the server has played
            played.clear();
            for(String s: data.getWhatUsersGuessed()){
                played.add(s);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}



// **************** need to create a space for the current state of the word... needs to be sent from the server so that everyone has the updated version************
// this space will also display if the player is a winner or not??? na have an additional space so that the users can see the whole word and the end game message (winner/ loser)
// preguntale a la bryanna