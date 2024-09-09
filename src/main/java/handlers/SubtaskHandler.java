package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static handlers.BaseHttpHandler.Endpoint.DELETE_SUBTASK;
import static handlers.BaseHttpHandler.Endpoint.GET_SUBTASK;
import static handlers.BaseHttpHandler.Endpoint.GET_SUBTASKS;
import static handlers.BaseHttpHandler.Endpoint.POST_SUBTASK;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {


    public void handle(HttpExchange exchange) throws IOException {
        BaseHttpHandler.Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_SUBTASKS: {
                //handleGetComments(exchange);
                break;
            }
            case GET_SUBTASK: {
                //handleGetComments(exchange);
                break;
            }
            case POST_SUBTASK: {
                //handleGetComments(exchange);
                break;
            }
            case DELETE_SUBTASK: {
                //handlePostComments(exchange);
                break;
            }
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }
}

