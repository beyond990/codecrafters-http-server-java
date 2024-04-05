import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientHandler implements Runnable {

    private Socket client;
    private Path directory;
    private Request request;
    private Response response;

    public ClientHandler(Socket client, Path directory) {
        this.directory = directory;
        Thread thread = Thread.currentThread();
        System.err.println("clientHandler-thread: " + thread.threadId());
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("Running Handler");
        try {
            request = new Request(client.getInputStream());
            response = new Response(client.getOutputStream());
            if (request.getMethod() != null && request.getMethod().equals("GET")) {
                System.out.println("In GET");
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
                } else if (request.getUrl().startsWith("/files/")) {
                    Path filePath = Paths.get(this.directory.toString(), request.getUrl().replace("/files/", ""));
                    File file = new File(filePath.toString());
                    if (file.exists()) {
                        response.sendFile(file);
                    } else {
                        response.setResponseCode(HttpCodes.NOT_FOUND);
                        response.sendResponseCodeOnly();
                    }
                } else {
                    response.setResponseCode(HttpCodes.NOT_FOUND);
                    response.sendResponseCodeOnly();
                }
            } else if (request.getMethod() != null && request.getMethod().equals("POST")) {
                System.out.println("In POST");
                if (request.getUrl().startsWith("/files")) {
                    Path filePath = Paths.get(this.directory.toString(), request.getUrl().replace("/files/", ""));
                    try {
                        request.saveFile(filePath);
                        response.setResponseCode(HttpCodes.CREATED);
                    } catch (IOException e) {
                        System.err.println("Unable to write file: " + filePath.toString());
                        response.setResponseCode(HttpCodes.BAD_REQUEST);
                    }
                    response.sendResponseCodeOnly();
                } else {
                    response.setResponseCode(HttpCodes.BAD_REQUEST);
                    response.sendResponseCodeOnly();
                }
            } else {
                System.err.println("Bad Method");
            }
            this.client.close();
        } catch (IOException e) {
            System.err.println("IOExecption: " + e.getMessage());
        }
        System.out.println("Leaving Handler");
    }
}
