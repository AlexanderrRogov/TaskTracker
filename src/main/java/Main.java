import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

public class Main {
    public static void main(String[] args) {
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



        TaskManager taskManager = new TaskManager();
        taskManager.addTask(task0);
        taskManager.addTask(task1);
        taskManager.addEpic(epic0);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask0);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);


        for (Task task :taskManager.getTasks()) {
            System.out.println(task.toString());
        }

        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask.toString());
        }

        for (Epic epic :taskManager.getEpics()) {
            System.out.println(epic.toString());
        }


        var task0ById = taskManager.getTask(task0.getId());
        task0ById.setInfo("Новое инфо");
        task0ById.setName("Новое название");
        task0ById.setTaskStatus(TaskStatus.DONE);
        taskManager.addTask(task0);

        var task1ById = taskManager.getTask(task1.getId());
        task1ById.setTaskStatus(TaskStatus.DONE);
        taskManager.addTask(task1ById);

        var subtasks = taskManager.getSubtasks();

        for (Subtask subtask : subtasks) {
            if(subtask.getId().equals(subtask0.getId())) {
                subtask.setTaskStatus(TaskStatus.DONE);
                taskManager.addSubtask(subtask);
            }
        }

        var subTask1ById = taskManager.getSubtask(subtask1.getId());
        subTask1ById.setTaskStatus(TaskStatus.DONE);
        taskManager.addSubtask(subtask1);

        var subTask2ById = taskManager.getSubtask(subtask2.getId());
        subTask2ById.setTaskStatus(TaskStatus.DONE);
        taskManager.addSubtask(subtask2);

        for (Task task :taskManager.getTasks()) {
            System.out.println(task.toString());
        }

        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask.toString());
        }

        for (Epic epic :taskManager.getEpics()) {
            System.out.println(epic.toString());
        }

        taskManager.deleteSubtask(subtask2.getId());

        for (Epic epic :taskManager.getEpics()) {
            System.out.println(epic.toString());
        }
    }
}