package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.HashMap;
import java.util.Map;
import model.Epic;

public class InMemoryTaskManager implements TaskManager {
    private Map<Long, Epic> epics = new HashMap<>();
    private long nextEpicId = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public Map<Long, Epic> getEpics() {
        return epics;
    }

    @Override
    public Epic getEpicByID (long id) {
        return epics.get(id);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(nextEpicId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public SubTask getSubTaskById(long subTaskId) {
        for (Epic epic : epics.values()) {
            SubTask subTask = epic.getSubTasks().get(subTaskId);
            if (subTask != null) {
                return subTask;
            }
        }
        return null;
    }

    @Override
    public void updateEpicById(Long id, Epic epic) {
        if (id == null || epic == null) {
            System.out.println("Ошибка: ID или Задача не могут быть null.");
            return;
        }
        if (epics.containsKey(id)) {
            Epic existingEpic = epics.get(id);
            existingEpic.setName(epic.getName());
            existingEpic.setDescription(epic.getDescription());
            existingEpic.setStatus(epic.getStatus());
            existingEpic.calcStatusEpic();
            System.out.println("Задача с ID " + id + " успешно обновлен.");
        } else {
            System.out.println("Ошибка: Задача с ID " + id + " не найден.");
        }
    }

    @Override
    public void updateSubtaskById(Long id, SubTask subTask){
        for (Epic epic : epics.values()){
            HashMap<Long, SubTask> subTasks = epic.getSubTasks();
            if (subTasks.containsKey(id)){
                epic.update(id,subTask);
            } else {
                System.out.println("Подзадачи с таким ID нет");
            }
        }

    }

    @Override
    public void createSubTask(long epicId, SubTask subTask) {
        if (epics.containsKey(epicId)) {
            long subTaskId = epics.get(epicId).getSubTasks().size() + 1;
            epics.get(epicId).createSubtask(subTask, subTaskId);
            System.out.println("Подзадача создана с ID: " + subTaskId);
        } else {
            System.out.println("Задача с ID " + epicId + " не найден.");
        }
    }

    @Override
    public void deleteEpic(Long id) {
        epics.remove(id);
        System.out.println("задача удалена");
    }

    @Override
    public void printAllEpic() {
        if (!epics.isEmpty()){
            for (Epic epic : epics.values()) {
                System.out.println(epic);
            }
        } else {
            System.out.println("Задач пока нет");
        }

    }

    @Override
    public void printSubTaskById(long subTaskId) {
        SubTask subTask = getSubTaskById(subTaskId);
        if (subTask != null) {
            System.out.println("Найдена подзадача: " + subTask);
            historyManager.add(subTask);
        } else {
            System.out.println("Подзадача с ID " + subTaskId + " не найдена.");
        }
    }

    @Override
    public void getHistoryManager() {
        if(historyManager.getHistory().size() > 0){
            for(Task task : historyManager.getHistory()) {
                System.out.println(task);
            }
        } else {
            System.out.println("Пока что вы не смотрели свои задачи)");
        }

    }


    @Override
    public void deleteSubtask(Long item1, Long item2){
        Epic epic = epics.get(item1);
        if (epic != null){
            epic.deleteItemSubtask(item2);
        } else {
            System.out.println("Задач пока нет");
        }

    }

    @Override
    public void printEpic(Long id) {
        if (epics.containsKey(id)) {
            System.out.println(epics.get(id));
            historyManager.add(epics.get(id));
        } else {
            System.out.println("Задача с ID " + id + " не найден.");
        }
//        moveToWatch(epics.get(id));
    }

//    public void moveToWatch(Task task) {
//        if(listOfViewedTasks.size() <= 10){
//            listOfViewedTasks.add(task);
//        } else {
//            listOfViewedTasks.remove(0);
//            listOfViewedTasks.add(task);
//        }
//
//    }
//
//    @Override
//    public ArrayList<Task> getHistory() {
//        if(listOfViewedTasks.size() == 0){
//            System.out.println("Вы пока не смотрели свои задачи)");
//        } else {
//            for (Task task : listOfViewedTasks){
//                System.out.println(task);
//            }
//        }
//        return listOfViewedTasks;
//    }
}