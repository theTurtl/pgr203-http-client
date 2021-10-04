package no.kristiania.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {

    @Test
    void dummyTest() {
        assertEquals(13, 10+3);
    }

    @Test
    void ShouldReturnStatusCode() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals(200, client.getStatusCode());
    }

    @Test
    void ShouldReturnStatusCode404() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/htl");
        assertEquals(404, client.getStatusCode());
    }

    @Test
    void ShouldReturnStatusHeaderFields() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals("text/html; charset=utf-8", client.getHeader("Content-Type"));
    }

    @Test
    void ShouldReadContentLenght() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");

        assertEquals(3741, client.getContentLenght());

        assertEquals(3741, client.getContentLength());

    }

    @Test
    void shouldReadMessageBody() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");

        assertTrue(
                client.getMessageBody().startsWith("\n<!DOCTYPE html>\n<htm"),

        assertTrue(client.getMessageBody().startsWith("\n<!DOCTYPE html>\n<htm"),
                "should be html: " + client.getMessageBody()

        );
    }
}