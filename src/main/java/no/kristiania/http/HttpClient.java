package no.kristiania.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HttpClient {

    public HttpClient(String host, int port, String requestTarget) {
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("httpbin.org", 80);

        socket.getOutputStream().write(
                ("GET /html HTTP/1.1\r\n" +
                        "Connection: close\r\r" +
                        "Host: httpbin.org\r\n" +
                        "\r\n").getBytes()
        );

        InputStream in = socket.getInputStream();

        int c;
        //-1 er ikke en gyldig byte. Derfor vil while loopen fortsette helt til
        //det det forekommer bytes som ikke er gyldig.
        while ((c = in.read()) != -1) {
            System.out.print((char)c);
        }
    }

    public int getStatusCode() {
        return 200;
    }
}
