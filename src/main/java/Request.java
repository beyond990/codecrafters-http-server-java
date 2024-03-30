import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Request {

    private String method;
    private String url;
    private String httpVersion;
    private String host;
    private String userAgent;
    private String accept;
    private String acceptLanguage;
    private String acceptEncoding;
    private String connection;
    private boolean upgradeInsecureRequests;
    private String contentType;
    private Integer contentLength;
    private String body;

    Request(InputStream inputStream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));
            List<String> headers = new ArrayList<String>();

            boolean inBody = false;
            while (true) {
                String line = bufferedReader.readLine();
                if (line.equals("")) {
                    inBody = true;
                } else if (inBody) {
                    this.body += line + "\n";
                } else {
                    headers.add(line);
                }
            }
            String[] request = headers.get(0).split(" ");
            this.method = request[0];
            this.url = request[1];
            this.httpVersion = request[2];
            headers.remove(0);

            for (String header : headers) {
                String[] temp = header.split(":", 2);
                String key = temp[0];
                String value = temp[1].trim();
                switch (key.toLowerCase()) {
                    case "host":
                        this.host = value;
                        break;
                    case "user-agent":
                        this.userAgent = value;
                        break;
                    case "accept":
                        this.accept = value;
                        break;
                    case "accept-language":
                        this.acceptLanguage = value;
                        break;
                    case "accept-encoding":
                        this.acceptEncoding = value;
                        break;
                    case "connection":
                        this.connection = value;
                        break;
                    case "upgrade-insecure-requests":
                        this.upgradeInsecureRequests = Boolean.valueOf(value);
                        break;
                    case "content-type":
                        this.contentType = value;
                        break;
                    case "content-length":
                        this.contentLength = Integer.valueOf(value);
                        break;

                    default:
                        break;
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
        return this.host;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public String getAccept() {
        return this.accept;
    }

    public String getAcceptLanguage() {
        return this.acceptLanguage;
    }

    public String getAcceptEncoding() {
        return this.acceptEncoding;
    }

    public String getConnection() {
        return this.connection;
    }

    public Boolean getUpgradeInsecureRequests() {
        return this.upgradeInsecureRequests;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Integer getContentLength() {
        return this.contentLength;
    }

    public String getBody() {
        return this.body;
    }
}
