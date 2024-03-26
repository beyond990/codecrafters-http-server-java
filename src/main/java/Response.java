import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Response {

    private BufferedWriter out;
    private String responseCode;
    private String contentType;
    private String contentLength;
    private String body;
    private String EOL = "\r\n";

    Response(OutputStream outputStream) {
        this.out = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        this.setResponseCode(HttpCodes.OK);
        this.setContentType("text/http");
    }

    public void setResponseCode(HttpCodes responseCode) {
        this.responseCode = responseCode.toString();
    }

    public void setContentType(String contentType) {
        this.contentType = "Content-Type: " + contentType;
    }

    public void setBody(String body) {
        this.body = body;
        this.contentLength = "Content-Length: " + Integer.toString(body.length());
    }

    public void sendResponseCodeOnly() {
        try {
            this.out.append(this.responseCode).append(EOL);
            this.out.append(EOL);
            this.out.flush();
        } catch (IOException e) {
            System.err.println("IOExcetion: " + e.getMessage());
        }
    }

    public void sendResponse() {
        try {
            this.out.append(this.responseCode).append(EOL);
            this.out.append(this.contentType).append(EOL);
            this.out.append(this.contentLength).append(EOL);
            this.out.append(EOL);
            this.out.append(body).append(EOL);
            this.out.flush();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

}
