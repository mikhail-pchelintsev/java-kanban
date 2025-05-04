package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import model.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String query = requestURI.getQuery();

        try {
            switch (method) {
                case "GET":
                    handleGet(exchange, query);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange, query);
                    break;
                default:
                    sendText(exchange, "Method Not Allowed", 405);
            }
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error: " + e.getMessage(), 500);
            e.printStackTrace();
        }
    }

    private void handleGet(HttpExchange exchange, String query) throws IOException {
        if (query != null && query.startsWith("id=")) {
            try {
                long id = Long.parseLong(query.split("=")[1]);
                Epic epic = taskManager.getEpicById(id);
                if (epic != null) {
                    sendText(exchange, HttpTaskServer.getGson().toJson(epic), 200);
                } else {
                    sendNotFound(exchange, "Epic not found");
                }
            } catch (NumberFormatException e) {
                sendBadRequest(exchange, "Invalid ID format");
            }
        } else {
            Map<Long, Epic> epics = taskManager.getEpics();
            sendText(exchange, HttpTaskServer.getGson().toJson(epics.values()), 200);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Received body: " + body);

        Epic epic = HttpTaskServer.getGson().fromJson(body, Epic.class);
        if (epic == null || epic.getName() == null || epic.getDescription() == null) {
            sendBadRequest(exchange, "Invalid epic data");
            return;
        }

        taskManager.createEpic(epic);
        sendText(exchange, "Epic created successfully", 200);
    }

    private void handleDelete(HttpExchange exchange, String query) throws IOException {
        if (query != null && query.startsWith("id=")) {
            try {
                long id = Long.parseLong(query.split("=")[1]);
                Epic epic = taskManager.getEpicById(id);
                if (epic != null) {
                    taskManager.deleteEpic(id);
                    sendText(exchange, "Epic deleted", 200);
                } else {
                    sendNotFound(exchange, "Epic not found");
                }
            } catch (NumberFormatException e) {
                sendBadRequest(exchange, "Invalid ID format");
            }
        } else {
            sendBadRequest(exchange, "ID parameter is missing");
        }
    }
}
