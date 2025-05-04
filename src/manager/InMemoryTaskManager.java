package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.exception.ManagerSaveException;
import model.Epic;
import model.SortOrder;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Map<Long, Epic> epics = new HashMap<>();
    private long nextEpicId = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public Map<Long, Epic> getEpics() {
        return epics;
    }

    @Override
    public Epic getEpicById(long id) {
        return epics.get(id);
    }

    public TreeSet<Epic> getPrioritizedEpic(Map<Long, Epic> epics, SortOrder sortOrder) {
        Comparator<Epic> comparator = Comparator
                .comparing(Epic::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparingLong(Epic::getId);

        TreeSet<Epic> prioritizedEpic;
        if (sortOrder.equals(SortOrder.ASCENDING)) {
            prioritizedEpic = new TreeSet<>(comparator); // по возрастанию
        } else if (sortOrder.equals(SortOrder.DESCENDING)) {
            prioritizedEpic = new TreeSet<>(comparator.reversed()); // по убыванию
        } else {
            throw new IllegalStateException("Unexpected value: " + sortOrder);
        }

        prioritizedEpic.addAll(epics.values());

        return prioritizedEpic;
    }

    public TreeSet<SubTask> getPrioritizedSubTask(Map<Long, Epic> epics, SortOrder sortOrder, long subTaskId) {
        TreeSet<SubTask> prioritizedSubTasks;
        if (sortOrder.equals(SortOrder.ASCENDING)) {
            prioritizedSubTasks = new TreeSet<>(Comparator.comparing(SubTask::getStartTime));
        } else if (sortOrder.equals(SortOrder.DESCENDING)) {
            prioritizedSubTasks = new TreeSet<>(Comparator.comparing(SubTask::getStartTime).reversed());
        } else {
            throw new IllegalStateException("Unexpected value: " + sortOrder);
        }
        for (Epic epic : epics.values()) {
            SubTask subTask = epic.getSubTasks().get(subTaskId);
            if (subTask != null) {
                prioritizedSubTasks.add(subTask);
            }
        }

        return prioritizedSubTasks;
    }

    @Override
    public void createEpic(Epic epic) throws ManagerSaveException {
        if (epic.getId() != 0 && epics.containsKey(epic.getId())) {
            throw new ManagerSaveException("Epic with such ID already exists");
        }
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
    public void updateEpicById(Long id, Epic epic) throws ManagerSaveException {
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
    public void updateSubtaskById(Long id, SubTask subTask) throws ManagerSaveException {
        if (hasOverlapping(subTask)) {
            System.out.println("Ошибка: подзадача пересекается по времени с другой задачей.");
            return;
        }
        boolean updated = epics.values().stream()
                .filter(epic -> epic.getSubTasks().containsKey(id))
                .peek(epic -> epic.update(id, subTask))
                .findFirst()
                .isPresent();
        if (!updated) {
            System.out.println("Подзадачи с таким ID нет");
        }
    }

    @Override
    public void createSubTask(long epicId, SubTask subTask) throws ManagerSaveException {
        if (!epics.containsKey(epicId)) {
            System.out.println("Задача с ID " + epicId + " не найдена.");
            return;
        }

        if (hasOverlapping(subTask)) {
            System.out.println("Ошибка: подзадача пересекается по времени с другой задачей.");
            return;
        }

        long subTaskId = epics.get(epicId).getSubTasks().size() + 1;
        subTask.setId(subTaskId);
        epics.get(epicId).createSubtask(subTask, subTaskId);
        System.out.println("Подзадача создана с ID: " + subTaskId);
    }


    @Override
    public void deleteEpic(Long id) throws ManagerSaveException {
        epics.remove(id);
        System.out.println("задача удалена");
    }

    @Override
    public void printAllEpic() {
        if (!epics.isEmpty()) {
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void removeHistoryById(int id) {
        historyManager.removeHistory(id);
    }

    @Override
    public void deleteSubtask(Long item1, Long item2) throws ManagerSaveException {
        Epic epic = epics.get(item1);
        if (epic != null) {
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
    }

    private boolean isOverlapping(SubTask t1, SubTask t2) {
        if (t1.getStartTime() == null || t2.getStartTime() == null
                || t1.getDuration() == null || t2.getDuration() == null) {
            return false;
        }

        LocalDateTime start1 = t1.getStartTime();
        LocalDateTime end1 = start1.plus(t1.getDuration());
        LocalDateTime start2 = t2.getStartTime();
        LocalDateTime end2 = start2.plus(t2.getDuration());

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private boolean hasOverlapping(SubTask newTask) {
        return epics.values().stream()
                .flatMap(epic -> epic.getSubTasks().values().stream())
                .filter(existing -> {
                    Long existingId = existing.getId();
                    Long newId = newTask.getId();
                    return !existingId.equals(newId);
                })
                .anyMatch(existing -> isOverlapping(existing, newTask));
    }
}