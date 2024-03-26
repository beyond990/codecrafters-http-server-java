import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Response {

    private OutputStreamWriter outputStreamWriter;
    private String responseCode;
    private String contentType;
    private String body;

    private String EOL = "\r\n";

    Response(OutputStream outputStream) {
        this.outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
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
    }

    public void sendResponseCodeOnly() {
        this.write(this.responseCode);
        this.flush();
    }

    public void sendResponse() {
        this.write(this.responseCode);
        this.write(this.contentType);
        this.write("Content-Length: " + Integer.toString(body.length()));
        this.write(EOL);
        this.write(body);
        this.flush();
    }

    private void write(String line) {
        try {
            this.outputStreamWriter.write(line + EOL);
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    private void flush() {
        try {
            this.outputStreamWriter.flush();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    public void close() {
        try {
            this.outputStreamWriter.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

}
