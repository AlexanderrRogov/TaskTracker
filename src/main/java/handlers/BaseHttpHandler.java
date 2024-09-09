package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import controller.FileBackedTaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BaseHttpHandler {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void writeResponse(HttpExchange exchange,
                              String responseString,
                              int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    public void writeGetResponse(HttpExchange exchange,
                                 Task responseString,
                                 int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(gson.toJson(responseString).getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    public void writeGetListResponse(HttpExchange exchange,
                                     List<? extends Task> responseString,
                                     int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(gson.toJson(responseString).getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }


    public Integer getTaskId(HttpExchange exchange) {
        var path = exchange.getRequestURI().getPath();
        var splitPath = path.split("/");
        if (splitPath.length == 3) {
            return Integer.parseInt(path.split("/")[2]);
        } else {
            return null;
        }
    }

    public Task getTaskFromPost(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return gson.fromJson(body, new TaskHandler.TaskToken().getType());
    }

    class TaskToken extends TypeToken<Task> {

    }

    enum Endpoint {
        POST_TASK, GET_TASKS, GET_TASK, UNKNOWN, DELETE_TASK, GET_SUBTASK, GET_SUBTASKS,
        POST_SUBTASK, DELETE_SUBTASK, GET_EPICS, GET_EPIC, POST_EPIC, DELETE_EPIC,
        GET_EPIC_SUBTASKS, GET_HISTORY, GET_PRIORITIZED
    }

    public Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (requestMethod.equals("GET") && pathParts[1].equals("tasks") && pathParts[2].isEmpty()) {
            return Endpoint.GET_TASKS;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("tasks")) {
            return Endpoint.GET_TASK;
        }
        if (requestMethod.equals("POST") && pathParts[1].equals("tasks")) {
            return Endpoint.POST_TASK;
        }
        if (requestMethod.equals("DELETE") && pathParts[1].equals("task")) {
            return Endpoint.DELETE_TASK;
        }

        if (requestMethod.equals("GET") && pathParts[1].equals("subtasks")) {
            return Endpoint.GET_SUBTASKS;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("subtask")) {
            return Endpoint.GET_SUBTASK;
        }
        if (requestMethod.equals("POST") && pathParts[1].equals("subtask")) {
            return Endpoint.POST_SUBTASK;
        }
        if (requestMethod.equals("DELETE") && pathParts[1].equals("subtask")) {
            return Endpoint.DELETE_SUBTASK;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("epics")) {
            return Endpoint.GET_EPICS;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("epic")) {
            return Endpoint.GET_EPIC;
        }
        if (requestMethod.equals("POST") && pathParts[1].equals("epic")) {
            return Endpoint.POST_EPIC;
        }
        if (requestMethod.equals("DELETE") && pathParts[1].equals("epic")) {
            return Endpoint.DELETE_EPIC;
        }
        if (requestMethod.equals("GET") && pathParts[3].equals("epicSubtasks")) {
            return Endpoint.GET_EPIC_SUBTASKS;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("prioritized")) {
            return Endpoint.GET_PRIORITIZED;
        }
        if (requestMethod.equals("GET") && pathParts[2].equals("history")) {
            return Endpoint.GET_HISTORY;
        }
        return Endpoint.UNKNOWN;
    }
}