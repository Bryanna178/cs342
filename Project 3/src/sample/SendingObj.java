package sample;

import java.io.Serializable;

public class SendingObj implements Serializable {

    private String msg;
    private Integer strikes;
    private boolean win;

    // default const
    SendingObj(){
        this.msg = "";
        this.strikes = 0;
        this.win = false;
    }

    SendingObj(SendingObj so2){
        this.msg = so2.msg;
        this.strikes = so2.strikes;
        this.win = so2.win;
    }

    public void setMsg(String m){ this.msg = m;}
    public String getMsg(){ return this.msg;}

    public void setStrikes(int n){ this.strikes = n;}
    public int getStrikes(){ return this.strikes;}

    public void setWin(){ this.win = true;}
    public boolean getWinStatus(){ return this.win;}


}
