import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Logs from your program will appear here!");

        List<ClientHandler> clients = new ArrayList<>();
        ExecutorService pool = Executors.newCachedThreadPool();

        try {
            ServerSocket listner = new ServerSocket(4221);
            listner.setReuseAddress(true);
            while (!listner.isClosed()) {
                Socket client = listner.accept(); // Wait for connection from client.
                System.out.println("accepted new connection");
                ClientHandler clientHandler = new ClientHandler(client);
                clients.add(clientHandler);
                pool.execute(clientHandler);
            }
            listner.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

    }

}
