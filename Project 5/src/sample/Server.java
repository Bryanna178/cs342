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

    // added for project 5
    private int countdown;      // messing around with this
    private ArrayList<String> wordsLst;
    private String actualWord;          // in the case user wants to guess the whole word/phrase
    private String takingApartWord;     // if guessing letter by letter
    private boolean guessed = false;

    //added 4/29/19
    private char[] wordInParts;
    private int remainingLetters;


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

        // pick the random word when the server is created****
        wordsLst = new ArrayList<>();
        wordsLst.add("yoyo");
        this.actualWord = wordsLst.get(0);
        this.takingApartWord = wordsLst.get(0);

        //added
        wordInParts = this.actualWord.toCharArray();
        this.remainingLetters = actualWord.length();

        System.out.println("word list made");

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

                //added
                allCliConn.add(c1);
                // placed on a Platform.runLater to fix the exception
                Platform.runLater(new Runnable() {
                    @Override
                    public synchronized void run() {
                        clientsConnected.add("Client " + c1.getCliNum() + " connected");
                    }
                });

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

        //added the sending object into the client thread
        SendingObj cliObj;      // contains points and what is being sent over to the server

        ObjectOutputStream cliOutput;
        ObjectInputStream cliInput;

        // constructor for a new client thread
        CliThread(Socket s, int num){
            this.cliSocket = s;
            this.num = num;
            this.cliPoints = 0;

            //added
            this.cliObj = new SendingObj();
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

                cliObj.setWordLen(actualWord.length());
                out.writeObject(cliObj);

                // ready to enter the infinite loop where the server will be waiting for any client to send something over
                // (we need it to pass through the client object over)
                while(true){
                    //added

                    // the sendingObj comes in from a client and the server deals with it here
                    //---------------------------------------------------------------------------------------------- PROJECT 5
                    SendingObj data = (SendingObj) in.readObject();
                    System.out.println("rcvd " + data.getMsg() + " from " + data.getName() + " player number " + this.num);

                    if(!guessed){

                        //------------------------------------------------------------------------------------------- neeed to fix this...
                        // checking the string if the guess is in it            // indexOf return non -1 if exists
                        // check for more of the same char in word...
//                        while(takingApartWord.indexOf(data.getMsg().charAt(0)) != -1){
//                            data.setMsg("good guess");
//                            data.setPosOfGuess(takingApartWord.indexOf(data.getMsg().charAt(0)));   // sends the index of the char in word
//                            out.writeObject(data);
//
//                            //spits up the word
//                            String[] parts = takingApartWord.split(data.getMsg(),1);    // lim is how many splits
//                            //put word back together after taking out the first occurance of the letter
//                            takingApartWord = "";
//                            for(String s: parts){
//                                takingApartWord = takingApartWord + s;
//                            }
//                            System.out.println("new word "+ takingApartWord);
//                        }
                        //-----------------------------------------------------------------------------------------


                        // if what the client sends is the correct thing... then they won
                        if(data.getMsg().equals(actualWord)){
                            System.out.println("WINNERRRR");
                            data.setMsg("YOU WON");
                            out.writeObject(data);      // send updated msg to client

                            Winner.add("WINNER IS PLAYER "+ data.getName());

                            // then send to everyone in the server that there is a winner
                            SendingObj losingMsg = new SendingObj();
                            losingMsg.setMsg("LOSER");
                            // send out msg to losers
                            for(CliThread ct: allCliConn){
                                if(ct.num != this.num){
                                    ct.getCliObjOut().writeObject(losingMsg);
                                }
                            }
                        }
                        // else it is not correct so keep trying
                        else{
                            System.out.println("try again");
                            data.setMsg("try again");

                            //mark strike if it is incorrect guess
                            data.setStrikes(data.getStrikes() + 1);
                            out.writeObject(data);
                        }
                    }



                    //---------------------------------------------------------------------------------------------     PROJECT 5

                    // probably move this up... where the data comes in
                    cliObj = data;      // reset the client object that pertains to this client thread...
                    System.out.println("new client "+ this.num + " obj "+ cliObj.getStrikes() +" stikes "+ cliObj.getMsg() + " msg, won: "+ cliObj.getWinStatus());

//                    // get what the client sent and send it to all clients on server...           // USE THIS IDEA TO CHECK FOR A WINNER
//                    for(CliThread ct: allCliConn){
//                        out.writeObject("get go " + data + " from someone ");
//                    }


                    Platform.runLater(new Runnable() {
                        @Override
                        public synchronized void run() {
                            played.add(data.getName() + " " + num + ": " + data.getMsg());

                            //displays points... may not be needed on server end
                            //points.add(data.getName() + ": " + cliObj.getStrikes() + " points");            // MAYBE MAKE data INTO cliObj FROM THE GET GO
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
