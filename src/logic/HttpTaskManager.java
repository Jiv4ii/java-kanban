package logic;


import HTTP.KVTaskClient;
import com.google.gson.*;

import entities.CustomException;
import entities.EpicTask;
import entities.SubTask;
import entities.Task;

import java.io.IOException;


public class HttpTaskManager extends FileBackedTasksManager {
    private final String path;
    private KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(String path) {
        this.path = path;
        gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            client = new KVTaskClient(path);
        } catch (CustomException e) {
            System.out.println(e.getMessage());
        }

    }


    @Override
    protected void save() {
        try {
            client.put("tasks/task", gson.toJson(showTasksList()));
            client.put("tasks/epictask", gson.toJson(showEpicTasksList()));
            client.put("tasks/subtask", gson.toJson(showSubTasksList()));
            client.put("tasks/history", gson.toJson(getHistory()));
            client.put("tasks", gson.toJson(prioritizedTasks));
        } catch (CustomException e) {
            System.out.println(e.getMessage());

        }
    }


    public static HttpTaskManager loadFromServer(String newpath) throws CustomException {
        HttpTaskManager newManager = new HttpTaskManager(newpath);
        KVTaskClient newclient = new KVTaskClient(newpath);
        Gson newgson = new Gson();

        JsonArray taskJson = JsonParser.parseString(newclient.load("tasks/task")).getAsJsonArray();
        JsonArray epicsJson = JsonParser.parseString(newclient.load("tasks/epictask")).getAsJsonArray();
        JsonArray subTasksJson = JsonParser.parseString(newclient.load("tasks/subtask")).getAsJsonArray();
        JsonArray historyJson = JsonParser.parseString(newclient.load("tasks/history")).getAsJsonArray();
        JsonArray prioritizedJson = JsonParser.parseString(newclient.load("tasks")).getAsJsonArray();
        for (JsonElement element : taskJson) {
            Task taskFromJson = newgson.fromJson(element, Task.class);
            newManager.tasks.put(taskFromJson.getId(), taskFromJson);
        }
        for (JsonElement element : epicsJson) {
            EpicTask taskFromJson = newgson.fromJson(element, EpicTask.class);
            newManager.epics.put(taskFromJson.getId(), taskFromJson);
        }
        for (JsonElement element : subTasksJson) {
            SubTask taskFromJson = newgson.fromJson(element, SubTask.class);
            newManager.subTasks.put(taskFromJson.getId(), taskFromJson);
        }

        newManager.id = taskJson.size() + epicsJson.size() + subTasksJson.size();


        for (JsonElement element : historyJson) {
            Task taskFromJson = newgson.fromJson(element, Task.class);
            newManager.getById(taskFromJson.getId());
        }
        for (JsonElement element : prioritizedJson) {
            Task taskFromJson = newgson.fromJson(element, Task.class);
            newManager.prioritizedTasks.add(taskFromJson);
        }

        return newManager;


    }


}
