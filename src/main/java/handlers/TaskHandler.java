package handlers;

import com.sun.net.httpserver.HttpExchange;
import controller.TaskManager;
import model.Task;
import util.Managers;


import java.io.IOException;
import java.util.List;


public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void processGetList(HttpExchange exchange) throws IOException {
        List<Task> taskList = taskManager.getTasks();
        String data = Managers.getGson().toJson(taskList);
        sendText(exchange, data);
    }

    @Override
    protected void processGetById(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = taskManager.getTask(taskId);
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
                    taskManager.createTask(task);
                    sendText(exchange, Managers.getGson().toJson(task, Task.class));
                } else {
                    sendHasInteractions(exchange);
                }
            } catch (IOException e) {
                sendInternalServerError(exchange, e);
            }
        } else {
            sendText(exchange, "Таск с ID " + taskId + " существует");
        }
    }

    @Override
    protected void processUpdate(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        if (taskId != null) {
            try {
                var task = getTaskFromPost(exchange);
                if(taskManager.hasIntersection(task)) {
                    taskManager.updateTask(getTaskFromPost(exchange));
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
        var task = taskManager.deleteTask(taskId);
        sendText(exchange, Managers.getGson().toJson(task, Task.class));
    }
}
