package sample;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientTest {

    @BeforeAll
    static void message1(){
        System.out.println("*** Starting Client Class Tests ***");
    }

    //test1
    @Test
    void setPlayed() {
        try {
            Client a = new Client("Michaelangelo", "127.0.0.1", 1234);
            a.setPlayed("Paper");
            assertEquals(a.getPlayed(), "Paper", "Function setPlayed should set played variable to Paper.\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //test2
    @Test
    void setNumPoints() {
        try {
            Client b = new Client("Donatello", "127.0.0.1", 1234);
            b.setNumPoints(3);
            assertEquals(b.getNumPoints(), 3, "Function setNumPoints should set points variable to 3.\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //test3
    @Test
    void setPlayAgain() {
        try {
            Client c = new Client("Leonardo", "127.0.0.1", 1234);
            c.setPlayAgain(true);
            assertTrue(c.getPlayAgain(), "setPlayAgain function should return true if player wishes to play again.\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //test4
    @Test
    void getName() {
        try {
            Client d = new Client("Raphael", "127.0.0.1", 1234);
            assertEquals(d.getName(), "Raphael", "getName function should return name entered in Client constructor.\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //test5
    @Test
    void getSocket() {
        try {
            Client e = new Client("Chancho", "127.0.0.1", 1234);
            assertEquals(e.getSocket().getPort(), 1234, "getSocket should return port number entered in client constructor.\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @AfterAll
    static void message2(){
        System.out.println("*** Finished with Client Class Tests ***");
    }
}
