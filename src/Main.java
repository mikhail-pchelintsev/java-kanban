import manager.Managers;
import interfaces.TaskManager;
import model.Epic;
import model.SortOrder;
import model.Status;
import model.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int command = scanner.nextInt();
            scanner.nextLine();

            switch (command) {
                case 1:
                    createEpic(manager, scanner);
                    break;
                case 2:
                    createSubtask(manager, scanner);
                    break;
                case 3:
                    manager.printAllEpic();
                    break;
                case 4:
                    System.out.println("Введите ID эпика:");
                    long epicId = scanner.nextLong();
                    printAllSubtaskInEpic(manager,epicId);
                    break;
                case 5:
                    System.out.println("Введите ID эпика:");
                    long epicIdToPrint = scanner.nextLong();
                    manager.printEpic(epicIdToPrint);
                    break;
                case 6:
                    System.out.println("Введите ID подзадачи:");
                    long subtaskId = scanner.nextLong();
                    manager.printSubTaskById(subtaskId);
                    break;
                case 7:
                    System.out.println("Введите ID эпика для удаления:");
                    long epicIdToDelete = scanner.nextLong();
                    manager.deleteEpic(epicIdToDelete);
                    break;
                case 8:
                    System.out.println("Введите ID задачи");
                    long epicIdToFindSubtask = scanner.nextLong();
                    System.out.println("Введите ID подзадачи для удаления:");
                    long subtaskIdToDelete = scanner.nextLong();
                    manager.deleteSubtask(epicIdToFindSubtask,subtaskIdToDelete);
                    break;
                case 9:
                    updateEpic(manager,scanner);
                    break;
                case 10:
                    updateSubtask(manager,scanner);
                    break;
                case 11:
                    System.out.println("Ваш список просмотренных задач");
                    if (manager.getHistory().size() == 0) {
                        System.out.println("Ваш список пока пустой)");
                    }
                    manager.getHistory().forEach(System.out::println);
                    break;
                case 12:
                    System.out.println("Введите ID задачи для удаления из просмотренных:");
                    int id = scanner.nextInt();
                    manager.removeHistoryById(id);
                    System.out.println("Задача успешно удалена из просмотренных.");
                    break;
                case 13:
                    System.out.println("Хотите отсортировать по возрастанию введите ASCENDING," +
                            " по убыванию введите DESCENDING:");
                    String sortOrderEpic = scanner.next().toUpperCase();
                    manager.getPrioritizedEpic(manager.getEpics(), SortOrder.valueOf(sortOrderEpic)).forEach(System.out::println);
                    break;
                case 14:
                    System.out.println("Введите ID эпика, к которому относится подзадача:");
                    int epicIdToSort = scanner.nextInt();
                    System.out.println("Хотите отсортировать по возрастанию введите ASCENDING," +
                            " по убыванию введите DESCENDING:");
                    String sortOrderSubTask = scanner.next().toUpperCase();
                    manager.getPrioritizedSubTask(manager.getEpics(), SortOrder.valueOf(sortOrderSubTask), epicIdToSort)
                            .forEach(System.out::println);
                    break;
                case 0:
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неизвестная команда. Попробуйте снова.");
            }
        }
    }

    public static void printAllSubtaskInEpic(TaskManager manager, Long id) {
        if (manager.getEpics().containsKey(id)) {
            manager.getEpics().get(id).printAllSubtask();
        } else {
            System.out.println("Задача с ID " + id + " не найден.");
        }

    }

    public static void printMenu() {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Создать эпик");
        System.out.println("2. Создать подзадачу");
        System.out.println("3. Получить список всех эпиков");
        System.out.println("4. Получить список всех подзадач эпика");
        System.out.println("5. Получить эпик по ID");
        System.out.println("6. Получить подзадачу по ID");
        System.out.println("7. Удалить эпик по ID");
        System.out.println("8. Удалить подзадачу по ID");
        System.out.println("9. Обновить задачу по ID");
        System.out.println("10. Обновить подзадачу по ID");
        System.out.println("11. Показать список просмотренных задач");
        System.out.println("12. Удалить просмотренную задачу");
        System.out.println("13. Получить список отсортированнях задач");
        System.out.println("14. Получить список отсортированнях подзадач");
        System.out.println("0. Выйти");
    }

    private static void createEpic(TaskManager manager, Scanner scanner) {
        System.out.println("Введите название эпика:");
        String name = scanner.nextLine();
        System.out.println("Введите описание эпика:");
        String description = scanner.nextLine();

        Epic epic = new Epic(name, description, Status.NEW);
        manager.createEpic(epic);
        System.out.println("Задача создан с ID: " + epic.getId());
    }

    private static void updateEpic(TaskManager manager, Scanner scanner) {
        System.out.println("Введите ID задачи, котрой хотите обновить:");
        long epicId = scanner.nextLong();
        scanner.nextLine();
        if (!manager.getEpics().containsKey(epicId)) {
            System.out.println("Задача с ID " + epicId + " не найден.");
            return;
        }
        System.out.println("Введите новое название задачи:");
        String name = scanner.nextLine();
        System.out.println("Введите новое описание задачи:");
        String description = scanner.nextLine();;
        Epic epic = new Epic(name, description, Status.NEW);
        manager.updateEpicById(epicId, epic);
        System.out.println("Задача создана с ID: " + epic.getId());
    }

    private static void updateSubtask(TaskManager manager, Scanner scanner) {
        System.out.println("Введите ID подзадачи, котрой хотите обновить:");
        long subtaskId = scanner.nextLong();
        scanner.nextLine();
        System.out.println("Введите новое название подзадачи:");
        String name = scanner.nextLine();
        System.out.println("Введите новое описание подзадачи:");
        String description = scanner.nextLine();
        System.out.println("Введите продолжительность работы в минутах:");
        int durationOfMinures = scanner.nextInt();
        Duration duration = Duration.ofMinutes(durationOfMinures);
        System.out.print("Введите дату и время (в формате 01.02.2025 17:35): ");
        scanner.nextLine();
        String input = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        try {
            SubTask subTask = new SubTask(name, description, Status.NEW, duration, LocalDateTime.parse(input,
                    formatter));
            subTask.setId(subtaskId);
            manager.updateSubtaskById(subtaskId, subTask);
            System.out.println("Задача создана с ID: " + subTask.getId());
        } catch (Exception e) {
            System.out.println("Неверный формат ввода. Попробуйте снова.");
        }


    }

    private static void createSubtask(TaskManager manager, Scanner scanner) {
        System.out.println("Введите ID эпика, к которому относится подзадача:");
        long epicId = scanner.nextLong();
        scanner.nextLine();
        System.out.println("Введите название подзадачи:");
        String name = scanner.nextLine();
        System.out.println("Введите описание подзадачи:");
        String description = scanner.nextLine();
        System.out.println("Укажите статус: 1 - NEW, 2 - DONE, 3 - IN_PROGRESS");
        int number = scanner.nextInt();
        scanner.nextLine();
        Status status = pointStatus(number);
        System.out.println("Введите продолжительность работы в минутах:");
        Integer durationOfMinures = scanner.nextInt();
        Duration duration = Duration.ofMinutes(durationOfMinures);
        System.out.print("Введите дату и время (в формате 01.02.2025 17:35): ");
        scanner.nextLine();
        String input = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        try {
            SubTask subTask = new SubTask(name, description, status, duration, LocalDateTime.parse(input, formatter));
            manager.createSubTask(epicId, subTask);
        } catch (Exception e) {
            System.out.println("Неверный формат ввода. Попробуйте снова.");
        }
    }

    public static Status pointStatus(int number) {
        switch (number) {
            case 1:
                return Status.NEW;
            case 2:
                return Status.DONE;
            case 3:
                return Status.IN_PROGRESS;
            default:
                System.out.println("Неверный выбор статуса. Установлен статус по умолчанию: NEW.");
                return Status.NEW;
        }
    }


}