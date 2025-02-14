package manager;

import model.Epic;
import model.SubTask;

import java.util.HashMap;

public class TaskManager {
    protected HashMap<Long, Epic> epics = new HashMap<>();
    private long nextEpicId = 1;

    public HashMap<Long, Epic> getEpics() {
        return epics;
    }

    public void createEpic(Epic epic) {
        epic.setId(nextEpicId++);
        epics.put(epic.getId(), epic);
    }

    public SubTask getSubTaskById(long subTaskId) {
        for (Epic epic : epics.values()) {
            SubTask subTask = epic.getSubTasks().get(subTaskId);
            if (subTask != null) {
                return subTask;
            }
        }
        return null;
    }

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

    public void printSubTaskById(long subTaskId) {
        SubTask subTask = getSubTaskById(subTaskId);
        if (subTask != null) {
            System.out.println("Найдена подзадача: " + subTask);
        } else {
            System.out.println("Подзадача с ID " + subTaskId + " не найдена.");
        }
    }

    public void createSubTask(long epicId, SubTask subTask) {
        if (epics.containsKey(epicId)) {
            long subTaskId = epics.get(epicId).getSubTasks().size() + 1;
            epics.get(epicId).createSubtask(subTask, subTaskId);
            System.out.println("Подзадача создана с ID: " + subTaskId);
        } else {
            System.out.println("Задача с ID " + epicId + " не найден.");
        }
    }

    public void deleteEpic(Long id) {
        epics.remove(id);
        System.out.println("задача удалена");
    }

    public void printAllEpic() {
        if (!epics.isEmpty()){
            for (Epic epic : epics.values()) {
                System.out.println(epic);
            }
        } else {
            System.out.println("Задач пока нет");
        }

    }

    public void deleteSubtask(Long item1, Long item2){
        Epic epic = epics.get(item1);
        if (epic != null){
            epic.deleteItemSubtask(item2);
        } else {
            System.out.println("Задач пока нет");
        }

    }

    public void printEpic(Long id) {
        if (epics.containsKey(id)) {
            System.out.println(epics.get(id));
        } else {
            System.out.println("Задача с ID " + id + " не найден.");
        }
    }

    public void printAllSubtaskInEpic(Long id) {
        if (epics.containsKey(id)) {
            epics.get(id).printAllSubtask();
        } else {
            System.out.println("Задача с ID " + id + " не найден.");
        }
    }
}