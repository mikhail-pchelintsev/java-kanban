package manager;

import model.Epic;
import model.Status;
import model.SubTask;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    private FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
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


    public void readFromFile() throws IOException {
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
        }
    }

    public void save() {
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
            System.out.println("Ошибка при прочтении файла: " + e);
        }
    }
}
