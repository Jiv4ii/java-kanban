package tests;

import HTTP.HttpTaskServer;
import HTTP.KVServer;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import entities.*;
import interfaces.TaskManager;
import logic.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class HttpTaskServerTest {
    private final Task testTask = new Task(0, "Task", "Task testring", Status.NEW, Instant.now(), Duration.ofHours(15));
    private final EpicTask testEpic = new EpicTask(1, "TestEpic", "Epic Testing");
    private final SubTask testSub1 = new SubTask(2, "SubTask1", "Sub Testing", Status.NEW, 1, Instant.now().plus(Duration.ofDays(1)), Duration.ofHours(15));
    private final SubTask testSub2 = new SubTask(3, "SubTask2", "Sub Testing", Status.NEW, 1, Instant.now().minus(Duration.ofDays(2)), Duration.ofHours(15));
    private final Gson gson = new GsonBuilder().create();

    private KVServer kvServer;
    private HttpTaskServer taskServer;

    @BeforeEach
    void create() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

        taskServer = new HttpTaskServer();
    }

    @AfterEach
    void stop() {
        kvServer.stop();
        taskServer.stop();
    }

    @Test
    void shouldAddTaskAndGetIt() throws IOException, InterruptedException {
        String json = gson.toJson(testTask);
        final HttpResponse<String> response = put(json);



        final HttpResponse<String> response2 = load("/tasks/task?id=0");
        String jsonResponce = response2.body();
        System.out.println(jsonResponce);
        Task jsonTask = gson.fromJson(jsonResponce, Task.class);



        final HttpResponse<String> response3 = load("/tasks/task/");
        Type taskListType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> listJson = gson.fromJson(response3.body(), taskListType);
        ArrayList<Task> list = new ArrayList<>(Arrays.asList(testTask));


        Assertions.assertEquals(200, response.statusCode(), "Вернулся Неверный код состояния");
        Assertions.assertEquals(testTask, jsonTask, "Вернулась неверная задача");
        Assertions.assertEquals(list, listJson, "Список задач отличается от исходного");
    }

    @Test
    void shouldAddEpicTaskWithoutSubsAndGetIt() throws IOException, InterruptedException {
        String json = gson.toJson(testEpic);
        final HttpResponse<String> response = put(json);


        final HttpResponse<String> response2 = load("/tasks/task?id=1");
        String jsonResponce = response2.body();
        EpicTask jsonEpic = gson.fromJson(jsonResponce, EpicTask.class);


        final HttpResponse<String> response3 = load("/tasks/epictask/");
        Type taskListType = new TypeToken<ArrayList<EpicTask>>() {
        }.getType();
        ArrayList<Task> listJson = gson.fromJson(response3.body(), taskListType);
        ArrayList<Task> list = new ArrayList<>(Arrays.asList(testEpic));


        Assertions.assertEquals(200, response.statusCode(), "Вернулся Неверный код состояния");
        Assertions.assertEquals(testEpic, jsonEpic, "Вернулась неверная задача");
        Assertions.assertEquals(list, listJson, "Список задач отличается от исходного");
    }

    @Test
    void shouldAddEpicTaskAndGetIt() throws IOException, InterruptedException {
        String json = gson.toJson(testEpic);
        put(json);


        String jsonSub = gson.toJson(testSub1);
        final HttpResponse<String> response = put(jsonSub);



        final HttpResponse<String> response2 = load("/tasks/task?id=2");
        String jsonResponce = response2.body();
        SubTask jsonSubs = gson.fromJson(jsonResponce, SubTask.class);



        final HttpResponse<String> response3 = load("/tasks/subtask/");
        Type taskListType = new TypeToken<ArrayList<SubTask>>() {
        }.getType();
        ArrayList<SubTask> listJson = gson.fromJson(response3.body(), taskListType);
        ArrayList<SubTask> list = new ArrayList<>(Arrays.asList(testSub1));


        Assertions.assertEquals(200, response.statusCode(), "Вернулся Неверный код состояния");
        Assertions.assertEquals(testSub1, jsonSubs, "Вернулась неверная задача");
        Assertions.assertEquals(list, listJson, "Список задач отличается от исходного");
    }

     HttpResponse<String> load(String path) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080" + path);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
            return  client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    HttpResponse<String> put(String json) throws IOException, InterruptedException{
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/task");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();
            return   client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    HttpResponse<String> delete(String path) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080" + path);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        return  client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void shouldDeleteById() throws IOException,InterruptedException{
        String json = gson.toJson(testTask);
        put(json);

        String path = "/tasks/task?id=0";
        HttpResponse<String> response = delete(path);
        Assertions.assertEquals(200,response.statusCode());
    }

    @Test
    void shouldDeleteTasksByRequest() throws IOException,InterruptedException{
        String json = gson.toJson(testTask);
        put(json);

        String path = "/tasks/task/";
        HttpResponse<String> response = delete(path);
        Assertions.assertEquals(200,response.statusCode());
    }
    @Test
    void shouldDeleteEpicsByRequest() throws IOException,InterruptedException{
        String json = gson.toJson(testEpic);
        put(json);

        String path = "/tasks/epictask/";
        HttpResponse<String> response = delete(path);
        Assertions.assertEquals(200,response.statusCode());
    }

    @Test
    void shouldDeleteSubsByRequest() throws IOException,InterruptedException{
        String json = gson.toJson(testEpic);
        String jsonSub = gson.toJson(testSub1);
        put(json);
        put(jsonSub);

        String path = "/tasks/subtask/";
        HttpResponse<String> response = delete(path);
        Assertions.assertEquals(200,response.statusCode());
    }

    @Test
    void shouldGetPrioritizeTask() throws IOException,InterruptedException{
        String json = gson.toJson(testTask);
        String jsonEpic= gson.toJson(testEpic);
        String jsonSub1 = gson.toJson(testSub1);
        String jsonSub2 = gson.toJson(testSub2);
        put(json);
        put(jsonEpic);
        put(jsonSub1);
        put(jsonSub2);

        TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(task -> task.getStartTime().toEpochMilli()));
        TreeSet<Task> prioritizedTasks2 = new TreeSet<>(Comparator.comparing(task -> task.getStartTime().toEpochMilli()));
        prioritizedTasks.add(testTask);
        prioritizedTasks.add(testSub1);
        prioritizedTasks.add(testSub2);

        String path = "/tasks/prioritize";
        HttpResponse<String> response = load(path);

        Type taskListType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> listJson = gson.fromJson(response.body(), taskListType);
        for (Task task : listJson) {
            prioritizedTasks2.add(task);
        }
        Assertions.assertEquals(prioritizedTasks,prioritizedTasks2,"Приоритезированный список десериализован неправильно");
    }

    @Test
    void shouldGetHistory() throws IOException,InterruptedException{
        String json = gson.toJson(testTask);
        String jsonEpic= gson.toJson(testEpic);
        String jsonSub1 = gson.toJson(testSub1);
        String jsonSub2 = gson.toJson(testSub2);
        put(json);
        put(jsonEpic);
        put(jsonSub1);

        load("/tasks/task?id=0");
        load("/tasks/task?id=2");
        load("/tasks/task?id=1");
        load("/tasks/task?id=0");

        //Фокусы чтобы поставить время для Эпика
        TaskManager manager = new InMemoryTaskManager();
        manager.createTask(testTask);
        manager.createEpicTask(testEpic);
        manager.createSubTask(testSub1);
        EpicTask epic = (EpicTask) manager.getById(1);

        ArrayList<Task> history = new ArrayList<>(Arrays.asList(testSub1,epic,testTask));

        HttpResponse<String> response = load("/tasks/history/");
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray array = jsonElement.getAsJsonArray();
        ArrayList<Task> historyJson = new ArrayList<>();
        for (JsonElement element : array) {
            JsonObject jsonObject = element.getAsJsonObject();
            if(jsonObject.has("epicType")){
                EpicTask epicTask = gson.fromJson(jsonObject,EpicTask.class);
                historyJson.add(epicTask);
                continue;
            }
            if(jsonObject.has("subType")){
                SubTask subTask = gson.fromJson(jsonObject,SubTask.class);
                historyJson.add(subTask);
                continue;
            }
            Task task = gson.fromJson(jsonObject,Task.class);
            historyJson.add(task);

        }

        Assertions.assertEquals(history,historyJson,"История не соответствует");

    }

    @Test
    void shouldReturnEpicsSubTasks() throws IOException,InterruptedException{
        String jsonEpic= gson.toJson(testEpic);
        String jsonSub1 = gson.toJson(testSub1);
        String jsonSub2 = gson.toJson(testSub2);
        put(jsonEpic);
        put(jsonSub1);
        put(jsonSub2);

        ArrayList<SubTask> listSub= new ArrayList<>();
        listSub.add(testSub1);
        listSub.add(testSub2);

        HttpResponse<String> response = load("/tasks/subtask/epic/?id=1");
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        ArrayList<SubTask> listJsonSub= new ArrayList<>();
        for (JsonElement element : jsonArray) {
            SubTask sub = gson.fromJson(element,SubTask.class);
            listJsonSub.add(sub);
        }
        Assertions.assertEquals(listSub,listJsonSub,"Подзадачи Эпика не совпадают");



    }


}
