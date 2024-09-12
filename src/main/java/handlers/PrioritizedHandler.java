package handlers;

import com.sun.net.httpserver.HttpExchange;
import controller.TaskManager;
import model.Task;
import util.Managers;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler  {


    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void processGetList(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getPrioritizedTasks();
        String data = Managers.getGson().toJson(history);
        sendText(exchange, data);
    }
}
