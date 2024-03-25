import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible
        // when running tests.
        Main main = new Main();
        main.run();
    }

    private void run() {

        System.out.println("Logs from your program will appear here!");
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        String EOL = "\r\n";
        String OK = "HTTP/1.1 200 OK" + EOL;
        String NOT_FOUND = "HTTP/1.1 404 Not Found" + EOL;

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            clientSocket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");

            List<String> headers = readRequest(clientSocket.getInputStream());
            String[] request = headers.get(0).split(" ");
            if (request[0].equals("GET")) {
                if (request[1].equals("/")) {
                    clientSocket.getOutputStream().write(OK.getBytes(StandardCharsets.UTF_8));
                    clientSocket.getOutputStream().write(EOL.getBytes(StandardCharsets.UTF_8));
                } else if (request[1].startsWith("/echo")) {
                    String echo = request[1].replace("/echo/", "");
                    String content_type = "Content-Type: text/plain" + EOL;
                    String content_length = "Content-Length: " + Integer.toString(echo.length()) + EOL;
                    clientSocket.getOutputStream().write(OK.getBytes(StandardCharsets.UTF_8));
                    clientSocket.getOutputStream()
                            .write(content_type.getBytes(StandardCharsets.UTF_8));
                    clientSocket.getOutputStream()
                            .write(content_length.getBytes(StandardCharsets.UTF_8));
                    clientSocket.getOutputStream().write(EOL.getBytes(StandardCharsets.UTF_8));
                    clientSocket.getOutputStream().write(echo.getBytes(StandardCharsets.UTF_8));
                    clientSocket.getOutputStream().write(EOL.getBytes(StandardCharsets.UTF_8));
                } else {
                    clientSocket.getOutputStream().write(NOT_FOUND.getBytes(StandardCharsets.UTF_8));
                    clientSocket.getOutputStream().write(EOL.getBytes(StandardCharsets.UTF_8));
                }
            }
            clientSocket.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

    }

    private List<String> readRequest(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        List<String> headers = new ArrayList<String>();

        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            if (!line.equals("")) {
                headers.add(line);
            }
        }

        return headers;
    }

}
