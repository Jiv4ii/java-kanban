package HTTP;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import entities.*;
import interfaces.TaskManager;
import logic.HttpTaskManager;
import logic.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;


import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public TaskManager manager;
    private final HttpServer server;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        this.manager = new InMemoryTaskManager();
        server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        server.createContext("/tasks/Id", this::doingbyId);
        server.createContext("/tasks/task", this::doingTasks);
        server.createContext("/tasks/epictask", this::doingEpics);
        server.createContext("/tasks/subtask", this::doingSubTasks);
        server.createContext("/tasks/history", this::doingHistory);
        server.createContext("/tasks/prioritize", this::doingPrioritize);
        server.start();

        gson = new GsonBuilder().create();

    }


    private void doingEpics(HttpExchange e) throws IOException {
        String resp;
        String method = e.getRequestMethod();
        switch (method) {
            case "GET":
                resp = gson.toJson(manager.showEpicTasksList());
                System.out.println("Эпики получены.");
                sendText(e, resp);
                break;
            case "POST":
                JsonElement jsonElement = JsonParser.parseString(new String(e.getRequestBody().readAllBytes(), UTF_8));
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                EpicTask epicPost = gson.fromJson(jsonObject, EpicTask.class);
                manager.createEpicTask(epicPost);
                System.out.println("ID: " + epicPost.getId() + "  Эпик добавлен");
                e.sendResponseHeaders(200, 0);
                e.close();
                break;
            case "DELETE":
                manager.deleteAllEpics();
                System.out.println("Все Эпики удалены.");
                e.sendResponseHeaders(200, 0);
                e.close();
                break;
            default:
                System.out.println("Метод - " + method + " абсолютно не реализован");
                e.sendResponseHeaders(400,0);
        }
    }

    private void doingSubTasks(HttpExchange e) throws IOException {
        String resp;
        String method = e.getRequestMethod();
        switch (method) {
            case "GET":
                String[] path = e.getRequestURI().getPath().split("/");
                if (path.length > 3 && path[3].equals("epic")) {
                    String query = e.getRequestURI().getQuery();
                    HashMap<String, String> queryMap = queryToMap(query);
                    int id = Integer.parseInt(queryMap.get("id"));
                    if (!manager.getById(id).getType().equals("EpicTask")) {
                        e.sendResponseHeaders(400, 0);
                        return;
                    }
                    EpicTask eSearched = (EpicTask) manager.getById(id);
                    resp = gson.toJson(eSearched.getEpicsSubTasks());
                    sendText(e, resp);
                    return;
                }
                resp = gson.toJson(manager.showSubTasksList());
                System.out.println("Подзадачи получены.");
                sendText(e, resp);
                break;
            case "POST":
                JsonElement jsonElement = JsonParser.parseString(new String(e.getRequestBody().readAllBytes(), UTF_8));
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                SubTask subPost = gson.fromJson(jsonObject, SubTask.class);
                manager.createSubTask(subPost);
                System.out.println("ID: " + subPost.getId() + "  Подзадача добавлена");
                e.sendResponseHeaders(200, 0);
                e.close();
                break;
            case "DELETE":
                manager.deleteAllSubTasks();
                System.out.println("Все Подзадачи удалены.");
                e.sendResponseHeaders(200, 0);
                e.close();
                break;
            default:
                System.out.println("Метод - " + method + " абсолютно не реализован");
                e.sendResponseHeaders(400,0);
        }

    }

    private void doingTasks(HttpExchange e) throws IOException {
        String resp;
        String method = e.getRequestMethod();
        switch (method) {
            case "GET":
                resp = gson.toJson(manager.showTasksList());
                System.out.println("Задачи получены.");
                sendText(e,resp);
                break;
            case "POST":
                JsonElement jsonElement = JsonParser.parseString(new String(e.getRequestBody().readAllBytes(), UTF_8));
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Task taskPost = gson.fromJson(jsonObject, Task.class);
                manager.createTask(taskPost);
                System.out.println("ID: " + taskPost.getId() + "  Задача добавлена");
                e.sendResponseHeaders(200, 0);
                e.close();
                break;
            case "DELETE":
                manager.deleteAllTasks();
                System.out.println("Все Задачи удалены.");
                e.sendResponseHeaders(200, 0);
                e.close();
                break;
            default:
                System.out.println("Метод - " + method + " абсолютно не реализован");
                e.sendResponseHeaders(400,0);
        }


    }

    private void doingHistory(HttpExchange e) throws IOException {
        String resp;
        String method = e.getRequestMethod();
        if (method.equals("GET")) {
            resp = gson.toJson(manager.getHistory());
            System.out.println("История получена.");
            sendText(e, resp);
        }
    }

    private void doingPrioritize(HttpExchange e) throws IOException {
        String resp;
        String method = e.getRequestMethod();
        if (method.equals("GET")) {
            resp = gson.toJson(manager.getPrioritizedTasks());
            System.out.println("Приоритетный список получен.");
            sendText(e, resp);

        }
    }


    private void doingbyId(HttpExchange e) throws IOException {
        String method = e.getRequestMethod();
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
            System.out.println("Задача: " + taskGet.getId() + " получена");
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());
            e.sendResponseHeaders(400, 0);
            return;
        }

        String resp;

        switch (taskGet.getType()) {
            case "Task":
                resp = gson.toJson(taskGet);
                break;
            case "EpicTask":
                EpicTask epic = (EpicTask) taskGet;
                resp = gson.toJson(epic);
                break;
            case "SubTask":
                SubTask sub = (SubTask) taskGet;
                resp = gson.toJson(sub);
                break;
            default:
                e.sendResponseHeaders(400, 0);
                return;
        }
        try {
            sendText(e, resp);
        } catch (IOException exception) {
            System.out.println("IO exception");
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

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

}

