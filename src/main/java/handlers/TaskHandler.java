package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
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
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
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
        var taskId = getTaskId(exchange);
        var task = fileBackedTaskManager.getTask(taskId);
        if(task != null) {
            writeGetResponse(exchange, task, 200);
        } else {
            writeResponse(exchange, "Таск с идентификатором " + taskId + " не найден", 404);
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
}
