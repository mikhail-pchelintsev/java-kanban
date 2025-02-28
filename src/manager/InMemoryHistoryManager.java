package manager;

import interfaces.HistoryManager;
import model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Collection<Task> listOfViewedTasks = new ArrayList<>();


    @Override
    public Collection<Task> getHistory() {
        return new ArrayList<>(listOfViewedTasks);
    }

    @Override
    public int historySize() {
        return listOfViewedTasks.size();
    }

    @Override
    public void add (Task task) {
        if(listOfViewedTasks.size() > 10){
            listOfViewedTasks.remove(0);
        }
        listOfViewedTasks.add(task);
    }

}
