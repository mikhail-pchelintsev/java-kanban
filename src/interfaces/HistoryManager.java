package interfaces;

import model.Task;

import java.util.Collection;
import java.util.List;

public interface HistoryManager {
    void add(Task task);

    Collection<Task> getHistory();

    int historySize();
}
