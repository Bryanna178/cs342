package sample;

import java.io.Serializable;

public class SendingObj implements Serializable {

    private String msg;                     // whats being sent to server and back to the client from the server
    private Integer strikes;                // will be used to keep track is the player can keep guessing or not
    private boolean win;                    // used to identify the winner
    private boolean game = false;           // allows for player to still send guesses (is the game still on? has someone already guessed the word?)
    private String name;
    private int posOfGuess;                 // tells the user where in the string the char is found
    private int wordLen;                    // lets the user know how long the word is

    // default const
    SendingObj(){
        this.msg = "";
        this.strikes = 0;
        this.win = false;
        this.game = false;
        this.posOfGuess = -1;
        this.wordLen = -1;
    }

    SendingObj(SendingObj so2){
        this.msg = so2.msg;
        this.strikes = so2.strikes;
        this.win = so2.win;
        this.game = so2.game;
        this.posOfGuess = so2.posOfGuess;
        this.wordLen = so2.wordLen;
    }

    public void setMsg(String m){ this.msg = m;}
    public String getMsg(){ return this.msg;}

    public void setStrikes(int n){ this.strikes = n;}
    public int getStrikes(){ return this.strikes;}

    public void setWin(){ this.win = true;}
    public boolean getWinStatus(){ return this.win;}

    public void setGame(){ this.game = true;}
    public boolean getGame(){ return this.game;}

    public void setName(String s){ this.name = s;}
    public String getName(){ return this.name;}

    public void setPosOfGuess(int pos){ this.posOfGuess = pos;}
    public int getPosOfGuess(){ return this.posOfGuess;}

    public void setWordLen(int len){ this.wordLen = len;}
    public int getWordLen(){ return this.wordLen;}

}
