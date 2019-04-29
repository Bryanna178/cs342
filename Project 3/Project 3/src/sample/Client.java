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
    private int numPoints;
    private Boolean playAgain;
    private transient Socket socket;
    transient ObservableList<String> Played;
    transient ObservableList<String> points;

    private ObjectOutputStream cliOutput;
    private ObjectInputStream cliInput;

    private SendingObj sndObj;



    //constructor
    public Client(String name, String ip, int port) throws IOException {
        this.Name = name;
        this.played = " ";
        this.numPoints = 0;
        this.playAgain = false;
        this.socket = new Socket(ip, port);
        this.Played = FXCollections.observableArrayList();
        this.points = FXCollections.observableArrayList();

        // added so that each Client obj has its own input and output stream
        this.cliOutput = new ObjectOutputStream(this.socket.getOutputStream());          // input and output streams
        this.cliInput = new ObjectInputStream(this.socket.getInputStream());
        socket.setTcpNoDelay(true);

        this.sndObj = new SendingObj();         // obj that will be sent to the server
        this.sndObj.setName(name);              // setting the objects its name (owner)
    }

    // what a client gets from the server               // PLAY AROUND WITH THIS....
    public synchronized void run(){
        try {
            while (true) {
                System.out.println("waiting for serv to send");
                SendingObj data = (SendingObj) cliInput.readObject();
                this.sndObj = data;
                System.out.println("from server"+data.getMsg());
                System.out.println("strikes... "+ data.getStrikes());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //setter functions
    public void setPlayed(String x){
        this.played = x;
    }
    public void setNumPoints(int x){
        this.numPoints = x;
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
    public int getNumPoints(){
        return this.numPoints;
    }
    public Boolean getPlayAgain(){
        return this.playAgain;
    }
    public Socket getSocket(){
        return this.socket;
    }

    public ObjectOutputStream getCliObjOut(){ return this.cliOutput;}
    public ObjectInputStream getCliInput(){ return this.cliInput;}

    public SendingObj getSndObj(){ return this.sndObj;}
    public  void setSndObj(SendingObj so){ this.sndObj = so;}
}
