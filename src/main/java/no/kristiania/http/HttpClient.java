package no.kristiania.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;

public class HttpClient {


    private final int statusCodeVer2;

    private final HashMap<String, String> headerFields = new HashMap<>();
    private final String messageBody;

    public HttpClient(String host, int port, String requestTarget) throws IOException {
        Socket socket = new Socket(host, port);


        socket.getOutputStream().write(
                ("GET " + requestTarget + " HTTP/1.1\r\n" +
                        "Connection: close\r\n" +
                        "Host: " + host + "\r\n" +
                        "\r\n").getBytes()
        );

        String statusLine = readLine(socket);


        String headerLine;

        while (!(headerLine = readLine(socket)).isBlank()) {
            int colonPos = headerLine.indexOf(':');
            String key = headerLine.substring(0, colonPos).trim();
            String value = headerLine.substring(colonPos+1).trim();
            headerFields.put(key, value);
        }

        this.messageBody = readCharacters(socket, getContentLength());



        this.statusCodeVer2 = Integer.parseInt(statusLine.split(" ")[1]);


    }

    private String readCharacters(Socket socket, int contentLength) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream in = socket.getInputStream();

        for (int i = 0; i < contentLength; i++) {
            result.append((char) in.read());
        }
        return result.toString();
    }

    private String readLine(Socket socket) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream in = socket.getInputStream();

        int c;

        while ((c = in.read()) != -1 && c != '\r') {

            result.append((char)c);
        }

        return result.toString();
    }



    public int getStatusCode() {
        return statusCodeVer2;
    }


    public String getHeader(String s) {
        return headerFields.get(s);
    }

    public int getContentLength() {
        return Integer.parseInt(getHeader("Content-Length"));
    }

    public String getMessageBody() {
        return messageBody;
    }
}
