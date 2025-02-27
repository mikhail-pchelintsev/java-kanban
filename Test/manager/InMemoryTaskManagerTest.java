package manager;

import model.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.SubTask;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    public static TaskManager manager = Managers.getDefault();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    @BeforeAll
    public static void beforeAll() {

        Epic epic1 = new Epic("Переезд", "Покупка дома", Status.NEW);
        Epic epic2 = new Epic("Накопить на новый телефон", "Устроиться на работу", Status.NEW);

        SubTask subTask1 = new SubTask("Просмотреть хороших риелторов",
                "С хорошей репутацией", Status.NEW);
        SubTask subTask2 = new SubTask("Посмотреть хорошие обьявления", "ПОсмотреть отзыви", Status.NEW);

        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubTask(epic1.getId(), subTask1);
        manager.createSubTask(epic2.getId(), subTask2);
    }
    @Test
    void classesAreEqualIfTheirEqualIsID() {
        assertEquals(manager.getEpicByID(1), manager.getEpicByID(1), "должны быть одинаковыми");
    }

    @Test
    void epicCantBeAddedAsSubTaskToItself() {
        Epic epic = new Epic("Переезд", "Покупка дома", Status.NEW);
        manager.createEpic(epic);
        SubTask invalidSubTask = new SubTask("Переезд", "Покупка дома", Status.NEW);
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
        Epic foundEpic = manager.getEpicByID(1);
        assertEquals("Переезд", foundEpic.getName(), "Нахвание не совпадает");
        assertEquals("Покупка дома", foundEpic.getDescription(), "Описание не совпадает");
        assertEquals(Status.NEW, foundEpic.getStatus(), "Status не совпадает");
    }

    @Test
    void add() {
        historyManager.add(manager.getEpicByID(1));
        assertNotNull(historyManager, "История не пустая.");
        assertEquals(1, historyManager.historySize(), "История не пустая.");
    }
}