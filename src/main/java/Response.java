import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Response {

    private OutputStream outputStream;
    private BufferedWriter out;
    private String responseCode;
    private String contentType;
    private String contentLength;
    private String body;
    private String EOL = "\r\n";

    Response(OutputStream outputStream) {
        this.outputStream = outputStream;
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

    public void sendFile(File file) {
        try {
            this.out.append(this.responseCode).append(EOL);
            this.out.append("Content-Type: application/octetstream").append(EOL);
            this.out.append("Content-Length: ").append(Long.toString(file.length())).append(EOL);
            this.out.append(EOL);
            this.out.flush();
            byte[] byteArray = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(byteArray, 0, byteArray.length);
            this.outputStream.write(byteArray, 0, byteArray.length);
            this.outputStream.flush();
            bis.close();
        } catch (IOException e) {
            System.err.println("IOException : " + e.getMessage());
        }
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
            this.out.append(body);
            this.out.flush();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

}
