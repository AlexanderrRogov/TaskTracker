package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

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
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    public void handleGetEpics(HttpExchange exchange) throws IOException {
        var tasks = fileBackedTaskManager.getEpics();
        writeGetListResponse(exchange, tasks, 200);
    }

    public void handleGetEpic(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = fileBackedTaskManager.getEpic(taskId);
        if(task != null) {
            writeGetResponse(exchange, task, 200);
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
}
