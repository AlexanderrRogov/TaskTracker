package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {


    public void handle(HttpExchange exchange) throws IOException {
        BaseHttpHandler.Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
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
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    public void handleGetSubtasks(HttpExchange exchange) throws IOException {
        var tasks = fileBackedTaskManager.getSubtasks();
        writeGetListResponse(exchange, tasks, 200);
    }

    public void handleGetSubtask(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = fileBackedTaskManager.getSubtask(taskId);
        if(task != null) {
            writeGetResponse(exchange, task, 200);
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
}

