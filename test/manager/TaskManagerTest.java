package manager;

import interfaces.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    void shouldCreateAndRetrieveEpic() {
        Epic epic = new Epic("Test Epic", "Description", Status.NEW);
        taskManager.createEpic(epic);
        Epic fetched = taskManager.getEpicById(epic.getId());
        assertNotNull(fetched);
        assertEquals(epic.getName(), fetched.getName());
    }

    @Test
    void shouldAddSubTaskToEpic() {
        Epic epic = new Epic("Epic", "desc", Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask", "desc", Status.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        taskManager.createSubTask(epic.getId(), subTask);
        assertFalse(taskManager.getEpicById(epic.getId()).getSubTasks().isEmpty());
    }

    @Test
    void shouldDetectOverlappingTasks() {
        Epic epic = new Epic("Epic", "desc", Status.NEW);
        taskManager.createEpic(epic);
        SubTask s1 = new SubTask("s1", "desc", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.createSubTask(epic.getId(), s1);
        SubTask s2 = new SubTask("s2", "desc", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(15));

        try {
            taskManager.createSubTask(epic.getId(), s2);
            fail("Expected overlap to be detected");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("пересекается"));
        }
    }
}
