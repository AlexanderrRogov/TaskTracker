package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;



public class BaseHttpHandler  {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public void writeResponse(HttpExchange exchange,
                              String responseString,
                              int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    enum Endpoint {POST_TASK, GET_TASKS, GET_TASK, UNKNOWN, DELETE_TASK, GET_SUBTASK, GET_SUBTASKS,
        POST_SUBTASK, DELETE_SUBTASK, GET_EPICS, GET_EPIC, POST_EPIC, DELETE_EPIC,
        GET_EPIC_SUBTASKS, GET_HISTORY, GET_PRIORITIZED}

    public Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (requestMethod.equals("GET") && pathParts[1].equals("tasks")) {
            return Endpoint.GET_TASKS;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("task")) {
            return Endpoint.GET_TASK;
        }
        if (requestMethod.equals("POST") && pathParts[1].equals("task")) {
            return Endpoint.POST_TASK;
        }
        if (requestMethod.equals("DELETE") && pathParts[1].equals("task")) {
            return Endpoint.DELETE_TASK;
        }

        if (requestMethod.equals("GET") && pathParts[1].equals("subtasks")) {
            return Endpoint.GET_SUBTASK;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("subtask")) {
            return Endpoint.GET_SUBTASKS;
        }
        if (requestMethod.equals("POST") && pathParts[1].equals("subtask")) {
            return Endpoint.GET_SUBTASKS;
        }
        if (requestMethod.equals("DELETE") && pathParts[1].equals("subtask")) {
            return Endpoint.GET_SUBTASKS;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("epics")) {
            return Endpoint.GET_SUBTASKS;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("epics")) {
            return Endpoint.GET_SUBTASKS;
        }
        if (requestMethod.equals("GET") && pathParts[1].equals("epic")) {
            return Endpoint.GET_SUBTASKS;
        }
        if (requestMethod.equals("POST") && pathParts[1].equals("epic")) {
            return Endpoint.GET_SUBTASKS;
        }
        return Endpoint.UNKNOWN;
    }
}