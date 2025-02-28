package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;

public class Managers {
    private Managers() {};

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager() ;

    }
}
