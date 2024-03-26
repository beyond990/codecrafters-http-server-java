
enum HttpCodes {
    OK("200 OK"),
    NOT_FOUND("404 Not Found");

    private String response;

    HttpCodes(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "HTTP/1.1 " + this.response + "\r\n";
    }
}
