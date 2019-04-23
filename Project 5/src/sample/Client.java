package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Client implements Runnable, Serializable{
    private String Name;
    private String played;
    private Boolean playAgain;
    private transient Socket socket;
    transient ObservableList<String> Played;
    private ObjectOutputStream cliOutput;
    private ObjectInputStream cliInput;

    //constructor
    public Client(String name, String ip, int port) throws IOException {
        this.Name = name;
        this.played = " ";
        this.playAgain = false;
        this.socket = new Socket(ip, port);
        this.Played = FXCollections.observableArrayList();

        // added so that each Client obj has its own input and output stream
        this.cliOutput = new ObjectOutputStream(this.socket.getOutputStream());          // input and output streams
        this.cliInput = new ObjectInputStream(this.socket.getInputStream());
        socket.setTcpNoDelay(true);
    }

    public Client(Client c1) throws IOException{
        this.Name = c1.Name;
        this.played = c1.played;
        this.playAgain = c1.playAgain;
        this.socket = c1.socket;
        this.Played = c1.Played;

        // added so that each Client obj has its own input and output stream
        this.cliOutput = c1.cliOutput;
        this.cliInput = c1.cliInput;
        socket.setTcpNoDelay(true);
    }

    // not sure whats supposed to go here... maybe nothing is fine...?
    public void run(){
        try {
            while (true) {
                Serializable data = (Serializable) cliInput.readObject();
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
}
