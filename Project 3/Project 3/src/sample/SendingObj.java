package sample;

import java.io.Serializable;

public class SendingObj implements Serializable {

    private String msg;                     // whats being sent to server and back to the client from the server
    private Integer strikes;                // will be used to keep track is the player can keep guessing or not
    private boolean win;                    // used to identify the winner
    private boolean game = false;           // allows for player to still send guesses (is the game still on? has someone already guessed the word?)
    private String name;

    // default const
    SendingObj(){
        this.msg = "";
        this.strikes = 0;
        this.win = false;
        this.game = false;
    }

    SendingObj(SendingObj so2){
        this.msg = so2.msg;
        this.strikes = so2.strikes;
        this.win = so2.win;
        this.game = so2.game;
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

}
