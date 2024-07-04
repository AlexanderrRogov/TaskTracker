import controller.InMemoryTaskManager;
import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import util.Managers;
import util.TaskStatusUpdater;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager taskManager) {
        Task task0 = new Task("Сервис транзакций_V1", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW);
        Task task1 = new Task("Сервис транзакций_V2", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS);

        Epic epic0 = new Epic("Сервис транзакций_V2", "Написать сервис для передачи информации по проводкам");

        Subtask subtask0 = new Subtask("Сервис транзакций_V10", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS);
        Subtask subtask1 = new Subtask("Сервис транзакций_V40", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS);
        epic0.setSubTaskIds(subtask0.getId());
        epic0.setSubTaskIds(subtask1.getId());

        Epic epic1 = new Epic("Сервис транзакций_V33", "Написать сервис для передачи информации по проводкам");
        Subtask subtask2 = new Subtask("Сервис транзакций_V59", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW);
        epic1.setSubTaskIds(subtask2.getId());


        taskManager.addTask(task0);
        taskManager.addTask(task1);
        taskManager.addEpic(epic0);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask0);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);


        System.out.println("Задачи:");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : taskManager.getEpics()) {
            System.out.println(epic);

            for (Task task : taskManager.getSubtasksForEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }


        taskManager.getTask(task0.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getEpic(epic0.getId());
        taskManager.getTask(task0.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getEpic(epic0.getId());
        taskManager.getTask(task0.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getEpic(epic0.getId());
        taskManager.getSubtask(subtask2.getId());

        taskManager.getEpic(epic0.getId());
        taskManager.getTask(task0.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getTask(task0.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getEpic(epic0.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getEpic(epic0.getId());
        taskManager.getTask(task0.getId());

        taskManager.getTask(task0.getId());


        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}