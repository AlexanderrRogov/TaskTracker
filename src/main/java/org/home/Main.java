package org.home;

public class Main {
    public static void main(String[] args) {
        Task task0 = new Task("Сервис транзакций_V1", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW);
        Task task1 = new Task("Сервис транзакций_V2", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS);

        Epic epic0 = new Epic("Сервис транзакций_V2", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW);

        Subtask subtask0 = new Subtask("Сервис транзакций_V10", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS, epic0.getId());
        Subtask subtask1 = new Subtask("Сервис транзакций_V40", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS, epic0.getId());
        epic0.putTaskToSubtaskList(subtask0);
        epic0.putTaskToSubtaskList(subtask1);

        Epic epic1 = new Epic("Сервис транзакций_V33", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW);
        Subtask subtask2 = new Subtask("Сервис транзакций_V59", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW, epic1.getId());
        epic1.putTaskToSubtaskList(subtask2);


        TaskManager taskManager = new TaskManager();
        taskManager.addTask(task0);
        taskManager.addTask(task1);
        taskManager.addTask(epic0);
        taskManager.addTask(epic1);

        taskManager.showAllTasks();

        var task0ById = taskManager.getTaskById(task0.getId());
        task0ById.setInfo("Новое инфо");
        task0ById.setName("Новое название");
        task0ById.setTaskStatus(TaskStatus.DONE);
        taskManager.updateTask(task0);

        var task1ById = taskManager.getTaskById(task1.getId());
        task1ById.setTaskStatus(TaskStatus.DONE);
        taskManager.updateTask(task1ById);

        var subTaskListFromEpic0 = taskManager.getSubtaskListById(epic0.getId());

        for (Subtask subtask : subTaskListFromEpic0) {
            if(subtask.getId().equals(subtask0.getId())) {
                subtask.setTaskStatus(TaskStatus.DONE);
                taskManager.updateTask(subtask);
            }
        }

        var subTask1ById = taskManager.getTaskById(subtask1.getId());
        subTask1ById.setTaskStatus(TaskStatus.DONE);
        taskManager.updateTask(subtask1);

        var subTask2ById = taskManager.getTaskById(subtask2.getId());
        subTask2ById.setTaskStatus(TaskStatus.DONE);
        taskManager.updateTask(subtask2);

        taskManager.removeById(subtask2.getId());
    }
}