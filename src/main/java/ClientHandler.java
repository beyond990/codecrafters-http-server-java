import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket client;
    private Request request;
    private Response response;

    public ClientHandler(Socket client) {
        Thread thread = Thread.currentThread();
        System.err.println("clientHandler-thread: " + thread.threadId());
        this.client = client;
    }

    @Override
    public void run() {
        try {
            request = new Request(client.getInputStream());
            response = new Response(client.getOutputStream());
            if (request.getMethod() != null && request.getMethod().equals("GET")) {
                if (request.getUrl().equals("/")) {
                    response.sendResponseCodeOnly();
                } else if (request.getUrl().startsWith("/echo")) {
                    response.setContentType("text/plain");
                    response.setBody(request.getUrl().replace("/echo/", ""));
                    response.sendResponse();
                } else if (request.getUrl().equals("/user-agent")) {
                    response.setContentType("text/plain");
                    response.setBody(request.getUserAgent());
                    response.sendResponse();
                } else {
                    response.setResponseCode(HttpCodes.NOT_FOUND);
                    response.sendResponseCodeOnly();
                }
            }
            this.client.close();
        } catch (IOException e) {
            System.err.println("IOExecption: " + e.getMessage());
        }
    }
}
