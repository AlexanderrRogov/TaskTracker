import com.sun.net.httpserver.HttpServer;
import controller.FileBackedTaskManager;
import controller.TaskManager;
import handlers.EpicHandler;
import handlers.HistoryHandler;
import handlers.PrioritizedHandler;
import handlers.SubtaskHandler;
import handlers.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final TaskManager taskManager = new FileBackedTaskManager();

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        // завершаем работу сервера для корректной работы тренажёра
        //httpServer.stop(1);
    }

}
