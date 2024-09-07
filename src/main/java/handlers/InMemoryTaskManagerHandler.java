package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class InMemoryTaskManagerHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InMemoryTaskManagerHandler.Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case POST_TASK: {
                //handleGetPosts(exchange);
                break;
            }
            case GET_TASKS: {
                //handleGetComments(exchange);
                break;
            }
            case GET_TASK: {
                //handlePostComments(exchange);
                break;
            }
            case DELETE_TASK: {
                //handlePostComments(exchange);
                break;
            }
            case :
                break;
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private InMemoryTaskManagerHandler.Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (requestMethod.equals("POST") && pathParts[1].equals("posts")) {
            return Endpoint.POST_TASK;
        }

        if (requestMethod.equals("GET") && pathParts[1].equals("tasks")) {
            return Endpoint.GET_TASKS;
        }
        if (requestMethod.equals("DELETE") && pathParts[1].equals("tasks")) {
            return Endpoint.DELETE_TASK;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("task")) {
            return Endpoint.GET_TASK;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("subtask")) {
            return Endpoint.GET_SUBTASK;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("subtasks")) {
            return Endpoint.GET_SUBTASKS;
        }

        return InMemoryTaskManagerHandler.Endpoint.UNKNOWN;
    }

    enum Endpoint {POST_TASK, GET_TASKS, GET_TASK, UNKNOWN, DELETE_TASK, GET_SUBTASK, GET_SUBTASKS}
}
