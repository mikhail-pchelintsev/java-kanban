package model;

import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private Long epicId;
    private HashMap<Long, SubTask> subTasks = new HashMap<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Long getEpicId() {
        return epicId;
    }

    public void update(Long id,SubTask subTask){
        if (id == null || subTask == null) {
            System.out.println("Ошибка: ID или подадача не могут быть null.");
            return;
        }
        if(subTasks.containsKey(id)){
            SubTask existingSubtask = subTasks.get(id);
            existingSubtask.setName(subTask.getName());
            existingSubtask.setDescription(subTask.getDescription());
            existingSubtask.setStatus(subTask.getStatus());
            System.out.println("Подзадача под " + id + " ID была обновлена");
        } else {
            System.out.println("Подзадачи с таким ID нет");
        }
    }

    public HashMap<Long, SubTask> getSubTasks() {
        return subTasks;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }

    public void createSubtask(SubTask subTask, long subTaskId) {
        subTask.setSubTaskId(subTaskId);
        subTasks.put(subTaskId, subTask);
        calcStatusEpic();
    }

    public void deleteItemSubtask(Long id) {
        subTasks.remove(id);
        calcStatusEpic();
    }

    public void clearSubtask() {
        subTasks.clear();
        calcStatusEpic();
    }

    public void printAllSubtask() {
        calcStatusEpic();
        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTask);
        }
    }

    public void updateSubtask(Long id, SubTask subTask) {
        subTasks.put(id, subTask);
        calcStatusEpic();
    }

    public void printSubtask(Long id) {
        System.out.println(subTasks.get(id));
    }

    public void calcStatusEpic() {
        boolean allDone = true;
        boolean allNew = true;

        for (SubTask subTask : subTasks.values()) {
            if (subTask == null) continue;

            if (subTask.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (subTask.getStatus() != Status.NEW) {
                allNew = false;
            }
        }

        if (allNew || subTasks.isEmpty()) {
            setStatus(Status.NEW);
        } else if (allDone) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "epicId=" + epicId +
                ", name=" + getName() +
                ", status=" + getStatus() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(epicId, epic.epicId) && Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicId, subTasks);
    }
}