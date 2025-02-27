package manager;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    HashMap<Long, Epic> getEpics();

    void createEpic(Epic epic);

    SubTask getSubTaskById(long subTaskId);

    void updateEpicById(Long id, Epic epic);

    void updateSubtaskById(Long id, SubTask subTask);

    void printSubTaskById(long subTaskId);

    void createSubTask(long epicId, SubTask subTask);

    void deleteEpic(Long id);

    void printAllEpic();

    void deleteSubtask(Long item1, Long item2);

    void printEpic(Long id);

    void printAllSubtaskInEpic(Long id);

    HistoryManager getHistoryManager();

    Epic getEpicByID(long id);




}
