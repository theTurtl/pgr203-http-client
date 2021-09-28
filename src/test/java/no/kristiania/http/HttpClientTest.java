package no.kristiania.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {

    @Test
    void dummyTest() {
        assertEquals(13, 10+3);
    }

    @Test
    void ShouldReturnStatusCode() {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals(200, client.getStatusCode());
    }

    @Test
    void ShouldReturnStatusCode404() {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals(404, client.getStatusCode());
    }
}