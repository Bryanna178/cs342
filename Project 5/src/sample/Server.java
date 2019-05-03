package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

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
    private ArrayList<Character> lettersSoFar;          // stores the words that have been guessed so far
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
        this.wordsLst = new ArrayList<>();

        try{
            //read in dictionary.txt file
            File file = new File("src/sample/dictionary.txt");

            BufferedReader br = new BufferedReader(new FileReader(file));

            String s;
            while((s = br.readLine()) != null){
                //insert words into a arrayList
                this.wordsLst.add(s);
            }
        }
        catch(Exception e){
            System.out.println("file could not be read");
        }

        //need to choose random word from wordsLst before parsing word into a vector of characters (wordInParts)
        //also need to store word in variable actualWord in case user wants to guess full word
        this.actualWord = getRandWord(wordsLst);

        //added
        wordInParts = this.actualWord.toCharArray();
        this.remainingLetters = actualWord.length();
        this.lettersSoFar = new ArrayList<Character>();      // char array that is of the length of the word...
        for(int i = 0; i < remainingLetters; i++){
            lettersSoFar.add('_');
        }
        System.out.println("inside word arr ");
        for(char c: lettersSoFar){
            System.out.println(c);
        }

        System.out.println("word list made... letters so far len " + lettersSoFar.size());

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

    //function to pick random index of array containing dictionary words
    public String getRandWord(ArrayList<String> words){
        Random rand = new Random();
        return words.get(rand.nextInt(words.size()));
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
                cliObj.setGuessedSoFar(lettersSoFar);
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
                        // if what the client sends is the correct thing... then they won
                        if(data.getMsg().equals(actualWord)){
                            sendWinnerNotice(data,out);
//                            System.out.println("WINNERRRR");
//                            data.setMsg("YOU WON");
//                            out.writeObject(data);      // send updated msg to client
//
//                            Winner.add("WINNER IS PLAYER "+ data.getName());
//
//                            // then send to everyone in the server that there is a winner
//                            SendingObj losingMsg = new SendingObj();
//                            losingMsg.setMsg("LOSER");
//                            // send out msg to losers
//                            for(CliThread ct: allCliConn){
//                                if(ct.num != this.num){
//                                    ct.getCliObjOut().writeObject(losingMsg);
//                                }
//                            }
                        }
                        // else it is not correct so keep trying
                        else{
                            int counter = 0;
                            char userGuess = data.getMsg().charAt(0);
                            for(char c: wordInParts){
                                System.out.println("char from word... "+c);
                                if(c == userGuess){
                                    System.out.println("correct letter");
                                    data.setMsg("good guess");
                                    data.addPosOfGuess(counter);
                                    lettersSoFar.remove(counter);        // adds the letter in the spot it belongs
                                    lettersSoFar.add(counter,c);
                                    remainingLetters--;                 // take away the remaining letters
//                                    out.writeObject(data);
                                }
                                counter++;
                            }

                            System.out.println("curr");
                            for(char c: lettersSoFar){
                                System.out.print(c);
                            }

                            // check if the word was solved for
                            if(remainingLetters == 0){
                                data.getGuessedSoFar().clear();
                                for(char c: lettersSoFar){
                                    data.getGuessedSoFar().add(c);
                                }
                                sendWinnerNotice(data,out);
                            }

                            // if positions were found and still no winner send the obj
                            if(data.getPosOfGuess().size() > 0 && !guessed){
                                data.getGuessedSoFar().clear();
                                for(char c: lettersSoFar){
                                    data.getGuessedSoFar().add(c);
                                }
                                out.writeObject(data);
                            }

                            // if no letter was guessed correctly
                            else if(!data.getMsg().equals("good guess")){
                                System.out.println("try again");
                                data.setMsg("try again");

                                //mark strike if it is incorrect guess
                                data.setStrikes(data.getStrikes() + 1);
                                out.writeObject(data);
                            }
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

        // need to somehow send over the status of the word to all the clients on the server...********************************************
        public void sendWinnerNotice(SendingObj data,ObjectOutputStream out){
            try{
                guessed = true;
                System.out.println(" WINNERRRR");                                    // MAKE INTO A FUNCTION
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
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        public void sendCurrWord(ArrayList<Character> charArr){         // FINISH!!!!!!!!!

        }

    }
}
