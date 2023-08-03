package tests;

import logic.HttpTaskManager;
import HTTP.KVServer;
import HTTP.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import entities.EpicTask;
import entities.Status;
import entities.SubTask;
import entities.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class HttpTaskManagerTest {
    private HttpTaskManager manager;
    private KVServer server;
    private final Task testTask = new Task(0,"Task", "Task testring", Status.NEW, Instant.now(), Duration.ofHours(15));
    private final EpicTask testEpic = new EpicTask(1,"TestEpic", "Epic Testing");
    private final SubTask testSub1 = new SubTask(2,"SubTask1", "Sub Testing", Status.NEW, 1, Instant.now().plus(Duration.ofDays(1)), Duration.ofHours(15));
    private final SubTask testSub2 = new SubTask(3,"SubTask2", "Sub Testing", Status.NEW, 1, Instant.now().plus(Duration.ofDays(2)), Duration.ofHours(15));
    private KVTaskClient client;
    Gson gson = new Gson();

    @BeforeEach
    void create() throws IOException {
        server = new KVServer();
        server.start();
        manager = new HttpTaskManager("http://localhost:8078");
        client = new KVTaskClient("http://localhost:8078");
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    void shouldSaveTasks() throws IOException {
        manager.createTask(testTask);
        JsonArray tasksJson = JsonParser.parseString(client.load("tasks/task")).getAsJsonArray();
        Task taskJson = gson.fromJson(tasksJson.get(0), Task.class);
        Assertions.assertEquals(testTask,taskJson,"Неверное сохранение задач");

    }

    @Test
    void shouldSaveEpic() throws IOException {
        manager.createEpicTask(testEpic);
        JsonArray epicsJson = JsonParser.parseString(client.load("tasks/epictask")).getAsJsonArray();
        EpicTask epicJson = gson.fromJson(epicsJson.get(0), EpicTask.class);
        Assertions.assertEquals(testEpic,epicJson,"Неверное сохранение Эпиков");

    }

    @Test
    void shouldSaveSubTasks() throws IOException {
        manager.createEpicTask(testEpic);
        manager.createSubTask(testSub2);
        JsonArray subtasksJson = JsonParser.parseString(client.load("tasks/subtask")).getAsJsonArray();
        SubTask taskJson = gson.fromJson(subtasksJson.get(0), SubTask.class);
        Assertions.assertEquals(testSub2,taskJson,"Неверное сохранение Подзадач");
    }

    @Test
    void shouldSaveHistory() throws IOException {
        manager.createTask(testTask);
        manager.createEpicTask(testEpic);
        manager.getById(0);
        manager.getById(1);
        ArrayList<Task> history = new ArrayList<>(Arrays.asList(testTask,testEpic));


        JsonArray historyJson = JsonParser.parseString(client.load("tasks/history")).getAsJsonArray();
        Task taskJson = gson.fromJson(historyJson.get(0), Task.class);
        EpicTask epicJson = gson.fromJson(historyJson.get(1), EpicTask.class);

        ArrayList<Task> historyfJson = new ArrayList<>(Arrays.asList(taskJson,epicJson));

        Assertions.assertEquals(history,historyfJson, "История сохраняется неверно");
    }

    @Test
    void shouldLoadFromServer() throws IOException {
        manager.createTask(testTask);
        manager.createEpicTask(testEpic);
        manager.createSubTask(testSub1);
        manager.getById(2);
        manager.getById(1);
        HttpTaskManager manager1 =HttpTaskManager.loadFromServer("http://localhost:8078");
        Assertions.assertEquals(manager.showTasksList(),manager1.showTasksList(),"Неверная загрузка задач с сервера");
        Assertions.assertEquals(manager.showEpicTasksList(),manager1.showEpicTasksList(),"Неверная загрузка Эпиков с сервера");
        Assertions.assertEquals(manager.showSubTasksList(),manager1.showSubTasksList(),"Неверная загрузка Подзадач с сервера");
        Assertions.assertEquals(manager.getHistory(),manager1.getHistory(),"Неверная загрузка истории с сервера");
        Assertions.assertEquals(manager.getPrioritizedTasks(),manager1.getPrioritizedTasks(),"Неверная загрузка приоритетного списка с сервера");
    }

}
