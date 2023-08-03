package HTTP;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    String path;
    public String token;

    private final HttpClient client;
    public KVTaskClient(String path) throws IOException {
        this.path = path;
        client = HttpClient.newHttpClient();
        URI url = URI.create(path + "/register");
        System.out.println(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {

            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            token = response.body();
        } catch ( InterruptedException e){
            System.out.println(e.getMessage());
        }

    }

    public void put(String key,String json) throws IOException{
        try {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(path + "/save/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();


            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch ( InterruptedException e){
            System.out.println(e.getMessage());
        }

    }

    public String load(String key) throws IOException{

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(path + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch ( InterruptedException e){
            System.out.println(e.getMessage());
            return null;
        }

    }
}
