package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.TaskManager;
import model.Epic;
import model.Task;
import util.Managers;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void processGetList(HttpExchange exchange) throws IOException {
        List<Epic> taskList = taskManager.getEpics();
        String data = Managers.getGson().toJson(taskList);
        sendText(exchange, data);
    }

    @Override
    protected void processGetById(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = taskManager.getEpic(taskId);
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
                var task = taskManager.createEpic(getTaskFromPost(exchange));
                sendText(exchange, Managers.getGson().toJson(task, Task.class));
            } catch (IOException e) {
                sendInternalServerError(exchange, e);
            }
        } else {
            sendText(exchange, "Эпик с ID " + taskId + " существует");
        }
    }

    @Override
    public void processGetSubtasksOfEpic(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var tasks = taskManager.getSubtasksForEpic(taskId);
        sendText(exchange, Managers.getGson().toJson(tasks));
    }

    @Override
    protected void processDelete(HttpExchange exchange) throws IOException {
        var taskId = getTaskId(exchange);
        var task = taskManager.deleteEpic(taskId);
        sendText(exchange, Managers.getGson().toJson(task, Task.class));
    }
}
