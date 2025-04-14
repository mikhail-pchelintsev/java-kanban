package manager;

import manager.exception.ManagerLoadException;
import manager.exception.ManagerSaveException;
import model.Epic;
import model.Status;
import model.SubTask;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    private FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.readFromFile();
        return manager;
    }

    @Override
    public void createSubTask(long epicId, SubTask subTask) {
        super.createSubTask(epicId, subTask);
        save();
    }

    @Override
    public void updateSubtaskById(Long id, SubTask subTask) {
        super.updateSubtaskById(id, subTask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpicById(Long id, Epic epic) {
        super.updateEpicById(id, epic);
        save();
    }

    @Override
    public void deleteEpic(Long id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(Long item1, Long item2) {
        super.deleteSubtask(item1, item2);
        save();
    }

    public void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null) return;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 5 || parts.length == 8) {
                    String name = parts[0];
                    String description = parts[1];
                    Status status = Status.valueOf(parts[2]);
                    long id = Long.parseLong(parts[4]);

                    Epic epic = new Epic(name, description, status);
                    epic.setId(id);
                    createEpic(epic);
                } else if (parts.length == 7 || parts.length == 8) {
                    String name = parts[0];
                    String description = parts[1];
                    Status status = Status.valueOf(parts[2]);
                    Duration duration = Duration.parse(parts[3]);
                    long subTaskId = Long.parseLong(parts[4]);
                    long epicId = Long.parseLong(parts[5]);
                    LocalDateTime startTime = LocalDateTime.parse(parts[6]);

                    SubTask subTask =   new SubTask(name, description, status, duration,startTime);
                    subTask.setSubTaskId(subTaskId);
                    subTask.setStartTime(startTime);
                    createSubTask(epicId, subTask);
                }
            }
        } catch (IOException | RuntimeException e) {
            throw new ManagerLoadException("Ошибка при загрузке файла: " + e);
        }
    }


    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            String header = "name,description,status,duration,subTaskId,epicId,startTime\n";
            writer.write(header);
            for (Epic epic : getEpics().values()) {
                writer.write(String.join(",",
                        epic.getName(),
                        epic.getDescription(),
                        epic.getStatus().name(),
                        "", "", "", ""));
                writer.write("," + epic.getId());
                writer.newLine();

                for (SubTask subTask : epic.getSubTasks().values()) {
                    writer.write(String.join(",",
                            subTask.getName(),
                            subTask.getDescription(),
                            subTask.getStatus().name(),
                            subTask.getDuration().toString(),
                            String.valueOf(subTask.getSubTaskId()),
                            String.valueOf(epic.getId()),
                            subTask.getStartTime().toString()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла: " + e);
        }
    }
}
