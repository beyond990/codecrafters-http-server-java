
enum HttpCodes {
    OK("200 OK"),
    CREATED("201 Created"),
    BAD_REQUEST("400 Bad Request"),
    NOT_FOUND("404 Not Found");

    private String response;

    HttpCodes(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "HTTP/1.1 " + this.response;
    }
}
