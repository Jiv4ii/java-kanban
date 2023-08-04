import HTTP.HttpTaskServer;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import logic.HttpTaskManager;
import HTTP.KVServer;
import entities.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args) throws CustomException {
        try {
            new HttpTaskServer();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Gson gson = new Gson();

        Task testTask = new Task(0, "Task", "Task testring", Status.NEW, Instant.now(), Duration.ofHours(15));
        EpicTask testEpic = new EpicTask(1, "TestEpic", "Epic Testing");
        SubTask testSub1 = new SubTask(2, "SubTask1", "Sub Testing", Status.NEW, 1, Instant.now().minus(Duration.ofDays(1)), Duration.ofHours(15));
        SubTask testSub2 = new SubTask(3, "SubTask2", "Sub Testing", Status.NEW, 1, Instant.now().plus(Duration.ofDays(2)), Duration.ofHours(15));
        String json = gson.toJson(testEpic);
        String json2 = gson.toJson(testSub1);
        String json3 = gson.toJson(testTask);
        try {
            put(json, "/epictask");
            put(json2, "/subtask");
            put(json3,"/task");
            System.out.println(load("/Id?id=0").body());
            System.out.println(load("/Id?id=1").body());
            System.out.println(load("/history").body());


        } catch (IOException | InterruptedException e){
            System.out.println("sd");
        }


    }

    static HttpResponse<String> load(String path) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks" + path);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    static HttpResponse<String> put(String json, String path) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks" + path);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    static HttpResponse<String> delete(String path) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks" + path);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
