package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable, Serializable{
    private String Name;
    private String played;
    private Boolean playAgain;
    private transient Socket socket;
    transient ObservableList<String> Played;
    transient ObservableList<String> totalStrikes;   // used to keep track of how many wrong guesses have been made
    transient ObservableList<Images> gameStatus;     // displays the images that relate to the current state of the player
    private int strikes;
    private ObjectOutputStream cliOutput;
    private ObjectInputStream cliInput;

    // added
    private SendingObj sndObj;
    private ArrayList<Images> imagesArray = new ArrayList<>();
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

    //constructor
    public Client(String name, String ip, int port) throws IOException {
        this.Name = name;
        this.played = " ";
        this.playAgain = false;
        this.socket = new Socket(ip, port);
        this.Played = FXCollections.observableArrayList();

        //added
        this.strikes = 0;
        this.totalStrikes = FXCollections.observableArrayList();
        this.gameStatus = FXCollections.observableArrayList();
        this.sndObj = new SendingObj();
        this.sndObj.setName(name);

        // added the images to the image array
        this.imagesArray.add(hm1);
        this.imagesArray.add(hm2);
        this.imagesArray.add(hm3);
        this.imagesArray.add(hm4);
        this.imagesArray.add(hm5);
        this.imagesArray.add(hm6);
        this.imagesArray.add(hm7);
        this.imagesArray.add(hm8);
        this.imagesArray.add(hm9);
        this.imagesArray.add(hm10);
        this.imagesArray.add(hm11);
        this.imagesArray.add(hm12);

        // added so that each Client obj has its own input and output stream
        this.cliOutput = new ObjectOutputStream(this.socket.getOutputStream());          // input and output streams
        this.cliInput = new ObjectInputStream(this.socket.getInputStream());
        socket.setTcpNoDelay(true);
    }

    // what a client gets from the server               // PLAY AROUND WITH THIS....
    public synchronized void run(){
        try {
            while (true) {
//                System.out.println("waiting for serv to send");
//                SendingObj data = (SendingObj) cliInput.readObject();
//                this.sndObj = data;
//                System.out.println("from server "+data.getMsg() + "word len "+ data.getWordLen());
//                System.out.println("strikes... "+ data.getStrikes());
//                if(data.getMsg().equals("try again")){
//                    setHangmanImage(data.getStrikes(),imagesArray);
//                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //setter functions
    public void setPlayed(String x){
        this.played = x;
    }
    public void setPlayAgain(Boolean x){
        this.playAgain = x;
    }

    //added for the images
    public ObservableList<Images> getGameStatus(){ return this.gameStatus;}


    //getter functions
    public String getName(){
        return this.Name;
    }
    public String getPlayed(){
        return this.played;
    }
    public Boolean getPlayAgain(){
        return this.playAgain;
    }
    public Socket getSocket(){
        return this.socket;
    }

    public ObjectOutputStream getCliObjOut(){ return this.cliOutput;}
    public ObjectInputStream getCliInput(){ return this.cliInput;}

    //added
    public SendingObj getSndObj(){ return this.sndObj;}
    //maybe no need for setter...

//    // takes in the message from the server and displays image of where the player is at
//    public void setHangmanImage(int strikeNum, ArrayList<Images> picArray){
//        gameStatus.clear();     // clear out the current image
//        switch(strikeNum){
//            case 1:
//                gameStatus.add(picArray.get(0));
//                break;
//            case 2:
//                gameStatus.add(picArray.get(1));
//                break;
//        }
//    }
}
