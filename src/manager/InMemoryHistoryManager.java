package manager;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> listOfViewedTasks = new ArrayList<>();


    @Override
    public void getHistory() {
        if(listOfViewedTasks.size() == 0){
            System.out.println("Вы пока не смотрели свои задачи)");
        } else {
            for (Task task : listOfViewedTasks){
                System.out.println(task);
            }
        }
    }

    @Override
    public int historySize() {
        return listOfViewedTasks.size();
    }

    @Override
    public void add (Task task) {
        if(listOfViewedTasks.size() <= 10){
            listOfViewedTasks.add(task);
        } else {
            listOfViewedTasks.remove(0);
            listOfViewedTasks.add(task);
        }
    }

}
