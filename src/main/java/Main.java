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
        ServerSocket serverSocket = null;
        List<ClientHandler> clients = new ArrayList<ClientHandler>();
        ExecutorService pool = Executors.newCachedThreadPool();

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept(); // Wait for connection from client.
                System.out.println("accepted new connection");
                ClientHandler clientThread = new ClientHandler(client);
                clients.add(clientThread);
                pool.execute(clientThread);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

    }

}
