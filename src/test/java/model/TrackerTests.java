package model;

import controller.FileBackedTaskManager;
import controller.HistoryManager;
import controller.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrackerTests {

    Task task0;
    static Subtask subtask0;
    TaskManager taskManager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();
    Task task1 = new Task("Сервис транзакций_V2", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS, "2024-04-25 13:30", "2024-04-25 14:30");
    static Epic epic0 = new Epic("Сервис транзакций_V2", "Написать сервис для передачи информации по проводкам");
    static Subtask subtask1 = new Subtask("Сервис транзакций_V40", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS, "2024-04-25 13:30", "2024-04-25 14:30");
    static Epic epic1 = new Epic("Сервис транзакций_V33", "Написать сервис для передачи информации по проводкам");
    static Subtask subtask2 = new Subtask("Сервис транзакций_V59", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW, "2024-04-25 13:30", "2024-04-25 14:30");
    static Subtask subtask3 = new Subtask("Сервис транзакций_V60", "Написать сервис для передачи информации по проводкам", TaskStatus.DONE, "2024-04-25 15:30", "2024-04-25 16:35");


    @BeforeAll
        static void update() {

            epic0.setSubTaskInfo(subtask1);
            epic1.setSubTaskInfo(subtask2);
        }


    @BeforeEach
    void setup() {
        task0 = new Task("Сервис транзакций_V1", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW, "2024-04-25 13:30", "2024-04-25 14:30");
        subtask0 = new Subtask("Сервис транзакций_V10", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS, "2024-04-25 13:30", "2024-04-25 14:30");
        epic0.setSubTaskInfo(subtask0);
        epic0.setSubTaskInfo(subtask2);
    }

    @Test
    void test() {
           taskManager.addTask(task0);
           Task testTask = taskManager.getTask(task0.getId());
           assertEquals(task0, testTask);
      }

    @Test
    void addNewTask() {
        Task task0 = new Task("Сервис транзакций_V1_NEW", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW, "2024-04-25 13:30", "2024-04-25 13:30");
        taskManager.addTask(task0);

        final Task savedTask = taskManager.getTask(task0.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task0, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task0, tasks.get(0), "Задачи не совпадают.");
    }


    @Test
    void add() {
        HistoryManager historyManagerAdd = Managers.getDefaultHistory();
        historyManagerAdd.addTask(task0);
        final List<Task> history = historyManagerAdd.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void saveToFile() {
        FileBackedTaskManager fileBackedTaskManager = Managers.getFileBackedTaskManager();
        fileBackedTaskManager.addTask(task0);
        fileBackedTaskManager.addEpic(epic0);
        fileBackedTaskManager.addSubtask(subtask1);
        final HashMap<TaskType, ArrayList<? extends Task>> restoredTasks = FileBackedTaskManager.loadFromFile();
        assertNotNull(restoredTasks, "Файлы восстановлены");
    }

    @Test
    void checkEpicDuration() {
        FileBackedTaskManager fileBackedTaskManager = Managers.getFileBackedTaskManager();
        var epic32 = new Epic("Сервис транзакций_V2", "Написать сервис для передачи информации по проводкам");
        var subtask32 = new Subtask("Сервис транзакций_V10", "Написать сервис для передачи информации по проводкам 1", TaskStatus.IN_PROGRESS, "2024-04-25 13:30", "2024-04-25 14:30");
        var subtask33 = new Subtask("Сервис транзакций_V11", "Написать сервис для передачи информации по проводкам 2", TaskStatus.DONE, "2024-04-25 15:30", "2024-04-25 16:30");
        var subtask44 = new Subtask("Сервис транзакций_V12", "Написать сервис для передачи информации по проводкам 3", TaskStatus.IN_PROGRESS, "2024-04-27 18:30", "2024-04-28 19:30");
        epic32.setSubTaskInfo(subtask32);
        epic32.setSubTaskInfo(subtask33);
        epic32.setSubTaskInfo(subtask44);
        fileBackedTaskManager.addEpic(epic32);

        final HashMap<TaskType, ArrayList<? extends Task>> restoredTasks = FileBackedTaskManager.loadFromFile();
        assertNotNull(restoredTasks, "Файлы восстановлены");
    }

    @Test
    void checkHistory() {
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

        Task taskFromManager = taskManager.getTask(task0.getId());
        Task taskFromHistory = taskManager.getHistory().get(0);
        assertEquals(taskFromManager, taskFromHistory);
        }
    }
