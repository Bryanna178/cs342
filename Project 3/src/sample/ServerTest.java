package sample;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    void setNumClients() {
        try {
            Server a = new Server(1234);
            a.setNumClients(2);
            assertEquals(a.getNumClients(), 2, "setNumClients should return the correct number of clients playing.\n");
        } catch(IOException e){
            e.printStackTrace();
        }


    }

    @Test
    void setIsWinner() {
        try {
            Server a = new Server(1234);
            a.setIsWinner(true);
            assertTrue(a.getIsWinner(), "If client is a winner return true.\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Test
    void setWinner() {
        try {
            Server a = new Server(1234);
            a.setWinner("Client 1");
            assertEquals(a.getWinner(), "Client 1", "setWinner should return the correct winner's name.\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Test
    void setServerStatus() {
        try {
            Server a = new Server(1234);
            a.setServerStatus(true);
            assertTrue(a.getServerStatus(), "setServerStatus should return true.\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Test
    void setClients() {
        try {
            Server a = new Server(1234);
            Client b = new Client("Donatello", "127.0.0.1", 1234);
            ArrayList<Client> clients = new ArrayList<>();
            clients.add(b);
            a.setClients(clients);
            assertEquals(a.getClients().get(0).getName(), "Donatello", "setClients should properly store clients in arrayList.\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}