package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS: {
                //handleGetComments(exchange);
                break;
            }
            case GET_TASK: {
                //handlePostComments(exchange);
                break;
            }
            case POST_TASK: {
                //handleGetPosts(exchange);
                break;
            }
            case DELETE_TASK: {
                //handlePostComments(exchange);
                break;
            }
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
}

    public void handleGetTasks(HttpExchange exchange) throws IOException {
        String response = i
                .map(Post::toString)
                .collect(Collectors.joining("\n"));
        writeResponse(exchange, response, 200);
    }

}
