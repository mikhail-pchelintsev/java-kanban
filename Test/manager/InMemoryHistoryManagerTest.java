package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
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
    void addHistory() {
        historyManager.add(manager.getEpicByID(1));
        assertNotNull(historyManager, "История не пустая.");
        assertEquals(1, historyManager.historySize(), "История не пустая.");
    }
}