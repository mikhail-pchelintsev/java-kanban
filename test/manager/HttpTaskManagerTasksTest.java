package manager;

import http.DurationAdapter;
import http.HttpTaskServer;
import http.LocalDateTimeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import interfaces.TaskManager;
import model.Epic;
import model.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    private TaskManager manager = new InMemoryTaskManager();
    private HttpTaskServer taskServer;
    private static final Gson gson = JsonUtil.GSON;


    public HttpTaskManagerTasksTest() throws IOException {
        taskServer = new HttpTaskServer(manager);
    }

    @BeforeEach
    public void setUp() {
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    private HttpResponse<String> postEpic(String json) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test 2", "Testing task 2", Status.NEW);
        String taskJson = gson.toJson(epic);
        System.out.println("Request JSON: " + taskJson);

        HttpResponse<String> response = postEpic(taskJson);

        System.out.println("Response status: " + response.statusCode());
        System.out.println("Response body: " + response.body());

        assertEquals(200, response.statusCode(), "Неверный код ответа");

        List<Epic> epics = new ArrayList<>(manager.getEpics().values());
        System.out.println(epics);

        assertEquals(manager.getEpicById(1).getName(), "Test 2", "Эпик не был добавлен в менеджер");
        assertEquals(manager.getEpicById(1).getStatus(), Status.NEW, "Эпик не был добавлен в менеджер");
        assertEquals(manager.getEpicById(1).getDescription(), "Testing task 2", "Эпик не был добавлен в менеджер");
    }
}
