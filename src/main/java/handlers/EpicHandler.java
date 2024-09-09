package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        BaseHttpHandler.Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_EPICS: {
                //handleGetComments(exchange);
                break;
            }
            case GET_EPIC: {
                //handleGetComments(exchange);
                break;
            }
            case POST_EPIC: {
                //handleGetComments(exchange);
                break;
            }
            case DELETE_EPIC: {
                //handlePostComments(exchange);
                break;
            }
            case GET_EPIC_SUBTASKS: {
                //handlePostComments(exchange);
                break;
            }
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }
}
