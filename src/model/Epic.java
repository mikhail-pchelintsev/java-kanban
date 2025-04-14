package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private HashMap<Long, SubTask> subTasks = new HashMap<>();
    private static final DateTimeFormatter FORMATTER_FOR_YEAR = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    public String getEndTime() {
        return getStartTime().plus(getMinDuration()).format(FORMATTER_FOR_YEAR);
    }

    public LocalDateTime getStartTime() {
        if (subTasks.isEmpty()) {
            return LocalDateTime.now();
        }
        LocalDateTime minStart = null;
        for (SubTask subTask : subTasks.values()) {
            if (minStart == null || subTask.getStartTime().isBefore(minStart)) {
                minStart = subTask.getStartTime();
            }
        }
        return minStart;
    }

    public Duration getMinDuration() {
        if (subTasks.isEmpty()) {
            return Duration.ZERO; // если нет подзадач, возвращаем Duration.ZERO
        }
        Duration minDuration = null;
        for (SubTask subTask : subTasks.values()) {
            if (subTask != null && subTask.getDuration() != null) {
                if (minDuration == null || subTask.getDuration().compareTo(minDuration) < 0) {
                    minDuration = subTask.getDuration();
                }
            }
        }
        return minDuration != null ? minDuration : Duration.ZERO;
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }

    public void update(Long id, SubTask subTask) {
        if (id == null || subTask == null) {
            System.out.println("Ошибка: ID или подадача не могут быть null.");
            return;
        }
        if (subTasks.containsKey(id)) {
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

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%02d:%02d", hours, minutes);  // формат ЧЧ:ММ
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "epicId=" + getId() +
                ", name=" + getName() +
                ", status=" + getStatus() +
                ", duration=" + formatDuration(getMinDuration()) +
                ", startTime=" + getStartTime().format(FORMATTER_FOR_YEAR) +
                ", endTime=" + getEndTime() +
                '}';
    }


}