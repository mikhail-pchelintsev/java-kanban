package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File file;

    @BeforeEach
    void setUp() throws IOException {
        file = File.createTempFile("save", ".txt");
        manager = FileBackedTaskManager.loadFromFile(file);
    }

    @Test
    void shouldSaveAndLoadEmptyFile() throws IOException {
        Epic epic1 = new Epic("Переезд", "Покупка дома", Status.NEW);
        manager.createEpic(epic1);
        List<String> lines = Files.readAllLines(file.toPath());
        assertEquals(2, lines.size());
        assertEquals("name,description,status,id", lines.get(0), "Заголовок должен быть правильным");
    }

    @Test
    void shouldSaveAndLoadMultipleEpics() {
        Epic epic1 = new Epic("Переезд", "Покупка дома", Status.NEW);
        Epic epic2 = new Epic("Работа", "Найти новую работу", Status.NEW);
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(2, loadedManager.getEpics().size(), "Должны загрузиться 2 эпика");
        assertTrue(loadedManager.getEpics().containsKey(epic1.getId()), "Эпик 1 должен быть загружен");
        assertTrue(loadedManager.getEpics().containsKey(epic2.getId()), "Эпик 2 должен быть загружен");
    }

    @Test
    void shouldSaveAndLoadMultipleSubtasks() {
        Epic epic = new Epic("Учеба", "Пройти курс Java", Status.NEW);
        manager.createEpic(epic);

        SubTask subTask1 = new SubTask("Читать книгу", "Глава 1", Status.NEW);
        SubTask subTask2 = new SubTask("Решить задачи", "Практика", Status.NEW);

        manager.createSubTask(epic.getId(), subTask1);
        manager.createSubTask(epic.getId(), subTask2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        loadedManager.loadFromFile(file);

        assertEquals(2, loadedManager.getEpics().get(epic.getId()).getSubTasks().size(), "Должны загрузиться 2 подзадачи");
    }
}
