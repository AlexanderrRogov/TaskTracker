package handlers;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.TaskManager;
import model.Task;
import util.Managers;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class BaseHttpHandler implements HttpHandler {

    TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        switch (endpoint) {
            case GET_ONE:
                processGetById(exchange);
                break;

            case GET_LIST:
                processGetList(exchange);
                break;

            case CREATE:
                processCreate(exchange);
                break;

            case UPDATE:
                processUpdate(exchange);
                break;

            case DELETE:
                processDelete(exchange);
                break;
            case GET_SUBTASKS_OF_EPIC:
                processGetSubtasksOfEpic(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    protected Endpoint getEndpoint(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "DELETE" -> {
                return Endpoint.DELETE;
            }
            case "GET" -> {
                String[] requestPathParts = requestPath.split("/");

                if (requestPathParts.length == 3) {
                    return Endpoint.GET_ONE;
                }
                if (requestPathParts.length == 4) {
                    return Endpoint.GET_SUBTASKS_OF_EPIC;
                }
                return Endpoint.GET_LIST;
            }
            case "POST" -> {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (requestBody.isEmpty()) {
                    return Endpoint.UNKNOWN;
                }

                Integer id = getTaskId(exchange);
                if (id == null) {
                    return Endpoint.CREATE;
                }

                return Endpoint.UPDATE;
            }
        }

        return Endpoint.UNKNOWN;
    }

   public void processGetSubtasksOfEpic(HttpExchange h) throws IOException {
       sendMethodNotAllowed(h);
   }

    protected void processGetById(HttpExchange h) throws IOException {
        sendMethodNotAllowed(h);
    }

    protected void processGetList(HttpExchange h) throws IOException {
        sendMethodNotAllowed(h);
    }

    protected void processCreate(HttpExchange h) throws IOException {
        sendMethodNotAllowed(h);
    }


    protected void processDelete(HttpExchange h) throws IOException {
        sendMethodNotAllowed(h);
    }

    protected void processUpdate(HttpExchange h) throws IOException {
        sendMethodNotAllowed(h);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendModify(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(201, 0);
        h.close();
    }

    public void sendNotFound(HttpExchange h) throws IOException {
        byte[] resp = "Not Found".getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public void sendHasInteractions(HttpExchange h) throws IOException {
        byte[] resp = "Not Acceptable".getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public void sendInternalServerError(HttpExchange h, Exception e) throws IOException {
        String message = "Internal Server Error:" + e.getMessage();
        byte[] resp = message.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(500, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public void sendMethodNotAllowed(HttpExchange h) throws IOException {
        String message = "Method Not Allowed";
        byte[] resp = message.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(405, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public Integer getTaskId(HttpExchange exchange) {
        var path = exchange.getRequestURI().getPath();
        var splitPath = path.split("/");
        if (splitPath.length == 3) {
            return Integer.parseInt(path.split("/")[2]);
        } else {
            return null;
        }
    }

    public Task getTaskFromPost(HttpExchange exchange) throws IOException {

            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return Managers.getGson().fromJson(body, Task.class);

    }

    public enum Endpoint { GET_ONE, GET_LIST, CREATE, UPDATE, DELETE,  UNKNOWN, GET_SUBTASKS_OF_EPIC }
}