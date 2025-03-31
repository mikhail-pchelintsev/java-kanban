package interfaces;

import manager.exception.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;
import java.util.List;

import java.util.Map;

public interface TaskManager {

    Map<Long, Epic> getEpics();

    void createEpic(Epic epic) throws ManagerSaveException;

    SubTask getSubTaskById(long subTaskId);

    void updateEpicById(Long id, Epic epic) throws ManagerSaveException;

    void updateSubtaskById(Long id, SubTask subTask) throws ManagerSaveException;

    void printSubTaskById(long subTaskId);

    void createSubTask(long epicId, SubTask subTask) throws ManagerSaveException;

    void deleteEpic(Long id) throws ManagerSaveException;

    void printAllEpic();

    void deleteSubtask(Long item1, Long item2) throws ManagerSaveException;

    void printEpic(Long id);

    List<Task> getHistory();

    Epic getEpicById(long id);

    void removeHistoryById(int id);


}
