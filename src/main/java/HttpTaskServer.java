import com.sun.net.httpserver.HttpServer;
import handlers.InMemoryTaskManagerHandler;
import handlers.PostsHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/FileBackedTaskManager", new InMemoryTaskManagerHandler(posts));
        httpServer.createContext("/InMemoryHistoryManager", new PostsHandler(posts));
        httpServer.createContext("/InMemoryTaskManager", new PostsHandler(posts));
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        // завершаем работу сервера для корректной работы тренажёра
        httpServer.stop(1);
    }

}
