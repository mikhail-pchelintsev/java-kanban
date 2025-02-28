package interfaces;

import model.Epic;
import model.SubTask;

import java.util.HashMap;
import java.util.Map;

public interface TaskManager {

    Map<Long, Epic> getEpics();

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


    void getHistoryManager();

    Epic getEpicByID(long id);




}
