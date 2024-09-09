package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        writeGetListResponse(exchange, fileBackedTaskManager.getHistory(), 200);
    }
}




