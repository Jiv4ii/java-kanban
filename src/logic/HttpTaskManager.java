package logic;


import HTTP.KVTaskClient;
import com.google.gson.*;

import entities.EpicTask;
import entities.SubTask;
import entities.Task;

import java.io.IOException;


public class HttpTaskManager extends FileBackedTasksManager {
    private final String path;
    private KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(String path)  {
        this.path = path;
        gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            client = new KVTaskClient(path);
        } catch (IOException e){
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
        } catch (IOException e){
            System.out.println("vruh");

        }
    }


    public  static HttpTaskManager loadFromServer(String newpath) throws IOException{
        HttpTaskManager newManager = new HttpTaskManager(newpath);
        KVTaskClient newclient = new KVTaskClient(newpath);
        Gson newgson = new Gson();

        JsonArray taskJson= JsonParser.parseString(newclient.load("tasks/task")).getAsJsonArray();
        JsonArray epicsJson=  JsonParser.parseString(newclient.load("tasks/epictask")).getAsJsonArray();
        JsonArray subTasksJson=  JsonParser.parseString(newclient.load("tasks/subtask")).getAsJsonArray();
        JsonArray historyJson=  JsonParser.parseString(newclient.load("tasks/history")).getAsJsonArray();
        if (taskJson.size() != 0){
            for (JsonElement element : taskJson) {
                Task taskFromJson = newgson.fromJson(element,Task.class);
                newManager.createTask(taskFromJson);
            }
        }
        if (epicsJson.size() != 0){
            for (JsonElement element : epicsJson) {
                EpicTask taskFromJson = newgson.fromJson(element,EpicTask.class);
                newManager.createEpicTask(taskFromJson);
            }
        }
        if (subTasksJson.size() != 0){
            for (JsonElement element : subTasksJson) {
                SubTask taskFromJson = newgson.fromJson(element,SubTask.class);
                newManager.createSubTaskFromServer(taskFromJson);
            }
        }
        if (historyJson.size() != 0){
            for (JsonElement element : historyJson) {
                  Task taskFromJson = newgson.fromJson(element,Task.class);
                newManager.getById(taskFromJson.getId());
            }
        }
        return newManager;


    }

    void createSubTaskFromServer(SubTask createdSubTask){
        for (Task task : prioritizedTasks) {
            if (!(createdSubTask.getEndTime().isBefore(task.getStartTime()) || task.getEndTime().isBefore(createdSubTask.getStartTime()))) {
                throw new IllegalStateException("Время выполненения задачи пересекается с другими");
            }
        }


        if (createdSubTask.getId() == null) {
            createdSubTask.setId(id);
        }

        subTasks.put(createdSubTask.getId(),createdSubTask);
        id++;

        prioritizedTasks.add(createdSubTask);

    }


}
