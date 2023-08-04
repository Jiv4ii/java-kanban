package tests;

import entities.*;
import logic.HttpTaskManager;
import HTTP.KVServer;
import HTTP.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import logic.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpTaskManagerTest extends TaskManagerTest{
    private KVServer server;

    private KVTaskClient client;
    private Gson gson = new Gson();
    private String path = "http://localhost:8078";


    @BeforeEach
    void create() throws IOException {
        server = new KVServer();
        server.start();
        manager = new HttpTaskManager(path);
        try {
            client = new KVTaskClient(path);
        } catch (CustomException e){
            System.out.println(e.getMessage());
        }

         emptyManager = new HttpTaskManager(path);
        manager.createEpicTask(testEpic);
        manager.createTask(testTask);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);

    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    void shouldSaveTasks() throws CustomException {
        JsonArray tasksJson = JsonParser.parseString(client.load("tasks/task")).getAsJsonArray();
        Task taskJson = gson.fromJson(tasksJson.get(0), Task.class);
        Assertions.assertEquals(testTask,taskJson,"Неверное сохранение задач");

    }

    @Test
    void shouldSaveEpic() throws CustomException {

        JsonArray epicsJson = JsonParser.parseString(client.load("tasks/epictask")).getAsJsonArray();
        EpicTask epicJson = gson.fromJson(epicsJson.get(0), EpicTask.class);
        Assertions.assertEquals(testEpic,epicJson,"Неверное сохранение Эпиков");

    }

    @Test
    void shouldSaveSubTasks() throws CustomException {
        JsonArray subtasksJson = JsonParser.parseString(client.load("tasks/subtask")).getAsJsonArray();
        SubTask taskJson = gson.fromJson(subtasksJson.get(0), SubTask.class);
        Assertions.assertEquals(testSub1,taskJson,"Неверное сохранение Подзадач");
    }

    @Test
    void shouldSaveHistory() throws CustomException{
        manager.getById(0);
        manager.getById(1);
        List<Task> history = manager.getHistory();


        JsonArray historyJson = JsonParser.parseString(client.load("tasks/history")).getAsJsonArray();
        Task taskJson = gson.fromJson(historyJson.get(1), Task.class);
        EpicTask epicJson = gson.fromJson(historyJson.get(0), EpicTask.class);

        List<Task> historyfJson = new ArrayList<>(Arrays.asList(epicJson,taskJson));

        Assertions.assertEquals(history,historyfJson, "История сохраняется неверно");
    }

    @Test
    void shouldLoadFromServer() throws IOException {
        manager.getById(2);
        manager.getById(1);
        HttpTaskManager manager1;
        try {
            manager1 =HttpTaskManager.loadFromServer("http://localhost:8078");
        } catch (CustomException e){
            System.out.println(e.getMessage());
            return;
        }
        Assertions.assertEquals(manager.getId(),manager1.getId(),"Id Неверно восстановлен");
        Assertions.assertEquals(manager.showTasksList(),manager1.showTasksList(),"Неверная загрузка задач с сервера");
        Assertions.assertEquals(manager.showEpicTasksList(),manager1.showEpicTasksList(),"Неверная загрузка Эпиков с сервера");
        Assertions.assertEquals(manager.showSubTasksList(),manager1.showSubTasksList(),"Неверная загрузка Подзадач с сервера");
        Assertions.assertEquals(manager.getHistory(),manager1.getHistory(),"Неверная загрузка истории с сервера");
        Assertions.assertEquals(manager.getPrioritizedTasks(),manager1.getPrioritizedTasks(),"Неверная загрузка приоритетного списка с сервера");
    }

}
