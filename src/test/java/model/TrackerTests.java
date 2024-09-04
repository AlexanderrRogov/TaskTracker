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
    Task task1 = new Task("Сервис транзакций_V2", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS);
    static Epic epic0 = new Epic("Сервис транзакций_V2", "Написать сервис для передачи информации по проводкам");
    static Subtask subtask1 = new Subtask("Сервис транзакций_V40", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS);
    static Epic epic1 = new Epic("Сервис транзакций_V33", "Написать сервис для передачи информации по проводкам");
    static Subtask subtask2 = new Subtask("Сервис транзакций_V59", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW);

        @BeforeAll
        static void update() {

            epic0.setSubTaskIds(subtask1.getId());
            epic1.setSubTaskIds(subtask2.getId());
        }


    @BeforeEach
    void setup() {
        task0 = new Task("Сервис транзакций_V1", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW);
        subtask0 = new Subtask("Сервис транзакций_V10", "Написать сервис для передачи информации по проводкам", TaskStatus.IN_PROGRESS);
        epic0.setSubTaskIds(subtask0.getId());
    }

       @Test
       void test() {
           taskManager.addTask(task0);
           Task testTask = taskManager.getTask(task0.getId());
           assertEquals(task0, testTask);
      }

    @Test
    void addNewTask() {
        Task task0 = new Task("Сервис транзакций_V1_NEW", "Написать сервис для передачи информации по проводкам", TaskStatus.NEW);
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
