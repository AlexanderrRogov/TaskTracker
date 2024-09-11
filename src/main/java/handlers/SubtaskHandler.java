package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.TaskManager;
import model.Subtask;
import model.Task;
import util.Managers;

import java.io.IOException;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void processGetList(HttpExchange exchange) throws IOException {
        List<Subtask> taskList = taskManager.getSubtasks();
        String data = Managers.getGson().toJson(taskList);
        sendText(exchange, data);
    }

    @Override
    protected void processGetById(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = taskManager.getSubtask(taskId);
        if (task != null) {
            var str = Managers.getGson().toJson(task, Task.class);
            sendText(exchange, str);
        } else {
            sendNotFound(exchange);
        }
    }

    @Override
    protected void processCreate(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        if (taskId == null) {
            try {
                var task = getTaskFromPost(exchange);
                if(taskManager.hasIntersection(task)) {
                    var subtask = taskManager.createSubtask(task);
                    sendText(exchange, Managers.getGson().toJson(subtask, Task.class));
                } else {
                    sendHasInteractions(exchange);
                }
            } catch (IOException e) {
                sendInternalServerError(exchange, e);
            }
        } else {
            sendText(exchange, "Сабтаск с ID " + taskId + " существует");
        }
    }

    @Override
    protected void processUpdate(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        if (taskId != null) {
            try {
                var task = getTaskFromPost(exchange);
                if(taskManager.hasIntersection(task)) {
                    taskManager.updateSubtask(task);
                    sendModify(exchange);
                } else {
                    sendHasInteractions(exchange);
                }
            } catch (IOException e) {
                sendInternalServerError(exchange, e);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    @Override
    protected void processDelete(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = taskManager.deleteSubtask(taskId);
        sendText(exchange, Managers.getGson().toJson(task, Task.class));
    }
}

