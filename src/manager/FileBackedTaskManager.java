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
            String header = reader.readLine(); // читаем заголовок

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] fields = line.split(",", -1); // разрешаем пустые значения

                long id = Long.parseLong(fields[0]);
                String type = fields[1];
                String name = fields[2];
                String description = fields[3];
                Status status = Status.valueOf(fields[4]);

                if (type.equals("EPIC")) {
                    Epic epic = new Epic(name, description, status);
                    epic.setId(id);
                    createEpic(epic);
                } else if (type.equals("SUBTASK")) {
                    long duration = fields[5].isEmpty() ? 0 : Long.parseLong(fields[5]);
                    LocalDateTime startTime = fields[6].isEmpty() ? null : LocalDateTime.parse(fields[6]);
                    long epicId = Long.parseLong(fields[7]);

                    SubTask subTask = new SubTask(name, description, status, Duration.ofMinutes(duration), startTime);
                    subTask.setId(id);
                    createSubTask(epicId, subTask);
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка при загрузке файла: " + e.getMessage());
        } catch (Exception e) {
            throw new ManagerLoadException("Ошибка при разборе содержимого файла: " + e.getMessage());
        }
    }




    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,description,status,duration,startTime,epicId\n");

            for (Epic epic : getEpics().values()) {
                writer.write(String.format("%d,EPIC,%s,%s,%s,,,,\n",
                        epic.getId(),
                        epic.getName(),
                        epic.getDescription(),
                        epic.getStatus()));
                for (SubTask subTask : epic.getSubTasks().values()) {
                    writer.write(String.format("%d,SUBTASK,%s,%s,%s,%d,%s,%d\n",
                            subTask.getSubTaskId(),
                            subTask.getName(),
                            subTask.getDescription(),
                            subTask.getStatus(),
                            subTask.getDuration() != null ? subTask.getDuration().toMinutes() : 0,
                            subTask.getStartTime() != null ? subTask.getStartTime() : "",
                            epic.getId()));
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

}
