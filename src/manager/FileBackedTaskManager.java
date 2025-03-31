package manager;

import manager.exception.ManagerLoadException;
import manager.exception.ManagerSaveException;
import model.Epic;
import model.Status;
import model.SubTask;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    private FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerLoadException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.readFromFile();
        return manager;
    }

    @Override
    public void createSubTask(long epicId, SubTask subTask) throws ManagerSaveException {
        super.createSubTask(epicId, subTask);
        save();
    }

    @Override
    public void updateSubtaskById(Long id, SubTask subTask) throws ManagerSaveException {
        super.updateSubtaskById(id, subTask);
        save();
    }

    @Override
    public void createEpic(Epic epic) throws ManagerSaveException {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpicById(Long id, Epic epic) throws ManagerSaveException {
        super.updateEpicById(id, epic);
        save();
    }

    @Override
    public void deleteEpic(Long id) throws ManagerSaveException {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(Long item1, Long item2) throws ManagerSaveException {
        super.deleteSubtask(item1, item2);
        save();
    }


    public void readFromFile() throws ManagerLoadException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null) return;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Epic epic = new Epic(parts[0], parts[1], Status.valueOf(parts[2]));
                    long id = Long.parseLong(parts[3]);
                    epic.setId(id);
                    createEpic(epic);
                } else if (parts.length == 5) {
                    SubTask subTask = new SubTask(parts[0], parts[1], Status.valueOf(parts[2]));
                    long subTaskId = Long.parseLong(parts[3]);
                    long epicId = Long.parseLong(parts[4]);
                    subTask.setSubTaskId(subTaskId);
                    createSubTask(epicId, subTask);
                }
            }

        } catch (IOException | ManagerSaveException e) {
            throw new ManagerLoadException("Ошибка при сохранении файла: " + e);
        }
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            String header = "name,description,status,id\n";
            writer.write(header);
            for (Epic epic : getEpics().values()) {
                writer.write(epic.getName() + "," + epic.getDescription() + "," + epic.getStatus() + "," + epic.getId());
                writer.newLine();
                for (SubTask subTask : epic.getSubTasks().values()) {
                    writer.write(subTask.getName() + "," + subTask.getDescription() + "," + subTask.getStatus() + "," + subTask.getSubTaskId() + "," + epic.getId());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла: " + e);
        }
    }
}
