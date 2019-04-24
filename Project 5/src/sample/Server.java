package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
    private int numClients;
    private Boolean isWinner;
    private String winner;
    private int portNum;
    private ServerSocket serverSocket;
    private Socket socket;
    private ArrayList<Client> clients;
    ObservableList<String> clientsConnected;
    ObservableList<String> played;
    ObservableList<String> Winner;

    private ArrayList<CliThread> allCliConn;
    private int totalCli;               // used to distinguish a client and server

    //constructor
    public Server(int port) throws IOException {
        // TODO Auto-generated constructor stub
        this.numClients = 0;
        this.isWinner = false;
        this.winner = " ";
        this.portNum = port;
        this.clients = new ArrayList<>();
        this.clientsConnected = FXCollections.observableArrayList();
        this.played = FXCollections.observableArrayList();
        this.Winner = FXCollections.observableArrayList();

        this.allCliConn = new ArrayList<>();
        this.totalCli = 0;
    }

    public synchronized void run(){
        totalCli++;     // total is now 1

        try{
            ServerSocket serverSocket = new ServerSocket(this.getPortNum());
            System.out.println("Server made");

            //worry about player thing later
            while(true){
                CliThread c1 = new CliThread(serverSocket.accept(),totalCli);
                System.out.println("New cli joined");

                allCliConn.add(c1);     // adding client
                c1.start();

                clientsConnected.add("Client " + totalCli + " connected");
                totalCli++;     // add to the total clients in server
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("some error when creating cli thread");
        }
    }

    //setter functions
    public void setNumClients(int x){
        this.numClients = x;
    }
    public void setIsWinner(Boolean x){
        this.isWinner = x;
    }
    public void setWinner(String x){
        this.winner = x;
    }
    public void setClients(ArrayList<Client> x){
        this.clients = x;
    }

    //getter functions
    public int getNumClients(){
        return this.numClients;
    }
    public Boolean getIsWinner(){
        return this.isWinner;
    }
    public String getWinner(){
        return this.winner;
    }
    public int getPortNum(){
        return this.portNum;
    }
    public ServerSocket getServerSocket(){
        return this.serverSocket;
    }
    public Socket getSocket(){
        return this.socket;
    }
    public ArrayList<Client> getClients(){
        return this.clients;
    }

    // this is the object that is being stored in the server. could store in the points and its opponent thread
    class CliThread extends Thread{
        Socket cliSocket;
        int num;
        int cliPoints;

        ObjectOutputStream cliOutput;
        ObjectInputStream cliInput;

        // constructor for a new client thread
        CliThread(Socket s, int num){
            this.cliSocket = s;
            this.num = num;
            this.cliPoints = 0;
        }

        // getters for the client stuff
        public Socket getCliSocket(){ return this.cliSocket;}
        public int getCliNum(){ return this.num;}
        public ObjectOutputStream getCliObjOut(){ return this.cliOutput;}
        public ObjectInputStream getCliInput(){ return this.cliInput;}

        // whats going to run when the server receives something from any client
        public void run(){
            try{
                // create the input and output streams for the new client being connected
                ObjectOutputStream out = new ObjectOutputStream(this.cliSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(this.cliSocket.getInputStream());
                cliSocket.setTcpNoDelay(true);

                this.cliOutput = out;
                this.cliInput = in;

                // ready to enter the infinite loop where the server will be waiting for any client to send something over
                // (we need it to pass through the client object over)
                while(true){
                    // this is for a test for when what is sent over is a string
                    Serializable data = (Serializable) in.readObject();
                    System.out.println("rcvd "+data + " from " + this.num);

                    Platform.runLater(new Runnable() {
                        @Override
                        public synchronized void run() {
                            played.add(num + ": " + data);
                        }
                    });
                }
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("some error inside cli thread");
            }
        }
    }
}
