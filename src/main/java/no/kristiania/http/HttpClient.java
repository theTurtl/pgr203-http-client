package no.kristiania.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.HashMap;

public class HttpClient {

    //private final int statusCode;
    private final int statusCodeVer2;
    //HashMap er en fin måte å lagre sammenhengende informasjon på, hvor en kan finne informasjon ved å søke
    //etter sammenhengende informasjon. headerFields her er navnet på hver header. På denne måten kan du søke etter
    //bestemte headere, og alltid få sammenhengende informasjon som tilhører bestemte header.
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

        //Dette er et eksempel på en kode som vil legge inn et sammenhengende datasett i opprettet HashMap.
        //headerFields.put("Content-Type", "text/html; charset=utf-8");

        //Med stringBuilder så kan en legge kontinuerlig legge en inn input.
        //Her gjør vi det for ytelse, da det kontant vil komme inn ny input fra InputStream
        //Her vil altså bytsene lest av while loopen komme. Igjen, i form av bytes (ASCII numre).
        //StringBuilder result = new StringBuilder();

        //Her tar vi imot "HTTP/1.1 200 OK"
        String statusLine = readLine(socket);

        //Opretter en ny variabel som lagrer String
        String headerLine;
        //Denne while løkken vil lese av alle headere til en blank linje oppstår, slik som HTTP protokollen tilsier.
        while (!(headerLine = readLine(socket)).isBlank()) {
            //Her finner definerer vi indexen for hvor kolon oppstår i input. Ettersom verdien av alle headere
            //kommer etter en kolonn, så kan vi på denne måten skille disse to verdiene (header, headerVerdi)
            int colonPos = headerLine.indexOf(':');
            //Her definerer vi hvilke verdier som hører til hva
            //trim() brukes for å fjerne whitespace, noe som er viktig for korrekt lesing av verdi!
            String key = headerLine.substring(0, colonPos).trim();
            //Samme her.
            String value = headerLine.substring(colonPos+1).trim();
            //Deretter hiver vi disse verdiene i HashMap headerFields!
            headerFields.put(key, value);
        }

        this.messageBody = readCharacters(socket, getContentLenght());


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
        return headerFields.get(s);
    }

    public int getContentLenght() {
        return Integer.parseInt(getHeader("Content-Length"));
    }

    public String getMessageBody() {
        return messageBody;
    }
}
