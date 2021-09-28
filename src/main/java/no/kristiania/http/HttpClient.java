package no.kristiania.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.SQLOutput;

public class HttpClient {

    //private final int statusCode;
    private final int statusCodeVer2;

    public HttpClient(String host, int port, String requestTarget) throws IOException {
        Socket socket = new Socket(host, port);


        socket.getOutputStream().write(
                ("GET " + requestTarget + " HTTP/1.1\r\n" +
                        "Connection: close\r\n" +
                        "Host: " + host + "\r\n" +
                        "\r\n").getBytes()
        );

        //Med stringBuilder så kan en legge kontinuerlig legge en inn input.
        //Her gjør vi det for ytelse, da det kontant vil komme inn ny input fra InputStream
        //Her vil altså bytsene lest av while loopen komme. Igjen, i form av bytes (ASCII numre).
        //StringBuilder result = new StringBuilder();

        //Her tar vi imot "HTTP/1.1 200 OK"
        String statusLine = readLine(socket);
        //Her blir "HTTP/1.1 200 OK" gjort om til en array, hvor array.[1] (som er "200") parset til Int.
        //Deretter blir den returnert til variabelen statusCodeVer2.
        this.statusCodeVer2 = Integer.parseInt(statusLine.split(" ")[1]);

        //InputStream in = socket.getInputStream();

        //int c;

        //-1 er ikke en gyldig byte. Derfor vil while loopen fortsette helt til
        //det det forekommer bytes som ikke er gyldig. bytes (0 - 255 some r gyldig)
        //while ((c = in.read()) != -1) {
        //    result.append((char)c);
        //}

        //Her legger vi inn alle ASCII numrene i en ny variabel (bytsene), konvertert til String verdier (istedet for Int).
        //String responseMessage = result.toString();

        //Deretter lager vi et array bestemt av karakteren " " (mellomrom).
        //Eksempel: før: 23452312 340433 9032; etter: {"23452312", "340433", "9032"}
        //Så bestemmer vi hvilke av verdien vi vil hente fra arrayet, som her er [1] (altså andre verdi i arrayet).
        //Dette passser overens, da statuskoden som hentes fra server kommer rett etter requestTarget (HTTP/1.1).
        //Deretter plasserer vi denne verdien til en egen variabel, som vi kan senere returnere ved bruk (her: test klassen)
        //this.statusCode = Integer.parseInt(responseMessage.split(" ")[1]);
    }

    private String readLine(Socket socket) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream in = socket.getInputStream();

        int c;

        //Så lenge c (bytes) ikke er lik -1 og ikke starter fra start, så vil den fortsette å lese c.
        //Denen while loopen vil stoppe når InputStream in begynner på ny HTTP header (hver header begynner på linjestrat).
        //Derfor vil c her være = "HTTP/1.1 200 OK", bare i ASCII tall (bytes).
        while ((c = in.read()) != -1 && c != '\r') {
            //Her mater vi innholder fra c (som er fra InputStream in), i form av karakterer.
            result.append((char)c);
        }
        //Her returnerer vi "HTTP/1.1 200 OK" til variabelen ovenfor.
        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("httpbin.org", 80);

    }

    public int getStatusCode() {
        return statusCodeVer2;
    }


    public String getHeader(String s) {
        return null;
    }
}
