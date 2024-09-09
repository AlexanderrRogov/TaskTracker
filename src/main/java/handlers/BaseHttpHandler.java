package handlers;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.FileBackedTaskManager;
import model.Epic;
import model.Task;

import util.Managers;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BaseHttpHandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        BaseHttpHandler.Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_EPICS: {
                handleGetEpics(exchange);
                break;
            }
            case GET_EPIC: {
                handleGetEpic(exchange);
                break;
            }
            case POST_EPIC: {
                handlePostEpic(exchange);
                break;
            }
            case DELETE_EPIC: {
                handleDeleteEpic(exchange);
                break;
            }
            case GET_EPIC_SUBTASKS: {
                handleGetEpicSubtasks(exchange);
                break;
            }
            case GET_SUBTASKS: {
                handleGetSubtasks(exchange);
                break;
            }
            case GET_SUBTASK: {
                handleGetSubtask(exchange);
                break;
            }
            case POST_SUBTASK: {
                handlePostSubtask(exchange);
                break;
            }
            case DELETE_SUBTASK: {
                handleDeleteSubtask(exchange);
                break;
            }
            case GET_TASKS: {
                handleGetTasks(exchange);
                break;
            }
            case GET_TASK: {
                handleGetTask(exchange);
                break;
            }
            case POST_TASK: {
                handlePostTask(exchange);
                break;
            }
            case DELETE_TASK: {
                handleDeleteTask(exchange);
                break;
            }
            case GET_PRIORITIZED: {
                handlePrioritized(exchange);
                break;
            }
            case GET_HISTORY: {
                handleHistory(exchange);
                break;
            }
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    public void handlePrioritized(HttpExchange exchange) throws IOException {
        writeGetListResponse(exchange, fileBackedTaskManager.getPrioritizedTasks(), 200);
    }

    public void handleHistory(HttpExchange exchange) throws IOException {
        writeGetListResponse(exchange, fileBackedTaskManager.getHistory(), 200);
    }

    public void handleGetTasks(HttpExchange exchange) throws IOException {
        ArrayList<Task> tasks = fileBackedTaskManager.getTasks();
        if(tasks.isEmpty()) {
            writeResponse(exchange, "История тасков пуста. Добавьте таск", 404);
        } else {
            writeGetListResponse(exchange, tasks, 200);
        }

    }

    public void handleGetTask(HttpExchange exchange) throws IOException {
        try {
            var taskId = getTaskId(exchange);
            var task = fileBackedTaskManager.getTask(taskId);
            if (task != null) {
                var str = Managers.getGson().toJson(task, Task.class);
                writeResponse(exchange, str, 200);
            } else {
                writeResponse(exchange, "Таск с идентификатором " + taskId + " не найден", 404);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handlePostTask(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        if(taskId == null) {
            var task = fileBackedTaskManager.createTask(getTaskFromPost(exchange));
            writeResponse(exchange, "Новый таск создан, ID " + task.getId(), 201);
        } else {
            var updatedTask = fileBackedTaskManager.updateTask(getTaskFromPost(exchange));
            writeResponse(exchange, "Таск обновлён, ID " + updatedTask.getId(), 201);
        }
    }

    public void handleDeleteTask(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = fileBackedTaskManager.deleteTask(taskId);
        writeResponse(exchange, "Таск удалён, ID " + task.getId(), 200);
    }

    public void handleGetSubtasks(HttpExchange exchange) throws IOException {
        var tasks = fileBackedTaskManager.getSubtasks();
        writeGetListResponse(exchange, tasks, 200);
    }

    public void handleGetSubtask(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = fileBackedTaskManager.getSubtask(taskId);
        if(task != null) {
            var str = Managers.getGson().toJson(task, Task.class);
            writeResponse(exchange, str, 200);
        } else {
            writeResponse(exchange, "Cабтаск с идентификатором " + taskId + " не найден", 404);
        }
    }

    public void handlePostSubtask(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        if(taskId == null) {
            var task = fileBackedTaskManager.createSubtask(getTaskFromPost(exchange));
            writeResponse(exchange, "Новый таск создан, ID " + task.getId(), 201);
        } else {
            var updatedTask = fileBackedTaskManager.updateTask(getTaskFromPost(exchange));
            writeResponse(exchange, "Таск обновлён, ID " + updatedTask.getId(), 201);
        }
    }

    public void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = fileBackedTaskManager.deleteSubtask(taskId);
        writeResponse(exchange, "Cабтаск удалён, ID " + task.getId(), 200);
    }

    public void handleGetEpics(HttpExchange exchange) throws IOException {
        var tasks = fileBackedTaskManager.getEpics();
        writeGetListResponse(exchange, tasks, 200);
    }

    public void handleGetEpic(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = fileBackedTaskManager.getEpic(taskId);
        if(task != null) {
            var str = Managers.getGson().toJson(task, Epic.class);
            writeResponse(exchange, str, 200);
        } else {
            writeResponse(exchange, "Эпик с идентификатором " + taskId + " не найден", 404);
        }
    }

    public void handlePostEpic(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        if(taskId == null) {
            var task = fileBackedTaskManager.createEpic(getTaskFromPost(exchange));
            writeResponse(exchange, "Новый эпик создан, ID " + task.getId(), 201);
        } else {
            writeResponse(exchange, "Эпик не найден ID" + taskId, 201);
        }
    }

    public void handleDeleteEpic(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = fileBackedTaskManager.deleteEpic(taskId);
        writeResponse(exchange, "Эпик удалён, ID " + task.getId(), 200);
    }

    public void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var tasks = fileBackedTaskManager.getSubtasksForEpic(taskId);
        writeGetListResponse(exchange, tasks, 200);
    }

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

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


    public void writeGetListResponse(HttpExchange exchange,
                                     List<? extends Task> responseString,
                                     int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(Managers.getGson().toJson(responseString.toString()).getBytes(DEFAULT_CHARSET));
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
            return Managers.getGson().fromJson(body, Task.class);

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
        if (requestMethod.equals("GET") && pathParts[1].equals("prioritized")) {
            return Endpoint.GET_PRIORITIZED;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("history")) {
            return Endpoint.GET_HISTORY;
        }
        if (requestMethod.equals("DELETE") && pathParts[1].equals("epic")) {
            return Endpoint.DELETE_EPIC;
        }
        if(pathParts.length == 4) {
            if (requestMethod.equals("GET") && pathParts[2].equals("epicSubtasks")) {
                return Endpoint.GET_EPIC_SUBTASKS;
            }
        }
        return Endpoint.UNKNOWN;
    }
}