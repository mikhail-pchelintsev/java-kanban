package interfaces;

import model.Task;

import java.util.*;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();

    int historySize();

    void removeHistory(long id);
}
