package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import model.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public static TaskManager manager = Managers.getDefault();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @BeforeAll
    public static void beforeAll() {

        Epic epic1 = new Epic("Переезд", "Покупка дома", Status.NEW);
        Epic epic2 = new Epic("Накопить на новый телефон", "Устроиться на работу", Status.NEW);

        SubTask subTask1 = new SubTask("Просмотреть хороших риелторов",
                "С хорошей репутацией", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        SubTask subTask2 = new SubTask("Посмотреть хорошие обьявления", "ПОсмотреть отзыви", Status.NEW,
                Duration.ofMinutes(90), LocalDateTime.now().plusDays(1));

        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubTask(epic1.getId(), subTask1);
        manager.createSubTask(epic2.getId(), subTask2);
    }
    @Test
    void classesAreEqualIfTheirEqualIsID() {
        assertEquals(manager.getEpicById(1), manager.getEpicById(1), "должны быть одинаковыми");
    }

    @Test
    void epicCantBeAddedAsSubTaskToItself() {
        Epic epic = new Epic("Переезд", "Покупка дома", Status.NEW);
        manager.createEpic(epic);
        SubTask invalidSubTask = new SubTask("Переезд", "Покупка дома", Status.NEW, Duration.ofMinutes(30)
        , LocalDateTime.now());
        int subtaskCountBefore = epic.getSubTasks().size();
        manager.createSubTask(epic.getId(), invalidSubTask);
        int subtaskCountAfter = epic.getSubTasks().size();
        assertEquals(subtaskCountBefore, subtaskCountAfter, "Эпик не должен содержать себя как подзадачу");
    }

    @Test
    void createSubTask() {
        assertNotNull(manager.getEpics().get(1L).getSubTasks());
    }

    @Test
    void createEpic() {
        assertNotNull(manager.getEpics());
    }

    @Test
    void findById() {
        Epic foundEpic = manager.getEpicById(1);
        assertEquals("Переезд", foundEpic.getName(), "Нахвание не совпадает");
        assertEquals("Покупка дома", foundEpic.getDescription(), "Описание не совпадает");
        assertEquals(Status.NEW, foundEpic.getStatus(), "Status не совпадает");
    }

    @Test
    void add() {
        historyManager.add(manager.getEpicById(1));
        assertNotNull(historyManager, "История не пустая.");
        assertEquals(1, historyManager.historySize(), "История не пустая.");
    }

    @Test
    void shouldCreateAndRetrieveEpic() {
        Epic epic = new Epic("Переезд", "Покупка дома", Status.NEW);
        taskManager.createEpic(epic);  // Создание эпика
        Epic fetched = taskManager.getEpicById(epic.getId());
        assertNotNull(fetched);
        assertEquals(epic.getName(), fetched.getName());
    }

    @Test
    void shouldAddSubTaskToEpic() {
        Epic epic = new Epic("Переезд", "Покупка дома", Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Просмотр риелторов", "Проверить отзывы", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.createSubTask(epic.getId(), subTask);
        assertFalse(taskManager.getEpicById(epic.getId()).getSubTasks().isEmpty());
    }

    @Test
    void shouldDetectOverlappingTasks() {
        Epic epic = new Epic("Переезд", "Покупка дома", Status.NEW);
        taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("Посмотреть риелторов", "Проверить отзывы", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.createSubTask(epic.getId(), subTask1);
        SubTask subTask2 = new SubTask("Посмотреть квартиры", "Изучить объявления", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(15));

        assertNotNull(subTask1.getId(), "ID первой подзадачи не должен быть null");
        assertNotNull(subTask2.getId(), "ID второй подзадачи не должен быть null");
        taskManager.createSubTask(epic.getId(), subTask2);
        SubTask overlappingSubTask = taskManager.getSubTaskById(subTask2.getId());
        assertNull(overlappingSubTask, "Подзадача не должна быть null");
    }
}