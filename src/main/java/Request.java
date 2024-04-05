import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Request {

    private String method;
    private String url;
    private String httpVersion;
    private HashMap<String, String> headers;
    private String body;

    Request(InputStream inputStream) {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(inputStream));

            String startline = in.readLine();
            if (startline == null)
                return;

            String[] request = startline.split(" ");
            this.method = request[0];
            this.url = request[1];
            this.httpVersion = request[2];

            if (headers == null)
                headers = new HashMap<>();
            boolean inBody = false;
            while (in.ready()) {
                String line = in.readLine();
                if (line.equals("")) {
                    inBody = true;
                } else if (inBody) {
                    this.body += line + "\n";
                } else {
                    String[] lineParts = line.split(": ");
                    this.headers.put(lineParts[0].toLowerCase(), lineParts[1].trim());
                }
            }

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

    }

    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return this.url;
    }

    public String getHttpVersion() {
        return this.httpVersion;
    }

    public String getHost() {
        return this.headers.get("host");
    }

    public String getUserAgent() {
        return this.headers.get("user-agent");
    }

    public String getAccept() {
        return this.headers.get("accept");
    }

    public String getAcceptLanguage() {
        return this.headers.get("accept-language");
    }

    public String getAcceptEncoding() {
        return this.headers.get("accept-encoding");
    }

    public String getConnection() {
        return this.headers.get("connectioin");
    }

    public Boolean getUpgradeInsecureRequests() {
        return Boolean.valueOf(this.headers.get("upgrade-insecure-requests"));
    }

    public String getContentType() {
        return this.headers.get("content-type");
    }

    public Integer getContentLength() {
        return Integer.valueOf(this.headers.get("content-length"));
    }

    public String getHeader(String header) {
        if (this.headers.containsKey(header)) {
            return this.headers.get(header);
        }
        return "";
    }

    public String getBody() {
        return this.body;
    }
}
