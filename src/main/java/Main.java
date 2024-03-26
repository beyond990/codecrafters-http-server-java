import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {

        System.out.println("Logs from your program will appear here!");
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            clientSocket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");
            Request request = new Request(clientSocket.getInputStream());
            Response response = new Response(clientSocket.getOutputStream());

            if (request.getMethod().equals("GET")) {
                if (request.getUrl().equals("/")) {
                    response.sendResponseCodeOnly();
                } else if (request.getUrl().startsWith("/echo")) {
                    response.setContentType("text/plain");
                    response.setBody(request.getUrl().replace("/echo/", ""));
                    response.sendResponse();
                } else {
                    response.setResponseCode(HttpCodes.NOT_FOUND);
                    response.sendResponseCodeOnly();
                }
            }
            clientSocket.close();

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

    }

}
