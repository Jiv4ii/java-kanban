package HTTP;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import entities.*;
import interfaces.TaskManager;
import logic.HttpTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;


import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public TaskManager manager;
    private final HttpServer server;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        this.manager = new HttpTaskManager("http://localhost:8078");
        server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        server.start();

        gson = new GsonBuilder().create();
        server.createContext("/tasks/task", this::doingbyId);
        server.createContext("/tasks/task/", this::doingTasks);
        server.createContext("/tasks/epictask", this::doingTasks);
        server.createContext("/tasks/subtask", this::doingTasks);
        server.createContext("/tasks/history", this::doingTasks);
        server.createContext("/tasks/prioritize", this::doingTasks);

    }

    private void doingTasks(HttpExchange e) throws IOException {
        String direction = e.getRequestURI().getPath().split("/")[2];
        byte[] resp;
        String method = e.getRequestMethod();
        switch (method) {
            case "GET": {
                String[] path = e.getRequestURI().getPath().split("/");
                if (path.length > 3 && path[3].equals("epic")) {
                    String query = e.getRequestURI().getQuery();
                    HashMap<String, String> queryMap = queryToMap(query);
                    int id = Integer.parseInt(queryMap.get("id"));
                    if (!manager.getById(id).getType().equals("EpicTask")) {
                        e.sendResponseHeaders(400, 0);
                        return;
                    }
                    System.out.println("good");
                    EpicTask eSearched = (EpicTask) manager.getById(id);
                    String subTasks = gson.toJson(eSearched.getEpicsSubTasks());
                    resp = subTasks.getBytes(UTF_8);
                    e.getResponseHeaders().add("Content-Type", "application/json");
                    e.sendResponseHeaders(200, resp.length);
                    e.getResponseBody().write(resp);
                    return;
                }

                switch (direction) {
                    case "task":
                        resp = gson.toJson(manager.showTasksList()).getBytes(UTF_8);
                        break;
                    case "epictask":
                        resp = gson.toJson(manager.showEpicTasksList()).getBytes(UTF_8);
                        break;
                    case "subtask":
                        resp = gson.toJson(manager.showSubTasksList()).getBytes(UTF_8);
                        break;
                    case "history":
                        resp = gson.toJson(manager.getHistory()).getBytes(UTF_8);
                        break;
                    case "prioritize":
                        resp = gson.toJson(manager.getPrioritizedTasks()).getBytes(UTF_8);
                        break;
                    default:
                        throw new IllegalStateException("illegal Task type");

                }
                e.getResponseHeaders().add("Content-Type", "application/json");
                e.sendResponseHeaders(200, resp.length);
                e.getResponseBody().write(resp);
                e.close();
                return;

            }
            case "DELETE": {
                switch (direction) {
                    case "task":
                        manager.deleteAllTasks();
                        System.out.println("Все Задачи удалены.");
                        e.sendResponseHeaders(200, 0);
                        e.close();
                        break;
                    case "epictask":
                        manager.deleteAllEpics();
                        System.out.println("Все Эпики удалены.");
                        e.sendResponseHeaders(200, 0);
                        e.close();
                        break;
                    case "subtask":
                        manager.deleteAllSubTasks();
                        System.out.println("Все Подзадачи удалены.");
                        e.sendResponseHeaders(200, 0);
                        e.close();
                        break;
                }
            }
        }
    }

    private void doingbyId(HttpExchange e) throws IOException {
        String method = e.getRequestMethod();
        System.out.println("/start/");
        System.out.println();

        switch (method) {
            case "GET":
                getById(e);
                break;
            case "DELETE":
                String queryDel = e.getRequestURI().getQuery();
                HashMap<String, String> queryMapDel = queryToMap(queryDel);
                int idDel = Integer.parseInt(queryMapDel.get("id"));
                manager.deleteById(idDel);
                System.out.println("Task with id " + idDel + " deleted");
                e.sendResponseHeaders(200, 0);
                break;
            case "POST":
                postById(e);

        }
        e.close();

    }

    private void getById(HttpExchange e) throws IOException {
        System.out.println("/start/get");
        String query = e.getRequestURI().getQuery();
        HashMap<String, String> queryMap = queryToMap(query);
        int id = Integer.parseInt(queryMap.get("id"));
        Task taskGet;
        try {
            taskGet = manager.getById(id);
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());
            e.sendResponseHeaders(400, 0);
            return;
        }

        byte[] resp;

        switch (taskGet.getType()) {
            case "Task":
                String taskToJson = gson.toJson(taskGet);
                resp = taskToJson.getBytes(UTF_8);
                break;
            case "EpicTask":
                EpicTask epic = (EpicTask) taskGet;
                String epicToJson = gson.toJson(epic);
                resp = epicToJson.getBytes(UTF_8);
                break;
            case "SubTask":
                SubTask sub = (SubTask) taskGet;
                String subToJson = gson.toJson(sub);
                resp = subToJson.getBytes(UTF_8);
                break;
            default:
                e.sendResponseHeaders(400, 0);
                return;
        }
        try {
            e.getResponseHeaders().add("Content-Type", "application/json");
            e.sendResponseHeaders(200, resp.length);
            e.getResponseBody().write(resp);
            e.close();
        } catch (IOException exception) {
            System.out.println("IO exception");
        }
    }

    private void postById(HttpExchange e) {
        try {
            JsonElement jsonElement = JsonParser.parseString(new String(e.getRequestBody().readAllBytes(), UTF_8));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            System.out.println(jsonObject);
            if (jsonObject.has("epicType")) {
                EpicTask epicPost = gson.fromJson(jsonObject, EpicTask.class);
                System.out.println(epicPost);
                manager.createEpicTask(epicPost);
                Integer idEpic = manager.showEpicTasksList().get(manager.showEpicTasksList().indexOf(epicPost)).getId();
                System.out.println("ID: " + idEpic + "  Эпик добавлен");
                e.sendResponseHeaders(200, 0);
                e.close();
                return;
            }
            if (jsonObject.has("subType")) {
                SubTask subPost = gson.fromJson(jsonObject, SubTask.class);
                manager.createSubTask(subPost);
                Integer idSub = manager.showSubTasksList().get(manager.showSubTasksList().indexOf(subPost)).getId();
                System.out.println("ID: " + idSub + "  Подзадача добавлена");
                e.sendResponseHeaders(200, 0);
                e.close();
                return;
            }
            Task taskPost = gson.fromJson(jsonObject, Task.class);
            manager.createTask(taskPost);
            Integer idTask = manager.showTasksList().get(manager.showTasksList().indexOf(taskPost)).getId();
            System.out.println("ID: " + idTask + "  Задача добавлена");
            e.sendResponseHeaders(200, 0);
            e.close();

        } catch (IOException exception) {
            System.out.println("yeas");
        }
    }

    private HashMap<String, String> queryToMap(String query) {
        HashMap<String, String> result = new HashMap<>();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    result.put(key, value);
                }
            }
        }
        return result;
    }

    public void stop() {
        server.stop(0);
    }

}

